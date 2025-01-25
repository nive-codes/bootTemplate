package com.nive.prjt.com.file.service.impl;

import com.nive.prjt.com.file.domain.ComFileTempDomain;
import com.nive.prjt.com.file.mapper.ComFileMetaMapper;
import com.nive.prjt.com.file.mapper.ComFileTempMetaMapper;
import com.nive.prjt.com.file.service.ComFileTempMetaService;
import com.nive.prjt.com.idgen.ComTableIdGnrService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author nive
 * @class ComFileTempMetaService
 * @desc 파일업로드 공통 서비스 - 파일 temp 테이블 데이터를 권리하는 구현체
 * @since 2025-01-23
 */
@Service
@AllArgsConstructor
public class ComFileTempMetaServiceImpl implements ComFileTempMetaService {

    private final ComFileTempMetaMapper comFileTempMetaMapper;
    private final ComTableIdGnrService fileIdGenService;


    @Override
    public String insertFileTempMeta(ComFileTempDomain comFileTempDomain) {
        String id = fileIdGenService.getNextId();
        comFileTempDomain.setFileId(id);
        comFileTempMetaMapper.insertFileTempMeta(comFileTempDomain);
        return id;
    }


    @Override
    public void updateFileTempMeta(ComFileTempDomain comFileTempDomain) {

        comFileTempMetaMapper.insertFileTempMeta(comFileTempDomain);

    }

    @Override
    public void updateFileTempStatus(ComFileTempDomain comFileTempDomain) {
        comFileTempMetaMapper.updateFileTempMeta(comFileTempDomain);
    }

    @Override
    public void deleteFileTempMeta(ComFileTempDomain comFileTempDomain) {
        comFileTempMetaMapper.deleteFileTempMeta(comFileTempDomain);
    }

    @Override
    public ComFileTempDomain selectFileTempMeta(String fileId, int fileSeq) {
        return comFileTempMetaMapper.selectFileTempMeta(fileId, fileSeq);

    }
}
