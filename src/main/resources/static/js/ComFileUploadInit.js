function initDropzone(upldFileDiv, upldFileId, upldFileType, upldFileSize,fileCnt, upldFilePath,fileList) {

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
                /*서버로 넘길 추가 데이터 - ComFileTempDomainRequest.java*/
                fileType: upldFileType,
                fileSize: upldFileSize,
                filePath: upldFilePath,
                fileId: document.getElementById(upldFileId).value
            };
        },
        init: function() {
            console.log("fileList : " + fileList);


            const decodedFileList = decodeHtmlEntities(fileList);
            const parsedFileList = JSON.parse(decodedFileList);
            console.log("parsedFileList : " +parsedFileList);


            let self = this;  // self에 this(Dropzone 인스턴스) 저장

            // 기존 파일을 Dropzone에 추가(mockFile 처리)
            /*https://nicescript.tistory.com/17 참조*/
            // 기존 파일 추가
            if (Array.isArray(parsedFileList)) {
                parsedFileList.forEach(function(file) {
                    var mockFile = {
                        name: file.fileOrignNm,
                        size: file.fileSize,
                        fileId: file.fileId,
                        fileSeq: file.fileSeq,
                        fileOrd: file.fileOrd,
                        fileStat : 'COMP'
                        // url: '/api/files/upload/' + file.fileId + '/' + file.fileSeq // URL은 실제 파일 경로로 수정
                    };

                    self.emit("addedfile", mockFile);  // Dropzone에 파일 추가
                    self.emit("complete", mockFile);   // 파일 업로드 완료 처리

                    // 서버에서 파일 조회 후 미리보기 적용
                    if(upldFileType === 'IMAGE'){
                        console.log(mockFile.fileId, mockFile.fileSeq);
                        fetch('/api/files/thumb/dropzone/'+mockFile.fileId+'/'+mockFile.fileSeq)
                            .then(response => response.blob()) // 바이너리 데이터 변환
                            .then(blob => {
                                let reader = new FileReader();
                                reader.onload = function(event) {
                                    self.emit("thumbnail", mockFile, event.target.result); // 썸네일 적용
                                };
                                reader.readAsDataURL(blob);
                            })
                            .catch(error => console.error("파일 불러오기 실패:", error));
                    }

                });
            } else {
                console.error("parsedFileList 배열이 아닙니다.", parsedFileList);
            }

            this.on("success", function(file, response) {
                console.log("파일 업로드 성공:", response);
                if (response.code === 'SUCCESS') {
                    document.getElementById(upldFileId).value = response.data.fileId;
                    file.fileId = response.data.fileId;
                    file.fileSeq = response.data.fileSeq;
                    file.fileOrd = response.data.fileOrd;
                    file.fileStat = 'TEMP'
                    file.url = '/api/tempFiles/upload/'+file.fileId + '/' + file.fileSeq;   //temp 파일 다운로드 경로

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
                    var removeUrl = "";
                    if(file.fileStat === 'TEMP') removeUrl = '/api/tempFiles/upload/';
                    if(file.fileStat === 'COMP') removeUrl = '/api/files/upload/';
                    alert(removeUrl)
                    fetch(removeUrl + file.fileId, {
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


function decodeHtmlEntities(encodedString) {
    const parser = new DOMParser();
    const decodedString = parser.parseFromString(encodedString, "text/html").body.textContent;
    return decodedString;
}
