package com.nive.prjt.nive.member.service;

import com.nive.prjt.com.file.service.ComFileRestService;
import com.nive.prjt.com.file.service.ComFileType;
import com.nive.prjt.com.idgen.ComTableIdGnrService;
import com.nive.prjt.config.exception.business.BusinessException;
import com.nive.prjt.config.response.ApiCode;
import com.nive.prjt.nive.member.domain.MemberDomain;
import com.nive.prjt.nive.member.mapper.MemberMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;
import java.util.Objects;

/**
 * @author nive
 * @class MemberServiceImpl
 * @desc 회원 데이터 + 파일 업로드 예제 service(비즈니스 구현) interface
 * @since 2025-01-21
 */
@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class MemberServiceImpl implements MemberService {

    private final MemberMapper memberMapper;
    private final ComTableIdGnrService memberIdGenService;
    private final ComFileRestService comFileRestService;


    @Override
    public boolean existMember(String nm) {

        if(Objects.isNull(nm) || nm.isBlank()){
            /*Controller의 bindingResult를 통해 필수값 검증이 되어서 메서드 호출이 되어야 하지만*/
            /*무시하고 이 메서드가 실행되는 경우 예외처리*/
            throw new BusinessException("이름은 필수값이므로 입력해야 합니다.","/member/insertForm");
        }

        return memberMapper.existMember(nm);
    }

    @Override
    public String insertMember(MemberDomain member, MultipartHttpServletRequest request) {

        /*파일 검증 로직 처리*/
       comFileRestService.transferFile(member.getFileId(),ComFileType.IMAGE,10L,5,"member");
       comFileRestService.transferFile(member.getFileId2(),ComFileType.DOCUMENT,10L,5,"member");


        String memberId = memberIdGenService.getNextId();
        member.setMemberId(memberId);
        memberMapper.insertMember(member);
        return memberId;

    }

    @Override
    public void updateMember(MemberDomain member, MultipartHttpServletRequest request) {

        if(Objects.isNull(member.getMemberId()) || member.getMemberId().isBlank()){
            throw new BusinessException("회원ID가 없으므로 목록에서 다시 선택 후, 수정해주세요.","/member/updateForm");
        }
        memberMapper.updateMember(member);
    }


    @Override
    public void deleteMember(String memberId) {

        if(Objects.isNull(memberId) || memberId.isBlank()){
            throw new BusinessException("회원ID가 없으므로 목록에서 다시 선택 후, 수정해주세요.","/member/updateForm");
        }

        memberMapper.deleteMember(memberId);
    }

    @Override
    public MemberDomain findMember(String memberId) {
        if(Objects.isNull(memberId) || memberId.isBlank()){
            throw new BusinessException("회원정보를 찾을 수 없습니다.","/member/selectForm");
        }

        return memberMapper.findMember(memberId);
    }

    @Override
    public List<MemberDomain> findSearchMembers(String keyword, String condition) {
        return memberMapper.findSearchMembers(keyword,condition);
    }


}
