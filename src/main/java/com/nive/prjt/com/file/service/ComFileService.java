package com.nive.prjt.com.file.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * @author nive
 * @class ComFileService
 * @desc ComFileMetaService 및 ComFileUploadService를 활용한 파일 관리를 담당하는 interface
 * FileMetaService와 FileUploadService의 결합 구현 interface
 * @since 2025-01-16
 */
public interface ComFileService {

    /**
     * 단일파일을 실제로 서버에 저장(fileId가 있으면 meta update 없으면 meta insert)
     * @param file 업로드한 파일
     * @param filePath 파일 업로드 경로(모듈명)
     * @param fileId 기존 파일ID
     * @param fileType 파일검증 Enum
     * @param fileSizeMB 파일용량
     * @return blooean 성공여부 return
     */
    String uploadFile(MultipartFile file, String filePath, String fileId, ComFileType fileType, long fileSizeMB);

    /**
     * 다중파일 실제로 서버 저장(최초 insert)
     * @param files 업로드한 파일 배열
     * @param filePath 파일 업로드 경로(모듈명)
     *  @param fileId 기존 파일ID
     *  @param fileType 파일검증 Enum
     *  @param fileSizeMB 파일용량
     */
    String uploadFileList(MultipartFile[] files, String filePath, String filId, ComFileType fileType, long fileSizeMB);


    /**
     * meta데이터 논리 삭제 호출
     * @param fileId 기존 파일ID
     * @return
     */
    boolean deleteFile(String fileId);

    /**
     * meata데이터 + 파일 물리 삭제 처리
     * @param fileId
     * @return
     */
    boolean deleteRealFile(String fileId);




}
