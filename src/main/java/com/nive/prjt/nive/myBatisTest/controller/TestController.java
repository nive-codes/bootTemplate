package com.nive.prjt.nive.myBatisTest.controller;

import com.nive.prjt.nive.myBatisTest.domain.TestDomain;
import com.nive.prjt.nive.myBatisTest.service.TestService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * [클래스 역할 간략 설명]
 *
 * @author hosikchoi
 * @desc thymeleaf 활용
 * @since 2025-01-08
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {

    private final TestService testService;

    @GetMapping("/new")
    public String newTest(HttpServletRequest request) {
        return "test/new";
    }

    @PostMapping("/save")
    public String saveTest(TestDomain testDomain, Model model) {
        try {
            testService.insertTest(testDomain);
            return "redirect:/test/list";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "/test/new";
        }
    }

    @GetMapping("/list")
    public String list(TestDomain testDomain, HttpServletRequest request) {
        testService.findAll(testDomain);
        return "test/list";
    }

}
