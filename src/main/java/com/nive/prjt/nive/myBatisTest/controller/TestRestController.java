package com.nive.prjt.nive.myBatisTest.controller;

import com.nive.prjt.config.response.ApiCode;
import com.nive.prjt.nive.myBatisTest.domain.TestDomain;
import com.nive.prjt.nive.myBatisTest.service.TestRestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.nive.prjt.config.response.ApiResponse;
/**
 * [클래스 역할 간략 설명]
 *
 * @author nive
 * @desc rest api (ResponseEntity) 용 module 작성
 * @since 2025-01-11
 */
@Tag(name = "02.Test CRUD Controller", description = "테스트용 CRUD 컨트롤러입니다.")
@RestController
@RequestMapping("/testApi")
@RequiredArgsConstructor

public class TestRestController {

    private  final TestRestService testRestService;

    // 데이터 삽입 (POST /testApi)
    @PostMapping
    @Operation(summary = "데이터 등록", description = "test 데이터를 등록합니다.")
    public ApiResponse insertTest(@RequestBody @Valid TestDomain testDomain, BindingResult bindingResult) {
        // 검증 오류가 있는지 확인
        if (bindingResult.hasErrors()) {
            return ApiResponse.fail(ApiCode.VALIDATION_FAILED);
        }
        return testRestService.insertTest(testDomain);
    }

    // 데이터 조회 (GET /testApi/{tbIdx})
    @GetMapping("/{tbIdx}")
    @Operation(summary = "데이터를 조회", description = "test 데이터 조회합니다.")
    public ApiResponse getTest(@PathVariable String tbIdx) {
        return testRestService.getTest(tbIdx);
    }

    // 데이터 삭제 (DELETE /testApi/{tbIdx})
    @DeleteMapping("/{tbIdx}")
    @Operation(summary = "데이터를 삭제합니다.", description = "test 데이터를 삭제합니다")
    public ApiResponse deleteTest(@PathVariable String tbIdx) {
        return testRestService.deleteTest(tbIdx);
    }

    // 데이터 수정 (PUT /testApi/{tbIdx})
    @PatchMapping("/{tbIdx}")
    @Operation(summary = "데이터를 수정합니다.", description = "test 데이터를 수정합니다")
    public ApiResponse updateTest(@PathVariable String tbIdx, @RequestBody @Valid TestDomain testDomain, BindingResult bindingResult) {
        // 검증 오류가 있는지 확인
        if (bindingResult.hasErrors()) {
            return ApiResponse.fail(ApiCode.VALIDATION_FAILED);
        }
        return testRestService.updateTest(tbIdx, testDomain);
    }

    // 데이터 전체 조회 (GET /testApi)
    @GetMapping
    @Operation(summary = "전체 데이터 조회", description = "전체 데이터를 조회합니다.")
    public ApiResponse findAll(TestDomain testDomain) {
        return testRestService.findAll(testDomain);
    }


}
