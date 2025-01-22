package com.nive.prjt.com.file.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import com.nive.prjt.com.file.domain.ComFileDomain;
import com.nive.prjt.com.file.service.ComFileMetaService;
import com.nive.prjt.com.file.service.ComFileType;
import com.nive.prjt.com.file.service.ComFileUploadService;
import com.nive.prjt.config.exception.business.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Set;

import static org.mockito.Mockito.*;

class ComFileServiceImplTest {

    @InjectMocks
    private ComFileServiceImpl comFileService;

    @Mock
    private ComFileMetaService comFileMetaService;

    @Mock
    private ComFileUploadService comFileUploadService;

    @BeforeEach
    void setUp() {
        // Mockito 객체 초기화
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("파일 업로드 성공 시 파일 ID 반환")
    void uploadFile_ValidFile_Success() {
        // given: 테스트 데이터 설정
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "sample content".getBytes());
        String filePath = "testPath";
        String fileId = null;
        ComFileType fileType = mock(ComFileType.class);
        long maxSizeMB = 10;
        Set<String> allowedExtensions = Set.of(".txt");

        // when: Mock 객체 설정 (파일 확장자와 업로드 서비스 동작 정의)

        // 1. 파일 확장자를 검증하는 메서드의 반환값을 정의
        // fileType.getAllowedExtensions() 메서드가 호출되면 allowedExtensions(Set.of(".txt"))를 반환하도록 설정
        // 실제 코드에서 fileType 객체가 확장자를 검증할 때 사용할 확장자 목록을 정의하는 부분
        when(fileType.getAllowedExtensions()).thenReturn(allowedExtensions);

        // 2. 파일 업로드가 정상적으로 된 것으로 가정
        // comFileUploadService는 Mock 객체로, 실제 파일 업로드 작업을 수행하지 않고, 해당 메서드가 호출되면 true를 반환
        // 이는 파일 업로드가 성공적으로 처리되었다는 가정
        when(comFileUploadService.uploadFile(file, filePath)).thenReturn(true);

        // 3. insertFileMeta 메서드가 호출될 때, "generatedFileId"라는 값이 반환되도록 설정
        // comFileMetaService는 Mock 객체로, 실제 데이터베이스와 상호작용하지 않고, 해당 메서드가 호출되면 "generatedFileId"를 반환하도록 설정
        // insertFileMeta는 파일 메타 정보를 DB에 저장하는 메서드인데, 테스트에서는 실제 DB 작업을 하지 않고, 대신 "generatedFileId"라는 더미값을 반환하도록 설정
        // 이는 파일 메타 데이터를 DB에 삽입할 때 반환되는 파일 ID를 테스트에서 가정하는 부분
        when(comFileMetaService.insertFileMeta(any(ComFileDomain.class))).thenReturn("generatedFileId");

        // when: 실제 메서드 호출
        String resultFileId = comFileService.uploadFile(file, filePath, fileId, fileType, maxSizeMB);

        // then: 결과 검증
        assertNotNull(resultFileId, "파일 ID가 반환되어야 합니다.");
        verify(comFileUploadService, times(1)).uploadFile(file, filePath);  // 파일 업로드가 1번 호출되었는지 검증
        verify(comFileMetaService, times(1)).insertFileMeta(any(ComFileDomain.class));  // 파일 메타 데이터 삽입이 1번 호출되었는지 검증
    }

    @Test
    @DisplayName("잘못된 파일 확장자 시 예외 발생")
    void uploadFile_InvalidFile_ThrowsException() {
        // given: 테스트 데이터 설정 (지원되지 않는 확장자)
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "sample content".getBytes());
        String filePath = "testPath";
        String fileId = null;
        ComFileType fileType = mock(ComFileType.class);
        long maxSizeMB = 10;

        // when: Mock 객체 설정 (지원되지 않는 확장자 지정)
        when(fileType.getAllowedExtensions()).thenReturn(Set.of(".jpg")); // 파일 확장자를 ".jpg"로 설정 (실제 파일은 ".txt")

        // when & then: 파일 업로드 시 예외 발생을 예상하고 검증
        BusinessException exception = assertThrows(BusinessException.class, () ->
                comFileService.uploadFile(file, filePath, fileId, fileType, maxSizeMB)
        );
        assertEquals("파일 업로드가 정상적으로 처리되지 못했습니다.", exception.getMessage());  // 예외 메시지 확인
        verify(comFileUploadService, never()).uploadFile(file, filePath);  // 파일 업로드 메서드는 호출되지 않았음을 검증
    }

    @Test
    @DisplayName("파일 삭제 성공")
    void deleteFileList_Success() {
        // given: 테스트 데이터 설정 (삭제할 파일 ID)
        String fileId = "testFileId";

        // when: Mock 객체 설정 (삭제가 정상적으로 처리됨)
        doNothing().when(comFileMetaService).deleteFileMetaList(fileId);

        // when: 실제 메서드 호출
        boolean result = comFileService.deleteFileList(fileId);

        // then: 결과 검증
        assertTrue(result, "파일 삭제가 성공해야 합니다.");
        verify(comFileMetaService, times(1)).deleteFileMetaList(fileId);  // 파일 삭제 메서드가 1번 호출되었는지 검증
    }

    @Test
    @DisplayName("파일 삭제 실패")
    void deleteFileList_Failure() {
        // given: 테스트 데이터 설정 (삭제할 파일 ID)
        String fileId = "testFileId";

        // when: Mock 객체 설정 (삭제 실패 시 예외 발생)
        doThrow(new RuntimeException("Delete failed")).when(comFileMetaService).deleteFileMetaList(fileId); // 예외가 발생하도록 설정

        // when: 실제 메서드 호출
        boolean result = comFileService.deleteFileList(fileId);

        // then: 결과 검증
        assertFalse(result, "파일 삭제가 실패해야 합니다."); // 삭제가 실패했음을 검증
        verify(comFileMetaService, times(1)).deleteFileMetaList(fileId);  // 파일 삭제 메서드가 1번 호출되었는지 검증
    }
}