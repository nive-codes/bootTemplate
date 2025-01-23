package com.nive.prjt.com.file.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author nive
 * @class ComFileDomain
 * @desc 파일 업로드 domain 객체
 * @since 2025-01-16
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ComFileDomain {

    private String fileId;                      //파일ID
    private String fileParentId;                //부모파일ID
    private int fileSeq;                        //파일 시퀀스
    private String fileUpldNm;                  //업로드된 파일 명
    private String fileOrignNm;                 //원본 파일 명
    private String filePath;                    //파일 경로(fileModule과 동일)
    private String fileModule;                  //파일업로드 요청한 모듈(ex memberTb)
    private long fileSize;                      //파일 크기
    private int fileOrd;                        //파일 순서(db에서 자동으로 update)
    private String delYn;                       //논리 삭제 여부
    private String thumYn;                      //썸네일 여부
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date crtDt;                         //생성일시
    private String crtId;                       //생성자ID
    private String crtIpAddr;                   //생성자IP
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updDt;                         //수정일시
    private String updId;                       //수정자ID
    private String updIpAddr;                   //수정자IP
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date delDt;                         //삭제일시
    private String delId;                       //삭제자ID
    private String delIpAddr;                   //삭제자IP
}
