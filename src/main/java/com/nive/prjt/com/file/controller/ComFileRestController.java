package com.nive.prjt.com.file.controller;

import com.nive.prjt.com.file.domain.ComFileDomain;
import com.nive.prjt.com.file.domain.ComFileTempDomain;
import com.nive.prjt.com.file.service.ComFileRestService;
import com.nive.prjt.com.file.service.ComFileService;
import com.nive.prjt.config.response.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author nive
 * @class ComFileRestController
 * @desc 기타 모듈에서의 insert, update 이후 파일 단일 동작(order 수정, delete 동작 정의 Controller)
 * @since 2025-01-16
 */
@RestController
@RequestMapping("/api/files/")
@AllArgsConstructor
public class ComFileRestController {


    private ComFileRestService comFileRestService;


    // 파일 업로드 시 Temp 파일에 최초 업로드
    @PostMapping("/temp/upload")
    public ApiResponse firstInsertFile(@RequestParam("files") MultipartFile[] files,      // 파일 배열
                                       @RequestBody ComFileTempDomain comFileTempDomain

    ) {
        /*String filePath, String fileId, String fileType, long fileSize 활용, temp_id는 auto 증가*/
        String idx = comFileRestService.uploadFileTemp(files,comFileTempDomain);

        return ApiResponse.ok("파일업로드 성공했습니다.",idx);
    }



    @GetMapping("/{fileId}")
    public ApiResponse getFile(@PathVariable String fileId){
        return ApiResponse.ok("파일 데이터를 불러왔습니다.");
    }

    @PutMapping("/upload")
    public ApiResponse updateFile(@PathVariable String fileId, @RequestParam("files") MultipartFile[] files){
        return ApiResponse.ok("파일데이터 수정했습니다.");
    }
}
