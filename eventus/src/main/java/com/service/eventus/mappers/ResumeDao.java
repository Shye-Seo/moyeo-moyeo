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
	
	@Select("select id from staff_resume where staff_id = #{staff_id} and staff_resume_flag = 1")
	int selectResumeId(int staff_id); // 이력서 아이디 조회
	
	@Select("select file_name from staff_file where resume_id = #{resume_id}")
	String selectProfile(int resume_id); //프로필 이미지 조회
	
	@Insert("insert into staff_resume (staff_id, staff_email, staff_address, staff_school, staff_major, staff_school_start, staff_school_end, staff_school_state, staff_career_eventName, staff_career_businessName, staff_career_position, staff_career_workday) "
			+ "value (#{staff_id}, #{staff_email}, #{staff_address}, #{staff_school}, #{staff_major}, #{staff_school_start}, #{staff_school_end}, #{staff_school_state}, #{staff_career_eventName}, #{staff_career_businessName}, #{staff_career_position}, #{staff_career_workday})")
	boolean insertResume(ResumeVo resumeVo); //이력서 등록
	
	@Insert("insert into staff_file (staff_id, resume_id, file_name) value (#{staff_id},#{resume_id}, #{file_name})")
	boolean insertProfile(int staff_id, int resume_id, String file_name); //이력서 프로필 등록
	
	@Update("update staff_resume set staff_resume_flag = 0 where staff_id =#{staff_id}")
	boolean disabledPreResume(int staff_id); //이전 이력서 비활성화
	
	@Select("select * from user where id = #{staff_id}")
	MemberVo getStaffInfo(int staff_id); // 지원자 이력서 조회 모달(이름, 나이, 성별, 생년월일, 전화번호)
	
	@Select("select * from staff_resume where id = #{resume_id}")
	ResumeVo getStaffResume(int resume_id); // 지원자 이력서 조회 모달(이메일, 주소, 학력, 경력)
	
	@Select("select staff_id from staff_resume where id = #{resume_id}")
	int selectStaffId(int resume_id);
}
