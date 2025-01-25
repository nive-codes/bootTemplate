package com.nive.prjt.nive.member.service;

import com.nive.prjt.com.idgen.ComTableIdGnrService;
import com.nive.prjt.config.exception.business.BusinessException;
import com.nive.prjt.nive.member.domain.MemberDomain;
import com.nive.prjt.nive.member.mapper.MemberMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockMultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceImplMockingTest {

    @Mock
    private MemberMapper memberMapper;


    @Mock
    private ComTableIdGnrService memberIdGenService;

    @InjectMocks
    private MemberServiceImpl memberService;

    private MemberDomain member;

    @BeforeEach
    void setUp() {
        member = new MemberDomain();
        member.setMemberId("123");
        member.setFileId("fileId_1");
        member.setNm("Test Member");
    }


    @Test
    @DisplayName("회원 존재 여부 확인 - 회원이 존재하면 true 반환")
    void testExistMemberWhenExists() {
        // Arrange
        String name = "Test Member";
        when(memberMapper.existMember(name)).thenReturn(true);

        // Act
        boolean result = memberService.existMember(name);

        // Assert
        assertTrue(result);
    }

    @Test
    @DisplayName("회원 존재 여부 확인 - 회원이 존재하지 않으면 false 반환")
    void testExistMemberWhenNotExists() {
        // Arrange
        String name = "Non Existent Member";
        when(memberMapper.existMember(name)).thenReturn(false);

        // Act
        boolean result = memberService.existMember(name);

        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("회원 추가 시 파일 업로드 및 회원 ID 생성")
    void testInsertMember() {
        // Arrange: DOCUMENT 타입의 파일로 변경

        MockMultipartHttpServletRequest request = new MockMultipartHttpServletRequest();

//        String memberId = memberService.insertMember(member, request);
        memberService.insertMember(member, request);
        // Assert
//        assertEquals("123", idx);
        verify(memberMapper, times(1)).insertMember(any(MemberDomain.class));
    }

    @Test
    @DisplayName("회원 수정 시 회원ID가 없으면 예외 발생")
    void testUpdateMemberWhenMemberIdIsNull() {
        // Arrange
        member.setMemberId(null);  // 회원ID 비어있음
//        2025.01.22 File에서 MultipartReuqest로 변경
//        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "sample content".getBytes());

        MockMultipartHttpServletRequest request = new MockMultipartHttpServletRequest();
        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () ->
                memberService.updateMember(member, request)
        );
        assertEquals("회원ID가 없으므로 목록에서 다시 선택 후, 수정해주세요.", exception.getMessage());
    }

    @Test
    @DisplayName("회원 삭제 시 회원ID가 없으면 예외 발생")
    void testDeleteMemberWhenMemberIdIsNull() {
        // Arrange
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
        // Arrange
        String keyword = "Test";
        String condition = "name";
        List<MemberDomain> expectedList = List.of(member);
        when(memberMapper.findSearchMembers(keyword, condition)).thenReturn(expectedList);

        // Act
        List<MemberDomain> result = memberService.findSearchMembers(keyword, condition);

        // Assert
        assertEquals(expectedList, result);
    }

    @Test
    @DisplayName("회원 조회 시 회원ID가 비어있으면 예외 발생")
    void testFindMemberWhenMemberIdIsNull() {
        // Arrange
        String memberId = null;

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () ->
                memberService.findMember(memberId)
        );
        assertEquals("회원정보를 찾을 수 없습니다.", exception.getMessage());
    }
}