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
    private String staff_address;

    private int year;
    private int month;
    private int day;
    private String identification_number; // 주민등록번호

}
