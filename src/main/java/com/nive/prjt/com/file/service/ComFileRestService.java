package com.nive.prjt.com.file.service;

import com.nive.prjt.com.file.domain.ComFileTempDomain;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author nive
 * @class ComFileRestService
 * @since 2025-01-23
 * @desc ComFileService -> ComFileRestService로 수정 및 변경 (file upload 부분을 비동기 형태로 활용 + 임시 파일 테이블 활용) - ComFileMetaService와 ComFileUploadService 조합
 */
public interface ComFileRestService {

    /**
     * 파일 업로드 요청 temp 파일에 업로드 요청 처리 및 검증(fileType에 따른 enum데이터 검증? 혹은 전체 fileType을 검증할까? 용량 검증도 추가)
     * @param files 업로드한 파일
     * @Param comFileTempDomain COmFileTempDomain 객체
     */
    String uploadFileTemp(MultipartFile[] files, ComFileTempDomain comFileTempDomain);


}
