package com.ink2insight.springbootbackend.utils;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ImageTextExtractor {
    public static String extractText(MultipartFile file) throws IOException {
        String contentType = file.getContentType();
        if (contentType == null ||
                !(contentType.equals("image/jpeg") || contentType.equals("image/png"))) {
            return "Unsupported file format";
        }

        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath("C:/Program Files/Tesseract-OCR");

        File tempFile = Files.createTempFile("upload-", file.getOriginalFilename()).toFile();
        file.transferTo(tempFile);

        try {
            return tesseract.doOCR(tempFile);
        } catch (TesseractException e) {
            e.printStackTrace();
            return "Error reading image";
        } finally {
            tempFile.delete();
        }
    }
}
