package com.service.eventus.mappers;

import com.service.eventus.master.MasterVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
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
            "where u.user_id='test5'\n" +
            "order by e.event_startDate desc;")
    List<MasterVo> getListUserApp(String user_id);
}

