package com.nive.prjt.com.file.service.impl;

import com.nive.prjt.com.file.domain.ComFileTempDomain;
import com.nive.prjt.com.file.service.*;
import com.nive.prjt.config.exception.business.BusinessException;
import com.nive.prjt.config.response.ApiCode;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * @author nive
 * @class ComFileRestService
 * @desc ComFileRestService를 통한 파일 temp 데이터 검증, 파일 검증, metaService 및 uploadService 핸들링
 * @since 2025-01-23
 */
@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class ComFileRestServiceImpl implements  ComFileRestService {

    private final ComFileMetaService comFileMetaService;
    private final ComFileUploadService comFileUploadService;


    @Override
    public void deleteFile(ComFileTempDomain comFileTempDomain) {

    }

    @Override
    public void deleteExpiredTemp(ComFileTempDomain comFileTempDomain) {

    }

    @Override
    public void deleteRealFile(ComFileTempDomain comFileTempDomain) {

    }


}
