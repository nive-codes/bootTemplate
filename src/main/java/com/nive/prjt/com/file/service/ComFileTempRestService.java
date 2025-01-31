package com.nive.prjt.com.file.service;

import com.nive.prjt.com.file.domain.ComFileTempDomain;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author nive
 * @class ComFileTempRestService
 * @desc ComFileTempRestService로 임시 파일 데이터 수정 및 변경 - ComFileTempMetaService와 ComFileUploadService 조합
 * @since 2025-01-23
 */
public interface ComFileTempRestService {
    /**
     * 임시 테이블 저장 및 파일 업로드(parameter로 받은 fileType으로 데이터 검증, 용량 검증, 파일 확장자 체크)
     * @param files 업로드한 파일
     * @Param comFileTempDomain COmFileTempDomain 객체
     */
    String uploadFileTemp(MultipartFile[] files, ComFileTempDomain comFileTempDomain);

    /**
     * 임시 테이블의 데이터 삭제 및 실제 파일 삭제(임시 파일까지 삭제처리를 한다)
     * @param comFileTempDomain
     */
    void deleteFileTemp(String fileId, ComFileTempDomain comFileTempDomain);

}
