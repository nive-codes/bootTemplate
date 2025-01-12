package com.nive.prjt.nive.myBatisTest.service;

import com.nive.prjt.nive.myBatisTest.domain.TestDomain;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface TestRestService {
    // 데이터 생성 (POST /tests)
    ResponseEntity<String> insertTest(TestDomain testDomain);

    // 특정 데이터 조회 (GET /tests/{tbIdx})
    // Optional은 상태 값을 전달할 수 있으므로 rest api에서는 삭제
    ResponseEntity<TestDomain> getTest(String tbIdx);

    // 데이터 삭제 (DELETE /tests/{tbIdx})
    ResponseEntity<String> deleteTest(String tbIdx);

    // 데이터 수정 (PUT /tests/{tbIdx})
    ResponseEntity<String> updateTest(String tbIdx, TestDomain testDomain);

    // 전체 데이터 조회 (GET /tests)
    ResponseEntity<Object> findAll(TestDomain testDomain);
}
