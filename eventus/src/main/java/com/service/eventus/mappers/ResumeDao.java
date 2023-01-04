package com.service.eventus.mappers;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.service.eventus.member.MemberVo;

@Mapper
@Repository
public interface ResumeDao {
	
	@Select("select id user_name, user_gender, user_birth, user_phone, user_email from user where user_id = #{user_id}")
	MemberVo viewMember_forResume(String user_id); // 이력서 페이지를 위한 유저정보조회
}
