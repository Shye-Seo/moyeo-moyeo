package com.service.eventus.master;

import lombok.Data;

@Data
public class MasterVo {
    private int id;
    private String event_id;
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

    private int staff_id;
    private String event_position_pay;
    private String event_venue;
    private int event_check;
    private int contract_check;
    private int pass_check; // 스태프 합격 여부
    private String work_date;
    private int list_no; // 페이징 처리할 때 list 번호 미리 저장

    private int year;
    private int month;
    private int day;
    private String identification_number; // 주민등록번호
    private String contract_date;

}
