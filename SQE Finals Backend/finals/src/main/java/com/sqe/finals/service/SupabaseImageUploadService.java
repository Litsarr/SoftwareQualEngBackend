package com.sqe.finals.service;


import org.json.JSONObject;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;

@Service
public class SupabaseImageUploadService {

    private final String supabaseUrl = "https://ozptbbwzmxdbmzgeyqmf.supabase.co";
    private final String supabaseApiKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im96cHRiYnd6bXhkYm16Z2V5cW1mIiwicm9sZSI6InNlcnZpY2Vfcm9sZSIsImlhdCI6MTcyNzUwODM1NCwiZXhwIjoyMDQzMDg0MzU0fQ.TGdnDvtzbGJoirE7z67M3wXuSdUg4O72CmullV5qdSA"; // Your API key here

    public String uploadImage(File imageFile) throws IOException {
        // Create headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.set("Authorization", "Bearer " + supabaseApiKey); // API key for authentication

        // Create the body with Multipart data
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new FileSystemResource(imageFile)); // "file" is the form field name

        // Create HttpEntity with body and headers
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // Construct the URL for the Supabase Storage API endpoint
        String uploadUrl = supabaseUrl + "/storage/v1/object/product-images/" + imageFile.getName();

        // Use RestTemplate to send POST request
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                uploadUrl,
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        // Handle the response
        if (response.getStatusCode() == HttpStatus.OK) {
            // Assuming the response body contains JSON with the "Key" field
            JSONObject jsonResponse = new JSONObject(response.getBody());
            String imagePath = jsonResponse.getString("Key"); // Extract the "Key" (image path)
            return imagePath; // Return the image path (e.g., "product-images/lebron 10 side.png")
        } else {
            // Handle error scenarios, e.g., log or throw an exception
            throw new RuntimeException("Failed to upload image to Supabase Storage");
        }
    }
}
