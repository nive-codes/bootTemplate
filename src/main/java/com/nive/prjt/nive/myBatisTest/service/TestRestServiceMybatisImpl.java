package com.nive.prjt.nive.myBatisTest.service;

import com.nive.prjt.config.exception.business.BusinessRestException;
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
    public ResponseEntity<String> insertTest(TestDomain testDomain) {

        // 비즈니스 규칙: 이름은 중복될 수 없습니다.
        boolean sameNm = testMapper.existsByNm(testDomain.getNm());
        if (sameNm) {
            log.error("Duplicate name attempt: {}", testDomain.getNm());
            throw new BusinessRestException("이미 존재하는 이름입니다.", "DUPLIATE",HttpStatus.CONFLICT);
        }

        // tbIdx 생성 (UUID 기반)
        String tbIdx = generateCustomId();
        testDomain.setTbIdx(tbIdx);

        testMapper.insertTest(testDomain);

        return ResponseEntity.status(HttpStatus.CREATED).body("Test Insert ID: " + tbIdx);
        /*중요 정보인 경우 return 방지 후 messages 전달*/
//        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입이 완료되었습니다.");
    }


    @Override
    @Transactional(readOnly = true) /*데이터 변경 작업이 없다는 보증을 제공*/
    public ResponseEntity<TestDomain> getTest(String tbIdx) {
        TestDomain result = testMapper.findById(tbIdx);

        if (result == null) {
            throw new BusinessRestException("존재하지 않는 TEST 데이터 입니다.", "NOT_FOUND", HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<String> deleteTest(String tbIdx) {
        TestDomain existingTest = testMapper.findById(tbIdx);
        if (existingTest == null) {
            throw new BusinessRestException("존재하지 않는 TEST 데이터 입니다.", "NOT_FOUND",HttpStatus.NOT_FOUND);
        }

        testMapper.deleteTest(tbIdx);
        return ResponseEntity.ok("Test with ID: " + tbIdx + " delete completed.");
    }

    @Override
    public ResponseEntity<String> updateTest(String tbIdx, TestDomain testDomain) {
        TestDomain existingTest = testMapper.findById(tbIdx);
        if (existingTest == null) {
            throw new BusinessRestException("존재하지 않는 TEST 데이터 입니다.", "NOT_FOUND",HttpStatus.NOT_FOUND);
        }

        testDomain.setTbIdx(tbIdx);
        testMapper.updateTest(testDomain);
        return ResponseEntity.ok("Test with ID: " + tbIdx + " updated completed.");
    }

    @Override
    public ResponseEntity<Object> findAll(TestDomain testDomain) {
        log.debug("TestDomain.findAll testSearch={}", testDomain.getTestSearch());

        if (testDomain.getTestSearch() != null && testDomain.getTestSearch().trim().isEmpty()) {
            throw new BusinessRestException("잘못된 검색어입니다.", "NOT_FOUND",HttpStatus.NOT_FOUND);
//            return ResponseEntity.badRequest().body("잘못된 검색어입니다.");
        }else{
            List<TestDomain> resultList = testMapper.findAll(testDomain);
            return ResponseEntity.ok(resultList);
        }
    }
    private String generateCustomId() {
        // 원하는 방식으로 고유 Key 생성 (UUID 사용)
        return "TEST_" + UUID.randomUUID().toString();
    }
}
