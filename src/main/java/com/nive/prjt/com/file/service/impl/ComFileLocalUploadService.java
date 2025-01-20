package com.nive.prjt.com.file.service.impl;

import com.nive.prjt.com.file.service.ComFileUploadService;
import com.nive.prjt.config.exception.business.BusinessRestException;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;



/**
 * @author nive
 * @class FileLocalUploadService
 * @desc 파일 로컬 업로드 service impl
 * config/FileConfig.java 및 yml의 설정에 따라 bean 생성
 * 예외처리는 모두 ComFileService쪽에서 처리하도록 수정
 * @since 2025-01-16
 */
@Slf4j
public class ComFileLocalUploadService implements ComFileUploadService {

    @Value("${spring.file-storage.path}")
    String fileStoragePath;

    @Override
    public boolean uploadFile(MultipartFile file, String filePath) {
        fileStoragePath = fileStoragePath.replace("\\", "/");
        System.out.println("fileStoragePath : " + fileStoragePath);
        try {
            Path targetPath = Paths.get(fileStoragePath, filePath);
            System.out.println("filePath : "+filePath);
            Files.createDirectories(targetPath.getParent()); // 경로가 없을 경우 생성
            file.transferTo(targetPath.toFile()); // 파일 저장
            log.info("파일 업로드 성공: {}", filePath);
            return true;
        } catch (IOException e) {
            log.error("파일 업로드 실패: {}", filePath, e);
            return false;
        } catch (Exception e) {
            log.error("파일 업로드 실패: {}", filePath, e);
            return false;
        }
    }

    @Override
    public File selectFile(String filePath) {
        Path targetPath = Paths.get(fileStoragePath, filePath);
        File file = targetPath.toFile();
        if (file.exists() && file.isFile()) {
            return file;
        } else {
            log.warn("파일 없음: {}", filePath);
            return null; // 파일이 없으면 null 반환
        }
    }

    @Override
    public boolean deleteFile(String filePath) {
        try {
            Path targetPath = Paths.get(fileStoragePath, filePath);
            boolean deleted = Files.deleteIfExists(targetPath); // 파일이 존재하면 삭제
            if (deleted) {
                log.info("파일 삭제 성공: {}", filePath);
            } else {
                log.warn("파일 삭제 실패 (파일 없음): {}", filePath);
            }
            return deleted;
        } catch (IOException e) {
            log.error("파일 삭제 실패: {}", filePath, e);
            return false;
        } catch (Exception e) {
            log.error("파일 삭제 실패: {}", filePath, e);
            return false;
        }
    }

    @Override
    public boolean isFileExist(String filePath) {
        System.out.println("fileStoragePath : " + fileStoragePath);
        Path targetPath = Paths.get(fileStoragePath, filePath);
        return Files.exists(targetPath);
    }

    @Override
    public File uploadThumbnail(MultipartFile file, String filePath) {
        try {
            // 파일 경로에 '_thum'을 붙여 썸네일 경로 생성
            String thumbnailFilePath = filePath.substring(0, filePath.lastIndexOf(".")) + "_thum" + filePath.substring(filePath.lastIndexOf("."));
            Path targetPath = Paths.get(fileStoragePath, thumbnailFilePath);  // fileStoragePath와 filePath 합침
            File outputFile = targetPath.toFile();

            Thumbnails.of(file.getInputStream())
                    .size(100, 100) // 썸네일 크기 설정
                    .toFile(outputFile); // 썸네일 파일 저장

            return outputFile;
        } catch (IOException e) {
            log.error("썸네일 생성 실패: {}", filePath, e);
            return null;

        }catch (Exception e) {
            log.error("썸네일 생성 실패: {}", filePath, e);
            return null;
        }
    }
}
