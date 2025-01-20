package com.nive.prjt.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

/**
 * @author nive
 * @class AwsS3Config
 * @desc S3 Client Builder Bean 생성
 * @since 2025-01-19
 */
@Configuration
@Profile("s3")
public class AwsS3Config {


    @Value("${spring.file-storage.access-key}")
    private String accessKey;

    @Value("${spring.file-storage.secret-key}")
    private String secretKey;

    @Value("${spring.file-storage.region}") // 기본값 서울 리전으로 설정
    private String region;

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .region(Region.of(region))  // YML에서 읽어온 리전 적용
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)
                ))
                .build();
    }
}