package com.nive.prjt.com.file.service.impl;

import com.nive.prjt.com.file.domain.ComFileDomain;
import com.nive.prjt.com.file.mapper.ComFileMetaMapper;
import com.nive.prjt.com.file.service.ComFileMetaService;
import com.nive.prjt.com.idgen.ComTableIdGnrService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author nive
 * @class ComFileMetaServiceImpl
 * @desc 파일업로드 공통 서비스 - 파일 정보(meta)를 DB에서 관리하는 service의 구현체
 * @since 2025-01-16
 */
@Service
@AllArgsConstructor
@Transactional
public class ComFileMetaServiceImpl implements ComFileMetaService {

    private final ComFileMetaMapper comFileMetaMapper;
    private final ComTableIdGnrService fileIdGenService;


    @Override
    public String insertFileMeta(ComFileDomain comFileDomain) {
        String id = fileIdGenService.getNextId();
        comFileDomain.setFileId(id);
        comFileMetaMapper.insertFileMeta(comFileDomain);
        return id;
    }

    @Override
    public void updateFileMeta(ComFileDomain comFileDomain) {
//        int fileSeq = comFileMetaMapper.selectMaxFileSeq(comFileDomain.getFileId());
//        comFileDomain.setFileSeq(fileSeq);
        /*mybatis에서 update 처리*/
        comFileMetaMapper.updateFileMeta(comFileDomain);

    }

    @Override
    public ComFileDomain selectLatestFileMeta(String fileId) {
        return comFileMetaMapper.selectLatestFileMeta(fileId);
    }

    @Override
    public ComFileDomain selectFileMeta(String fileId, int fileSeq) {
        return comFileMetaMapper.selectFileMeta(fileId,fileSeq);
    }

    @Override
    public String selectLatestFileMetaPath(String fileId) {
        return comFileMetaMapper.selectLatestFileMetaPath(fileId);
    }

    @Override
    public String selectFileMetaPath(String fileId, int fileSeq) {
        return comFileMetaMapper.selectFileMetaPath(fileId,fileSeq);
    }

    @Override
    public List<ComFileDomain> selectFileMetaList(String fileId) {
        return comFileMetaMapper.selectFileMetaList(fileId);
    }

    @Override
    public int selectFileMetaListCount(String fileId) {
        return comFileMetaMapper.selectFileMetaListCount(fileId);
    }

    @Override
    public void deleteFileMetaList(String fileId) {
        comFileMetaMapper.deleteFileMetaList(fileId);
    }

    @Override
    public void deleteFileMeta(String fileId, int fileSeq) {
        comFileMetaMapper.deleteFileMeta(fileId,fileSeq);
    }
}
