package com.sqe.finals.controller;

import com.sqe.finals.service.SupabaseImageUploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/images")
public class ImageUploadController {

    private static final Logger logger = LoggerFactory.getLogger(ImageUploadController.class);

    @Autowired
    private SupabaseImageUploadService imageUploadService;

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadImage(@RequestParam("ImageTop") MultipartFile imageTop,
                                                           @RequestParam("ImageSide") MultipartFile imageSide) {
        Map<String, String> response = new HashMap<>();
        try {
            // Log the start of the upload process
            logger.info("Received image upload request: Top image: {}, Side image: {}",
                    imageTop.getOriginalFilename(), imageSide.getOriginalFilename());

            // Generate unique file paths (e.g., using UUID)
            String filePathTop = "uploads/" + UUID.randomUUID().toString() + ".jpg";
            String filePathSide = "uploads/" + UUID.randomUUID().toString() + ".jpg";

            logger.info("Generated file paths: Top - {}, Side - {}", filePathTop, filePathSide);

            // Convert MultipartFile to File
            File imageTopFile = convertMultipartToFile(imageTop);
            File imageSideFile = convertMultipartToFile(imageSide);

            logger.info("Converted MultipartFile to File: Top image - {}, Side image - {}",
                    imageTopFile.getAbsolutePath(), imageSideFile.getAbsolutePath());

            // Upload the files to Supabase and get the paths (not full URLs)
            String imageTopPath = imageUploadService.uploadImage(imageTopFile);  // Use the service to upload
            String imageSidePath = imageUploadService.uploadImage(imageSideFile);  // Use the service to upload

            // Log the success of the upload
            logger.info("Files uploaded successfully. Top image path: {}, Side image path: {}", imageTopPath, imageSidePath);

            // Store the paths in the response map (not full URLs yet)
            response.put("topImagePath", imageTopPath);  // Updated to match the frontend expectation
            response.put("sideImagePath", imageSidePath);  // Updated to match the frontend expectation

            // Return the response as JSON (Spring automatically converts Map to JSON)
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Log the error
            logger.error("Failed to upload image", e);
            response.put("error", "Failed to upload image: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    private File convertMultipartToFile(MultipartFile file) throws IOException {
        // Convert MultipartFile to File
        File convertedFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
        file.transferTo(convertedFile);
        return convertedFile;
    }
}

