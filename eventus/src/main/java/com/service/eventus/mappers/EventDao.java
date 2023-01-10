package com.service.eventus.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.service.eventus.event.ApplicationVo;
import com.service.eventus.event.BoothVo;
import com.service.eventus.event.EventFileVo;
import com.service.eventus.event.EventVo;
import com.service.eventus.event.ResumeVo;
import com.service.eventus.event.WorkRecordVo;
import com.service.eventus.member.MemberVo;

@Mapper
@Repository
public interface EventDao {

	@Select("select * from event order by event_status asc, id desc")
	List<EventVo> event_list();  // 행사현황 리스트
	
	@Insert("insert into staff_application (event_id, staff_id, resume_id, staff_position) value (#{event_id}, #{staff_id}, #{resume_id}, #{staff_position})")
	boolean insertApplication(ApplicationVo applicationVo); // 행사 지원하기
	
	@Select("select exists (select * from staff_application where event_id = #{event_id} and staff_id = #{staff_id}) as isChk")
	boolean isChkApplication (ApplicationVo applicationVo); //행사 지원 이력 여부 체크 (중복방지)
	
	@Select("select * from event where id = #{event_id}")
	EventVo viewEventDetail (int event_id); // 행사현황 세부페이지
	
	@Select("select * from event_file where event_id = #{event_id}")
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

	@Select("select staff_address from staff_resume r inner join staff_application s, user u where s.staff_id = u.id and r.staff_id = u.id and u.id = #{staff_id} and s.event_id = #{event_id}")
	String getStaffAddress(int event_id, int staff_id); // 지원현황 지원자 리스트(모집중) - 거주지
	
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

	@Select("select count(*) from staff_passer where event_id = #{event_id}")
	int staff_count(int event_id); // 근무직원 count

	@Select("select * from user u inner join staff_passer s where u.id = s.staff_id and s.event_id = #{event_id}")
	List<MemberVo> workStaff_list(int event_id); // 근무직원 리스트(진행중) - 직원정보(이름, 나이, 휴대폰번호)

	@Select("select * from staff_work_record where staff_id = #{staff_id} and event_id = #{event_id} and work_date = #{work_date}")
	List<WorkRecordVo> getWorkTime(int staff_id, int event_id, String work_date); // 당일 근무기록 get
	
	@Insert("insert into staff_work_record(id, work_start_time, work_date, staff_id, event_id) values(#{record_id}, #{start_time}, #{work_date}, #{staff_id}, #{event_id}) on duplicate key update work_start_time = #{start_time}")
	boolean record_startTime (int record_id, int event_id, int staff_id, String work_date, String start_time); // 출근시간 기록

	@Insert("insert into staff_work_record(id, work_outing_time, work_date, staff_id, event_id) values(#{record_id}, #{out_time}, #{work_date}, #{staff_id}, #{event_id}) on duplicate key update work_outing_time = #{out_time}")
	boolean record_outTime(int record_id, int event_id, int staff_id, String work_date, String out_time); // 외출시간 기록
	
	@Insert("insert into staff_work_record(id, work_comeback_time, work_date, staff_id, event_id) values(#{record_id}, #{back_time}, #{work_date}, #{staff_id}, #{event_id}) on duplicate key update work_comeback_time = #{back_time}")
	boolean record_backTime(int record_id, int event_id, int staff_id, String work_date, String back_time); // 복귀시간 기록
	
	@Insert("insert into staff_work_record(id, work_end_time, work_date, staff_id, event_id) values(#{record_id}, #{end_time}, #{work_date}, #{staff_id}, #{event_id}) on duplicate key update work_end_time = #{end_time}")
	boolean record_endTime(int record_id, int event_id, int staff_id, String work_date, String end_time); // 퇴근시간 기록
	
	// 당일 근무기록 없을 때, 출퇴근 기록
	@Insert("insert into staff_work_record(work_start_time, work_date, staff_id, event_id) values(#{start_time}, #{work_date}, #{staff_id}, #{event_id}) on duplicate key update work_start_time = #{start_time}")
	boolean record_startTime_new (int event_id, int staff_id, String work_date, String start_time); // 출근시간 기록

	@Insert("insert into staff_work_record(work_outing_time, work_date, staff_id, event_id) values(#{out_time}, #{work_date}, #{staff_id}, #{event_id}) on duplicate key update work_outing_time = #{out_time}")
	boolean record_outTime_new(int event_id, int staff_id, String work_date, String out_time); // 외출시간 기록
	
	@Insert("insert into staff_work_record(work_comeback_time, work_date, staff_id, event_id) values(#{back_time}, #{work_date}, #{staff_id}, #{event_id}) on duplicate key update work_comeback_time = #{back_time}")
	boolean record_backTime_new(int event_id, int staff_id, String work_date, String back_time); // 복귀시간 기록
	
	@Insert("insert into staff_work_record(work_end_time, work_date, staff_id, event_id) values(#{end_time}, #{work_date}, #{staff_id}, #{event_id}) on duplicate key update work_end_time = #{end_time}")
	boolean record_endTime_new(int event_id, int staff_id, String work_date, String end_time); // 퇴근시간 기록
	
	//이벤트 부스-----------------------------
	@Select("select count(*) from event e inner join event_booth b where e.id = b.event_id and e.id = #{event_id} and b.flag = 'Y'")
	int booth_count(int event_id); // 부스현황 count

	@Select("select * from event_booth where event_id = #{event_id} and flag = 'Y'")
	List<BoothVo> booth_list(int event_id); // 부스현황 리스트

	@Select("select e.event_title from event e inner join event_booth b where e.id = b.event_id and b.id = #{booth_id}")
	String getEventTitle(int booth_id); // 행사명 get
	
	@Select("select e.event_startDate from event e inner join event_booth b where e.id = b.event_id and b.id = #{booth_id}")
	String getStartDate(int booth_id); // 시작날짜 get
	
	@Select("select e.event_endDate from event e inner join event_booth b where e.id = b.event_id and b.id = #{booth_id}")
	String getEndDate(int booth_id); // 끝나는날짜 get

	@Insert("insert into event_booth(event_id, booth_name, counting, expected_time) values(#{event_id}, #{booth_name}, #{counting}, #{expected_time})")
	boolean register_booth(int event_id, String booth_name, String counting, String expected_time); // 부스등록

	@Update("update event_booth set booth_name = #{booth_name}, counting = #{counting}, expected_time = #{expected_time} where id = #{booth_id}")
	boolean modify_booth(int booth_id, String booth_name, String counting, String expected_time); // 부스수정

	@Update("update event_booth set flag = 'N' where id = #{booth_id}")
	boolean delete_booth(int booth_id); // 부스삭제

	@Select("select event_title from event where id = #{event_id}")
	String getTitle(int event_id); 

	@Update("update event set event_status = #{i} where id = #{event_id}")
	boolean setEventStatus(int event_id, int i); // 행사상태 set
}
