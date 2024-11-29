package com.warhammer.ecom.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Service
@Profile("prod")
public class S3ImageService {

    private final AmazonS3 amazonS3;

    private final String bucketName;

    public S3ImageService(AmazonS3 amazonS3, @Value("${cloud.aws.bucket.name}") String bucketName) {
        this.amazonS3 = amazonS3;
        this.bucketName = bucketName;
    }

    public String upload(MultipartFile file) {
        final String fileName = "pi_" + file.getName();

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());

        try (InputStream inputStream = file.getInputStream()) {
            amazonS3.putObject(bucketName, fileName, inputStream, objectMetadata);
        } catch (IOException e) {
            return null;
        }

        return amazonS3.getUrl(bucketName, fileName).toString();
    }

    public void delete(String URL) {
        try {
            amazonS3.deleteObject(bucketName, URL);
        } catch (AmazonS3Exception e) {
            e.printStackTrace();
        }
    }
}
