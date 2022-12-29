package com.service.eventus.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.service.eventus.event.EventFileVo;
import com.service.eventus.event.EventVo;
import com.service.eventus.event.ResumeVo;
import com.service.eventus.member.MemberVo;

@Mapper
@Repository
public interface EventDao {

	@Select("select * from event order by id desc")
	List<EventVo> event_list();  // 행사현황 리스트
	
	@Select("select * from event where id = #{event_id}")
	EventVo viewEventDetail (int event_id); // 행사현황 세부페이지
	
	@Select("select * from event_file where id = #{event_id}")
	List<EventFileVo> viewEventFileDetail (int event_id); // 행사현황 세부페이지 파일
	
	@Insert("insert into event (event_title, event_content, event_startDate, event_endDate, event_status, event_position, event_position_count, event_venue, event_deadline, created_at) "
			+ "values(#{event_title}, #{event_content}, #{event_startDate}, #{event_endDate}, 0, #{event_position}, #{event_position_count}, #{event_venue}, #{event_deadline}, sysdate())")
	boolean insertEvent (EventVo eventVo); //행사 추가
	
	@Insert("insert into event_file (event_id, file_name) value (#{event_id}, #{file_name})")
	boolean insertEventFile (EventFileVo eventFileVo); //행사 파일 추가
	
	@Select("select max(id) from event")
	int maxEventId (); //마지막 추가 행사 id
	
	@Select("select count(*) from event e inner join staff_application s where e.id = s.event_id and e.id = #{event_id}")
	int application_count(int event_id); // 지원현황 count
	
	@Select("select * from user u inner join staff_application s, event e where u.id = s.staff_id and e.id = s.event_id and e.id = #{event_id}")
	List<MemberVo> application_list(int event_id); // 지원현황 지원자 리스트(모집중) - 지원자 정보(이름, 나이, 휴대폰번호, 주소)
	
	@Select("select count(staff_career_eventName) from staff_resume r inner join staff_application s, user u where s.staff_id = u.id and r.staff_id = u.id and u.id = #{staff_id}")
	int staff_career(int staff_id); // 지원현황 지원자 리스트(모집중) - 행사경력 count
	
	
}
