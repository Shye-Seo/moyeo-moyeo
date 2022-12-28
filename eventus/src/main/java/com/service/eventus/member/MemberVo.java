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
    private String user_birthday;
    private String user_gender;
    private String user_phone;
	private String authority;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getEvent_id() {
		return event_id;
	}
	public void setEvent_id(int event_id) {
		this.event_id = event_id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getUser_pw() {
		return user_pw;
	}
	public void setUser_pw(String user_pw) {
		this.user_pw = user_pw;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getUser_email() {
		return user_email;
	}
	public void setUser_email(String user_email) {
		this.user_email = user_email;
	}
	public String getUser_mail_id() {
		return user_mail_id;
	}
	public void setUser_mail_id(String user_mail_id) {
		this.user_mail_id = user_mail_id;
	}
	public String getUser_mail_domain() {
		return user_mail_domain;
	}
	public void setUser_mail_domain(String user_mail_domain) {
		this.user_mail_domain = user_mail_domain;
	}
	public String getUser_birthday() {
		return user_birthday;
	}
	public void setUser_birthday(String user_birthday) {
		this.user_birthday = user_birthday;
	}
	public String getUser_gender() {
		return user_gender;
	}
	public void setUser_gender(String user_gender) {
		this.user_gender = user_gender;
	}
	public String getUser_phone() {
		return user_phone;
	}
	public void setUser_phone(String user_phone) {
		this.user_phone = user_phone;
	}
	public String getAuthority() {
		return authority;
	}
	public void setAuthority(String authority) {
		this.authority = authority;
	}
	
}