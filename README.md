# bootTemplate
SpringBoot 4.3.1 + Gradle + Mybatis Web Application Template


## 2025.02.03
### 작업 예정
1. 수정 시 기존 파일 업로드 불러오기 및 적용(dropzone.js)

### 작업 내용
1. 회원 저장 시 COM_FILE_TEMP -> COM_FILE 검증 및 저장 로직 처리
2. interceptor 테스트 완료(운영 상황 적용 profile)
3. 테스트 모듈(회원)에서의 파일 모듈 th:insert 예제 추가
4. ComFileUploadInit.js를 통한 Dropzone 초기화 호출 작성
5. BusinessException Exception발생 시 객체 전달 처리 추가

## 2025.01.31
### 작업 예정
1. interceptor를 통한 접속자 구분 처리
2. 파일업로드 비동기 처리 후 fileId return -> memberService insert, update 메소드와 연계 처리
3. 파일 업로드 invalid(com_file 이전 실패 시) 

### 작업 내용
1. 파일 업로드 / 삭제 샘플 thymeleaf 완료
2. 파일 삭제 로직 테스트 완료



## 2025.01.25
### 작업 예정
1. interceptor를 통한 접속자 구분 처리
2. 파일업로드 비동기 처리 후 fileId return -> memberService insert, update 메소드와 연계 처리

### 작업 내용
1. 파일업로드 service -> RestController를 통한 비동기 처리
2. 파일 업로드 샘플 thymeleaf 생성중
4. 예외처리(@RestControllerAdvice) rest api handler ResponseBody 추가(json 내려주기)
-> 애노테이션 달지 않으면 view찾는 문제 발생 해결

## 2025.01.22
### 작업 예정
1. interceptor를 통한 접속자 구분 처리
2. 파일업로드 비동기 처리 후 fileId return -> memberService insert, update 메소드와 연계 처리

### 작업 내용
1. 파일업로드 관련 mvc 샘플 작성 및 QA
2. MemberService 테스트 케이스 작성
3. MemberService Spring 테스트 케이스 작성
4. MemberController thymeleaf 작업

## 2025.01.21
### 작업 예정
1. 파일업로드 관련 mvc 샘플 작성 및 QA
2. interceptor를 통한 접속자 구분 처리

### 작업 내용
1. ComFileService(meta + 파일업로드) 소스 수정(작업 해야되는 메소드)
2. 예외처리 오류수정(RestControllerAdvice -> @ControllerAdvice(annotions = RestController.class)
3. 예외처리 수정(BusinessException + BusinessRestException 동일하게 처리 + mvc 및 apiReponse return 하게 처리)
4. ApiCode에서의 custom 객체 추가


## 2025.01.20
### 작업 예정
1. ComFileService(meta + 파일업로드) 테스트
2. 파일업로드 관련 mvc 샘플 작성 및 QA
3. interceptor를 통한 접속자 구분 처리

### 작업 내용
1. FileUploadS3Service의 테스트 완료
2. FileUploadLocalService의 테스트 완료
3. FileUploadS3Service 통합 테스트 완료(실제 bucket 테스트)
3. 파일 업로드의 예외처리는 ComFileService에서 잡은 후 처리 필요
4. src/test/java/com/nive/prjt/com/file/service/impl 전체 테스트 검증 완료


## 2025.01.19
### 작업 예정
1. FileUploadService의 테스트
2. FileService 테스트
3. 파일 오류 발생 시 rest api 형태의 businessException 에러 처리
4. 파일 업로드 s3 수정 및 확인 필요(s3client vs s3clinetBuilder 차이 확인)
### 작업 내용
1. S3client Bean 생성용 Config.java 생성
2. S3Client Bean을 주입받는 S3관련 FileUploadService 구현(내부에서 Builder 삭제)
3. S3Client Bean을 주입받는 형태로 mock bean를 주입받아 테스트 케이스 생성할 수 있도록 수정(기존에 소스안에서 this.S3client~ builder를 통해 생성하니 mock 객체를 주입받지 못하는 문제 발생) 유연성 증가


## 2025.01.18
### 작업 예정
1. FileUploadService의 테스트
2. FileService 테스트
3. 파일 오류 발생 시 rest api 형태의 businessException 에러 처리
4. 파일 업로드 s3 수정 및 확인 필요(s3client vs s3clinetBuilder 차이 확인)
### 작업 내용
1. s3 관련 gradle 추가
2. s3 파일 업로드 작업



## 2025.01.17
### 작업 예정
1. FileUploadService의 테스트
2. FileService 테스트
3. 파일 오류 발생 시 rest api 형태의 businessException 에러 처리
### 작업 내용
1. FileuploadService의 uploadFile 통폐합(return String 통일)
2. FileService 의존성 주입 후 소스 작성

## 2025.01.16
### 작업 예정
1. FileUploadService의 구현체 작성 예정(s3, local)
2. FileService 통합 예정(meta + upload 서비스 통합 객체)
3. FileRestController 작성 예정(기타 모듈에서 insert, update되는 것 외 삭제, 순서 변경 등)
### 작업 내용
1. ID gen service 생성(Egov Id gen 참조)
2. FileConfig.java 작성(FileUploadService의 구현체를 profile로 나눠서 작성-s3/local)
3. YML 프로파일 적용(dev / prod)
4. FileService 설계(MetaService + UploadServer = FileService 형태로 구현 중)
5. 파일 테이블 설계

## 2025.01.15
### 작업 예정
1. test api 및 운영 api 구분 처리
2. 파일 업로드 처리(s3 / local)
### 작업 내용
1. ErrorResponse.java 및 SuccessResponse.java ApiResponse.java로 통일 적용
2. ErrorCode.java Enum을 ApiCode로 변경, 성공/실패 메세지 및 코드 통일 적용
3. swagger 적용 및 커스텀
4. CorsConfig.java 작성
5. 테스트케이스 수정 및 검증

## 2024.01.14
### 작업 예정
1. Response 변경
2. ResponseEntity는 Http status가 중요한 경우에만 사용, 나머지는 전부 ApiResponse.java를 return 처리

### 작업 내용
1. Response 타입 통일 : 실패/성공 시 통일된 응답 객체 생성 및 클라이언트(프론트엔드) 전달
2. ApiResponse.java 생성 및 ApiCode.java 생성
3. Swagger 적용