package com.ink2insight.springbootbackend.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileProcessingService {
    public String processFile(MultipartFile file) {
        try {
            String filename = file.getOriginalFilename();
            long size = file.getSize();

            return "File received: " + filename + " (" + size + " bytes)";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error processing file.";
        }
    }
}
