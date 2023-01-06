package com.service.eventus.mappers;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.service.eventus.member.MemberVo;
import com.service.eventus.resume.ResumeVo;

@Mapper
@Repository
public interface ResumeDao {
	
	@Select("select id, user_name, user_gender, user_birth, user_phone, user_email from user where user_id = #{user_id}")
	MemberVo viewMember_forResume(String user_id); // 이력서 페이지를 위한 유저정보조회
	
	@Select("select * from staff_resume where staff_id = #{staff_id} and staff_resume_flag = 1")
	ResumeVo selectMyResume(int staff_id); // 이력서 조회
	
	@Insert("insert into staff_resume (staff_id, staff_email, staff_address, staff_school, staff_major, staff_school_start, staff_school_end, staff_school_state, staff_career_eventName, staff_career_businessName, staff_career_position, staff_career_workday) "
			+ "value (#{staff_id}, #{staff_email}, #{staff_address}, #{staff_school}, #{staff_major}, #{staff_school_start}, #{staff_school_end}, #{staff_school_state}, #{staff_career_eventName}, #{staff_career_businessName}, #{staff_career_position}, #{staff_career_workday})")
	boolean insertResume(ResumeVo resumeVo); //이력서 등록
	
	@Insert("insert into staff_file (staff_id, file_name) value (#{staff_id}, #{file_name})")
	boolean insertProfile(int staff_id ,String file_name); //이력서 프로필 등록
	
	@Update("update staff_resume set staff_resume_flag = 0 where staff_id =#{staff_id}")
	boolean disabledPreResume(int staff_id); //이전 이력서 비활성화
	
	
}
