package com.service.eventus.mappers;

import com.service.eventus.member.MemberVo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface MemberDao {

    // 회원가입
    @Insert("insert into user (user_id, user_pw, user_name, user_phone, user_email, user_birth, user_gender) " +
            "values (#{user_id}, #{user_pw}, #{user_name}, #{user_phone}, #{user_email}, #{user_birth}, #{user_gender})")
    int insertUser(MemberVo memberVo);

    @Select("select * from user where user_id = #{user_id} and user_pw = #{user_pw}")
    MemberVo viewMember(MemberVo memberVo);  // 로그인할 때 회원정보 가져올 때

    @Select("select count(*) from user where user_id=#{user_id}")
    int idChk(String user_id); // 아이디 중복 체크(회원가입)

    @Select("select user_id from user where user_name = #{user_name} and user_phone = #{user_phone}")
    String findId(MemberVo memberVo); // 아이디 찾기(user_id 반환)

    @Select("select user_id from user where user_id = #{user_id} and user_phone = #{user_phone}")
    String findIdForPw(MemberVo memberVo); // 비밀번호 변경을 위한 아이디 찾기(user_id 반환)

    @Select("select count(*) from user where user_id = #{user_id} and user_pw = #{user_pw}")
    int loginCheck(MemberVo memberVo); // 로그인할 때 회원확인(세션등록)

    @Update("update user set user_pw = #{user_pw} where user_id = #{user_id}")
    int updatePw(MemberVo memberVo); // 비밀번호 변경

    @Select("select count(*) from user where user_id = #{user_id} and user_phone = #{user_phone}")
    int findPwCheck(MemberVo memberVo); // 비밀번호 찾기 전에 등록된 유저인지 아이디와 전화번호로 확인

}