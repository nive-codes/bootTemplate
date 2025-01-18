# bootTemplate
SpringBoot 4.3.1 + Gradle + Mybatis Web Application Template

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