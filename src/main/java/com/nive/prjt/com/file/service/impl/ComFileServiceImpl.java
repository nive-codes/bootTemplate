package com.nive.prjt.com.file.service.impl;

import com.nive.prjt.com.file.domain.ComFileDomain;
import com.nive.prjt.com.file.service.ComFileMetaService;
import com.nive.prjt.com.file.service.ComFileService;
import com.nive.prjt.com.file.service.ComFileType;
import com.nive.prjt.com.file.service.ComFileUploadService;
import com.nive.prjt.config.exception.business.BusinessException;
import com.nive.prjt.config.exception.business.BusinessRestException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;


/**
 * @author nive
 * @class ComFileService
 * @desc ComFileMetaService 및 ComFileUploadService를 활용한 파일 관리를 담당하는 interface
 *  * FileMetaService와 FileUploadService의 결합 구현 interface
 * @since 2025-01-16
 */
@Service
@AllArgsConstructor
@Slf4j
public class ComFileServiceImpl implements ComFileService {

    private final ComFileMetaService comFileMetaService;
    private final ComFileUploadService comFileUploadService;


    /**
     * 단일 파일 업로드 로직 (fileId가 null인 경우 새로운 fileID를 생성해 return, fileId가 null이 아닌 경우 fileSeq 증가)
     * @param file 업로드한 파일
     * @param filePath 파일 업로드 경로(모듈명)
     * @param fileId 기존 파일ID
     * @param fileType 파일검증 Enum
     * @param fileSizeMB 파일용량
     * @return
     */
    @Override
    public String uploadFile(MultipartFile file, String filePath, String fileId, ComFileType fileType, long fileSizeMB) {

        if(validateFile(file,fileType, fileSizeMB)){

            ComFileDomain comFileDomain = createFileDomain(file,filePath,fileId);   //변수값을 토대로 comFileDomain  생성

            // 업로드 파일 시도
            if (comFileUploadService.uploadFile(file, filePath)) {
                return processFileId(fileId,comFileDomain);
            } else {
                log.warn("물리적 파일 업로드 실패 : {}", filePath);
                return null;
            }
        }else{
            log.warn("업로드할 파일 검증 실패했습니다.");
            return null;
        }
    }


    @Override
    public String uploadFileList(MultipartFile[] files, String filePath, String fileId, ComFileType fileType, long fileSizeMB)  {

        if (files == null || files.length == 0) {
            log.warn("업로드할 파일이 없습니다.");
            return null;
        }

        //   파일 반복문 처리
        for (MultipartFile file : files) {
            if(validateFile(file,fileType, fileSizeMB)){
                ComFileDomain comFileDomain = createFileDomain(file,filePath,fileId);
                //변수값을 토대로 comFileDomain  생성
                if (comFileUploadService.uploadFile(file, filePath)) {
                    fileId = processFileId(fileId, comFileDomain);
                } else {
                    log.error("물리적 파일 업로드 실패 : {}", file.getOriginalFilename());
                    throw new BusinessRestException("파일 업로드에 실패했습니다.","VALIDATE",HttpStatus.BAD_REQUEST);
                }
            } else {
                log.error("물리적 파일 데이터 검증 실패 : {}", file.getOriginalFilename());
                throw new BusinessRestException("파일 데이터 검증에 실패했습니다.","VALIDATE", HttpStatus.BAD_REQUEST);
            }
        }

        return fileId;  // 모든 파일이 처리된 후 fileId 반환
    }

    @Override
    public boolean deleteFile(String fileId) {
//        TODO 파일업로드 테스트 및 확인 완료 후 작성
        /*fileMetaService의 정보 논리삭제 호출*/
        return false;
    }


    @Override
    public boolean deleteRealFile(String fileId) {
//        TODO 파일업로드 테스트 및 확인 완료 후 작성
        /*fileMetaService의 정보 물리삭제 및 파일 삭제 호출*/
        return false;
    }


    /**
     * 파일 검증 로직을 공통 메소드로 분리
     * 1. 파일 여부 체크
     * 2. 확장자 존재 여부 체크
     * 3. 확장자 type 체크 여부
     * @param file
     * @param fileType
     * @return
     */

