package com.dhanush.webhookapp.Controller;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
public class WebhookController {

    private static String accessToken;
    private static String webhookUrl;

    public static void setWebhookDetails(String token, String url) {
        accessToken = token;
        webhookUrl = url;
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> receiveData(@RequestBody Map<String, Object> payload) {
        System.out.println("📩 Incoming payload: " + payload);

        String question = (String) payload.get("question");
        System.out.println("🧠 Question Received: " + question);

        // Step 4 placeholder SQL logic
        String finalQuery = "SELECT * FROM Employee WHERE salary > 50000;";

        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", accessToken);
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, String> finalBody = new HashMap<>();
            finalBody.put("finalQuery", finalQuery);

            HttpEntity<Map<String, String>> entity = new HttpEntity<>(finalBody, headers);

            System.out.println("📤 Sending POST to: " + webhookUrl);
            ResponseEntity<String> submitResponse = restTemplate.postForEntity(webhookUrl, entity, String.class);

            System.out.println("📤 Submission Response: " + submitResponse.getBody());
            return ResponseEntity.ok("✅ Query submitted successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("❌ Error submitting query: " + e.getMessage());
        }
    }
}
