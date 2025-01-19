package com.nive.prjt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

/**
 * @author hosikchoi
 * @class AwsS3Config
 * @desc S3 Client Bulder Bean 생성
 * @since 2025-01-19
 */
@Configuration
public class AwsS3Config {

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .region(Region.AP_NORTHEAST_2)  // 서울 리전
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create("your-access-key", "your-secret-key")
                ))
                .build();
    }
}
