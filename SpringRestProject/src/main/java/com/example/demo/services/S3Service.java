//package com.example.demo.services;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.util.UUID;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//import com.amazonaws.auth.AWSStaticCredentialsProvider;
//import com.amazonaws.auth.BasicAWSCredentials;
//import com.amazonaws.services.s3.AmazonS3;
//import com.amazonaws.services.s3.AmazonS3ClientBuilder;
//import com.amazonaws.services.s3.model.PutObjectRequest;
//
//
//import org.springframework.web.multipart.MultipartFile;
//
//@Service
//public class S3Service {
//
//    private final AmazonS3 s3Client;
//
//    @Value("${aws.access.key}")
//    private String awsAccessKey;
//
//    @Value("${aws.secret.key}")
//    private String awsSecretKey;
//
//    @Value("${aws.region}")
//    private String awsRegion;
//
//    @Value("${aws.s3.bucket.name}")
//    private String s3BucketName;
//
//    public S3Service() {
//        BasicAWSCredentials credentials = new BasicAWSCredentials(awsAccessKey, awsSecretKey);
//        this.s3Client = AmazonS3ClientBuilder.standard()
//                .withRegion(awsRegion)
//                .withCredentials(new AWSStaticCredentialsProvider(credentials))
//                .build();
//    }
//
//    public String uploadFile(MultipartFile file) throws IOException {
//        // Generate a unique file name
//        String fileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
//
//        // Create a temporary file to store the file data
//        Path tempFile = Files.createTempFile(fileName, ".tmp");
//        file.transferTo(tempFile.toFile());
//
//        // Upload the file to S3
//        s3Client.putObject(new PutObjectRequest(s3BucketName, fileName, tempFile.toFile()));
//
//        // Clean up the temporary file
//        Files.delete(tempFile);
//
//        // Return the URL of the uploaded file
//        return s3Client.getUrl(s3BucketName, fileName).toString();
//    }
//}
