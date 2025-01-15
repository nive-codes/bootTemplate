package com.nive.prjt.nive.myBatisTest.service;

import com.nive.prjt.config.response.ApiResponse;
import com.nive.prjt.nive.myBatisTest.domain.TestDomain;

public interface TestRestService {
    // 데이터 생성 (POST /tests)
    ApiResponse insertTest(TestDomain testDomain);

    // 특정 데이터 조회 (GET /tests/{tbIdx})
    // Optional은 상태 값을 전달할 수 있으므로 rest api에서는 삭제
    ApiResponse getTest(String tbIdx);

    // 데이터 삭제 (DELETE /tests/{tbIdx})
    ApiResponse deleteTest(String tbIdx);

    // 데이터 수정 (PUT /tests/{tbIdx})
    ApiResponse updateTest(String tbIdx, TestDomain testDomain);

    // 전체 데이터 조회 (GET /tests)
    ApiResponse findAll(TestDomain testDomain);
}
