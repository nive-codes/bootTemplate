package com.nive.prjt.nive.myBatisTest.controller;

import com.nive.prjt.nive.myBatisTest.domain.TestDomain;
import com.nive.prjt.nive.myBatisTest.service.TestRestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * [클래스 역할 간략 설명]
 *
 * @author nive
 * @desc rest api (ResponseEntity) 용 module 작성
 * @since 2025-01-11
 */
@RestController
@RequestMapping("/testApi")
@RequiredArgsConstructor
public class TestRestController {

    private  final TestRestService testRestService;

    // 데이터 삽입 (POST /testApi)
    @PostMapping
    public ResponseEntity<String> insertTest(@RequestBody @Valid TestDomain testDomain, BindingResult bindingResult) {
        // 검증 오류가 있는지 확인
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("Validation failed: " + bindingResult.getAllErrors().toString());
        }
        return testRestService.insertTest(testDomain);
    }

    // 데이터 조회 (GET /testApi/{tbIdx})
    @GetMapping("/{tbIdx}")
    public ResponseEntity<TestDomain> getTest(@PathVariable String tbIdx) {
        return testRestService.getTest(tbIdx);
    }

    // 데이터 삭제 (DELETE /testApi/{tbIdx})
    @DeleteMapping("/{tbIdx}")
    public ResponseEntity<String> deleteTest(@PathVariable String tbIdx) {
        return testRestService.deleteTest(tbIdx);
    }

    // 데이터 수정 (PUT /testApi/{tbIdx})
    @PutMapping("/{tbIdx}")
    public ResponseEntity<String> updateTest(@PathVariable String tbIdx, @RequestBody @Valid TestDomain testDomain, BindingResult bindingResult) {
        // 검증 오류가 있는지 확인
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("Validation failed: " + bindingResult.getAllErrors().toString());
        }
        return testRestService.updateTest(tbIdx, testDomain);
    }

    // 데이터 전체 조회 (GET /testApi)
    @GetMapping
    public ResponseEntity<Object> findAll(TestDomain testDomain) {
        return testRestService.findAll(testDomain);
    }


}
