package com.service.eventus.mappers;

import com.service.eventus.member.MemberVo;

import java.util.Map;

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
    @Insert("insert into user (user_id, user_pw, user_name, user_phone, user_email, user_birth, user_gender, user_date_join, user_authority) " +
            "values (#{user_id}, #{user_pw}, #{user_name}, #{user_phone}, #{user_email}, #{user_birth}, #{user_gender}, #{user_date_join}, #{user_authority})")
    int insertUser(MemberVo memberVo);

    @Select("select * from user where user_id = #{user_id}")
    MemberVo viewMember(MemberVo memberVo);  // 로그인할 때 회원정보 가져올 때

    @Select("select count(*) from user where user_id=#{user_id}")
    int idChk(String user_id); // 아이디 중복 체크(회원가입)

    @Select("select user_id, user_date_join from user where user_name = #{user_name} and user_phone = #{user_phone}")
    Map findId(MemberVo memberVo); // 아이디 찾기(user_id 반환)

    @Select("select user_id from user where user_id = #{user_id} and user_phone = #{user_phone}")
    String findIdForPw(MemberVo memberVo); // 비밀번호 변경을 위한 아이디 찾기(user_id 반환)

    @Select("select count(*) from user where user_id = #{user_id} and user_pw = #{user_pw}")
    int loginCheck(MemberVo memberVo); // 로그인할 때 회원확인(세션등록)

    @Update("update user set user_pw = #{user_pw} where user_id = #{user_id}")
    int updatePw(MemberVo memberVo); // 비밀번호 변경
    
    @Select("SELECT user_pw FROM user where user_id = #{user_id}")
    String selectPw(MemberVo memberVo); //비밀번호 변경을 위한 비밀번호 조회
    
    @Select("select file_name from staff_file where staff_id =#{user_id} order by id desc limit 1")
    String select_myProfile (int user_id); //프로필 조회

    @Update("update user set user_phone = #{user_phone} where user_id = #{user_id}")
    void updatePhone(MemberVo memberVo); //휴대폰 번호 변경

}