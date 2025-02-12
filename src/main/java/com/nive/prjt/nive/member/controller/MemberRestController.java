package com.nive.prjt.nive.member.controller;

import com.nive.prjt.config.response.ApiCode;
import com.nive.prjt.config.response.ApiResponse;
import com.nive.prjt.nive.member.domain.MemberDomain;
import com.nive.prjt.nive.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author nive
 * @class MemberRestController
 * @desc Member 테이블 관련 rest API Controller
 * @since 2025-02-11
 */

@RestController
@RequestMapping("/api/members")
@AllArgsConstructor
public class MemberRestController {

    private final MemberService memberService;

    @PostMapping
    public ApiResponse insert(@Valid @RequestBody MemberDomain memberDomain
                              ) {

        memberService.insertMember(memberDomain);
        return ApiResponse.ok(ApiCode.SUCCESS);

    }

    @PatchMapping("/{memberId}")
    public ApiResponse update(@PathVariable String memberId, @Valid @RequestBody MemberDomain memberDomain) {

//        전역 예외에 걸리도록 처리 -> bidngResult는 @ReuqestBody와 쓸 수 없다
//        if(bindingResult.hasErrors()) {
//            List<String> errorMessages = bindingResult.getFieldErrors()
//                    .stream()
//                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
//                    .collect(Collectors.toList());
//            return ApiResponse.fail(ApiCode.VALIDATION_FAILED,errorMessages);
//        }

        memberService.updateMember(memberDomain);

        return ApiResponse.ok(ApiCode.SUCCESS);
    }


    /***
     * delete는 요청 본문을 허용하지 않으므로 @RequestBody MemberDomain memberDomain 삭제
     * @param memberId
     * @return
     */
    @DeleteMapping("/{memberId}")
    public ApiResponse delete(@PathVariable String memberId) {

        memberService.deleteMember(memberId);

        return ApiResponse.ok(ApiCode.SUCCESS);
    }

}
