package com.warhammer.ecom.config;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/***
 * Configuration to interact with the AWS bucket used to stock the products' images.
 */
@Configuration
public class S3Config {
    @Bean
    @Profile("prod")
    public AmazonS3 amazonS3(@Value("${cloud.aws.region.static}") String region) {
        return AmazonS3ClientBuilder.standard().withRegion(region).build();
    }
}
