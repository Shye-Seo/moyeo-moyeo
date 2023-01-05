package com.service.eventus.mappers;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.service.eventus.member.MemberVo;
import com.service.eventus.resume.ResumeVo;

@Mapper
@Repository
public interface ResumeDao {
	
	@Select("select id, user_name, user_gender, user_birth, user_phone, user_email from user where user_id = #{user_id}")
	MemberVo viewMember_forResume(String user_id); // 이력서 페이지를 위한 유저정보조회
	
	@Insert("insert into staff_resume (staff_id, staff_email, staff_address, staff_school, staff_major, staff_school_start, staff_school_end, staff_school_state, staff_career_eventName, staff_career_businessName, staff_career_position, staff_career_workday) "
			+ "value (#{staff_id}, #{staff_email}, #{staff_address}, #{staff_school}, #{staff_major}, #{staff_school_start}, #{staff_school_end}, #{staff_school_state}, #{staff_career_eventName}, #{staff_career_businessName}, #{staff_career_position}, #{staff_career_workday})")
	boolean insertResume(ResumeVo resumeVo);
	
	@Insert("insert into staff_file (staff_id, file_name) value (#{staff_id}, #{file_name})")
	boolean insertProfile(int staff_id ,String file_name);
}
