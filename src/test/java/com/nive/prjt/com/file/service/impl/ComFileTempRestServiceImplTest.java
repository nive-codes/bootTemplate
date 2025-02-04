package com.nive.prjt.com.file.service.impl;
import com.nive.prjt.com.file.domain.ComFileTempDomain;
import com.nive.prjt.com.file.dto.ComFileTempDeleteRequest;
import com.nive.prjt.com.file.dto.ComFileTempDomainRequest;
import com.nive.prjt.com.file.service.ComFileTempMetaService;
import com.nive.prjt.com.file.service.ComFileUploadService;
import com.nive.prjt.config.exception.business.BusinessException;
import com.nive.prjt.config.response.ApiCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ComFileTempRestServiceImplTest {

    @InjectMocks
    private ComFileTempRestServiceImpl comFileTempRestServiceImpl;

    @Mock
    private ComFileTempMetaService comFileTempMetaService;

    @Mock
    private ComFileUploadService comFileUploadService;

    @Mock
    private MultipartFile mockFile;

    private ComFileTempDomain comFileTempDomain;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // 테스트용 ComFileTempDomain 설정
        comFileTempDomain = ComFileTempDomain.builder().build();
        comFileTempDomain.setFileId("testFileId");
        comFileTempDomain.setFilePath("/test/path");
        comFileTempDomain.setFileUpldNm("testFileName.jpg");
        comFileTempDomain.setFileSeq(1);
    }

    @Test
    void testUploadFileTemp() throws Exception {
        // Mock MultipartFile behavior
        when(mockFile.getOriginalFilename()).thenReturn("testFileName.jpg");
        when(mockFile.getSize()).thenReturn(1024L);

        // Mock methods of ComFileUploadService
        when(comFileUploadService.uploadFile(any(), any(), any())).thenReturn(true);

        comFileTempDomain.setFileId(null);  //fileId가 없는 상태로 insert 시 fileID 확인 필요

        // Mock methods of ComFileTempMetaService
        when(comFileTempMetaService.insertFileTempMeta(any())).thenReturn("newFileId");

//        comFileTempDomain.setFileType("IMAGE"); //ComFileType 대응
        ComFileTempDomainRequest comFileTempDomainRequest = new ComFileTempDomainRequest();
        comFileTempDomainRequest.setFileSize(10L);
        comFileTempDomainRequest.setFilePath("/test/path");
        comFileTempDomainRequest.setFileType("IMAGE");
        // Act
        ComFileTempDomain resultDomain = comFileTempRestServiceImpl.uploadFileTemp(new MultipartFile[]{mockFile}, comFileTempDomainRequest);

        // Assert
        assertNotNull(resultDomain.getFileId());
        assertEquals("newFileId", resultDomain.getFileId());
        verify(comFileUploadService, times(1)).uploadFile(any(), any(), any());
        verify(comFileTempMetaService, times(1)).insertFileTempMeta(any());
    }

    @Test
    void testUploadFileTempFileIdSame() throws Exception {
        // Mock MultipartFile behavior
        when(mockFile.getOriginalFilename()).thenReturn("testFileName.jpg");
        when(mockFile.getSize()).thenReturn(1024L);

        // Mock methods of ComFileUploadService
        when(comFileUploadService.uploadFile(any(), any(), any())).thenReturn(true);




        ComFileTempDomainRequest comFileTempDomainRequest = new ComFileTempDomainRequest();
        // 기존 fileId가 있을 때
        comFileTempDomainRequest.setFileId("testFileId");
        comFileTempDomainRequest.setFileSize(10L);
        comFileTempDomainRequest.setFilePath("/test/path");
        comFileTempDomainRequest.setFileType("IMAGE");

        // Mock methods of ComFileTempMetaService
        // insertFileTempMeta는 호출되지 않기 때문에 Mock을 설정하지 않음.

//        comFileTempDomain.setFileType("IMAGE"); // ComFileType 대응

        // Act
        ComFileTempDomain resultDomain= comFileTempRestServiceImpl.uploadFileTemp(new MultipartFile[]{mockFile}, comFileTempDomainRequest);

        // Assert
        assertNotNull(resultDomain.getFileId());  // fileId가 null이 아니어야 함
        assertEquals("testFileId", resultDomain.getFileId());  // 기존 fileId가 그대로 반환되어야 함
        verify(comFileUploadService, times(1)).uploadFile(any(), any(), any());  // 파일 업로드 메서드는 1번 호출되어야 함
        verify(comFileTempMetaService, times(0)).insertFileTempMeta(any());  // insertFileTempMeta는 호출되지 않아야 함
    }

    @Test
    void testUploadFileTemp_noFile() {
        // When no files are provided
        BusinessException thrown = assertThrows(BusinessException.class, () -> {
            comFileTempRestServiceImpl.uploadFileTemp(new MultipartFile[]{}, new ComFileTempDomainRequest());
        });

        assertEquals("존재하지 않는 파일입니다.", thrown.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, thrown.getHttpStatus());
    }

    @Test
    void testDeleteFileTemp_success() {
        // Mock for deleting file from storage
        when(comFileTempMetaService.selectFileTempMeta("testFileId", 1)).thenReturn(comFileTempDomain);
        when(comFileUploadService.isFileExist(any())).thenReturn(true);
        when(comFileUploadService.deleteFile(any())).thenReturn(true);
        ComFileTempDeleteRequest comFileTempDeleteRequest = new ComFileTempDeleteRequest();
        comFileTempDeleteRequest.setFileId("testFileId");
        comFileTempDeleteRequest.setFileSeq(1);
        // Act
        comFileTempRestServiceImpl.deleteFileTemp("testFileId",comFileTempDeleteRequest);

        // Assert
        verify(comFileTempMetaService, times(1)).deleteFileTempMeta(any());
        verify(comFileUploadService, times(1)).deleteFile(any());
    }

    @Test
    void testDeleteFileTemp_fileNotFound() {
        // Mock behavior when file does not exist
        when(comFileTempMetaService.selectFileTempMeta("testFileId", 1)).thenReturn(null);
        ComFileTempDeleteRequest comFileTempDeleteRequest = new ComFileTempDeleteRequest();
        comFileTempDeleteRequest.setFileId("testFileId");
        comFileTempDeleteRequest.setFileSeq(1);
        // Act & Assert
        BusinessException thrown = assertThrows(BusinessException.class, () -> {
            comFileTempRestServiceImpl.deleteFileTemp("testFileId",comFileTempDeleteRequest);
        });

        assertEquals("TEMP 테이블의 데이터를 찾을 수 없습니다", thrown.getMessage());
        assertEquals(ApiCode.NOT_FOUND, thrown.getApiCode());
    }

    @Test
    void testDeleteFileTemp_fileDeletionFailed() {
        // Mock behavior for file not being deleted
        when(comFileTempMetaService.selectFileTempMeta("testFileId", 1)).thenReturn(comFileTempDomain);
        when(comFileUploadService.isFileExist(any())).thenReturn(true);
        when(comFileUploadService.deleteFile(any())).thenReturn(false);
        ComFileTempDeleteRequest comFileTempDeleteRequest = new ComFileTempDeleteRequest();
        comFileTempDeleteRequest.setFileId("testFileId");
        comFileTempDeleteRequest.setFileSeq(1);
        // Act & Assert
        BusinessException thrown = assertThrows(BusinessException.class, () -> {
            comFileTempRestServiceImpl.deleteFileTemp("testFileId",comFileTempDeleteRequest);
        });

        assertEquals("존재하는 파일 삭제하지 못했습니다.", thrown.getMessage());
        assertEquals(ApiCode.VALIDATION_FAILED, thrown.getApiCode());
    }
}