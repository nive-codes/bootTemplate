package com.nive.prjt.com.file.controller;

import com.nive.prjt.com.file.domain.ComFileTempDomain;
import com.nive.prjt.com.file.service.ComFileTempRestService;
import com.nive.prjt.config.response.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author nive
 * @class ComFileTempRestController
 * @desc 타 모듈의 프론트에서 처리될 임시 파일 데이터 저장 및 파일 저장을 담당하는 controller
 * @since 2025-01-23
 */
@RestController
@RequestMapping("/api/tempFiles/")
@AllArgsConstructor
public class ComFileTempRestController {


    private ComFileTempRestService comFileTempRestService;

    // 파일 업로드 시 Temp 파일에 최초 업로드
    @PostMapping("/upload")
    public ApiResponse saveFile(@RequestParam("files") MultipartFile[] files,      // 파일 배열
                                        ComFileTempDomain comFileTempDomain

    ) {
        String idx = comFileTempRestService.uploadFileTemp(files,comFileTempDomain);
        return ApiResponse.ok("파일 업로드 성공했습니다.",ComFileTempDomain.builder().fileId(idx).build());
    }

    //temp 파일 삭제(모듈 save 전)
    //file ID, file seq를 파라미터로 받아서 처리
    @DeleteMapping("/upload")
    public ApiResponse deleteFile(ComFileTempDomain comFileTempDomain

    ) {
        comFileTempRestService.deleteFileTemp(comFileTempDomain);

        return ApiResponse.ok("파일 삭제 되었습니다.");
    }


    /*TODO expired된 데이터 전체 삭제 처리 + file 데이터 전체 삭제 처리 추가 필요*/

}
