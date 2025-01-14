package com.nive.prjt.nive.myBatisTest.domain;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.*;

@Schema(description = "테스트 객체")
@Getter
@Setter
@AllArgsConstructor   // 매개변수 생성자 추가
@NoArgsConstructor    // 기본 생성자 추가
public class TestDomain {

//    @NotNull(message = "ID는 필수입니다.")      /*insert service 레이어에서 논리적 오류가 발생하므로 주석*/
    private String tbIdx;

    @NotBlank(message = "이름은 공백일 수 없습니다.")
    //@Size(max = 10, message = "이름은 최대 10글자를 초과할 수 없습니다.")
    private String nm;

//    예시
//    @Email(message = "유효한 이메일 형식이어야 합니다.")
//    @NotBlank(message = "이메일은 필수입니다.")
//    private String email;
//
//    @Pattern(regexp = "^(01[0|1|6|7|8|9])-(\\d{3,4})-(\\d{4})$",
//            message = "전화번호는 010-1234-5678 형식이어야 합니다.")
//    private String phoneNumber;

    private String testSearch;


}
