package com.service.eventus.event;

import lombok.Data;

@Data
public class WorkRecordVo {
	private int id;
	private int staff_id;
	private int event_id;
	private String work_date;
	private String work_start_time;
	private String work_end_time;
	private String work_outing_time;
	private String work_comeback_time;
}
