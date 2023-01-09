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

    @Select("select event_title, user_name, user_gender, user_birth, user_phone, user_date_join from event e" +
            "    left join staff_application sa on e.id = sa.event_id" +
            "    left join user u on sa.staff_id = u.id" +
            "    where e.user_id = #{user_id} and sa.application_result='합격'" +
            "    order by e.event_startDate desc")
    List<MasterVo> getListMemberApp(String user_id);

    // 이력관리 - 지원현황 : 유저 아이디를 가지고 지원한 이벤트 목록 가져오기
    @Select ("select event_title, event_startDate, event_endDate, application_result from event e\n" +
            "left join staff_application sa on e.id=sa.event_id\n" +
            "left join user u on sa.staff_id=u.id\n" +
            "where u.user_id='test5' and sa.application_result='합격'\n" +
            "order by e.event_startDate desc;")
    List<MasterVo> getListUserApp(String user_id);

    @Select("select staff_id, work_date, event_title, user_name, user_phone, work_start_time, work_outing_time,"
            + " work_comeback_time, work_end_time, work_total_time from staff_work_record a "
            + "left JOIN user u ON a.staff_id= u.id left join event e ON a.event_id = e.id;")
    List<MasterVo> report_work_list();

    @Update("update staff_work_record set work_total_time = '${total_time}' where staff_id = ${staff_id}")
    int update_work_total_time(String total_time, int staff_id);

    @Select("select id from user where user_id = #{user_id}")
    int getUserId(String user_id);

    @Select("select id from event where event_title = #{event_title}")
    int getEventId(String event_title);

    @Insert("")
    int addContract(MasterVo masterVo);
}

