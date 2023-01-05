package com.service.eventus.event;

import java.util.Date;

import lombok.Data;

@Data
public class EventVo {
	private int id;
    private String event_title;
    private String event_content;
    private String event_startDate;
    private String event_endDate;
    private String event_venue;
    private String event_deadline;
    private String event_position;
    private String event_position_count;
    private int event_status;
    private Date created_at;
    private Date updated_at;
    private String flag;
    
    private int application_count;
    private int staff_count; 
    private int booth_count;
}
