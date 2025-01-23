package com.nive.prjt.com.file.service.impl;

import com.nive.prjt.com.file.domain.ComFileTempDomain;
import com.nive.prjt.com.file.service.ComFileMetaService;
import com.nive.prjt.com.file.service.ComFileUploadService;
import com.nive.prjt.com.idgen.ComTableIdGnrService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.nive.prjt.com.file.service.ComFileRestService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author hosikchoi
 * @class ComFileRestService
 * @desc ComFileRestService를 통한 파일 temp 데이터 검증, 파일 검증, metaService 및 uploadService 핸들링
 * @since 2025-01-23
 */
@Service
@Transactional
@AllArgsConstructor
public class ComFileRestServiceImpl implements  ComFileRestService {


    private final ComFileMetaService comFileMetaService;
    private final ComFileUploadService comFileUploadService;


    @Override
    public String uploadFileTemp(MultipartFile[] files, ComFileTempDomain comFileTempDomain) {

        return "";
    }
}
