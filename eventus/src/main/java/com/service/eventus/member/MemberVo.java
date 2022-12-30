package com.service.eventus.member;

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
	private String authority;
	
}