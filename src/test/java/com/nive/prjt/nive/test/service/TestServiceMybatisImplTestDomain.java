package com.nive.prjt.nive.test.service;

import com.nive.prjt.nive.myBatisTest.domain.TestDomain;
import com.nive.prjt.nive.myBatisTest.service.TestServiceMybatisImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@SpringBootTest //SpringBoot 통합 테스트(Mybatis 연결을 확인하기 위함)
@Transactional  // 테스트 후 DB 롤백
class TestServiceMybatisImplTestDomain {

    @Autowired
    private TestServiceMybatisImpl testService;

    @Test
    void insertSelect검증() {
        // given: 데이터 준비
        TestDomain testDomain = new TestDomain("1", "testName","");

        // when: 테스트 메서드 실행
        testService.insertTest(testDomain);


        // then: 결과 검증
        Optional<TestDomain> test = testService.getTest("1");
        assertThat(test.isPresent()).isTrue();
        assertThat(test.get().getNm()).isEqualTo("testName");

        // given: 중복 데이터 준비
        TestDomain testDomain2 = new TestDomain("2", "testName","");
        // when : 테스트 실행
        assertThatThrownBy(() -> testService.insertTest(testDomain2))
        // then : 중복 오류 확인(insertTest에서 throws와 동일한지 확인)
                .isInstanceOf(IllegalArgumentException.class);


    }

    @Test
    void delete검증() {
        // given: 데이터 삽입
        TestDomain testDomain = new TestDomain("3", "deleteTest", "");
        testService.insertTest(testDomain);

        // when: 데이터 삭제
        testService.deleteTest("3");

        // then: 데이터가 없는지 확인
        Optional<TestDomain> deletedTest = testService.getTest("3");
        assertThat(deletedTest.isPresent()).isFalse();
    }

    @Test
    void select목록전체조회() {
        // given: 여러 데이터 삽입
        testService.insertTest(new TestDomain("4", "firstTest", ""));
        testService.insertTest(new TestDomain("5", "secondTest", ""));

        // when: 전체 데이터를 조회
        List<TestDomain> allTests = testService.findAll(new TestDomain());

        // then: 데이터 개수 및 값 검증
        assertThat(allTests.size()).isGreaterThanOrEqualTo(2); // 적어도 두 개 존재(두개 이상)
        assertThat(allTests)
                .extracting("nm")
                .contains("firstTest", "secondTest");
    }

};