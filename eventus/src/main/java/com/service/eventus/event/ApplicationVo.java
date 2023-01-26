package com.service.eventus.event;

import lombok.Data;

@Data
public class ApplicationVo {
	private int id;
	private int event_id;
	private int staff_id;
	private int resume_id;
	private String staff_position;
	private int staff_result;
	private String app_created_at;
	
	//main
	private String event_title;
}	
