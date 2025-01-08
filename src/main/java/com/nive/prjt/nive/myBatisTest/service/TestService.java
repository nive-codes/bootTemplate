package com.nive.prjt.nive.myBatisTest.service;

import com.nive.prjt.nive.myBatisTest.domain.TestDomain;

import java.util.List;
import java.util.Optional;

public interface TestService {
    void insertTest(TestDomain testDomain);

    Optional<TestDomain> getTest(String tbIdx);

    void deleteTest(String tb_idx);

    void updateTest(TestDomain testDomain);

    List<TestDomain> findAll(TestDomain testDomain);
}
