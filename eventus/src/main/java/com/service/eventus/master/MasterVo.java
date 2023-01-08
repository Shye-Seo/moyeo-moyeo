package com.service.eventus.master;

import lombok.Data;

@Data
public class MasterVo {
    private int id;
    private String event_title;
    private String event_startDate;
    private String event_endDate;
    private String application_result;
    private String user_name;
    private String user_gender;
    private String user_birth;
    private String user_phone;
    private String user_date_join;

    //근무기록 리스트(관리자)
    private String work_date;
    private String work_start_time;
    private String work_outing_time;
    private String work_comeback_time;
    private String work_end_time;
    private String work_total_time;

    private int staff_id;
}
