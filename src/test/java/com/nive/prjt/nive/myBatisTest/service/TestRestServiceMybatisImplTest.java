package com.nive.prjt.nive.myBatisTest.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

import com.nive.prjt.config.exception.business.BusinessRestException;
import com.nive.prjt.config.response.SuccessResponse;
import com.nive.prjt.nive.myBatisTest.domain.TestDomain;
import com.nive.prjt.nive.myBatisTest.mapper.TestMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.mockito.Mockito.*;

/*logic을 가정하므로 mock은 validate 체크에 적합*/
/*단 Mybatis의 mapper 오류는 잡지 못하므로 test case의 재작성이 필요합니다.*/
@ExtendWith(MockitoExtension.class)
class TestRestServiceMybatisImplTest {

//    @Mock
//    private TestMapper testMapper;
//
//    @InjectMocks
//    private TestRestServiceMybatisImpl testRestService;
//
//    private TestDomain testDomain;
//
//    @BeforeEach
//    void setUp() {
//        testDomain = new TestDomain();
//        testDomain.setNm("Test Name");
//        testDomain.setTestSearch("Test");
//    }
//
//
//    @Test
//    @DisplayName("중복된 이름 입력 시 예외가 발생하는지 테스트")
//    void testInsertTest_DuplicateName_ShouldThrowException() {
//        // Given
//        when(testMapper.existsByNm(testDomain.getNm())).thenReturn(true);
//
//        // When & Then
//        BusinessRestException exception = assertThrows(BusinessRestException.class, () -> {
//            testRestService.insertTest(testDomain);
//        });
//        assertEquals("이미 존재하는 이름입니다.", exception.getMessage());
//        assertEquals("DUPLIATE", exception.getErrorCode());
//        assertEquals(HttpStatus.CONFLICT, exception.getHttpStatus());
//    }
//
//    @Test
//    @DisplayName("정상적으로 데이터가 삽입되는지 테스트")
//    void testInsertTest_Success() {
//        // Given
//        when(testMapper.existsByNm(testDomain.getNm())).thenReturn(false);
//        doNothing().when(testMapper).insertTest(testDomain);
//
//        // When
//        ResponseEntity<SuccessResponse> response = testRestService.insertTest(testDomain);
//
//        // Then
//        assertEquals(HttpStatus.CREATED, response.getStatusCode());
//        assertTrue(response.getBody().contains("Test Insert ID:"));
//    }
//
//    @Test
//    @DisplayName("존재하는 테스트 데이터를 조회할 수 있는지 테스트")
//    void testGetTest_Found() {
//        // Given
//        String tbIdx = "TEST_123";
//        when(testMapper.findById(tbIdx)).thenReturn(testDomain);
//
//        // When
//        ResponseEntity<SuccessResponse<TestDomain>> response = testRestService.getTest(tbIdx);
//
//        // Then
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertNotNull(response.getBody());
//        assertEquals(testDomain, response.getBody());
//    }
//
//
//    @Test
//    @DisplayName("존재하지 않는 테스트 데이터 조회 시 예외 발생 여부 테스트")
//    void testGetTest_NotFound() {
//        // Given
//        String tbIdx = "TEST_123";
//        when(testMapper.findById(tbIdx)).thenReturn(null);  /*null인 경우*/
//
//        // When & Then
//        BusinessRestException exception = assertThrows(BusinessRestException.class, () -> {
//            testRestService.getTest(tbIdx);
//        });
//
//        System.out.println(exception.getMessage());
//        System.out.println(exception.getMessage());
//        System.out.println(exception.getHttpStatus());
//
//        assertEquals("존재하지 않는 TEST 데이터 입니다.", exception.getMessage());
//        assertEquals("NOT_FOUND", exception.getErrorCode());
//        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
//    }
//
//    @Test
//    @DisplayName("정상적으로 테스트 데이터를 삭제할 수 있는지 테스트")
//    void testDeleteTest_Success() {
//        // Given
//        String tbIdx = "TEST_123";
//        when(testMapper.findById(tbIdx)).thenReturn(testDomain);
//        doNothing().when(testMapper).deleteTest(tbIdx);
//
//        // When
//        ResponseEntity<SuccessResponse> response = testRestService.deleteTest(tbIdx);
//
//        // Then
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertTrue(response.getBody().contains("delete completed"));
//    }
//
//    @Test
//    @DisplayName("존재하지 않는 테스트 데이터 삭제 시 예외 발생 여부 테스트")
//    void testDeleteTest_NotFound() {
//        // Given
//        String tbIdx = "TEST_123";
//        when(testMapper.findById(tbIdx)).thenReturn(null);
//
//        // When & Then
//        BusinessRestException exception = assertThrows(BusinessRestException.class, () -> {
//            testRestService.deleteTest(tbIdx);
//        });
//        assertEquals("존재하지 않는 TEST 데이터 입니다.", exception.getMessage());
//        assertEquals("NOT_FOUND", exception.getErrorCode());
//        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
//    }
//
//    @Test
//    @DisplayName("정상적으로 테스트 데이터를 업데이트할 수 있는지 테스트")
//    void testUpdateTest_Success() {
//        // Given
//        String tbIdx = "TEST_123";
//        when(testMapper.findById(tbIdx)).thenReturn(testDomain);
//        doNothing().when(testMapper).updateTest(testDomain);
//
//        // When
//        ResponseEntity<SuccessResponse> response = testRestService.updateTest(tbIdx, testDomain);
//
//        // Then
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertTrue(response.getBody().contains("updated completed"));
//    }
//
//    @Test
//    @DisplayName("존재하지 않는 테스트 데이터 업데이트 시 예외 발생 여부 테스트")
//    void testUpdateTest_NotFound() {
//        // Given
//        String tbIdx = "TEST_123";
//        when(testMapper.findById(tbIdx)).thenReturn(null); // findById가 null을 반환하도록 설정
//
//        // When & Then
//        BusinessRestException exception = assertThrows(BusinessRestException.class, () -> {
//            testRestService.updateTest(tbIdx, testDomain); // 예외가 발생해야 함
//        });
//
//        // Then
//        assertEquals("존재하지 않는 TEST 데이터 입니다.", exception.getMessage());
//        assertEquals("NOT_FOUND", exception.getErrorCode());
//        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
//    }
//
//    @Test
//    @DisplayName("잘못된 검색어로 findAll 호출 시 예외 발생 여부 테스트")
//    void testFindAll_InvalidSearch() {
//        // Given
//        testDomain.setTestSearch(" ");
//
//        // When & Then
//        BusinessRestException exception = assertThrows(BusinessRestException.class, () -> {
//            testRestService.findAll(testDomain);
//        });
//        assertEquals("잘못된 검색어입니다.", exception.getMessage());
//        assertEquals("NOT_FOUND", exception.getErrorCode());
//        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
//    }
//
//    @Test
//    @DisplayName("정상적인 검색어로 findAll 호출 시 데이터 반환 여부 테스트")
//    void testFindAll_Success() {
//        // Given
//        when(testMapper.findAll(testDomain)).thenReturn(List.of(testDomain));
//
//        // When
//        ResponseEntity<SuccessResponse> response = testRestService.findAll(testDomain);
//
//        // Then
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertNotNull(response.getBody());
//        assertTrue(((List<?>) response.getBody()).size() > 0);
//    }
}