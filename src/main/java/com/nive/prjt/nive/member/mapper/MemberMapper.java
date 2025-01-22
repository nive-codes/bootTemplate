package com.nive.prjt.nive.member.mapper;

import com.nive.prjt.nive.member.domain.MemberDomain;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MemberMapper {

    /**
     * 회원정보 등록
     * @param memberDomain
     */
    void insertMember(MemberDomain memberDomain);

    /**
     * 회원정보 수정
     * @param memberId
     */
    void updateMember(MemberDomain memberId);

    /**
     * 회원정보 삭제
     * @param memberId
     */
    void deleteMember(String memberId);

    /**
     * 회원 정보 단일 조회
     * @param memberId
     * @return
     */
    MemberDomain findMember(String memberId);

    /**
     * 회원 정보 전체 조회
     * @return List<MemberDomain>
     */
    List<MemberDomain> findAllMembers();

    /**
     * 회원 정보 전체 조회 - 검색 조건(동적 쿼리)
     * @param keyword
     * @param condition
     * @return List<MemberDomain>
     */
    List<MemberDomain> findSearchMembers(String keyword,String condition);

    /**
     * 회원이 존재하는지 체크
     * @param nm
     * @return boolean
     */
    @Select("SELECT COUNT(1) > 0 FROM member_tb WHERE nm = #{nm} AND del_yn = 'N'")
    boolean existMember(String nm);

}
