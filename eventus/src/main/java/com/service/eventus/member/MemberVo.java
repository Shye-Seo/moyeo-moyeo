package com.service.eventus.member;

import lombok.Data;

@Data
public class MemberVo {
    private int id;
    private String staff_id;
    private String staff_pw;
    private String staff_name;
    private String staff_email;
    private String staff_mail_id;
    private String staff_mail_domain;
    private String staff_birth;
    private String staff_gender;
    private String staff_phone;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getStaff_id() {
		return staff_id;
	}
	public void setStaff_id(String staff_id) {
		this.staff_id = staff_id;
	}
	public String getStaff_pw() {
		return staff_pw;
	}
	public void setStaff_pw(String staff_pw) {
		this.staff_pw = staff_pw;
	}
	public String getStaff_name() {
		return staff_name;
	}
	public void setStaff_name(String staff_name) {
		this.staff_name = staff_name;
	}
	public String getStaff_email() {
		return staff_email;
	}
	public void setStaff_email(String staff_email) {
		this.staff_email = staff_email;
	}
	public String getStaff_mail_id() {
		return staff_mail_id;
	}
	public void setStaff_mail_id(String staff_mail_id) {
		this.staff_mail_id = staff_mail_id;
	}
	public String getStaff_mail_domain() {
		return staff_mail_domain;
	}
	public void setStaff_mail_domain(String staff_mail_domain) {
		this.staff_mail_domain = staff_mail_domain;
	}
	public String getStaff_birth() {
		return staff_birth;
	}
	public void setStaff_birth(String staff_birth) {
		this.staff_birth = staff_birth;
	}
	public String getStaff_gender() {
		return staff_gender;
	}
	public void setStaff_gender(String staff_gender) {
		this.staff_gender = staff_gender;
	}
	public String getStaff_phone() {
		return staff_phone;
	}
	public void setStaff_phone(String staff_phone) {
		this.staff_phone = staff_phone;
	}
    
    
}