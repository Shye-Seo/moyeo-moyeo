package com.service.eventus.event;

import lombok.Data;

@Data
public class BoothVo {
	private int id;
	private int event_id;
	private String booth_name;
	private String counting;
	private String expected_time;
	
	private String event_startDate;
	private String event_endDate;
	private String event_title;
}
