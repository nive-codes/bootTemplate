package com.nive.prjt.com.file.service.impl;

import com.nive.prjt.com.file.domain.ComFileDomain;
import com.nive.prjt.com.file.service.ComFileMetaService;
import com.nive.prjt.com.file.service.ComFileService;
import com.nive.prjt.com.file.service.ComFileType;
import com.nive.prjt.com.file.service.ComFileUploadService;
import com.nive.prjt.config.exception.business.BusinessException;
import com.nive.prjt.config.exception.business.BusinessRestException;
import com.nive.prjt.config.response.ApiCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

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
    private final HttpServletRequest request;
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


        /*파일이 비어있으면 upload 호출 X*/
        if(isFileEmpty(file)){
            log.warn("업로드할 파일이 없습니다.");
            return null;
        }

        if(validateFile(file,fileType, fileSizeMB)){


            ComFileDomain comFileDomain = createFileDomain(file,filePath,fileId);   //변수값을 토대로 comFileDomain  생성
            log.info("comFileDomain : "+comFileDomain);
            if (comFileUploadService.uploadFile(file, comFileDomain.getFilePath()+comFileDomain.getFileUpldNm())) {
                return processFileId(fileId,comFileDomain);
            } else {
                log.error("물리적 파일 업로드 실패 : {}", file.getOriginalFilename());
                /*returnBusinessException에서 restController 요청인지 확인 후 return 처리*/
//                throw returnBusinessException("파일 업로드가 정상적으로 처리되지 못했습니다.","/error/404");
                throw new BusinessException("파일 업로드가 정상적으로 처리되지 못했습니다.","/error/404");


            }
        }else{
            log.error("물리적 파일 데이터 검증 실패 : {}", file.getOriginalFilename());
//            throw returnBusinessException("파일 검증이 정상적으로 이루어지지 않았습니다.","/error/404");
            throw new BusinessException("파일 업로드가 정상적으로 처리되지 못했습니다.","/error/404");
        }
    }


    @Override
    public String uploadFileList(List<MultipartFile> files, String filePath, String fileId, ComFileType fileType, long fileSizeMB)  {

        if (files == null || files.size() == 0) {
            log.warn("업로드할 파일이 없습니다.");
            return null;
        }

        //   파일 반복문 처리
        for (MultipartFile file : files) {

            if(validateFile(file,fileType, fileSizeMB)){
                ComFileDomain comFileDomain = createFileDomain(file,filePath,fileId);
                //변수값을 토대로 comFileDomain  생성
                log.info("comFileDomain : "+comFileDomain);
                if (comFileUploadService.uploadFile(file, comFileDomain.getFilePath()+comFileDomain.getFileUpldNm())) {
                    fileId = processFileId(fileId, comFileDomain);
                } else {
                    log.error("물리적 파일 업로드 실패 : {}", file.getOriginalFilename());
                    throw new BusinessException("파일 업로드가 정상적으로 처리되지 못했습니다.","/error/404");
//                    throw returnBusinessException("파일 업로드가 정상적으로 처리되지 못했습니다.","/error/404");
                }
            } else {
                log.error("물리적 파일 데이터 검증 실패 : {}", file.getOriginalFilename());
                throw new BusinessException("파일 업로드가 정상적으로 처리되지 못했습니다.","/error/404");
//                throw returnBusinessException("파일 검증이 정상적으로 이루어지지 않았습니다.","/error/404");
            }
        }

        return fileId;  // 모든 파일이 처리된 후 fileId 반환
    }

    @Override
    public boolean deleteFileList(String fileId) {
        try{
            comFileMetaService.deleteFileMetaList(fileId);
            return true;
        } catch (Exception e){
            return false;
        }
    }

    @Override
    public boolean deleteFile(String fileId, int fileSeq) {
        comFileMetaService.deleteFileMeta(fileId, fileSeq);
        return false;
    }

    @Override
    public boolean deleteRealFile(String fileId) {

//        TODO 파일업로드 테스트 및 확인 완료 후 작성
        /*fileMetaService의 정보 물리삭제 및 파일 삭제 호출*/
        return false;
    }

    @Override
    public List<ComFileDomain> selectFileList(String fileId) {
        return comFileMetaService.selectFileMetaList(fileId);
    }

    /**
     * 파일 검증 로직을 공통 메소드로 분리
     * 파일 여부 체크
     * @param file
     * @return boolean
     */
    private boolean isFileEmpty(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            log.warn("업로드할 파일이 비어 있습니다.");
            return true;
        }else{
            return false;
        }
    }

    /**
     * 파일 검증 로직을 공통 메소드로 분리
     * 1. 확장자 존재 여부 체크
     * 2. 확장자 type 체크 여부
     * @param file
     * @param fileType
     * @return boolean
     */
    private boolean validateFile(MultipartFile file, ComFileType fileType, long maxSizeMB) {
        log.info("파일 업로드 검증 합니다.");

//        if (file.isEmpty()) {
//            log.warn("업로드할 파일이 비어 있습니다.");
//            return false;
//        }

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
        comFileDomain.setFilePath(filePath+"/");    //파일 경로(모듈 명)
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


//        if(Objects.isNull(ext) || ext.isBlank()) {}
        if(ext == null || ext.isBlank()){
            return false;
        }else{
            String cleandExt = ext.trim().toLowerCase().replace(".","");
            return comFileType.getAllowedExtensions().contains(cleandExt);
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
    private String processFileId(String fileId, ComFileDomain comFileDomain){
        // fileId가 null 또는 빈 값인 경우 새로 추가, 그렇지 않으면 업데이트
        if (fileId == null || fileId.isBlank()) {
            try {
                fileId = comFileMetaService.insertFileMeta(comFileDomain);
            } catch (Exception e) {
                log.error("파일 메타 데이터 INSERT 실패 {}", e.getMessage());
                throw new BusinessException("파일 메타 데이터 update 실패","/error/404");
            }
        } else {
            try {
                comFileDomain.setFileId(fileId);
                comFileMetaService.updateFileMeta(comFileDomain);
            } catch (Exception e) {
                log.error("파일 메타 데이터 UPDATE 실패 {}", e.getMessage());
                throw new BusinessException("파일 메타 데이터 update 실패","/error/404");
            }
        }
        return fileId;
    }


//    20250121 수정 -> BusinessException과 BusinessRestException 통합으로 return 처리
//                 -> RestApi 및 mvc의 처리 시 자동으로 ApiResponse return 및 mvc 에러 처리가 가능하다.
//    /**
//     * 파일 업로드 시 공통 예외 처리 BusinessException return 메소드
//     * RestController요청인 경우 RestException 요청(ApiResponse return)
//     * Controller요청인 경우 Exception 요청(view return)
//     * @param message
//     * @param returnView
//     * @return
//     */
//    private RuntimeException returnBusinessException(String message, String returnView){
//        if (request.getAttribute("org.springframework.web.servlet.DispatcherServlet.CONTROLLER") instanceof RestController) {
//            return new BusinessRestException(message, "FILE_UPLOAD_FAILURE",HttpStatus.BAD_REQUEST);  // REST API 응답
//        } else {
//            return new BusinessException(message, returnView);  // View 리디렉션
//        }
//    }
}
