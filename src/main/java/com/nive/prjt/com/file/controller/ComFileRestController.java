package com.nive.prjt.com.file.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author nive
 * @class ComFileRestController
 * @desc 기타 모듈에서의 insert, update 이후 파일 단일 동작(order 수정, delete 동작 정의 Controller)
 * @since 2025-01-16
 */
@RestController
@RequestMapping("/api/com/file")
public class ComFileRestController {

//    private final ComFileUploadService fileUploadService;
//
//    public ComFileRestController(ComFileUploadService fileUploadService) {
//        this.fileUploadService = fileUploadService;
//    }

    @PostMapping("/ord")
    public void upload(MultipartFile[] files){

    }
}
