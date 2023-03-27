package com.service.eventus.master;

import lombok.Data;

@Data
public class MasterVo {
    private int id;
    private int event_id;
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
    private String staff_address;

    private String event_position;
    private String event_position_pay;
    private String event_venue;
    private int event_check;
    private int contract_check;
    private int pass_check; // 스태프 합격 여부
    private int list_no; // 페이징 처리할 때 list 번호 미리 저장

    private int year;
    private int month;
    private int day;
    private String identification_number; // 주민등록번호
    private String contract_date;
    
    private int resume_id; //지원할 때의 이력서

}
