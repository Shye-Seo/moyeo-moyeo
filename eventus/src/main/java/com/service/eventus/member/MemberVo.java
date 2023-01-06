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
    private String result; //지원자 합격여부
    
    private WorkRecordVo recordVo; //당일 근무기록 불러오기위함
}