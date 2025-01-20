package com.nive.prjt.com.file.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.DisplayName;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("local")
@ExtendWith(MockitoExtension.class)
class ComFileLocalUploadServiceMockingTest {

    @Mock
    private MultipartFile file;  // MultipartFile Mock 객체

    private ComFileLocalUploadService comFileUploadService;

    @BeforeEach
    void setUp() {
        comFileUploadService = new ComFileLocalUploadService();
        comFileUploadService.fileStoragePath = "/tmp/upload"; // 수동으로 경로 설정
    }

    @Test
    @DisplayName("파일 업로드 성공 테스트")
    void testUploadFileSuccess() throws IOException {
        // given
        String filePath = "test-file.txt";

        // when
        boolean result = comFileUploadService.uploadFile(file, filePath);

        // then
        assertTrue(result);
        verify(file, times(1)).transferTo(any(File.class)); // transferTo 메서드가 한번 호출되었는지 확인

    }


    @Test
    @DisplayName("파일이 존재하는지 확인하는 테스트-false")
    void testIsFileExist() throws IOException {
        // given
        String filePath = "existing-file.txt";

        // when
        boolean result = comFileUploadService.isFileExist(filePath);

        // then
        assertFalse(result);
    }

    @Test
    @DisplayName("파일이 존재하는지 확인하는 테스트-true")
    void testIsFileExistTrue() throws IOException {
        // 임시 파일 경로 지정
        String uploadFilePath = "/tmp/upload/test-file.txt";
        String content = "Test content for file upload.";
        String filePath = "test-file.txt";
        // 임시 파일 생성
        File file = new File(filePath);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(uploadFilePath))) {
            writer.write(content);
        }
        // 파일 존재 여부 확인(tmp/upload/test-file
        boolean result = comFileUploadService.isFileExist(filePath);

        // 결과 확인
        assertTrue(result);

    }

    @Test
    @DisplayName("파일 삭제 테스트")
    void testDeleteFile() throws IOException {
        // given
        String filePath = "test-file.txt";

        // when
        boolean result = comFileUploadService.deleteFile(filePath);

        // then
        assertTrue(result);
    }

}