package com.nive.prjt.com.file.service.impl;

import com.nive.prjt.com.file.service.ComFileUploadService;
import com.nive.prjt.config.AwsS3Config;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;


/*테스트 시 access key 및 secret key, bucket name 지정이 꼭 필요합니다.*/
@ActiveProfiles("s3")
@SpringBootTest
@Import(AwsS3Config.class)
class ComFileS3UploadServiceSpringTest {


    private final ComFileUploadService comFileUploadService;

    @Autowired
    public ComFileS3UploadServiceSpringTest(ComFileUploadService comFileUploadService) {
        this.comFileUploadService = comFileUploadService;
    }

    @Test
    @DisplayName("S3에 파일 업로드 테스트")
    public void testUploadFileToS3() throws IOException {

        String content = "This is a test file content";
        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "file", "test-upload.txt", "text/plain", content.getBytes());

        String filePath = "/test";  // S3에 저장될 경로
        String fileName = "test-upload.txt";  // S3에 저장될 경로

        // 실제 업로드 테스트
        boolean uploadResult = comFileUploadService.uploadFile(mockMultipartFile, filePath,fileName);

        // 업로드가 성공적으로 되었는지 확인
        assertTrue(uploadResult);

        // 업로드된 파일이 S3에 존재하는지 확인
        boolean fileExists = comFileUploadService.isFileExist(filePath);
        assertTrue(fileExists);

        // 파일 삭제 후 테스트 종료
        comFileUploadService.deleteFile(filePath);
        assertFalse(comFileUploadService.isFileExist(filePath));
    }
}