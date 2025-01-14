package com.nive.prjt.nive.myBatisTest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nive.prjt.nive.myBatisTest.domain.TestDomain;
import com.nive.prjt.nive.myBatisTest.service.TestRestService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(TestRestController.class)
class TestRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TestRestService testRestService;  // MockBean으로 TestRestService를 주입


    @Test
    void testValidationFailure() throws Exception {
        // 잘못된 TestDomain 생성
        TestDomain invalidTestDomain = new TestDomain();
        invalidTestDomain.setNm(""); // 이름을 공백으로 설정 (유효하지 않은 값)

        mockMvc.perform(post("/testApi") // MockMvcRequestBuilders로 명시적 선언
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(invalidTestDomain))) // JSON 본문 추가
                .andExpect(status().isBadRequest()) // HTTP 400 상태 코드 예상
                .andExpect(content().string(containsString("Validation failed"))) // 오류 메시지 예상
                .andExpect(content().string(containsString("이름은 공백일 수 없습니다."))) // 상세 메시지 확인
                .andReturn();
    }

}