package org.example.service;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.example.utility.Constants.IMAGEBBAPIKEY;

public class ImageUploader {

    public String uploadImage(byte[] imageBytes) throws IOException {
        // Encode image bytes to Base64
        String encodedImage = Base64.getEncoder().encodeToString(imageBytes);

        // Create HttpClient instance
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {

            // Create POST request
            HttpPost uploadFile = new HttpPost("https://api.imgbb.com/1/upload");

            // Build the request body
            HttpEntity entity = MultipartEntityBuilder.create()
                    .addTextBody("key", IMAGEBBAPIKEY, org.apache.http.entity.ContentType.TEXT_PLAIN.withCharset(StandardCharsets.UTF_8))
                    .addTextBody("image", encodedImage, org.apache.http.entity.ContentType.TEXT_PLAIN.withCharset(StandardCharsets.UTF_8))
                    .build();

            uploadFile.setEntity(entity);

            // Execute the request
            HttpResponse response = httpClient.execute(uploadFile);
            HttpEntity responseEntity = response.getEntity();

            if (responseEntity != null) {
                String responseString = EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);
                // Parse the JSON response to get the image URL
                String imageUrl = parseImageUrl(responseString);
                return imageUrl;
            }
        }
        return null;
    }

    private static String parseImageUrl(String jsonResponse) {
        // Implement JSON parsing here to extract the image URL from the response
        // Example using regex (not recommended for complex JSON parsing)
        String regex = "\"url\":\"(.*?)\"";
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regex);
        java.util.regex.Matcher matcher = pattern.matcher(jsonResponse);
        if (matcher.find()) {
            return matcher.group(1).replace("\\", "");
        }
        return null;
    }

}
