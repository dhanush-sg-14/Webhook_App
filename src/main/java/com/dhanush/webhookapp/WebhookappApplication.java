package com.dhanush.webhookapp;

import com.dhanush.webhookapp.Controller.WebhookController;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class WebhookappApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(WebhookappApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		RestTemplate restTemplate = new RestTemplate();

		String generateWebhookUrl = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";

		Map<String, String> requestBody = new HashMap<>();
		requestBody.put("name", "Dhanush S G");
		requestBody.put("regNo", "U25UV22T043033");
		requestBody.put("email", "dhanush.sg@campusuvce.in");

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

		ResponseEntity<Map> response = restTemplate.postForEntity(generateWebhookUrl, entity, Map.class);

		System.out.println("🔍 Full Response from Bajaj:");
		System.out.println(response.getBody());

		if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
			// Bajaj sends 'webhook' key, not 'webhookUrl'
			String webhookUrl = (String) response.getBody().get("webhook");
			String accessToken = (String) response.getBody().get("accessToken");

			if (webhookUrl == null || webhookUrl.isEmpty()) {
				System.out.println("❌ No webhook found in response — please check API response again.");
				return;
			}

			WebhookController.setWebhookDetails(accessToken, webhookUrl);

			System.out.println("✅ Webhook URL: " + webhookUrl);
			System.out.println("🔑 Access Token: " + accessToken);
			System.out.println("🌐 Waiting for Bajaj webhook to send SQL question...");
		} else {
			System.out.println("❌ Failed to generate webhook. Please check credentials or response.");
		}
	}
}
