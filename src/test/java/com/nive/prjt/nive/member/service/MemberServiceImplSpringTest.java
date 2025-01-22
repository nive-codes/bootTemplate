package com.nive.prjt.nive.member.service;

import org.springframework.boot.test.context.SpringBootTest;

import com.nive.prjt.config.exception.business.BusinessException;
import com.nive.prjt.nive.member.domain.MemberDomain;
import com.nive.prjt.nive.member.mapper.MemberMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
class MemberServiceImplSpringTest {

    @Autowired
    private MemberServiceImpl memberService;

    @Autowired
    private MemberMapper memberMapper;

    private MemberDomain member;

    @BeforeEach
    void setUp() {
        member = new MemberDomain();
        member.setMemberId("123");
//        member.setFileId("fileId_1");
        member.setNm("Test Member");

        // DB에 미리 테스트용 데이터 삽입
        memberMapper.insertMember(member);
    }

    @Test
    @DisplayName("회원 존재 여부 확인 - 회원이 존재하면 true 반환")
    void testExistMemberWhenExists() {
        String name = "Test Member";

        // DB에 이미 삽입된 상태에서 직접 조회하는 방식
        boolean result = memberService.existMember(name);

        assertTrue(result);
    }

    @Test
    @DisplayName("회원 추가 시 파일 업로드 및 회원 ID 생성")
    void testInsertMember() {
        MultipartFile file = new MockMultipartFile("file", "test.jpg", "application/image", "sample content".getBytes());


        // 실제 DB에 insert되고, ID 생성이 제대로 이루어지는지 확인
        String memberId = memberService.insertMember(member, file);
        MemberDomain memberDomain = memberService.findMember(memberId);
        assertEquals(memberDomain.getMemberId(), memberId);
    }

    @Test
    @DisplayName("회원 수정 시 회원ID가 없으면 예외 발생")
    void testUpdateMemberWhenMemberIdIsNull() {
        member.setMemberId(null);  // 회원ID 비어있음
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "sample content".getBytes());

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () ->
                memberService.updateMember(member, file)
        );
        assertEquals("회원ID가 없으므로 목록에서 다시 선택 후, 수정해주세요.", exception.getMessage());
    }

    @Test
    @DisplayName("회원 삭제 시 회원ID가 없으면 예외 발생")
    void testDeleteMemberWhenMemberIdIsNull() {
        String memberId = null;

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () ->
                memberService.deleteMember(memberId)
        );
        assertEquals("회원ID가 없으므로 목록에서 다시 선택 후, 수정해주세요.", exception.getMessage());
    }

    @Test
    @DisplayName("회원 검색 시 정상 동작")
    void testFindSearchMembers() {

        List<MemberDomain> result1 = memberService.findSearchMembers("", "");    //select 1
        List<MemberDomain> result2 = memberService.findSearchMembers("Test", "name");    //select 1
        List<MemberDomain> result3 = memberService.findSearchMembers("asdf", "name");    //select 0

        // 결과 확인
        assertEquals(result1.size(), result2.size());   //동일한 결과 값을 가지고 있어야한다.
        assertNotEquals(result2.size(),result3.size()); //result3의 사이즈는 0이여야한다.
        assertEquals(result3.size(),0);
    }

    @Test
    @DisplayName("회원 조회 시 회원ID가 비어있으면 예외 발생")
    void testFindMemberWhenMemberIdIsNull() {
        String memberId = null;

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () ->
                memberService.findMember(memberId)
        );
        assertEquals("회원정보를 찾을 수 없습니다.", exception.getMessage());
    }
}