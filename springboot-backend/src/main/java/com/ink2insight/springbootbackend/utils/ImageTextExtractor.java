package com.ink2insight.springbootbackend.utils;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.imageio.ImageIO;

public class ImageTextExtractor {
    public static String extractText(MultipartFile file) throws IOException {
        String contentType = file.getContentType();
        if (contentType == null || !(contentType.equals("image/jpeg") || contentType.equals("image/png"))) {
            return "Unsupported file format";
        }

        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath("C:/Program Files/Tesseract-OCR/tessdata");
        tesseract.setLanguage("eng");

        String originalFilename = file.getOriginalFilename();
        String extension = "";

        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
        } else {
            extension = ".jpg";
        }

        File tempFile = Files.createTempFile("upload-", extension).toFile();
        file.transferTo(tempFile);

        try {
            BufferedImage image = ImageIO.read(tempFile);
            return tesseract.doOCR(image);
        } catch (TesseractException e) {
            e.printStackTrace();
            return "TesseractException: " + e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            return "General Exception : " + e.getMessage();
        } finally {
            tempFile.delete();
        }
    }
}
