package com.nive.prjt.com.file.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author nive
 * @class FileLocalUploadService
 * @desc Upload 가능한 파일의 확장자 체크 타입
 * @ComFileService.checkValidation에서 검증
 * @since 2025-01-17
 */
public enum ComFileType {
    IMAGE(new String[]{"jpg", "jpeg", "png", "gif", "bmp"}),
    DOCUMENT(new String[]{"pdf", "doc", "docx", "txt", "hwp", "hwpx", "xls", "xlsx"}),
    VIDEO(new String[]{"mp4", "avi", "mkv"}),
    AUDIO(new String[]{"mp3", "wav", "aac"}),
    CUSTOM(new String[]{}); // 동적 추가를 위한 CUSTOM 타입

    private final List<String> allowedExtensions;

    ComFileType(String[] allowedExtensions) {
        this.allowedExtensions = new ArrayList<>(Arrays.asList(allowedExtensions));
    }

    // 확장자 목록에 새로운 항목 추가
    public void addCustomExtension(String extension) {
        if (!allowedExtensions.contains(extension)) {
            allowedExtensions.add(extension);
        }
    }

    public List<String> getAllowedExtensions() {
        return allowedExtensions;
    }

    public static ComFileType getFileTypeByExtension(String extension) {
        for (ComFileType fileType : ComFileType.values()) {
            if (fileType.allowedExtensions.contains(extension)) {
                return fileType;
            }
        }
        return CUSTOM; // 기본적으로 CUSTOM으로 반환
    }
}
