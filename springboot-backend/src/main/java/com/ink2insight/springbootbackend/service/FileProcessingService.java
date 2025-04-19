package com.ink2insight.springbootbackend.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileProcessingService {
    public String processFile(MultipartFile file) {
        try {
            // Just to test — read file details
            String filename = file.getOriginalFilename();
            long size = file.getSize();

            // TODO: Pass this to AI model or extract text

            return "File received: " + filename + " (" + size + " bytes)";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error processing file.";
        }
    }
}
