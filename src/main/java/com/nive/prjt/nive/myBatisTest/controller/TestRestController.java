package com.nive.prjt.nive.myBatisTest.controller;

import com.nive.prjt.nive.myBatisTest.domain.TestDomain;
import com.nive.prjt.nive.myBatisTest.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/testApi")
@RequiredArgsConstructor
public class TestRestController {

    private  final TestService testService;
//    public TestRestController(TestService testService) {
//        this.testService = testService;
//    }

}
