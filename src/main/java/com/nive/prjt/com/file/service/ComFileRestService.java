package com.nive.prjt.com.file.service;

import com.nive.prjt.com.file.domain.ComFileTempDomain;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author nive
 * @class ComFileRestService
 * @since 2025-01-23
 * @desc ComFileService -> ComFileRestService로 수정 및 변경 (file upload 부분을 비동기 형태로 활용) - ComFileMetaService와 ComFileUploadService 조합
 */
public interface ComFileRestService {

    /**
     * 본 테이블의 데이터를 논리삭제 처리한다.
     * @param comFileTempDomain
     */
    void deleteFile(ComFileTempDomain comFileTempDomain);


    /**
     * cron 혹은 Spring batch 활용
     * 임시 테이블의 일정시간이 지난 데이터 삭제 및 파일 삭제
     * @param comFileTempDomain
     */
    void deleteExpiredTemp(ComFileTempDomain comFileTempDomain);

    /**
     * cron 혹은 spring batch 활용
     * 본 테이블의 데이터를 물리 삭제 및 파일을 삭제한다.
     * @param comFileTempDomain
     */
    void deleteRealFile(ComFileTempDomain comFileTempDomain);


}
