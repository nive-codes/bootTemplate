package com.nive.prjt.nive.myBatisTest.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

import com.nive.prjt.config.exception.business.BusinessException;
import com.nive.prjt.config.response.ApiCode;
import com.nive.prjt.config.response.ApiResponse;
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

import java.util.List;

import static org.mockito.Mockito.*;

/*logic을 가정하므로 mock은 validate 체크에 적합*/
/*단 Mybatis의 mapper 오류는 잡지 못하므로 test case의 재작성이 필요합니다.*/
/*25.01.15 ApiReponse 로 교체*/
@ExtendWith(MockitoExtension.class)
class TestRestServiceMybatisImplTest {

    @Mock
    private TestMapper testMapper;

    @InjectMocks
    private TestRestServiceMybatisImpl testRestService;

    private TestDomain testDomain;

    @BeforeEach
    void setUp() {
        testDomain = new TestDomain();
        testDomain.setNm("Test Name");
        testDomain.setTestSearch("Test");
    }


    @Test
    @DisplayName("중복된 이름 입력 시 예외가 발생하는지 테스트")
    void testInsertTest_duplicate() {
        // Given
        when(testMapper.existsByNm(testDomain.getNm())).thenReturn(true);

        // when
        ApiResponse response = testRestService.insertTest(testDomain);

        System.out.println("response.getCode : "+ response.getCode());
        System.out.println("response.getMessage : "+ response.getMessage());

        // then
        assertEquals(ApiCode.SAME_DATA.getCode(), response.getCode());
        assertEquals(ApiCode.SAME_DATA.getMessage(), response.getMessage());
    }

    @Test
    @DisplayName("정상적으로 데이터가 삽입되는지 테스트")
    void testInsertTest_Success() {
        // Given
        when(testMapper.existsByNm(testDomain.getNm())).thenReturn(false);
        doNothing().when(testMapper).insertTest(testDomain);

        // When
        ApiResponse apiResponse = testRestService.insertTest(testDomain);

        // Then
        assertEquals(apiResponse.getCode(), ApiCode.SUCCESS.getCode());
        assertEquals(apiResponse.getMessage(), ApiCode.SUCCESS.getMessage());

    }

    @Test
    @DisplayName("존재하는 테스트 데이터를 조회할 수 있는지 테스트")
    void testGetTest_Found() {
        // Given
        String tbIdx = "TEST_123";
        when(testMapper.findById(tbIdx)).thenReturn(testDomain);

        // When
        ApiResponse apiResponse = testRestService.getTest(tbIdx);

        // Then
        assertEquals(apiResponse.getCode(), ApiCode.SUCCESS.getCode());
        assertEquals(apiResponse.getMessage(), ApiCode.SUCCESS.getMessage());
    }


    @Test
    @DisplayName("존재하지 않는 테스트 데이터 조회 시 예외 발생 여부 테스트")
    void testGetTest_NotFound() {
        // Given
        String tbIdx = "TEST_123";
        when(testMapper.findById(tbIdx)).thenReturn(null);  /*null인 경우*/

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            testRestService.getTest(tbIdx);
        });

//        System.out.println("exception.getMessage : " + exception.getMessage());
//        System.out.println("exception.getMessage : " + exception.getMessage());
//        System.out.println("exception.getHttpStatus : " + exception.getHttpStatus());

        //메시지까지는 검증할지 말지 향후 처리
        assertEquals("존재하지 않는 TEST 데이터 입니다.", exception.getMessage());
        assertEquals("NOT_FOUND", exception.getErrorCode());
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    @DisplayName("정상적으로 테스트 데이터를 삭제할 수 있는지 테스트")
    void testDeleteTest_Success() {
        // Given
        String tbIdx = "TEST_123";
        when(testMapper.findById(tbIdx)).thenReturn(testDomain);
        doNothing().when(testMapper).deleteTest(tbIdx);

        // When
        ApiResponse apiResponse = testRestService.deleteTest(tbIdx);


        // Then
        assertEquals(ApiCode.SUCCESS.getStatus(), HttpStatus.OK);
        assertEquals(ApiCode.SUCCESS.getCode(), apiResponse.getCode());

    }

    @Test
    @DisplayName("존재하지 않는 테스트 데이터 삭제 시 예외 발생 여부 테스트")
    void testDeleteTest_NotFound() {
        // Given
        String tbIdx = "TEST_123";
        when(testMapper.findById(tbIdx)).thenReturn(null);

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            testRestService.deleteTest(tbIdx);
        });
        assertEquals("존재하지 않는 TEST 데이터 입니다.", exception.getMessage());
        assertEquals("NOT_FOUND", exception.getErrorCode());
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    @DisplayName("정상적으로 테스트 데이터를 업데이트할 수 있는지 테스트")
    void testUpdateTest_Success() {
        // Given
        String tbIdx = "TEST_123";
        when(testMapper.findById(tbIdx)).thenReturn(testDomain);
        doNothing().when(testMapper).updateTest(testDomain);

        ApiResponse response = testRestService.updateTest(tbIdx, testDomain);
        assertEquals(ApiCode.SUCCESS.getStatus(), HttpStatus.OK);
        assertEquals(ApiCode.SUCCESS.getCode(), response.getCode());


    }

    @Test
    @DisplayName("존재하지 않는 테스트 데이터 업데이트 시 예외 발생 여부 테스트")
    void testUpdateTest_NotFound() {
        // Given
        String tbIdx = "TEST_123";
        when(testMapper.findById(tbIdx)).thenReturn(null); // findById가 null을 반환하도록 설정

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            testRestService.updateTest(tbIdx, testDomain); // 예외가 발생해야 함
        });

        // Then
        assertEquals("존재하지 않는 TEST 데이터 입니다.", exception.getMessage());
        assertEquals("NOT_FOUND", exception.getErrorCode());
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    @DisplayName("공백 검색어로 findAll 호출 시 예외 발생 여부 테스트")
    void testFindAll_InvalidSearch() {
        // Given
        testDomain.setTestSearch(" ");

        // When
        ApiResponse apiResponse = testRestService.findAll(testDomain);

        // Then
        assertEquals(apiResponse.getCode(), ApiCode.VALIDATION_FAILED.getCode());
        //커스텀 문구 검증
        assertEquals(apiResponse.getMessage(), "검색어에 공백(스페이스바)만 입력하실 수 없습니다.");

    }

    @Test
    @DisplayName("정상적인 검색어로 findAll 호출 시 데이터 반환 여부 테스트")
    void testFindAll_Success() {
        // Given
        when(testMapper.findAll(testDomain)).thenReturn(List.of(testDomain));

        // When
        ApiResponse apiResponse = testRestService.findAll(testDomain);


        // Then
        assertEquals(ApiCode.SUCCESS.getCode(), apiResponse.getCode());
        assertEquals(ApiCode.SUCCESS.getMessage(), apiResponse.getMessage());
        assertNotNull(apiResponse.getData());

    }
}