package com.service.eventus.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.service.eventus.event.EventVo;

@Mapper
@Repository
public interface EventDao {

	@Select("select * from event order by id desc")
	List<EventVo> event_list();  // 행사현황 리스트
	
	@Select("SELECT * FROM event where id = #{event_id}")
	EventVo viewEventDetail (int event_id); // 행사현황 세부페이지
}
