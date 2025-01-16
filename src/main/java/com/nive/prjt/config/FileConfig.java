package com.nive.prjt.config;

import com.nive.prjt.com.file.service.impl.ComFileLocalUploadService;
import com.nive.prjt.com.file.service.impl.ComFileS3UploadService;
import com.nive.prjt.com.file.service.ComFileUploadService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author nive
 * @class FileConfig
 * @desc yml의 profile에 따른 FileUpload bean 수동 생성입니다.
 * @since 2025-01-16
 */
@Configuration
public class FileConfig {

    @Bean
    @Profile("local") // local 프로파일에서만 사용
    public ComFileUploadService fileLocalUploadService() {
        return new ComFileLocalUploadService();
    }

    @Bean
    @Profile("s3") // s3 프로파일에서만 사용
    public ComFileUploadService fileS3UploadService() {
        return new ComFileS3UploadService();
    }
}


