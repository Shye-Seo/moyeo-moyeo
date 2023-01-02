package com.service.eventus.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
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
	
	@Update("update eventusdb.event set event_title = #{event_title}, event_content = #{event_content}, event_startDate = #{event_startDate}, event_endDate = #{event_endDate}, event_venue = #{event_venue}, "
			+ "event_deadline = #{event_deadline}, event_position = #{event_position}, event_position_count = #{event_position_count}, updated_at = sysdate() where id = #{id}")
	boolean updateEvent (EventVo eventVo); //행사 수정
	
	@Delete("delete from event_file where event_id = #{event_id} and file_name = #{file_name}")
	boolean deleteFile (int event_id, String file_name); //파일 삭제
	
	@Select("select count(*) from event e inner join staff_application s where e.id = s.event_id and e.id = #{event_id}")
	int application_count(int event_id); // 지원현황 count
	
	@Select("select * from user u inner join staff_application s, event e where u.id = s.staff_id and e.id = s.event_id and e.id = #{event_id}")
	List<MemberVo> application_list(int event_id); // 지원현황 지원자 리스트(모집중) - 지원자 정보(이름, 나이, 휴대폰번호)
	
	@Select("select count(staff_career_eventName) from staff_resume r inner join staff_application s, user u where s.staff_id = u.id and r.staff_id = u.id and u.id = #{staff_id}")
	int staff_career(int staff_id); // 지원현황 지원자 리스트(모집중) - 행사경력 count

	@Select("select staff_address from staff_resume r inner join staff_application s, user u where s.staff_id = u.id and r.staff_id = u.id and u.id = #{staff_id}")
	String getStaffAddress(int staff_id); // 지원현황 지원자 리스트(모집중) - 거주지
	
	@Select("select round((to_days(now()) - (to_days('${user_birth}'))) / 365)")
	String getUserAge(String user_birth); // 지원현황 지원자 리스트(모집중) - 나이계산(만 나이)

	@Update("update staff_application set application_result = '합격' where event_id = ${event_id} and staff_id = ${staff_id}")
	int accept_applicant(int event_id, int staff_id); // 지원자 수락
	
	@Update("update staff_application set application_result = '대기중' where event_id = ${event_id} and staff_id = ${staff_id}")
	int accept_applicant_cancel(int event_id, int staff_id); // 지원자 수락해제
	
	@Update("update staff_application set application_result = '불합격' where event_id = ${event_id} and staff_id = ${staff_id}")
	int reject_applicant(int event_id, int staff_id); // 지원자 불합격처리
	
	@Select("select application_result from staff_application where event_id = ${event_id} and staff_id = ${staff_id}")
	String getResult(int event_id, int staff_id); // 지원현황 지원자 리스트(모집중) - 지원결과

}