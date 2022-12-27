package com.service.eventus.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.service.eventus.event.EventVo;
import com.service.eventus.member.MemberVo;

@Mapper
@Repository
public interface EventDao {

	@Select("select * from event order by id desc")
	List<EventVo> event_list();  // 행사현황 리스트
	
	@Select("SELECT * FROM event where id = #{event_id}")
	EventVo viewEventDetail (int event_id); // 행사현황 세부페이지
	
	@Select("select count(*) from event e inner join staff_application s where e.id = s.event_id and e.id = #{event_id}")
	int application_count(int event_id); // 지원현황 count
	
	@Select("select * from user u inner join staff_application s, event e where u.id = s.staff_id and e.id = #{event_id}")
	List<MemberVo> application_list(int event_id); // 지원현황 지원자 리스트(모집중)
}
