package com.service.eventus.member;

import com.service.eventus.event.WorkRecordVo;

import lombok.Data;

@Data
public class MemberVo {
    private int id;
	private int event_id;
    private String user_id;
    private String user_pw;
    private String user_name;
    private String user_email;
    private String user_mail_id;
    private String user_mail_domain;
    private String user_birth;
    private String user_gender;
    private String user_phone;
	private int user_authority;
    private String user_date_join;
	
    private String staff_address; //지원자 주소
    private int career_count; //경력 count
    private String staff_age; //만 나이 set
    private String staff_phone; //휴대폰번호 형태 set
    private String staff_position; //지원자 포지션
    private int staff_result; //지원자 합불합 임시
    private int resume_id; //이력서번호
    private String resume_profile; //이력서 사진
    
    private WorkRecordVo recordVo; //당일 근무기록 불러오기위함
}