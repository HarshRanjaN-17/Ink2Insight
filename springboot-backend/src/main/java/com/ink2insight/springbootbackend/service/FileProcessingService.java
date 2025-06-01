package com.ink2insight.springbootbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import com.ink2insight.springbootbackend.utils.*;

@Service
public class FileProcessingService {
    @Autowired
    private AiExtractorService aiExtractorService;

    public String processFile(MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        String text = "";

        if (filename != null && filename.toLowerCase().endsWith(".pdf")) {
            text = PdfTextExtractor.extractText(file);
        } else {
            text = ImageTextExtractor.extractText(file);
        }

        // System.out.println("______EXTRACTED____" + text);
        String aiExtractedText = aiExtractorService.processWithOllama(text);
        // System.out.println(":::" + aiExtractedText);
        return aiExtractedText;
    }
}
