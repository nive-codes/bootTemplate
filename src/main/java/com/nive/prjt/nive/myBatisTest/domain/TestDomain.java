package com.nive.prjt.nive.myBatisTest.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor   // 매개변수 생성자 추가
@NoArgsConstructor    // 기본 생성자 추가
public class TestDomain {

    private String tbIdx;
    private String name;
    private String testSearch;

}
