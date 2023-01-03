package com.service.eventus.mappers;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface MasterDao {

    @Select("select event_title, user_name, user_gender, user_birth, user_phone, user_date_join from event" +
            "    left join staff_application on event.id = staff_application.event_id" +
            "    left join user on staff_application.staff_id = user.id" +
            "where event.user_id = '#{user_id}' and staff_application.application_result=\"합격\"")
    public MasterDao getListMemberJoined(String user_id);
}
