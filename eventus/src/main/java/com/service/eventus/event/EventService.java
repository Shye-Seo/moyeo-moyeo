package com.service.eventus.event;

import java.util.List;

import com.service.eventus.mappers.EventDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.service.eventus.member.MemberVo;

@Service
public class EventService {

	@Autowired
	private EventDao eventDao;
	 
	// 행사현황 리스트
	public List<EventVo> event_list() throws Exception {
		return eventDao.event_list();
	}
	
	// 행사 상세 조회
	public EventVo viewEventDetail(int event_id) {
		return eventDao.viewEventDetail(event_id);
	}
	
	// 행사 상세 조회 파일
	public List<EventFileVo> viewEventFileDetail (int event_id) {
		return eventDao.viewEventFileDetail(event_id);
	}
	
	//행사등록
	public int insertEvent(EventVo eventVo) {
		eventDao.insertEvent(eventVo);
		int id = eventDao.maxEventId();
		return id;
	}
	
	//행사 파일 등록
	public boolean insertEventFile(EventFileVo eventFileVo) {
		return eventDao.insertEventFile(eventFileVo);
	}
	
	//행사 수정
	public boolean updateEvent (EventVo eventVo) {
		return eventDao.updateEvent(eventVo);
	}
		
	//행사 파일 삭제
	public boolean deleteFile (int event_id, String file_name) {
		return eventDao.deleteFile(event_id, file_name);
	}

	// 지원현황 count
	public int application_count(int event_id) throws Exception {
		return eventDao.application_count(event_id);
	}
	
	// 지원현황 지원자 리스트(모집중)
	public List<MemberVo> application_list(int event_id) throws Exception {
		return eventDao.application_list(event_id);
	}
	
	// 지원현황 지원자 행사경력
	public int staff_career(int staff_id) throws Exception {
		return eventDao.staff_career(staff_id);
	}

	// 지원현황 지원자 거주지
	public String getAddress(int event_id, int staff_id) throws Exception {
		return eventDao.getStaffAddress(event_id, staff_id);
	}
	
	// 지원현황 지원자 만 나이
	public String getUserAge(String user_birth) throws Exception {
		return eventDao.getUserAge(user_birth);
	}

	// 지원자 수락
	public int accept_applicant(int event_id, int staff_id) {
		return eventDao.accept_applicant(event_id, staff_id);
	}
	
	// 지원자 수락해제
	public int accept_applicant_cancel(int event_id, int staff_id) {
		return eventDao.accept_applicant_cancel(event_id, staff_id);
	}
	
	// 지원자 불합격처리
	public int reject_applicant(int event_id, int staff_id) {
		return eventDao.reject_applicant(event_id, staff_id);
	}
	
	// 지원결과
	public String getResult(int event_id, int staff_id) {
		return eventDao.getResult(event_id, staff_id);
	}

	// 근무직원 count
	public int staff_count(int event_id) {
		return eventDao.staff_count(event_id);
	}

	// 근무직원 list
	public List<MemberVo> workStaff_list(int event_id) {
		return eventDao.workStaff_list(event_id);
	}

	// 당일 근무기록 get
	public List<WorkRecordVo> getWorkTime(int staff_id, int event_id, String work_date) {
		return eventDao.getWorkTime(staff_id, event_id, work_date);
	}
	
	// 출근시간 기록
	public boolean record_startTime(int record_id, int event_id, int staff_id, String work_date, String start_time) {
		return eventDao.record_startTime(record_id, event_id, staff_id, work_date, start_time);
	}

	// 외출시간 기록
	public boolean record_outTime(int record_id, int event_id, int staff_id, String work_date, String out_time) {
		return eventDao.record_outTime(record_id, event_id, staff_id, work_date, out_time);
	}
	
	// 복귀시간 기록
	public boolean record_backTime(int record_id, int event_id, int staff_id, String work_date, String back_time) {
		return eventDao.record_backTime(record_id, event_id, staff_id, work_date, back_time);
	}
	
	// 퇴근시간 기록
	public boolean record_endTime(int record_id, int event_id, int staff_id, String work_date, String end_time) {
		return eventDao.record_endTime(record_id, event_id, staff_id, work_date, end_time);
	}
	
	// 당일 근무기록 없을때, 출퇴근시간기록
	public boolean record_startTime_new(int event_id, int staff_id, String work_date, String start_time) {
		return eventDao.record_startTime_new(event_id, staff_id, work_date, start_time);
	}
	public boolean record_outTime_new(int event_id, int staff_id, String work_date, String out_time) {
		return eventDao.record_outTime_new(event_id, staff_id, work_date, out_time);
	}
	public boolean record_backTime_new(int event_id, int staff_id, String work_date, String back_time) {
		return eventDao.record_backTime_new(event_id, staff_id, work_date, back_time);
	}
	public boolean record_endTime_new(int event_id, int staff_id, String work_date, String end_time) {
		return eventDao.record_endTime_new(event_id, staff_id, work_date, end_time);
	}

	//-------------------부스----------------
	// 부스현황 count
	public int booth_count(int event_id) throws Exception {
		return eventDao.booth_count(event_id);
	}
	
	// 부스현황 리스트
	public List<BoothVo> booth_list(int event_id) {
		return eventDao.booth_list(event_id);
	}

	// 행사명 get
	public String getEventTitle(int booth_id) {
		return eventDao.getEventTitle(booth_id);
	}
	
	// 시작날짜 get
	public String getStartDate(int booth_id) {
		return eventDao.getStartDate(booth_id);
	}
		
	// 끝나는날짜 get
	public String getEndDate(int booth_id) {
		return eventDao.getEndDate(booth_id);
	}

	// 부스등록
	public boolean register_booth(int event_id, String booth_name, String counting, String expected_time) {
		return eventDao.register_booth(event_id, booth_name, counting, expected_time);
	}
	
	// 부스수정
	public boolean modify_booth(int booth_id, String booth_name, String counting, String expected_time) {
		return eventDao.modify_booth(booth_id, booth_name, counting, expected_time);
	}

	// 부스삭제
	public boolean delete_booth(int booth_id) {
		return eventDao.delete_booth(booth_id);
	}

	// 행사명 get (부스등록시)
	public String getTitle(int event_id) {
		return eventDao.getTitle(event_id);
	}

	// 행사상태 set
	public boolean setEventStatus(int event_id, int i) {
		return eventDao.setEventStatus(event_id, i);
	}	
}