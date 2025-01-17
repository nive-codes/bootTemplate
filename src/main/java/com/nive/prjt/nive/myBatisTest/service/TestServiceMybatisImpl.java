package com.nive.prjt.nive.myBatisTest.service;

import com.nive.prjt.config.exception.business.BusinessException;
import com.nive.prjt.nive.myBatisTest.domain.TestDomain;
import com.nive.prjt.nive.myBatisTest.mapper.TestMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class TestServiceMybatisImpl implements TestService {

    private final TestMapper testMapper;



//    @Autowired
//    public TestServiceMybatisImpl(TestMapper testMapper) {
//        this.testMapper = testMapper;
//    }

    @Override
    public void insertTest(TestDomain testDomain) {
        log.debug("TestDomain.insertTest nm= "+testDomain.getNm());

        boolean sameNm = testMapper.existsByNm(testDomain.getNm());

        if (sameNm) {
//            throw new IllegalArgumentException("이미 존재하는 이름입니다");
            throw new BusinessException("이미 존재하는 이름입니다 - BusinessException", "/test/testNew");

        }

        // tbIdx 생성 (UUID 기반)
        String tbIdx = generateCustomId();
        testDomain.setTbIdx(tbIdx);

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
        log.debug("TestDomain.findAll testDomain.testSearch="+ testDomain.getTestSearch());
        return testMapper.findAll(testDomain);
    }

    private String generateCustomId() {
        // 원하는 방식으로 고유 Key 생성 (UUID 사용)
        return "TEST_" + UUID.randomUUID().toString();
    }

}
