package com.ink2insight.springbootbackend.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class AiExtractorService {

    private final WebClient webClient = WebClient.create("http://localhost:11434");

    public String processWithOllama(String extractedText) {

        String prompt = "Extract only the following fields in a valid JSON format and stop: "
                + "employee_id, employee_name, email, mobile_no, patient_name, age, gender, relationship, details_of_illness, "
                + "treating_doctor_name, hospital_name, hospital_address, treatment_start_date, treatment_end_date, bill_amount, "
                + "bill_number, bill_date, remarks. Do not generate anything beyond the JSON. DO NOT add explanations, metadata, or tokens. "
                + "Do not include any additional text or formatting. Here is the text:\n\n"
                + extractedText;

        return webClient.post()
                .uri("/api/generate")
                .header("Content-Type", "application/json")
                .bodyValue(Map.of(
                        "model", "llama3",
                        "prompt", prompt,
                        "stream", false))
                .retrieve()
                .bodyToMono(String.class)
                .map(this::extractResponseText)
                .onErrorResume(e -> {
                    e.printStackTrace();
                    return Mono.just("{\"error\":\"AI extraction failed\"}");
                })
                .block();
    }

    private String extractResponseText(String fullJson) {
        try {
            int start = fullJson.indexOf("\"response\":\"") + 11;
            int end = fullJson.lastIndexOf("\"");

            if (start > 10 && end > start) {
                String raw = fullJson.substring(start, end);
                raw = raw
                        .replace("\\n", "")
                        .replace("\\\"", "\"")
                        .replace("```json", "")
                        .replace("```", "")
                        .trim();

                int jsonStart = raw.indexOf('{');
                int jsonEnd = raw.lastIndexOf('}');
                if (jsonStart != -1 && jsonEnd != -1 && jsonEnd > jsonStart) {
                    return raw.substring(jsonStart, jsonEnd + 1).trim();
                }
                return raw;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fullJson;
    }
}
