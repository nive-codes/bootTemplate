package com.nive.prjt.nive.member.service;

import com.nive.prjt.com.file.service.ComFileType;
import com.nive.prjt.com.idgen.ComTableIdGnrService;
import com.nive.prjt.config.exception.business.BusinessException;
import com.nive.prjt.nive.member.domain.MemberDomain;
import com.nive.prjt.nive.member.mapper.MemberMapper;
import lombok.AllArgsConstructor;
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
public class MemberServiceImpl implements MemberService {

    private final MemberMapper memberMapper;
    private final ComTableIdGnrService memberIdGenService;


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

//        List<MultipartFile> files1 = request.getFiles("file1");
//
//        List<MultipartFile> files2 = request.getFiles("file2");
//        화면단에서 name file로 하면 여기에 모두 들어온다...
//        List<MultipartFile> files = request.getFiles("file");

        return null;


//        member.setFileId(fileId);
//        String memberId = memberIdGenService.getNextId();
//        member.setMemberId(memberId);
//        memberMapper.insertMember(member);
//        return memberId;

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
