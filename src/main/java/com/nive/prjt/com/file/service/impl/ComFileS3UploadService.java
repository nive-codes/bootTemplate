package com.nive.prjt.com.file.service.impl;

import com.nive.prjt.com.file.service.ComFileUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;


import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/**
 * @author nive
 * @class FileS3UploadService
 * @desc AWS S3의 파일 업로드 서비스 impl
 * config/FileConfig.java 및 yml의 설정에 따라 bean 생성
 * FileConfig에서 yml의 bucket name을 받도록 수정
 * @since 2025-01-16
 */

@Slf4j
public class ComFileS3UploadService implements ComFileUploadService {

    private final S3Client s3Client;
    private final String bucketName;

    public ComFileS3UploadService(S3Client s3Client,String bucketName) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
    }

    @Override
    public boolean uploadFile(MultipartFile file, String filePath, String fileName) {

        log.error("s3Client.uploadFile bucket name {}" +bucketName);


        try {
            //  업로드할 파일을 RequestBody로 변환
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(filePath+"/"+fileName) // S3 내 파일 경로
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
            log.info("File uploaded to S3 successfully: {}", filePath+"/"+fileName);
            return true;
        } catch (IOException e) {
            log.error("Failed to upload file to S3: {}", e.getMessage());
            return false;
        }
    }

    /*사실 경로만 전달하면 될 것으로 예상됨... 파일을 다시 가지고 올 필요는 없지 않을지 검토*/
    @Override
    public File selectFile(String filePath) {
        try {
            // 로컬에 파일 다운로드
            File tempFile = File.createTempFile("s3-", filePath.replace("/", "_"));
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(filePath)
                    .build();

            s3Client.getObject(getObjectRequest, Path.of(tempFile.getPath()));
            log.info("File downloaded from S3: {}", filePath);
            return tempFile;
        } catch (Exception e) {
            log.error("Failed to download file from S3: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public boolean deleteFile(String filePath) {
        try {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(filePath)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);
            log.info("File deleted from S3: {}", filePath);
            return true;
        } catch (Exception e) {
            log.error("Failed to delete file from S3: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public boolean isFileExist(String filePath) {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(filePath)
                    .build();

            s3Client.getObject(getObjectRequest);
            return true; // 파일이 존재하면 예외 없이 처리됨
        } catch (Exception e) {
            log.warn("File does not exist in S3: {}", filePath);
            return false;
        }
    }

    @Override
    public File uploadThumbnail(MultipartFile file, String filePath) {
        return null;
    }
}
