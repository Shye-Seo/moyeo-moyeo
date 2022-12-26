package com.service.eventus.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.service.eventus.event.EventVo;

@Mapper
@Repository
public interface EventDao {

	@Select("select * from event order by id desc")
	public List<EventVo> event_list();  // 행사현황 리스트
	
	@Select("select * from event where id = #{event_id}")
	EventVo viewEventDetail (int event_id); // 행사현황 세부페이지
	
	@Insert("insert into event (event_title, event_content, event_startDate, event_endDate, event_status, event_position, event_position_count, created_at) "
			+ "values(#{event_title}, #{event_content}, #{event_startDate}, #{event_endDate}, 0, #{event_position}, #{event_position_count}, sysdate())")
	boolean insertEvent (EventVo eventVo); //행사 추가
	
	@Select("select max(id) from event")
	int maxEventId (); //마지막 추가 행사 id
	
	

}
