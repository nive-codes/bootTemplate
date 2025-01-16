package com.nive.prjt.com.file.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import com.nive.prjt.com.file.domain.ComFileDomain;
import com.nive.prjt.com.file.mapper.ComFileMetaMapper;
import com.nive.prjt.com.idgen.ComTableIdGnrService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ComFileMetaServiceImplTest {
    @Mock
    private ComFileMetaMapper comFileMetaMapper;

    @Mock
    private ComTableIdGnrService fileIdGenService;

    @InjectMocks
    private ComFileMetaServiceImpl comFileMetaService;

    private ComFileDomain comFileDomain;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Mockito 초기화

        comFileDomain = new ComFileDomain();
        comFileDomain.setFileId("FILE_0001");
        comFileDomain.setFileSeq(1);
        comFileDomain.setFilePath("/path/to/file");
    }

    @Test
    @DisplayName("파일 메타 정보를 성공적으로 삽입하고 ID를 반환한다.")
    public void testInsertFileMeta() {
        // give
        when(fileIdGenService.getNextId()).thenReturn("FILE_0002");  // ID 생성 mock
        doNothing().when(comFileMetaMapper).insertFileMeta(any(ComFileDomain.class)); // DB Insert mock

        // when
        String fileId = comFileMetaService.insertFileMeta(comFileDomain);
        System.out.println("fileId = " + comFileDomain.getFileId());
        System.out.println("fileId = " + fileId);
        // then
        assertNotNull(fileId);
        assertEquals("FILE_0002", fileId);
        verify(fileIdGenService, times(1)).getNextId();  // ID 생성 함수가 한 번 호출되었는지 확인
        verify(comFileMetaMapper, times(1)).insertFileMeta(comFileDomain);  // insert 함수가 한 번 호출되었는지 확인
    }

    @Test
    @DisplayName("파일 메타 정보를 업데이트할 때, 파일 시퀀스가 올바르게 설정된다.")
    public void testUpdateFileMeta() {
        // give
        when(comFileMetaMapper.selectMaxFileSeq("FILE_0001")).thenReturn(2); // 파일 시퀀스 최대값 mock
        doNothing().when(comFileMetaMapper).updateFileMeta(any(ComFileDomain.class)); // DB Update mock

        // when
        comFileMetaService.updateFileMeta(comFileDomain);

        // then
        verify(comFileMetaMapper, times(1)).selectMaxFileSeq("FILE_0001");  // selectMaxFileSeq 호출 여부 확인
        verify(comFileMetaMapper, times(1)).updateFileMeta(comFileDomain);  // updateFileMeta 호출 여부 확인
    }

    @Test
    @DisplayName("파일 ID로 파일 메타 정보를 정상적으로 조회할 수 있다.")
    public void testSelectFileMeta() {
        // give
        when(comFileMetaMapper.selectLatestFileMeta("FILE_0001")).thenReturn(comFileDomain);  // 파일 메타 정보 mock

        // when
        ComFileDomain result = comFileMetaService.selectLatestFileMeta("FILE_0001");

        // then
        assertNotNull(result, "조회된 파일 메타 정보가 null이 아니다.");
        assertEquals("FILE_0001", result.getFileId(), "조회된 파일 ID가 예상한 값과 일치하지 않는다.");
        assertEquals("/path/to/file", result.getFilePath(), "조회된 파일 경로가 예상한 값과 일치하지 않는다.");
        verify(comFileMetaMapper, times(1)).selectLatestFileMeta("FILE_0001");  // selectFileMeta 호출 여부 확인
    }

    @Test
    @DisplayName("파일 ID로 파일 경로를 정상적으로 조회할 수 있다.")
    public void testSelectFileMetaPath() {
        // give
        when(comFileMetaMapper.selectLatestFileMetaPath("FILE_0001")).thenReturn("/path/to/file");  // 경로 정보 mock

        // when
        String filePath = comFileMetaService.selectLatestFileMetaPath("FILE_0001");

        // then
        assertNotNull(filePath, "조회된 파일 경로가 null이 아니다.");
        assertEquals("/path/to/file", filePath, "조회된 파일 경로가 예상한 값과 일치하지 않는다.");
        verify(comFileMetaMapper, times(1)).selectLatestFileMetaPath("FILE_0001");  // selectFileMetaPath 호출 여부 확인
    }

    @Test
    @DisplayName("파일 메타 리스트를 성공적으로 삭제할 수 있다.")
    public void testDeleteFileMetaList() {
        // give
        doNothing().when(comFileMetaMapper).deleteFileMetaList("FILE_0001");  // delete mock

        // when
        comFileMetaService.deleteFileMetaList("FILE_0001");

        // then
        verify(comFileMetaMapper, times(1)).deleteFileMetaList("FILE_0001");  // deleteFileMetaList 호출 여부 확인
    }

    @Test
    @DisplayName("특정 파일 메타를 삭제할 수 있다.")
    public void testDeleteFileMeta() {
        // give
        doNothing().when(comFileMetaMapper).deleteFileMetaList("FILE_0001");  // delete mock

        // when
        comFileMetaService.deleteFileMeta("FILE_0001", 1);

        // then
        verify(comFileMetaMapper, times(1)).deleteFileMetaList("FILE_0001");  // deleteFileMeta 호출 여부 확인
    }

    @Test
    @DisplayName("파일 ID로 파일 메타 리스트를 정상적으로 조회할 수 있다.")
    public void testSelectFileMetaList() {
        // give
        List<ComFileDomain> fileList = List.of(comFileDomain);
        when(comFileMetaMapper.selectFileMetaList("FILE_0001")).thenReturn(fileList);  // 파일 리스트 mock

        // when
        List<ComFileDomain> result = comFileMetaService.selectFileMetaList("FILE_0001");

        // then
        assertNotNull(result, "파일 메타 리스트가 null이 아니다.");
        assertEquals(1, result.size(), "파일 메타 리스트의 크기가 예상한 값과 일치하지 않는다.");
        verify(comFileMetaMapper, times(1)).selectFileMetaList("FILE_0001");  // selectFileMetaList 호출 여부 확인
    }
}