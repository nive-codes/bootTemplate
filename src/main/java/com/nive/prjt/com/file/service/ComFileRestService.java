package com.nive.prjt.com.file.service;

import com.nive.prjt.com.file.domain.ComFileDomain;
import com.nive.prjt.com.file.domain.ComFileTempDomain;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author nive
 * @class ComFileRestService
 * @since 2025-01-23
 * @desc ComFileService -> ComFileRestService로 수정 및 변경 (file upload 부분을 비동기 형태로 활용) - ComFileMetaService와 ComFileUploadService 조합
 */
public interface ComFileRestService {

    /**
     *  TEMP에 저장된 파일 transter 처리
     * @param fileId
     * @param comFileType
     * @param fileSize
     * @param fileCnt
     * @return
     */
    void transferFile(String fileId,ComFileType comFileType, Long fileSize, int fileCnt,String filePath);

    /**
     * 본 테이블의 데이터를 논리삭제 처리한다.
     * @param comFileTempDomain
     */
    void deleteFile(ComFileTempDomain comFileTempDomain);


    /**
     * cron 혹은 Spring batch 활용
     * 논리 삭제된 항목을 일괄 meta데이터 삭제 및 파일 삭제 로직
     * @param comFileTempDomain
     */
    void deleteExpired(ComFileTempDomain comFileTempDomain);

    /**
     * 파일 정보 목록을 조회
     * @param fileId
     * @return
     */
    public List<ComFileDomain> selectFileMetaList(String fileId);

    /**
     * 파일 단일 정보를 조회
     * @param fileId
     * @param fileSeq
     * @return
     */
    public ComFileDomain selectFileMeta(String fileId, int fileSeq);


}
