package com.nive.prjt.nive.member.controller;

import com.nive.prjt.nive.member.domain.MemberDomain;
import com.nive.prjt.nive.member.service.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

/**
 * @author nive
 * @class MemberController
 * @desc 회원 데이터 + 파일 업로드 예제 Controller
 * @since 2025-01-21
 */
@Controller("/member")
@AllArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/insertForm")
    public String insertForm(Model model) {
        model.addAttribute("formFlag","Y"); //insert flag
        return "member/memberForm";
    }

    @PostMapping("/insert")
    public String insert(MemberDomain memberDomain, MultipartFile file, Model model) {

        memberService.insertMember(memberDomain,file);
        model.addAttribute("message","등록되었습니다.");

        return "member/searchForm";
    }

    @PostMapping("/updateForm")
    public String updateForm(MemberDomain memberDomain, Model model) {
        memberDomain = memberService.findMember(memberDomain.getMemberId());
        if(Objects.isNull(memberDomain)){
            model.addAttribute("formFlag","Y");
            return "member/memberForm";
        }
        model.addAttribute("formFlag","N"); //update flag
        model.addAttribute("memberDomain",memberDomain);
        return "member/memberForm";
    }

    @PostMapping("/update")
    public String update(MemberDomain memberDomain, MultipartFile file, Model model) {

        memberService.updateMember(memberDomain,file);
        model.addAttribute("formFlag","N"); //update flag
        model.addAttribute("message","수정되었습니다.");

        return "member/searchForm";
    }


    @PostMapping("/searchForm")
    public String searchForm(MemberDomain memberDomain, MultipartFile file,
                             @RequestParam(required = false,defaultValue = "") String search,
                             @RequestParam(required = false, defaultValue = "") String condition,
                             Model model
                             ) {

        memberService.findSearchMembers(search,condition);

        return "member/searchForm";
    }

}
