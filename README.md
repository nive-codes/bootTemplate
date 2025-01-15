# bootTemplate
SpringBoot 4.3.1 + Gradle + Mybatis Web Application Template

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