package com.nive.prjt.com.file.service.impl;

import com.nive.prjt.com.file.domain.ComFileDomain;
import com.nive.prjt.com.file.mapper.ComFileMetaMapper;
import com.nive.prjt.com.idgen.ComTableIdGnrService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ComFileMetaServiceImplSpringTest {

    @Autowired
    private ComFileMetaServiceImpl comFileMetaService;

    @Autowired
    private ComFileMetaMapper comFileMetaMapper;

    @Autowired
    private ComTableIdGnrService fileIdGenService;

    private ComFileDomain comFileDomain;

    @BeforeEach
    public void setUp() {
        comFileDomain = new ComFileDomain();
        comFileDomain.setFileUpldNm("testFileUpload.txt");
        comFileDomain.setFileOrignNm("testFileUpload.txt");
        comFileDomain.setFilePath("/path/to/file");
    }

    @Test
    @DisplayName("파일 메타 정보를 성공적으로 삽입하고 ID를 반환한다.")
    public void testInsertFileMeta() {
        // Act
        String fileId = comFileMetaService.insertFileMeta(comFileDomain);

        // Assert
        assertNotNull(fileId, "파일 ID가 null이 아니어야 한다.");

    }

    @Test
    @DisplayName("파일 메타 정보를 업데이트할 때, 파일 시퀀스가 올바르게 설정된다.")
    public void testUpdateFileMeta() {
        // Arrange
        //파일 데이터 생성
        String fileId = comFileMetaService.insertFileMeta(comFileDomain);
        int maxFileSeq = comFileMetaMapper.selectMaxFileSeq(fileId);

        // fileSeq 값을 업데이트
//        comFileDomain.setFileSeq(maxFileSeq + 1);  // 파일 시퀀스 증가
        comFileMetaService.updateFileMeta(comFileDomain);

        int afterMaxFileSeq = comFileMetaMapper.selectMaxFileSeq(fileId);

        System.out.println("maxFileSeq = " + maxFileSeq);
        System.out.println("afterMaxFileSeq = " + afterMaxFileSeq);

        // Assert
        assertEquals(maxFileSeq+1, afterMaxFileSeq, "파일 시퀀스의 업데이트 기능 확인");
    }


    @Test
    @DisplayName("파일 ID로 max(seq)의 파일 메타 정보를 정상적으로 조회할 수 있다.")
    public void testSelectLatestFileMeta() {
        // Arrange
        // 파일 메타 정보 삽입
        String idx = comFileMetaService.insertFileMeta(comFileDomain);  // 먼저 삽입해야 조회가 가능합니다.

        // Act
        ComFileDomain result = comFileMetaService.selectLatestFileMeta(idx);
        System.out.println("idx : "+idx);
        System.out.println("result : "+result);

        // Assert
        assertNotNull(result, "조회된 파일 메타 정보가 null이 아니다.");
        assertEquals(idx, result.getFileId(), "조회된 파일 ID가 예상한 값과 일치한다.");
        assertEquals("/path/to/file/text", result.getFilePath(), "조회된 파일 경로가 예상한 값과 일치한다.");
    }

    @Test
    @DisplayName("파일 ID 및 SEQ로 파일 메타 정보를 정상적으로 조회할 수 있다.")
    public void testSelectFileMeta() {

        // Arrange
        // 파일 메타 정보 삽입
        comFileDomain = new ComFileDomain();
        comFileDomain.setFileUpldNm("testFileUpload1.txt");
        comFileDomain.setFileOrignNm("testFileUpload1.txt");
        comFileDomain.setFilePath("/path/to/file");
        String idx = comFileMetaService.insertFileMeta(comFileDomain);  // 먼저 삽입해야 조회가 가능합니다.

        ComFileDomain comFileDomain2 = new ComFileDomain();
        comFileDomain2.setFileUpldNm("testFileUpload2.txt");
        comFileDomain2.setFileOrignNm("testFileUpload2.txt");
        comFileDomain2.setFilePath("/path/to/file");
        comFileDomain2.setFileId(idx);
        comFileMetaService.updateFileMeta(comFileDomain2);  // update를 통한 동일한 fileId와 seq의 차이 비교

        // Act
        ComFileDomain result = comFileMetaService.selectFileMeta(idx,1);
        ComFileDomain result2 = comFileMetaService.selectFileMeta(idx,2);


        // Assert
//        System.out.println("idx : "+idx);
//        System.out.println("result : "+result);
        assertNotEquals(result.getFileSeq(), result2.getFileSeq());
        assertEquals(result.getFileSeq(), 1);
        assertEquals(result2.getFileSeq(), 2);
        assertEquals(result.getFileId(), result2.getFileId(),"파일 ID가 일치 한다.");
        assertEquals(result.getFileUpldNm(), "testFileUpload1.txt","result 파일명이 예상 값과 일치한다.");
        assertEquals(result2.getFileUpldNm(), "testFileUpload2.txt","result2 파일명이 예상 값과 일치한다.");

    }

    @Test
    @DisplayName("파일 ID로 마지막으로 업로드된 동일 ID의 파일 경로를 정상적으로 조회할 수 있다.")
    public void testSelectLatestFileMetaPath() {
        // Arrange
        // 파일 메타 정보 삽입
        String idx = comFileMetaService.insertFileMeta(comFileDomain);  // 먼저 삽입해야 조회가 가능합니다.
        System.out.println("comFileDomain : "+comFileDomain);
        // Act
        String filePath = comFileMetaService.selectLatestFileMetaPath(idx);
        System.out.println("filePath : "+filePath);
        // Assert
        assertNotNull(filePath, "조회된 파일 경로가 null이 아니다.");
        assertEquals("/path/to/file/testFileUpload.txt", filePath, "조회한 값이 파일 경로와 일치한다.");
    }

    @Test
    @DisplayName("파일 ID 및 파일 SEQ로 파일 경로를 정상적으로 조회할 수 있다.")
    public void testSelectFileMetaPath() {

        // Arrange
        // 파일 메타 정보 삽입
        comFileDomain = new ComFileDomain();
        comFileDomain.setFileUpldNm("testFileUpload1.txt");
        comFileDomain.setFileOrignNm("testFileUpload1.txt");
        comFileDomain.setFilePath("/path/to/file");
        String idx = comFileMetaService.insertFileMeta(comFileDomain);  // 먼저 삽입해야 조회가 가능합니다.

        ComFileDomain comFileDomain2 = new ComFileDomain();
        comFileDomain2.setFileUpldNm("testFileUpload2.txt");
        comFileDomain2.setFileOrignNm("testFileUpload2.txt");
        comFileDomain2.setFilePath("/path/to/file");
        comFileDomain2.setFileId(idx);
        comFileMetaService.updateFileMeta(comFileDomain2);  // update를 통한 동일한 fileId와 seq의 차이 비교

        // Act
        String result = comFileMetaService.selectFileMetaPath(idx,1);
        String result2 = comFileMetaService.selectFileMetaPath(idx,2);


        // Assert
//        System.out.println("idx : "+idx);
//        System.out.println("result : "+result);
        assertNotNull(result,"업로드한 파일 경로가 존재한다.");
        assertNotNull(result2,"업로드한 파일 경로가 존재한다.");
        assertNotEquals(result,"/path/to/file/testFileUpload2.txt","result와 result2의 경로의 파일 경로는 다르다(다른 파일이다)");
        assertNotEquals(result2,"/path/to/file/testFileUpload1.txt","result와 result2의 경로의 파일 경로는 다르다(다른 파일이다)");
        assertEquals(result2,"/path/to/file/testFileUpload2.txt","result2와 comFileDomain2의 파일 경로는 동일하다.");
        assertEquals(result,"/path/to/file/testFileUpload1.txt","result와 comFileDomain의 파일 경로는 동일하다.");

    }

    @Test
    @DisplayName("파일 메타 리스트를 성공적으로 삭제할 수 있다.")
    public void testDeleteFileMetaList() {
        // Arrange
        // 먼저 파일 메타 정보를 삽입하여 삭제할 데이터를 준비
        String idx = comFileMetaService.insertFileMeta(comFileDomain);

        // insert cnt 조회
        int cnt = comFileMetaService.selectFileMetaListCount(idx);

        comFileMetaMapper.deleteFileMetaList(idx);

        int afterCnt = comFileMetaService.selectFileMetaListCount(idx);

        assertEquals(cnt,1,"insert한 1개의 객체가 갯수와 동일하다");
        assertNotEquals(cnt,afterCnt,"삭제 전과 삭제 후의 동일한 fileId값이 다르다");
        assertEquals(afterCnt,0,"삭제한 후의 메타 리스트의 길이는 0이다.");

    }

    @Test
    @DisplayName("특정 파일 메타를 삭제할 수 있다.")
    public void testDeleteFileMeta() {
        // Arrange
        // 먼저 파일 메타 정보를 삽입하여 삭제할 데이터를 준비
        String idx = comFileMetaService.insertFileMeta(comFileDomain);

        comFileMetaService.updateFileMeta(comFileDomain);

        // Act
        comFileMetaService.deleteFileMeta(idx,2);
        // Assert
        ComFileDomain result = comFileMetaService.selectLatestFileMeta(idx);
        System.out.println("result : "+ result);
        assertEquals(result.getFileSeq(), 1,"마지막으로 조회된 시퀀스는 1번이다.");
        assertNotEquals(result.getFileSeq(), 2,"삭제된 2번의 시퀀스가 아니다.");
        assertEquals(comFileMetaMapper.selectFileMetaListCount(idx),1,"현재 남아 있는 idx의 객체 값은 1개이다.");
    }

    @Test
    @DisplayName("파일 ID로 파일 메타 리스트를 정상적으로 조회할 수 있다.")
    public void testSelectFileMetaList() {
        // Arrange
        // 파일 메타 정보 삽입
        String idx = comFileMetaService.insertFileMeta(comFileDomain);

        for(int i = 0; i < 3; i++){
            comFileMetaService.updateFileMeta(comFileDomain);
        }

        // Act
        List<ComFileDomain> result = comFileMetaService.selectFileMetaList(idx);

        // Assert
        assertNotNull(result, "파일 메타 리스트가 null이 아니다.");
        assertEquals(4, result.size(), "4개가 입력된 list의 값이다.");
        assertEquals(result.size(),comFileMetaMapper.selectFileMetaListCount(idx), "count와 list.size()가 동일한 사이즈 값이다.");
    }
}
