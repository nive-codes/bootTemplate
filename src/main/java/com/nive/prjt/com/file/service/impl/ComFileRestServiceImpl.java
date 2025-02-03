package com.nive.prjt.com.file.service.impl;

import com.nive.prjt.com.file.domain.ComFileDomain;
import com.nive.prjt.com.file.domain.ComFileTempDomain;
import com.nive.prjt.com.file.service.*;
import com.nive.prjt.config.exception.business.BusinessException;
import com.nive.prjt.config.response.ApiCode;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * @author nive
 * @class ComFileRestService
 * @since 2025-01-23
 * @desc ComFileRestService를 통한 파일 temp 데이터 검증, 파일 검증, metaService 및 uploadService 핸들링
 * 각 모듈에서의 insert 요청 발생 시 TEMP 파일 검증
 * 각 모듈에서의 update 요청 발생 시 TEMP 파일 검증
 * 각 모듈에서의 파일 delete요청 시 파일 테이블 논리 삭제(spring batch를 활용한 물리 삭제 예정)
 * 각 모듈 데이터 delete 요청 시 파일 테이블 논리 삭제(spring batch를 활용한 물리 삭제 예정)
 */
@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class ComFileRestServiceImpl implements  ComFileRestService {

    private final ComFileMetaService comFileMetaService;
    private final ComFileTempMetaService comFileTempMetaService;

    @Override
    public void transferFile(String fileId,ComFileType comFileType, Long fileSize, int fileCnt, String filePath) {
        // 파일ID에 해당하는 PENDING 상태의 파일 목록 조회
        List<ComFileTempDomain> comFileTempDomainList = comFileTempMetaService.selectFileTempMeta(fileId);
        int allFileSize = comFileTempDomainList.size();

        // 전체 개수 검증
        if (!isCntValid(allFileSize, fileCnt)) {
            log.error("TEMP 파일 {} 개수 지정 초과: TEMP 저장 수 {}, 허용 개수 {}", fileId, allFileSize, fileCnt);

            /*PENDING 상태로 그대로 처리-알아서 삭제 하도록*/
            throw new BusinessException("파일 저장 개수가 잘못되었습니다.", ApiCode.VALIDATION_FAILED);
        }

        // 파일 목록 검증 및 이전 처리
        Iterator<ComFileTempDomain> iterator = comFileTempDomainList.iterator();
        while (iterator.hasNext()) {
            ComFileTempDomain comFileTempDomain = iterator.next();

            // 경로 검증
            if (!isPathValid(comFileTempDomain, filePath)) {
                log.warn("잘못된 파일 경로가 유효하지 않습니다. fileId: {}, fileSeq: {}, filePath: {}",comFileTempDomain.getFileId(), comFileTempDomain.getFileSeq(), filePath);

                comFileTempDomain.setInvalidReason("잘못된 파일 경로로 유효하지 않습니다.");
                updateFileStatusInvalid(comFileTempDomain);
                throw new BusinessException("유효하지 않는 파일 경로로 저장되려고 합니다. 잘못된 파일을 삭제 후 재진행 해주세요.", ApiCode.VALIDATION_FAILED,ComFileTempDomain.builder().fileSeq(comFileTempDomain.getFileSeq()).fileId(fileId).build());

            }
            // 사이즈 검증
            else if (!isSizeVaild(comFileTempDomain, fileSize)) {
                log.warn("파일 크기가 초과되었습니다. fileId: {}, fileSeq: {}, 제한 크기: {} bytes, 현재 크기: {} bytes",
                        comFileTempDomain.getFileId(), comFileTempDomain.getFileSeq(), fileSize, comFileTempDomain.getFileSize());

                comFileTempDomain.setInvalidReason("파일 크기가 초과되었습니다.");
                updateFileStatusInvalid(comFileTempDomain);
                throw new BusinessException("첨부파일의 크기가 잘못되었습니다.", ApiCode.VALIDATION_FAILED,ComFileTempDomain.builder().fileSeq(comFileTempDomain.getFileSeq()).fileId(fileId).build());

            }
            // 확장자 검증
            else if (!isExtValid(comFileTempDomain, comFileType)) {
                log.warn("파일 확장자가 유효하지 않습니다. fileId: {}, fileSeq: {}, fileName: {}",
                        comFileTempDomain.getFileId(), comFileTempDomain.getFileSeq(), comFileTempDomain.getFileUpldNm());

                comFileTempDomain.setInvalidReason("파일 확장자가 유효하지 않습니다.");
                updateFileStatusInvalid(comFileTempDomain);

                throw new BusinessException("첨부파일 확장자가 잘못되었습니다.", ApiCode.VALIDATION_FAILED,ComFileTempDomain.builder().fileSeq(comFileTempDomain.getFileSeq()).fileId(fileId).build());
            }

            // 파일 상태 업데이트
            comFileTempDomain.setFileStatus("VALID");
            comFileTempMetaService.updateFileTempStatus(comFileTempDomain);

            // 파일 메타 정보 본 테이블로 이전
            comFileMetaService.insertFileMeta(comFileTempDomain);

        }

        log.info("파일업로드 종료 : TEMP 파일 {} 개수 {} 중 {} 개 업로드 완료", fileId, allFileSize, comFileTempDomainList.size());
    }

    @Override
    public void deleteFile(ComFileTempDomain comFileTempDomain) {
        comFileMetaService.deleteFileMeta(comFileTempDomain.getFileId(),comFileTempDomain.getFileSeq());
    }

    @Override
    public void deleteExpired(ComFileTempDomain comFileTempDomain) {
        //   TODO 논리 삭제된 파일 데이터 delete 처리 및 실제 파일 삭제 처리

    }

    @Override
    public List<ComFileDomain> selectFileMetaList(String fileId) {
        return comFileMetaService.selectFileMetaList(fileId);
    }

    @Override
    public ComFileDomain selectFileMeta(String fileId, int fileSeq) {
        return comFileMetaService.selectFileMeta(fileId, fileSeq);
    }


    //   용량 검증
    private boolean isSizeVaild(ComFileTempDomain comFileTempDomain,Long maxSizeMB){
        long fileSizeMB = comFileTempDomain.getFileSize() / (1024 * 1024);  // MB 단위로 변환
        return fileSizeMB <= maxSizeMB;

    }

    //   파일 개수 검증
    private boolean isCntValid(int comFileTempDomainSize, int fileCnt){
        return comFileTempDomainSize <= fileCnt;
    }

    //   파일 저장 경로 검증
    private boolean isPathValid(ComFileTempDomain comFileTempDomain, String filePath){
        return filePath.equals(comFileTempDomain.getFilePath());
//        if(!filePath.equals(comFileTempDomain.getFilePath())){
//            return false;
//        }else{
//            return true;
//        }
    }

    //   파일 확장자 검증(파일타입)
    private boolean isExtValid(ComFileTempDomain comFileTempDomain, ComFileType comFileType){
        String fileSuffix = getFileSuffix(comFileTempDomain.getFileUpldNm());

        if (fileSuffix.isEmpty()) {
            log.warn("파일 명이 잘못되었거나 확장자가 없습니다. fileId: {}, fileSeq: {}", comFileTempDomain.getFileId(), comFileTempDomain.getFileSeq());
            return false;
        }

        ComFileType fileType = ComFileType.getFileTypeByExtension(fileSuffix);

        if (fileType == null) {
            log.warn("지원되지 않는 파일 확장자입니다. fileId: {}, fileSeq: {}, 확장자: {}", comFileTempDomain.getFileId(), comFileTempDomain.getFileSeq(), fileSuffix);
            return false;
        }

        return fileType.equals(comFileType);
    }

    /**
     * 파일 확장자 반환 메서드
     * @param fileNm 파일명
     * @return 확장자 (소문자로 변환), 없으면 빈 문자열 반환
     */
    private String getFileSuffix(String fileNm) {
        if (Objects.isNull(fileNm) || fileNm.isBlank() || !fileNm.contains(".")) {
            return "";
        }
        return fileNm.substring(fileNm.lastIndexOf(".") + 1).toLowerCase();
    }


    /*invalid update는 commit 처리*/
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void updateFileStatusInvalid(ComFileTempDomain comFileTempDomain) {
        comFileTempDomain.setFileStatus("INVALID");
        comFileTempMetaService.updateFileTempStatus(comFileTempDomain);
    }


}
