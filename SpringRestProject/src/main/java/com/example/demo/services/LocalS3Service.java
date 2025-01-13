package com.example.demo.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class LocalS3Service {
	
	@Value("${local.s3-bucket-path}")
	private String localS3BucketPath;
	
	public void uploadFile(String bucketName, String fileName, MultipartFile file) throws IOException{
		// Create bucket directory if it doesn't exist
		Path bucketPath = Paths.get(localS3BucketPath, bucketName);
		if(!Files.exists(bucketPath)) {
			Files.createDirectories(bucketPath);
		}
	
		Path filePath = bucketPath.resolve(fileName);
		Files.write(filePath, file.getBytes());
		
	}
	
	public byte[] downloadFile(String bucketName, String fileName) throws IOException{
		
		// Retrieve the file from the bucket directory
		Path filePath = Paths.get(localS3BucketPath, bucketName, fileName);
		return Files.readAllBytes(filePath);
	}
	
	
	

}
