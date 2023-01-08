package com.service.eventus.resume;

import lombok.Data;

@Data
public class ResumeVo {
	private int id;
	private int staff_id;
	private String staff_email; 
	private String staff_address;
	private String staff_school;
	private String staff_major;
	private String staff_school_start;
	private String staff_school_end;
	private String staff_school_state;
	private String staff_career_eventName;
	private String staff_career_businessName;
	private String staff_career_position;
	private String staff_career_workday;
	private int staff_resume_flag;
	private String staff_resume_created_at;
}