    private boolean validateFile(MultipartFile file, ComFileType fileType, long maxSizeMB) {
        if (file.isEmpty()) {
            log.warn("업로드할 파일이 비어 있습니다.");
            return false;
        }

        // 확장자 존재여부 확인
        String fileSuffix = getFileSuffix(file);
        if (fileSuffix == null) {
            log.warn("파일 확장자가 없습니다. : {}", file.getOriginalFilename());
            return false;
        }

        // 확장자 검증
        if (!isExtValid(fileSuffix, fileType)) {
            log.warn("허용되지 않은 파일 확장자입니다. 파일: {}", file.getOriginalFilename());
            return false;
        }

        // 파일 용량 검증 확인
        if (!isFileSizeValid(file, maxSizeMB)){
            log.warn("파일 용량이 너무 큽니다. 파일: {}", file.getOriginalFilename());
            return false;
        }

        return true;
    }

    /**
     * 변수로 받아온 파라미터를 토대로 ComFileDomain 생성
     * @param file
     * @param filePath
     * @param fileId
     * @return
     */
    private ComFileDomain createFileDomain(MultipartFile file, String filePath, String fileId) {
        ComFileDomain comFileDomain = new ComFileDomain();
        comFileDomain.setFileId(fileId);   // 변수로 받은 fileId
        comFileDomain.setFilePath(filePath);    //파일 경로(모듈 명)
        comFileDomain.setFileModule(filePath);    //파일 경로(모듈 명)
        comFileDomain.setFileOrignNm(file.getOriginalFilename());   //실제 파일 업로드명
        comFileDomain.setFileSize(file.getSize()); //파일 사이즈
        comFileDomain.setFileUpldNm(System.currentTimeMillis() + getFileSuffix(file));
        return comFileDomain;
    }

    /***
     * 파일 용량 검증 메서드
     * @param file
     * @param maxSizeMB
     * @return
     */
    private boolean isFileSizeValid(MultipartFile file, long maxSizeMB) {
        long fileSizeMB = file.getSize() / (1024 * 1024);  // MB 단위로 변환
        return fileSizeMB <= maxSizeMB;  // maxSizeMB보다 작거나 같으면 유효
    }

    /**
     *
     * @param ext 파일 타입(image, txt 등)
     * @param comFileType 첨부파일의 파일 확장자검증 Enum
     * @return
     */
    private boolean isExtValid(String ext, ComFileType comFileType) {
        if(ext == null || ext.isBlank()){
            return false;
        }else{
            return comFileType.getAllowedExtensions().contains(ext.trim().toLowerCase());
        }
    }

    /**
     * 파일 확장자 return 메서드
     * @param file
     * @return
     */
    private String getFileSuffix(MultipartFile file) {
        // 파일 이름에서 확장자를 추출
        String originalFilename = file.getOriginalFilename();
        if (originalFilename != null && originalFilename.contains(".")) {
            // 파일 확장자 추출 (점(.)을 기준으로 분리)
            return originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        return null;  // 확장자가 없을 경우 null 반환
    }

    /**
     * 이미지파일 확인 메서드
     * @param file
     * @return boolean
     */
    private boolean isImage(MultipartFile file) {
        String suffix = getFileSuffix(file);
        if (suffix != null) {
            // 이미지 파일 확장자 목록
            return suffix.equalsIgnoreCase(".jpg") ||
                    suffix.equalsIgnoreCase(".jpeg") ||
                    suffix.equalsIgnoreCase(".png") ||
                    suffix.equalsIgnoreCase(".gif") ||
                    suffix.equalsIgnoreCase(".bmp") ||
                    suffix.equalsIgnoreCase(".tiff");
        }
        return false;  // 이미지 확장자가 아닌 경우
    }

    /**
     * FileMetaService의 생성 / 수정 시 fileId를 반환
     * @param fileId
     * @param comFileDomain
     * @return String
     */
    private String processFileId(String fileId, ComFileDomain comFileDomain) {
        // fileId가 null 또는 빈 값인 경우 새로 추가, 그렇지 않으면 업데이트
        try{
            if (fileId == null || fileId.isBlank()) {
                fileId = comFileMetaService.insertFileMeta(comFileDomain);
            } else {
                comFileDomain.setFileId(fileId);
                comFileMetaService.updateFileMeta(comFileDomain);
            }
            return fileId;

        } catch (Exception e){
            log.error("파일 메타 데이터 업로드 실패 {}", e.getMessage());
            return null;
        }
    }
}
