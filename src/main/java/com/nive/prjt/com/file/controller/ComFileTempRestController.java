package com.nive.prjt.com.file.controller;

import com.nive.prjt.com.file.domain.ComFileTempDomain;
import com.nive.prjt.com.file.service.ComFileTempRestService;
import com.nive.prjt.config.response.ApiCode;
import com.nive.prjt.config.response.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

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
        // 업로드된 파일의 정보 포함하여 응답 반환
        return ApiResponse.ok("파일 업로드 성공했습니다.",
                ComFileTempDomain.builder()
                        .fileId(idx)
                        .fileSeq(comFileTempDomain.getFileSeq())  // fileSeq 값 확인
                        .fileOrd(comFileTempDomain.getFileOrd())  // fileOrd 값 확인
                        .build());
    }

    //temp 파일 삭제(모듈 save 전)
    //file ID, file seq를 파라미터로 받아서 처리
    @DeleteMapping("/upload/{fileId}")
    public ApiResponse deleteFile(@PathVariable String fileId, @RequestBody ComFileTempDomain comFileTempDomain) {

        if(Objects.isNull(fileId) || fileId.isBlank()){
            return ApiResponse.fail(ApiCode.NOT_FOUND,new ComFileTempDomain());
        }

        comFileTempRestService.deleteFileTemp(fileId, comFileTempDomain);

        return ApiResponse.ok("파일 삭제 되었습니다.");
    }


    /*TODO expired된 데이터 전체 삭제 처리 + file 데이터 전체 삭제 처리 추가 필요*/

}
