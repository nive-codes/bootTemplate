package com.nive.prjt.com.file.controller;

import com.nive.prjt.com.file.domain.ComFileDomain;
import com.nive.prjt.com.file.domain.ComFileTempDomain;
import com.nive.prjt.com.file.service.ComFileRestService;
import com.nive.prjt.config.response.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author nive
 * @class ComFileRestController
 * @desc 기타 모듈에서의 본 파일 테이블과의 상호동작을 담당하는 RestController
 * @since 2025-01-16
 */
@RestController
@RequestMapping("/api/files/")
@AllArgsConstructor
public class ComFileRestController {


    private ComFileRestService comFileRestService;

    @PostMapping("/upload")
    public ApiResponse saveFile(@RequestParam("files") MultipartFile[] files,      // 파일 배열
                                       @RequestBody ComFileTempDomain comFileTempDomain

    ) {
        return null;
    }

    //file ID, file seq를 파라미터로 받아서 처리
    @PatchMapping("/upload")
    public ApiResponse deleteFile(@RequestBody ComFileTempDomain comFileTempDomain

    ) {
        return null;
    }


    //file ID, file seq를 파라미터로 받아서 처리
    @DeleteMapping("/upload")
    public ApiResponse deleteMeta(@RequestBody ComFileTempDomain comFileTempDomain

    ) {
        return null;
    }

}
