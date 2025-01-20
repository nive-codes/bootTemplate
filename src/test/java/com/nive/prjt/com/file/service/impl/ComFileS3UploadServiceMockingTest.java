package com.nive.prjt.com.file.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import software.amazon.awssdk.core.sync.RequestBody;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("s3")
@ExtendWith(MockitoExtension.class)
class ComFileS3UploadServiceMockingTest {

    @Mock
    private S3Client mockS3Client; // mock 객체 주입

    @InjectMocks
    private ComFileS3UploadService comFileS3UploadService;  // 실제 서비스(mock을 주입받아서 생성)

    @BeforeEach
    void setUp() {
        // mock 객체가 제대로 주입되도록 초기화
        // Spring Boot의 @MockBean과 @InjectMocks가 자동으로 처리하므로 필요하지 않지만 예시로 남겨둠
    }

    @Test
    @DisplayName("S3에 파일 업로드 성공 테스트")
    public void testUploadFile() throws IOException {
        // 준비: MultipartFile mock
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getInputStream()).thenReturn(new ByteArrayInputStream("test content".getBytes()));
        when(mockFile.getSize()).thenReturn(12L);

        String filePath = "test/path/to/file.txt";

        // mock S3Client의 putObject 메서드 호출을 모의
        PutObjectResponse mockPutObjectResponse = PutObjectResponse.builder().build();
        when(mockS3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class)))
                .thenReturn(mockPutObjectResponse);

        // 메서드 실행
        boolean result = comFileS3UploadService.uploadFile(mockFile, filePath);

        // 검증: 업로드가 성공적으로 이루어졌는지
        assertTrue(result);

        // verify: S3의 putObject 메서드가 호출되었는지 확인
        verify(mockS3Client, times(1)).putObject(any(PutObjectRequest.class), any(RequestBody.class));
    }

    @Test
    @DisplayName("S3에서 파일 다운로드 성공 테스트")
    public void testSelectFile() throws IOException {
        String filePath = "test/path/to/file.txt";

        // mock: GetObjectResponse를 반환하는 부분을 설정
        GetObjectResponse mockResponse = mock(GetObjectResponse.class);

        // mock: S3의 getObject 메서드가 반환할 GetObjectResponse를 mock
        when(mockS3Client.getObject(any(GetObjectRequest.class), any(Path.class)))
                .thenReturn(mockResponse);  // GetObjectResponse 반환

        // 메서드 실행
        File downloadedFile = comFileS3UploadService.selectFile(filePath);

        // 검증: 다운로드된 파일이 null이 아닌지 확인
        assertNotNull(downloadedFile);
        assertTrue(downloadedFile.exists());

        // verify: S3의 getObject 메서드가 호출되었는지 확인
        verify(mockS3Client, times(1)).getObject(any(GetObjectRequest.class), any(Path.class));
    }

    @Test
    @DisplayName("S3에서 파일 삭제 성공 테스트")
    public void testDeleteFile() {
        String filePath = "test/path/to/file.txt";

        // mock: deleteObject 메서드가 반환하는 DeleteObjectResponse를 mock
        DeleteObjectResponse mockResponse = mock(DeleteObjectResponse.class);
        when(mockS3Client.deleteObject(any(DeleteObjectRequest.class))).thenReturn(mockResponse);

        // 메서드 실행
        boolean result = comFileS3UploadService.deleteFile(filePath);

        // 검증: 삭제가 성공적으로 이루어졌는지
        assertTrue(result);

        // verify: S3의 deleteObject 메서드가 호출되었는지 확인
        verify(mockS3Client, times(1)).deleteObject(any(DeleteObjectRequest.class));
    }

    @Test
    @DisplayName("S3에서 파일 존재 여부 확인 테스트")
    public void testIsFileExist() {
        String filePath = "test/path/to/file.txt";

        // mock: getObject 메서드를 호출하여 파일이 존재한다고 가정
        when(mockS3Client.getObject(any(GetObjectRequest.class))).thenReturn(null);

        // 메서드 실행
        boolean result = comFileS3UploadService.isFileExist(filePath);

        // 검증: 파일이 존재한다고 반환되는지
        assertTrue(result);

        // verify: S3의 getObject 메서드가 호출되었는지 확인
        verify(mockS3Client, times(1)).getObject(any(GetObjectRequest.class));
    }
}