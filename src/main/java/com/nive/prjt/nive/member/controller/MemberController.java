package com.nive.prjt.nive.member.controller;

import com.nive.prjt.com.file.service.ComFileType;
import com.nive.prjt.config.response.ApiCode;
import com.nive.prjt.nive.member.domain.MemberDomain;
import com.nive.prjt.nive.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Objects;

/**
 * @author nive
 * @class MemberController
 * @desc 회원 데이터 + 파일 업로드 예제 Controller
 * @since 2025-01-21
 */
@Controller
@RequestMapping("/member")
@AllArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/insertForm")
    public String insertForm(Model model, MemberDomain memberDomain) {
        model.addAttribute("memberDomain", memberDomain);

        model.addAttribute("formFlag","Y"); //insert flag
        return "member/memberForm";
    }

    @PostMapping("/insert")
    public String insert(@Valid MemberDomain memberDomain, BindingResult bindingResult, MultipartHttpServletRequest request, Model model,
                         RedirectAttributes redirectAttributes) {

        if(bindingResult.hasErrors()) {
            model.addAttribute("formFlag","Y"); //insert flag
            model.addAttribute("memberDomain",memberDomain);
            model.addAttribute("errorMessage", bindingResult.getAllErrors().get(0).getDefaultMessage());
            return "member/memberForm";
        }

        memberService.insertMember(memberDomain,request);
        redirectAttributes.addFlashAttribute("message","등록되었습니다.");

        return "redirect:/member/searchForm";
    }

    @GetMapping("/updateForm")
    public String updateForm(MemberDomain memberDomain, Model model) {
        memberDomain = memberService.findMember(memberDomain.getMemberId());

        if(Objects.isNull(memberDomain)){
            model.addAttribute("formFlag","Y");
            model.addAttribute("errorMessage","회원 정보가 없습니다.");
            return "member/memberForm";
        }


        model.addAttribute("formFlag","N"); //update flag
        model.addAttribute("memberDomain",memberDomain);
        return "member/memberForm";
    }

    @PostMapping("/update")
    public String update(@Valid MemberDomain memberDomain, BindingResult bindingResult, MultipartHttpServletRequest request, Model model) {

        if(bindingResult.hasErrors()) {
            model.addAttribute("formFlag","Y"); //insert flag
            model.addAttribute("errorMessage", bindingResult.getAllErrors().get(0).getDefaultMessage());
            return "member/memberForm";
        }

        memberService.updateMember(memberDomain,request);
        model.addAttribute("formFlag","N"); //update flag
        model.addAttribute("message","수정되었습니다.");

        return "member/searchForm";
    }

    @GetMapping("/searchForm")
    public String searchForm(
                             @RequestParam(required = false,defaultValue = "") String keyword,
                             @RequestParam(required = false, defaultValue = "") String condition,
                             Model model
                             ) {

        model.addAttribute("keyword",keyword);
        model.addAttribute("condition",condition);
        List list = memberService.findSearchMembers(keyword,condition);
        model.addAttribute("list",list);

        return "member/searchForm";
    }

}
