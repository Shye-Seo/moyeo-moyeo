package com.service.eventus.event;

import java.util.Date;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class EventVo {
	private int id;
    private String event_title;
    private String event_content;
    private String event_startDate;
    private String event_endDate;
    private String event_status;
    private String event_position;
    private String event_venue;
    private String event_deadline;
    private String event_position_count;
    private int application_count;
    private int booth_count;
    private Date created_at;
    private Date updated_at;
    private String flag;
    
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getEvent_title() {
		return event_title;
	}
	public void setEvent_title(String event_title) {
		this.event_title = event_title;
	}
	public String getEvent_content() {
		return event_content;
	}
	public void setEvent_content(String event_content) {
		this.event_content = event_content;
	}
	public String getEvent_startDate() {
		return event_startDate;
	}
	public void setEvent_startDate(String event_startDate) {
		this.event_startDate = event_startDate;
	}
	public String getEvent_endDate() {
		return event_endDate;
	}
	public void setEvent_endDate(String event_endDate) {
		this.event_endDate = event_endDate;
	}
	public String getEvent_status() {
		return event_status;
	}
	public void setEvent_status(String event_status) {
		this.event_status = event_status;
	}
	public String getEvent_position() {
		return event_position;
	}
	public void setEvent_position(String event_position) {
		this.event_position = event_position;
	}
	public String getEvent_position_count() {
		return event_position_count;
	}
	public void setEvent_position_count(String event_position_count) {
		this.event_position_count = event_position_count;
	}
	public Date getCreated_at() {
		return created_at;
	}
	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}
	public Date getUpdated_at() {
		return updated_at;
	}
	public void setUpdated_at(Date updated_at) {
		this.updated_at = updated_at;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
}
