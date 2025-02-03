function initDropzone(upldFileDiv, upldFileId, upldFileType, upldFileSize,fileCnt, upldFilePath) {

    // 파일 타입에 맞는 확장자 목록 설정(백엔드 Enum 참조)
    let fileExtValidate = "*";  // 기본값은 모든 파일 타입

// IMAGE일 때만 해당 확장자 배열을 사용
    if (upldFileType === 'IMAGE') {
        fileExtValidate = [".jpg", ".jpeg", ".png", ".gif", ".bmp", ".PNG"].join(",");
    } else if (upldFileType === 'DOCUMENT') {
        fileExtValidate = [".pdf", ".doc", ".docx", ".txt", ".hwp", ".hwpx", ".xls", ".xlsx"].join(",");
    } else if (upldFileType === 'VIDEO') {
        fileExtValidate = [".mp4", ".avi", ".mkv"].join(",");
    } else if (upldFileType === 'AUDIO') {
        fileExtValidate = [".mp3", ".wav", ".aac"].join(",");
    }
    console.log("fileExtValidate value : "+fileExtValidate)

    new Dropzone("#"+upldFileDiv, {
        url: "/api/tempFiles/upload",
        maxFilesize: upldFileSize,
        acceptedFiles: fileExtValidate,
        maxFiles: fileCnt,
        method: 'post',
        paramName: 'files',
        addRemoveLinks: true,
        dictRemoveFile: '삭제',
        /*지정한 파일 타입으로 temp에서 비교, 이후 모듈 별 ComFile로 이전 시 한번 더 데이터 비교 처리*/
        params: function() {
            return {
                fileType: upldFileType,
                fileSize: upldFileSize,
                filePath: upldFilePath,
                fileId: document.getElementById(upldFileId).value
            };
        },
        init: function() {
            this.on("success", function(file, response) {
                console.log("파일 업로드 성공:", response);
                if (response.code === 'SUCCESS') {
                    document.getElementById(upldFileId).value = response.data.fileId;
                    file.fileId = response.data.fileId;
                    file.fileSeq = response.data.fileSeq;
                    file.fileOrd = response.data.fileOrd;

                } else {
                    alert(response.message);
                }
            });

            this.on("error", function(file, errorMessage) {
                console.error("파일 업로드 실패:", errorMessage);
            });

            this.on("removedfile", function(file) {
                if (file.fileSeq) {
                    console.log("파일 삭제 시 fileSeq:", file.fileSeq);
                    fetch('/api/tempFiles/upload/' + file.fileId, {
                        method: 'DELETE',
                        headers: {
                            'Content-Type': 'application/json'
                        },
                        body: JSON.stringify({ fileSeq: file.fileSeq, fileId: document.getElementById(upldFileId).value })
                    }).then(response => {
                        if (!response.ok) {
                            throw new Error('파일 삭제 실패');
                        }
                        return response.json();
                    }).then(data => {
                        console.log("파일 삭제 완료:", data);
                    }).catch(error => {
                        console.error("파일 삭제 실패:", error);
                        alert("파일 삭제에 실패했습니다. 다시 시도해주세요.");
                    });
                }
            });
        }
    });
}