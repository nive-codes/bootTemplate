package com.nive.prjt.nive.member.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.stereotype.Service;

/**
 * @author nive
 * @class MemberDomain
 * @desc 회원 데이터 + 파일 업로드 예제 Domain
 * @since 2025-01-21
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MemberDomain {
        private String memberId;

        @NotBlank(message = "이름을 입력해주세요.")
        @Size(max = 10, message = "이름은 최대 10자리를 초과할 수 없습니다.")
        private String nm;

        @NotBlank(message = "전화번호를 입력해주세요.")
        @Pattern(regexp = "^(01[0|1|6|7|8|9])-(\\d{3,4})-(\\d{4})$",
                message = "전화번호는 010-1234-5678 형식이어야 합니다.")
        private String tellNo;

        @Email(message = "유효한 이메일 형식이 아닙니다.")
        private String email;

        private String fileId;

        private String fileId2;

        private String delYn;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm") // 날짜 포맷 설정
        private String crtDt;

        private String crtId;

        private String crtIpAddr;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm") // 날짜 포맷 설정
        private String updDt;

        private String updId;

        private String upd_ip_addr;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm") // 날짜 포맷 설정
        private String delDt;

        private String delId;

        private String delIpAddr;

}
