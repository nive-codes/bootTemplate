package com.nive.prjt.com.file.service.impl;

import com.nive.prjt.com.file.service.ComFileUploadService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * @author nive
 * @class FileS3UploadService
 * @desc AWS S3의 파일 업로드 서비스 impl
 * config/FileConfig.java 및 yml의 설정에 따라 bean 생성
 * @since 2025-01-16
 */
public class ComFileS3UploadService implements ComFileUploadService {

    @Override
    public boolean uploadFile(MultipartFile file, String filePath) {
        return false;
    }

    @Override
    public File selectFile(String filePath) {
        return null;
    }

    @Override
    public boolean deleteFile(String filePath) {
        return false;
    }

    @Override
    public boolean isFileExist(String filePath) {
        return false;
    }

    @Override
    public File uploadThumbnail(MultipartFile file, String filePath) {
        return null;
    }
}
