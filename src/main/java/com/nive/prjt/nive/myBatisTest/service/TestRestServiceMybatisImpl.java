package com.nive.prjt.nive.myBatisTest.service;

import com.nive.prjt.config.exception.business.BusinessException;
import com.nive.prjt.config.exception.business.BusinessRestException;
import com.nive.prjt.config.response.ApiCode;
import com.nive.prjt.config.response.ApiResponse;
import com.nive.prjt.nive.myBatisTest.domain.TestDomain;
import com.nive.prjt.nive.myBatisTest.mapper.TestMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class TestRestServiceMybatisImpl implements TestRestService {

    private final TestMapper testMapper;

    @Override
    public ApiResponse insertTest(TestDomain testDomain) {

        // 비즈니스 규칙: 이름은 중복될 수 없습니다.
        boolean sameNm = testMapper.existsByNm(testDomain.getNm());
        if (sameNm) {
            log.warn("Duplicate name attempt: {}", testDomain.getNm());
            // 비즈니스 로직 중 검증이므로 Exception이 아닌 ApiResponse를 통해 fail 메서드를 return
            //            throw new BusinessRestException("이미 존재하는 이름입니다.", "DUPLIATE",HttpStatus.CONFLICT);
            return ApiResponse.fail(ApiCode.SAME_DATA,testDomain);

        }

        // tbIdx 생성 (UUID 기반)
        String tbIdx = generateCustomId();
        testDomain.setTbIdx(tbIdx);

        testMapper.insertTest(testDomain);
        return ApiResponse.ok(ApiCode.SUCCESS);

    }


    @Override
    @Transactional(readOnly = true) /*데이터 변경 작업이 없다는 보증을 제공*/
    public ApiResponse getTest(String tbIdx) {
        TestDomain result = testMapper.findById(tbIdx);
        /*TEST데이터가 있으므로 요청이 왔지만 없으므로 예외 처리*/
        if (result == null) {
            throw new BusinessException("존재하지 않는 TEST 데이터 입니다.","NOT_FOUND",HttpStatus.NOT_FOUND);
//            return ApiResponse.fail(ApiCode.VALIDATION_FAILED);
        }
        return ApiResponse.ok(result);

    }

    @Override
    public ApiResponse deleteTest(String tbIdx) {
        TestDomain existingTest = testMapper.findById(tbIdx);
        /*TEST데이터가 있으므로 요청이 왔지만 없으므로 예외 처리*/
        if (existingTest == null) {
            throw new BusinessException("존재하지 않는 TEST 데이터 입니다.","NOT_FOUND",HttpStatus.NOT_FOUND);
        }

        testMapper.deleteTest(tbIdx);
        return ApiResponse.ok("삭제되었습니다.");
    }

    @Override
    public ApiResponse updateTest(String tbIdx, TestDomain testDomain) {
        TestDomain existingTest = testMapper.findById(tbIdx);
        if (existingTest == null) {
            throw new BusinessException("존재하지 않는 TEST 데이터 입니다.", "NOT_FOUND",HttpStatus.NOT_FOUND);
        }

        testDomain.setTbIdx(tbIdx);
        testMapper.updateTest(testDomain);
        return ApiResponse.ok("수정되었습니다.");
    }

    @Override
    public ApiResponse findAll(TestDomain testDomain) {
        log.debug("TestDomain.findAll testSearch={}", testDomain.getTestSearch());

        if (testDomain.getTestSearch() != null && testDomain.getTestSearch().trim().isEmpty()) {
            return ApiResponse.fail(ApiCode.VALIDATION_FAILED,"검색어에 공백(스페이스바)만 입력하실 수 없습니다.");
        }else{
            List<TestDomain> resultList = testMapper.findAll(testDomain);
            return ApiResponse.ok(resultList);
        }
    }
    private String generateCustomId() {
        // 원하는 방식으로 고유 Key 생성 (UUID 사용)
        return "TEST_" + UUID.randomUUID().toString();
    }
}
