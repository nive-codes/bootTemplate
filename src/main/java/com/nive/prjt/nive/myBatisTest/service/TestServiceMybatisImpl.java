package com.nive.prjt.nive.myBatisTest.service;

import com.nive.prjt.nive.myBatisTest.domain.TestDomain;
import com.nive.prjt.nive.myBatisTest.mapper.TestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TestServiceMybatisImpl implements TestService {

    private final TestMapper testMapper;

    /*생성자를 통한 testMapper 주입, 한개인 경우 @Autowired 생략 가능*/
    @Autowired
    public TestServiceMybatisImpl(TestMapper testMapper) {
        this.testMapper = testMapper;
    }

    @Override
    public void insertTest(TestDomain testDomain) {
        System.out.println("TestDomain.insertTest name= "+testDomain.getName());
        System.out.println("TestDomain.insertTest idx = "+testDomain.getTbIdx());
        testMapper.insertTest(testDomain);
    }


    @Override
    public Optional<TestDomain> getTest(String tb_idx) {
        TestDomain result = testMapper.findById(tb_idx);
        return Optional.ofNullable(result); /*null일 수도 있음*/

    }

    @Override
    public void deleteTest(String tb_idx) {
        testMapper.deleteTest(tb_idx);
    }

    @Override
    public void updateTest(TestDomain testDomain) {
        testMapper.updateTest(testDomain);
    }

    @Override
    public List<TestDomain> findAll(TestDomain testDomain) {
        return testMapper.findAll(testDomain);
    }
}
