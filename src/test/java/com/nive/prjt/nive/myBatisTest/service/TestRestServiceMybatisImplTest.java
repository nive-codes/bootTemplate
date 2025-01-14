package com.nive.prjt.nive.myBatisTest.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
import com.nive.prjt.exception.business.BusinessRestException;
import com.nive.prjt.nive.myBatisTest.domain.TestDomain;
import com.nive.prjt.nive.myBatisTest.mapper.TestMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

/*logic을 가정하므로 mock은 validate 체크에 적합*/
/*단 Mybatis의 mapper 오류는 잡지 못하므로 test case의 재작성이 필요합니다.*/
@ExtendWith(MockitoExtension.class)
class TestRestServiceMybatisImplTest {

    @Mock
    private TestMapper testMapper;

    @InjectMocks
    private TestRestServiceMybatisImpl testRestService;

//    @BeforeEach //mock 초기화
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }

    @Test
    void insertTest_success() {
        // Given
        TestDomain testDomain = new TestDomain();
        testDomain.setNm("Test Name");

        //입력한 값이 없는 경우 지정(미리 값을 예측)
        when(testMapper.existsByNm(testDomain.getNm())).thenReturn(false);

        // When
        ResponseEntity<String> response = testRestService.insertTest(testDomain);

        // Then
        assertThat(response.getStatusCodeValue()).isEqualTo(201);   //성공 201
        assertThat(response.getBody()).contains("Test Insert ID: TEST_");
        verify(testMapper, times(1)).insertTest(testDomain); //한번 호출되었는지 검증
    }

    @Test
    void insertTest_duplicateName() {
        // Given
        TestDomain testDomain = new TestDomain();
        testDomain.setNm("Duplicate Name");

        when(testMapper.existsByNm(testDomain.getNm())).thenReturn(true);   //true인 경우

        // When & Then
        BusinessRestException exception = assertThrows(
                BusinessRestException.class,
                () -> testRestService.insertTest(testDomain)
        );  //Exception 객체

        assertThat(exception.getMessage()).isEqualTo("이미 존재하는 이름입니다."); //BusinessException예외인지 확인 같은 메시지
        assertThat(exception.getErrorCode()).isEqualTo("DUPLICATE");    //같은 코드인지
        verify(testMapper, never()).insertTest(testDomain); //호출되지 않았는지 검증
    }

    @Test
    void getTest_success() {
        // Given
        String tbIdx = "TEST_123";
        TestDomain testDomain = new TestDomain();
        testDomain.setTbIdx(tbIdx);

        when(testMapper.findById(tbIdx)).thenReturn(testDomain);

        // When
        ResponseEntity<TestDomain> response = testRestService.getTest(tbIdx);

        // Then
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(testDomain);
        verify(testMapper, times(1)).findById(tbIdx);
    }

    @Test
    void deleteTest_success() {
        // Given
        String tbIdx = "TEST_123";
        TestDomain testDomain = new TestDomain();
        testDomain.setTbIdx(tbIdx);

        when(testMapper.findById(tbIdx)).thenReturn(testDomain);

        // When
        ResponseEntity<String> response = testRestService.deleteTest(tbIdx);

        // Then
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).contains("delete completed");
        verify(testMapper, times(1)).deleteTest(tbIdx);
    }

    @Test
    void deleteTest_notFound() {
        // Given
        String tbIdx = "TEST_123";
        when(testMapper.findById(tbIdx)).thenReturn(null);

        // When & Then
        BusinessRestException exception = assertThrows(
                BusinessRestException.class,
                () -> testRestService.deleteTest(tbIdx)
        );

        assertThat(exception.getMessage()).isEqualTo("존재하지 않는 TEST 데이터 입니다.");
        assertThat(exception.getErrorCode()).isEqualTo("NOT_FOUND");
        verify(testMapper, never()).deleteTest(tbIdx);
    }

    @Test
    void updateTest_success() {
        // Given
        String tbIdx = "TEST_123";
        TestDomain existingTest = new TestDomain();
        existingTest.setTbIdx(tbIdx);

        TestDomain updatedTest = new TestDomain();
        updatedTest.setNm("Updated Name");

        when(testMapper.findById(tbIdx)).thenReturn(existingTest);

        // When
        ResponseEntity<String> response = testRestService.updateTest(tbIdx, updatedTest);

        // Then
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).contains("updated completed");
        verify(testMapper, times(1)).updateTest(updatedTest);
    }

    @Test
    void updateTest_notFound() {
        // Given
        String tbIdx = "TEST_123";
        TestDomain updatedTest = new TestDomain();
        updatedTest.setNm("Updated Name");

        when(testMapper.findById(tbIdx)).thenReturn(null);

        // When & Then
        BusinessRestException exception = assertThrows(
                BusinessRestException.class,
                () -> testRestService.updateTest(tbIdx, updatedTest)
        );

        assertThat(exception.getMessage()).isEqualTo("존재하지 않는 TEST 데이터 입니다.");
        assertThat(exception.getErrorCode()).isEqualTo("NOT_FOUND");
        verify(testMapper, never()).updateTest(updatedTest);
    }

    @Test
    void findAll_success() {
        // Given
        TestDomain searchCriteria = new TestDomain();
        searchCriteria.setTestSearch("keyword");

        List<TestDomain> mockResults = new ArrayList<>();
        mockResults.add(new TestDomain());
        mockResults.add(new TestDomain());

        when(testMapper.findAll(searchCriteria)).thenReturn(mockResults);

        // When
        ResponseEntity<Object> response = testRestService.findAll(searchCriteria);

        // Then
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isInstanceOf(List.class);
        assertThat(((List<?>) response.getBody()).size()).isEqualTo(2);
        verify(testMapper, times(1)).findAll(searchCriteria);
    }

    @Test
    void findAll_badRequest() {
        // Given
        TestDomain searchCriteria = new TestDomain();
        searchCriteria.setTestSearch("  "); // 공백 문자열

        // When
        ResponseEntity<Object> response = testRestService.findAll(searchCriteria);

        // Then
        assertThat(response.getStatusCodeValue()).isEqualTo(400);
        assertThat(response.getBody()).isEqualTo("잘못된 검색어입니다.");
        verify(testMapper, never()).findAll(searchCriteria);
    }
}