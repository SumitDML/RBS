package com.dml.project.rbs.controller;

import com.amazonaws.HttpMethod;
import com.dml.project.rbs.service.AwsS3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/RBS")
public class AwsS3Controller {

    @Autowired
    private AwsS3Service service;


    @PreAuthorize("hasRole('Admin')")
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam(value = "file") MultipartFile file) {
        return new ResponseEntity<>(service.uploadFile(file), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('Admin')")
    @GetMapping("/download/{fileName}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable String fileName) {
        byte[] data = service.downloadFile(fileName);
        ByteArrayResource resource = new ByteArrayResource(data);
        return ResponseEntity
                .ok()
                .contentLength(data.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }

    @PreAuthorize("hasRole('Admin')")
    @DeleteMapping("/delete/{fileName}")
    public ResponseEntity<String> deleteFile(@PathVariable String fileName) {
        return new ResponseEntity<>(service.deleteFile(fileName), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('Admin')")
    @GetMapping("/generate-upload-url")
    public ResponseEntity<String> generateUploadUrl() {
        return ResponseEntity.ok(
                service.generatePreSignedUrl("/home/dml-sumit/Desktop/Project/RBS/Restaurant_Billing_System/src/main/resources/Restaurant Bill.pdf", "restaurantbill", HttpMethod.PUT));
    }


}

