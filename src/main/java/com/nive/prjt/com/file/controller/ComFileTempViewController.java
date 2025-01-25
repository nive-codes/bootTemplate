package com.nive.prjt.com.file.controller;

import com.nive.prjt.com.file.domain.ComFileTempDomain;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author hosikchoi
 * @class ComFileTempTestController
 * @desc temp 파일 업로드 비동기 테스트 뷰(dropzone)
 * @since 2025-01-23
 */

@Controller
@RequestMapping("/view/tempFiles")
public class ComFileTempViewController {


    @GetMapping("/dropZoneTest")
    public String dropZoneTest(Model model) {
        ComFileTempDomain comFileTempDomain = ComFileTempDomain.builder().fileType("IMAGE").fileSize(10L).filePath("tempTest").build();
        comFileTempDomain.setFileType("IMAGE");
        comFileTempDomain.setFileSize(10L);
        comFileTempDomain.setFilePath("tempTest");


        model.addAttribute("fileUploadInfo", comFileTempDomain);
        return "tempFiles/dropZoneTest";
    }
}
