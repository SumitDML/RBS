package com.dml.project.rbs.service;

import com.amazonaws.HttpMethod;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface AwsS3Service {
    String uploadFile(MultipartFile file);
    byte[] downloadFile(String fileName);
    String deleteFile(String fileName);

    File convertMultiPartFileToFile(MultipartFile file);
    String generatePreSignedUrl(String filePath,
                               String bucketName,
                               HttpMethod httpMethod);
}
