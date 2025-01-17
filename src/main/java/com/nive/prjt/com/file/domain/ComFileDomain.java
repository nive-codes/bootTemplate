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

    private String fileId;
    private int fileSeq;
    private String fileUpldNm;
    private String fileOrignNm;
    private String filePath;
    private String fileModule;
    private long fileSize;
    private int fileOrd;
    private String delYn;
    private String thumYn;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 날짜 포맷 설정
    private Date crtDt;
    private String crtId;
    private String crtIpAddr;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 날짜 포맷 설정
    private Date updDt;
    private String updId;
    private String updIpAddr;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 날짜 포맷 설정
    private Date delDt;
    private String delId;
    private String delIpAddr;
}
