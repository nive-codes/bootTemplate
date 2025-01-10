package com.nive.prjt.nive.myBatisTest.controller;

import com.nive.prjt.nive.myBatisTest.domain.TestDomain;
import com.nive.prjt.nive.myBatisTest.service.TestService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

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
        return "test/testNew";
    }

    @PostMapping("/save")
    public String saveTest(@Valid TestDomain testDomain, BindingResult bindingResult, Model model) {

        if(bindingResult.hasErrors()) {
            model.addAttribute("errorMessage", bindingResult.getAllErrors().get(0).getDefaultMessage());
            return "test/testNew";
        }

        try{
            testService.insertTest(testDomain);
        } catch (IllegalArgumentException e){
            model.addAttribute("errorMessage", e.getMessage());
            return "test/testNew";
        } catch (Exception e){
            model.addAttribute("errorMessage", e.getMessage());
            return "test/testNew";
        }

        return "redirect:/test/testList";
    }

    @GetMapping("/list")
    public String list(TestDomain testDomain, Model model) {
        List<TestDomain> list = testService.findAll(testDomain);
        model.addAttribute("list", list);
        return "test/testList";
    }

    @GetMapping("/searchList")
    public String searchList(@ModelAttribute("searchForm") TestDomain testDomain, Model model) {
        List<TestDomain> list = testService.findAll(testDomain);
        model.addAttribute("list", list);
        return "test/testSearchList";
    }

}
