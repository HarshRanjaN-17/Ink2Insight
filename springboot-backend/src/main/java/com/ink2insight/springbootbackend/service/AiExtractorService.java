package com.ink2insight.springbootbackend.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;
// import java.time.Duration;

@Service
public class AiExtractorService {

    private final WebClient webClient = WebClient.create("http://localhost:11434");

    public String processWithOllama(String extractedText) {
        String prompt = "Extract structured information from the following text. Respond only in valid, compact JSON format with a single object containing the following keys: "
                + "employee_id, employee_name, email, mobile_no, patient_name, age, gender, relationship, details_of_illness, treating_doctor_name, hospital_name, hospital_address, "
                + "treatment_start_date, treatment_end_date, bill_amount, bill_number, bill_date, remarks."
                + " Do not include markdown or extra explanation. If a field is not found, leave it null. Here is the text:\n\n"
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
                raw = raw.replace("\\n", "")
                        .replace("\\\"", "\"")
                        .replace("```json", "")
                        .replace("```", "")
                        .trim();
                return raw;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fullJson;
    }
}
