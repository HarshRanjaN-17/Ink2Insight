package com.ink2insight.springbootbackend.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import com.ink2insight.springbootbackend.utils.*;

@Service
public class FileProcessingService {
    public String processFile(MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        String text = "";

        if (filename != null && filename.toLowerCase().endsWith(".pdf")) {
            text = PdfTextExtractor.extractText(file);
        } else {
            text = ImageTextExtractor.extractText(file);
        }

        return text;
    }
}
