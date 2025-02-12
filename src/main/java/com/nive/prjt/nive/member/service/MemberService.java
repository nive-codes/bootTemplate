package com.nive.prjt.nive.member.service;


import com.nive.prjt.nive.member.domain.MemberDomain;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;

/**
 * @author nive
 * @class MemberServiceImpl
 * @desc 회원 데이터 + 파일 업로드 예제 service(비즈니스 구현) interface
 * @since 2025-01-21
 */
public interface MemberService {

    boolean existMember(String nm);

    String insertMember(MemberDomain member);

    void updateMember(MemberDomain member);

    void deleteMember(String memberId);

    MemberDomain findMember(String memberId);

    List<MemberDomain> findSearchMembers(String keyword,String condition);

}
