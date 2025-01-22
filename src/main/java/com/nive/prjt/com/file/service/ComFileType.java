package com.nive.prjt.com.file.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author nive
 * @class FileLocalUploadService
 * @desc Upload 가능한 파일의 확장자 체크 타입
 * @ComFileService.checkValidation에서 검증
 * @since 2025-01-17
 */
public enum ComFileType {
    IMAGE(Set.of("jpg", "jpeg", "png", "gif", "bmp")),
    DOCUMENT(Set.of("pdf", "doc", "docx", "txt", "hwp", "hwpx", "xls", "xlsx")),
    VIDEO(Set.of("mp4", "avi", "mkv")),
    AUDIO(Set.of("mp3", "wav", "aac"));

    /*list보다 속도가 더 빠른 Set. 동일하게 Collcations 인터페이스를 상속받지만 저장 방식 및 동작 상이*/
    /*중복 방지*/
    /*일정한 처리 시간*/
    private final Set<String> allowedExtensions;
    private static final Map<String, ComFileType> extensionMap = new HashMap<>();

    static {
        for (ComFileType fileType : values()) {
            for (String ext : fileType.allowedExtensions) {
                extensionMap.put(ext.toLowerCase(), fileType);
            }
        }
    }

    ComFileType(Set<String> allowedExtensions) {
        this.allowedExtensions = allowedExtensions;
    }

    public Set<String> getAllowedExtensions() {
        return allowedExtensions;
    }

    public static ComFileType getFileTypeByExtension(String extension) {
        if (extension == null || extension.isBlank()) {
            return null; // 확장자가 없을 경우 null 반환
        }
        return extensionMap.get(extension.toLowerCase());
    }

    public static void addCustomFileType(String extension, ComFileType fileType) {
        if (extension != null && !extension.isBlank()) {
            extensionMap.put(extension.toLowerCase(), fileType);
        }
    }
}