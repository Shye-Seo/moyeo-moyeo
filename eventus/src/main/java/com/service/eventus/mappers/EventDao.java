package com.service.eventus.mappers;

import java.util.List;
import java.util.Map;

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
	
	@Insert("insert into event (user_id, event_title, event_content, event_startDate, event_endDate, event_status, event_position, event_position_count, event_position_pay, event_position_startTime, event_position_endTime, event_venue, event_deadline, created_at) "
			+ "values(#{user_id}, #{event_title}, #{event_content}, #{event_startDate}, #{event_endDate}, 0, #{event_position}, #{event_position_count}, #{event_position_pay}, #{event_position_startTime}, #{event_position_endTime}, #{event_venue}, #{event_deadline}, sysdate())")
	boolean insertEvent (EventVo eventVo); //행사 추가
	
	@Insert("insert into event_file (event_id, file_name) value (#{event_id}, #{file_name})")
	boolean insertEventFile (EventFileVo eventFileVo); //행사 파일 추가
	
	@Select("select max(id) from event")
	int maxEventId (); //마지막 추가 행사 id
	
	@Update("update event set event_title = #{event_title}, event_content = #{event_content}, event_startDate = #{event_startDate}, event_endDate = #{event_endDate}, event_venue = #{event_venue}, event_deadline = #{event_deadline}, "
			+ "event_position = #{event_position}, event_position_count = #{event_position_count}, event_position_pay = #{event_position_pay}, event_position_startTime = #{event_position_startTime}, event_position_endTime = #{event_position_endTime}, updated_at = sysdate() where id = #{id}")
	boolean updateEvent (EventVo eventVo); //행사 수정
	
	@Delete("delete from event_file where event_id = #{event_id} and file_name = #{file_name}")
	boolean deleteFile (int event_id, String file_name); //파일 삭제
	
	@Select("select count(*) from event e inner join staff_application s where e.id = s.event_id and e.id = #{event_id}")
	int application_count(int event_id); // 지원현황 count
	
	@Select("select * from staff_application where event_id = #{event_id}")
	List<ApplicationVo> selectApplication (int event_id); // 지원현황 조회 (ApplicationVo)
	
	@Select("select * from user u inner join staff_application s, event e where u.id = s.staff_id and e.id = s.event_id and e.id = #{event_id}")
	List<MemberVo> application_list(int event_id); // 지원현황 지원자 리스트(모집중) - 지원자 정보(이름, 나이, 휴대폰번호)
	
	@Select("select distinct * from user u inner join staff_application s, event e, (SELECT A.event_id, A.staff_id, coalesce(B.staff_id, 0) as passState , A.staff_position "
			+ "FROM staff_application A "
			+ "LEFT OUTER JOIN staff_passer B "
			+ "ON A.staff_id=B.staff_id and A.event_id = B.event_id) c where u.id = c.staff_id and s.staff_id = c.staff_id and u.id = s.staff_id and e.id = c.event_id and e.id = s.event_id and e.id = #{event_id}")
	List<MemberVo> application_complete_list(int event_id); // 지원현황 지원자 리스트(모집완료) - 지원자 정보(이름, 나이, 휴대폰번호)
	
	@Select("select if(staff_career_eventName = \"\", 0, (length(staff_career_eventName) - length(replace(staff_career_eventName, ',', ''))) + 1) as count from staff_resume r inner join staff_application s where s.resume_id = r.id and s.staff_id = #{staff_id} and s.event_id = #{event_id}")
	int staff_career(int staff_id, int event_id); // 지원현황 지원자 리스트(모집중) - 행사경력 count

	@Select("select staff_address from staff_resume r inner join staff_application s where s.resume_id = r.id and  s.staff_id = #{staff_id} and s.event_id = #{event_id}")
	String getStaffAddress(int event_id, int staff_id); // 지원현황 지원자 리스트(모집중) - 거주지
	
	@Select("select round((to_days(now()) - (to_days('${user_birth}'))) / 365)")
	String getUserAge(String user_birth); // 지원현황 지원자 리스트(모집중) - 나이계산(만 나이)
	
	@Select("select distinct staff_position as position_count  from staff_application where event_id = #{event_id}")
	List<String> application_position_count (int event_id); //지원자 포지션 조회
	
	@Update("update staff_application set staff_result = #{staff_result} where event_id = #{event_id} and staff_id = #{staff_id}")
	boolean updateStaffResult(int staff_result, int event_id, int staff_id); //임시 합불합 상태 변환
	
	@Select("select staff_id from staff_application where event_id = #{event_id} and staff_result = 1")
	List<Integer> selectStatusPasser (int event_id); // 지원현황 임시 합불합 중 합격상태 조회
	
//	@Insert("<script>insert into staff_passer (event_id, staff_id) value "
//			+ "<foreach collection=\"passer_list\" item=\"staff_id\" separator=\",\" >"
//			+ "(#{event_id},#{staff_id})</foreach></script>")
//	boolean insertPasser(int event_id ,List passer_list); //합격자 확정 등록(리스트형식
	
	@Insert("insert into staff_passer (event_id, staff_id) value (#{event_id}, #{staff_id})")
	boolean insertOnePasser(int event_id ,int staff_id); //합격자 확정 등록
	
	@Select("SELECT e.event_title, a.staff_position, u.user_name, u.user_phone FROM staff_application a join user u , event e where a.staff_id= u.id and e.id = a.event_id and a.staff_id = #{staff_id} and a.event_id = #{event_id};")
	Map passerInfo(int event_id, int staff_id); //합격자 정보 조회
	
	@Select("SELECT count(*) FROM staff_passer where event_id = #{event_id}")
	int countPasser(int event_id); //합격자 수 조회
	
	@Update("update event set event_status = #{status} where id = #{event_id}")
	public boolean update_event_status(int status, int event_id); //이벤트 상태 변경
	
	@Update("update event set event_check = #{status} where id = #{event_id}")
	public boolean update_event_check(int status, int event_id); //이벤트 체크 변경
	
	@Select("select count(*) from staff_passer where event_id = #{event_id}")
	int staff_count(int event_id); // 근무직원 count

	@Select("select * from user u inner join staff_application a, staff_passer s, staff_resume r where u.id = s.staff_id and s.staff_id = a.staff_id and s.event_id = a.event_id and s.staff_id = r.staff_id and s.event_id = #{event_id}")
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
