package com.service.eventus.master;

import lombok.Data;

@Data
public class WorkLogVo {
	private int id;
	private int staff_id;
	private String staff_name;
	private String event_name;
	private String work_time;
	private int work_state;
	private String created_at;
	
	//프로필사진
	private String file_name;
	
}
