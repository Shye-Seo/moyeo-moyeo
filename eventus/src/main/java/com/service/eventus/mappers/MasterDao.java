package com.service.eventus.mappers;

import com.service.eventus.master.MasterVo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface MasterDao {

    // 주최한 행사에 지원해서 합격한 근로자들 목록 전부 출력
    @Select("select event_id, staff_id, event_title, user_name, user_gender, user_birth, user_phone, user_date_join from event e" +
            "    inner join staff_application sa on e.id = sa.event_id" +
            "    inner join user u on sa.staff_id = u.id" +
            "    where e.user_id = #{user_id}" +
            "    order by e.event_startDate desc")
    List<MasterVo> getListMemberApp(String user_id);

    // 이력관리 - 지원현황 : 유저 아이디를 가지고 지원한 이벤트 목록 가져오기
    @Select ("select staff_id, event_id, event_title, event_startDate, event_endDate, event_check from event e\n" +
            "inner join staff_application sa on e.id=sa.event_id\n" +
            "inner join user u on sa.staff_id=u.id\n" +
            "where u.user_id='test5'\n" +
            "order by e.event_startDate desc;")
    List<MasterVo> getListUserApp(String user_id);

    //근무기록 리스트(관리자)
    @Select("select a.id, staff_id, work_date, event_title, user_name, user_phone, work_start_time, work_outing_time,"
            + " work_comeback_time, work_end_time, work_total_time from staff_work_record a "
            + "left JOIN user u ON a.staff_id= u.id left join event e ON a.event_id = e.id;")
    List<MasterVo> report_work_list();

    //근무기록 소계시간(관리자)
    @Update("update staff_work_record set work_total_time = '${total_time}' where id = ${staff_id}")
    int update_work_total_time(String total_time, int staff_id);

    //근무기록 리스트(스태프)
    @Select("select * from staff_work_record where staff_id = ${staff_id}")
    List<MasterVo> report_work_list_Staff(int staff_id);
    
    @Select("select id from user where user_id = #{user_id}")
    int getUserId(String user_id);

    @Select("select id from event where event_title = #{event_title}")
    int getEventId(String event_title);

    @Insert("")
    int addContract(MasterVo masterVo);

    // 이벤트 정보 가져오기
    @Select("select * from event where id=#{id}")
    MasterVo getEventInfo(int id);

    // 근로계약서 정보 입력
    @Insert("insert into contract_file (event_id, staff_id, user_name, user_phone, staff_address, identification_number, contract_date) values (#{event_id}, #{staff_id}, #{user_name}, #{user_phone}, #{staff_address}, #{identification_number}, #{contract_date})")
    void insert_contract(MasterVo masterVo);

    // 행사 모집 합격자인지 확인
    @Select("select count(*) from staff_passer where event_id=#{event_id} and staff_id=#{staff_id}")
    int checkStaffPasser(MasterVo masterVo);

    // 근로계약서 등록되어 있는지 확인
    @Select("select count(*) from contract_file where event_id=#{event_id} and staff_id=#{staff_id}")
    int checkContractFile(MasterVo masterVo);
}
