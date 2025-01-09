package com.nive.prjt.nive.myBatisTest.mapper;

import com.nive.prjt.nive.myBatisTest.domain.TestDomain;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/*DAO와의 차이*/
//기존 : 클라이언트 -> Controller -> Service -> ServiceImpl -> DAO
@Mapper
public interface TestMapper {

    void insertTest(TestDomain testDomain);

    void updateTest(TestDomain testDomain);

    void deleteTest(String tb_idx);

    List<TestDomain> findAll(TestDomain testDomain);

    TestDomain findById(String tb_idx);

    @Select("SELECT COUNT(1) > 0 FROM test_tb WHERE nm = #{nm}")
    boolean existsByNm(String nm);




}
