package com.nive.prjt.com.file.mapper;

import com.nive.prjt.com.file.domain.ComFileDomain;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ComFileMetaMapper {

    @Select("SELECT COALESCE(MAX(file_seq), 0) FROM com_file WHERE file_id = #{fileId}")
    int selectMaxFileSeq(String fileId);  // 최대 fileSeq 조회

    void insertFileMeta(ComFileDomain comFileDomain);

    void updateFileMeta(ComFileDomain comFileDomain);

    ComFileDomain selectLatestFileMeta(String fileId);


    ComFileDomain selectFileMeta(String fileId,int fileSeq);

    String selectLatestFileMetaPath(String fileId);

    String selectFileMetaPath(String fileId, int fileSeq);

    List<ComFileDomain> selectFileMetaList(String fileId);
    int selectFileMetaListCount(String fileId);

    @Update("UPDATE COM_FILE SET DEL_YN = 'Y' WHERE file_id = #{fileId}")
    void deleteFileMetaList(String fileId);

    @Update("UPDATE COM_FILE SET DEL_YN = 'Y' WHERE file_id = #{fileId} and file_seq = #{fileSeq}")
    void deleteFileMeta(String fileId, int fileSeq);



}
