package com.nive.prjt.com.file.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * @author nive
 * @class FileUploadService
 * @desc 실제로 파일을 서버에 저장하는 메서드를 관리하는 interface
 * config/FileConfig.java 및 yml의 설정에 따라 bean 생성
 * @since 2025-01-16
 */
public interface ComFileUploadService {


    /**
     * 파일을 실제로 서버에 저장을 요청하는 메서드
     * @param file
     * @return boolean
     */
    boolean uploadFile(MultipartFile file, String filePath);

    /**
     * 파일이 실제로 서버에 저장된 파일을 가져오는 메서드
     * @param filePath
     * @return
     */
    File selectFile(String filePath);

    /**
     * 경로에 존재하는 실제 파일을 삭제하는 메서드
     * @param filePath
     * @return
     */
    boolean deleteFile(String filePath);

    /**
     * 파일이 존재하는지 확인하는 메서드
     * @param filePath
     * @return boolean
     */
    boolean isFileExist(String filePath);

    /**
     * 썸네일을 생성하는 메서드
     * @param file
     * @param filePath
     * @return
     */
    File uploadThumbnail(MultipartFile file, String filePath);



}
