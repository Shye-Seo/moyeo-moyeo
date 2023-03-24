package com.service.eventus.event;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.service.eventus.mappers.EventDao;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.service.eventus.member.MemberVo;

import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;

@Service
public class EventService {

	@Autowired
	private EventDao eventDao;
	
	// 행사현황 리스트
	public List<EventVo> event_list() throws Exception {
		return eventDao.event_list();
	}
	
	// 행사 상세 조회
	public EventVo viewEventDetail(int event_id) throws Exception {
		return eventDao.viewEventDetail(event_id);
	}
	
	// 행사 상세 조회 파일
	public List<EventFileVo> viewEventFileDetail (int event_id) throws Exception {
		return eventDao.viewEventFileDetail(event_id); 
	}
	
	//행사지원
	public boolean insertApplication(ApplicationVo applicationVo) throws Exception {
		return eventDao.insertApplication(applicationVo);
	} 
	
	//행사 지원 이력 여부 체크 (중복방지)
	boolean isChkApplication (ApplicationVo applicationVo) throws Exception {
		return eventDao.isChkApplication(applicationVo);
	}
	
	//행사등록
	public int insertEvent(EventVo eventVo) throws Exception {
		eventDao.insertEvent(eventVo);
		int id = eventDao.maxEventId();
		return id;
	}
	
	//행사 파일 등록
	public boolean insertEventFile(EventFileVo eventFileVo) throws Exception {
		return eventDao.insertEventFile(eventFileVo);
	}
	
	//행사 수정
	public boolean updateEvent (EventVo eventVo) throws Exception {
		return eventDao.updateEvent(eventVo);
	}
		
	//행사 파일 삭제
	public boolean deleteFile (int event_id, String file_name) throws Exception {
		return eventDao.deleteFile(event_id, file_name);
	}
	
	//지원현황
	List<ApplicationVo> selectApplication (int event_id) throws Exception {
		return eventDao.selectApplication(event_id);
	}
	//지원현황 - 포지션 종류 
	List<String> application_position_count (int event_id) throws Exception{
		return eventDao.application_position_count(event_id);
	}
	// 지원현황 count
	public int application_count(int event_id) throws Exception {
		return eventDao.application_count(event_id);
	}
	
	// 지원현황 지원자 리스트(모집중)
	public List<MemberVo> application_list(int event_id) throws Exception {
		return eventDao.application_list(event_id);
	}
	
	// 지원현황 지원자 리스트(모집완료)
	List<MemberVo> application_complete_list(int event_id) throws Exception {
		return eventDao.application_complete_list(event_id);
	}
	
	// 지원현황 지원자 행사경력
	public int staff_career(int staff_id, int event_id) throws Exception {
		return eventDao.staff_career(staff_id, event_id);
	}

	// 지원현황 지원자 거주지
	public String getAddress(int event_id, int staff_id) throws Exception {
		return eventDao.getStaffAddress(event_id, staff_id);
	}
	
	// 지원현황 지원자 만 나이
	public String getUserAge(String user_birth) throws Exception {
		return eventDao.getUserAge(user_birth);
	}

	// 합불합 임시 상태 변경
	public boolean updateStaffResult(int staff_result, int event_id, int staff_id) throws Exception {
		return eventDao.updateStaffResult(staff_result, event_id, staff_id);
	}
	
	public List<Integer> selectStatusPasser (int event_id) throws Exception{
		return eventDao.selectStatusPasser(event_id);
	}
	
	// 합격자 등록(리스트로 한번에)
//	public boolean insertPasser(int event_id ,List passer_list) throws Exception {
//		eventDao.update_event_status(1, event_id);
//		eventDao.update_event_check(1, event_id);
//		return eventDao.insertPasser(event_id, passer_list);
//	}
	
	//합격자 등록
	public Map insertOnePasser(int event_id ,int staff_id) {
		eventDao.insertOnePasser(event_id, staff_id);
		return eventDao.passerInfo(event_id, staff_id);
	}
	
	//문자발송
	// coolsms에 정보를 보내 회원가입시 인증번호를 보낸 후, 인증번호를 return 한다.
    public void sendSms_forPasser(String user_phone, String user_name, String event_name, String position_name) throws CoolsmsException {
        String api_key = "NCS9HI923SUSM5VF";
        String api_secret = "XLQEOJRXNGAIUHIFRGX5VUTCHEJV7D8N";

        Message coolsms = new Message(api_key, api_secret);

        // 4 params(to, from, type, text) are mandatory. must be filled
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("to", user_phone);
        params.put("from", "010-9878-0502"); // 발신전화번호. 테스트시에는 발신, 수신 둘다 본인 번호로 하면됨
        params.put("type", "SMS");
        params.put("text", "[모여모여]" +user_name +"님, "+ event_name + " 행사에 합격하셨습니다."); // 문자 내용 입력

        // send() 를 통해 전송
        try {
            JSONObject obj = (JSONObject) coolsms.send(params);
            System.out.println(obj.toString());
        }catch(CoolsmsException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCode());
        }
    }
	
	//모집종료
	public int end_hire(int event_id) {
		eventDao.update_event_status(1, event_id);
		eventDao.update_event_check(1, event_id);
		eventDao.update_fail_check(2, event_id);
		return eventDao.countPasser(event_id);
	}
	
	
	// 근무직원 count
	public int staff_count(int event_id) throws Exception {
		return eventDao.staff_count(event_id);
	}

	// 근무직원 list
	public List<MemberVo> workStaff_list(int event_id) throws Exception {
		return eventDao.workStaff_list(event_id);
	}

	// 당일 근무기록 get
	public List<WorkRecordVo> getWorkTime(int staff_id, int event_id, String work_date) throws Exception {
		return eventDao.getWorkTime(staff_id, event_id, work_date);
	}
	
	// 출근시간 기록
	public boolean record_startTime(int record_id, int event_id, int staff_id, String work_date, String start_time) throws Exception {
		return eventDao.record_startTime(record_id, event_id, staff_id, work_date, start_time);
	}

	// 외출시간 기록
	public boolean record_outTime(int record_id, int event_id, int staff_id, String work_date, String out_time) throws Exception {
		return eventDao.record_outTime(record_id, event_id, staff_id, work_date, out_time);
	}
	
	// 복귀시간 기록
	public boolean record_backTime(int record_id, int event_id, int staff_id, String work_date, String back_time) throws Exception {
		return eventDao.record_backTime(record_id, event_id, staff_id, work_date, back_time);
	}
	
	// 퇴근시간 기록
	public boolean record_endTime(int record_id, int event_id, int staff_id, String work_date, String end_time) throws Exception {
		return eventDao.record_endTime(record_id, event_id, staff_id, work_date, end_time);
	}
	
	// 당일 근무기록 없을때, 출퇴근시간기록
	public boolean record_startTime_new(int event_id, int staff_id, String work_date, String start_time) throws Exception {
		return eventDao.record_startTime_new(event_id, staff_id, work_date, start_time);
	}
	public boolean record_outTime_new(int event_id, int staff_id, String work_date, String out_time) throws Exception {
		return eventDao.record_outTime_new(event_id, staff_id, work_date, out_time);
	}
	public boolean record_backTime_new(int event_id, int staff_id, String work_date, String back_time) throws Exception {
		return eventDao.record_backTime_new(event_id, staff_id, work_date, back_time);
	}
	public boolean record_endTime_new(int event_id, int staff_id, String work_date, String end_time) throws Exception {
		return eventDao.record_endTime_new(event_id, staff_id, work_date, end_time);
	}
	
	//근무기록로그
	public boolean insert_work_log(int staff_id, String staff_name, int event_id, String event_name, String work_time, int work_state) {
		return eventDao.insert_work_log(staff_id, staff_name, event_id, event_name, work_time, work_state);
	}

	//-------------------부스----------------
	// 부스현황 count
	public int booth_count(int event_id) throws Exception {
		return eventDao.booth_count(event_id);
	}
	
	// 부스현황 리스트
	public List<BoothVo> booth_list(int event_id) throws Exception {
		return eventDao.booth_list(event_id);
	}

	// 행사명 get
	public String getEventTitle(int booth_id) throws Exception{
		return eventDao.getEventTitle(booth_id);
	}
	
	// 시작날짜 get
	public String getStartDate(int booth_id) throws Exception{
		return eventDao.getStartDate(booth_id);
	}
		
	// 끝나는날짜 get
	public String getEndDate(int booth_id) throws Exception{
		return eventDao.getEndDate(booth_id);
	}

	// 부스등록
	public boolean register_booth(int event_id, String booth_name, String counting, String expected_time) throws Exception{
		return eventDao.register_booth(event_id, booth_name, counting, expected_time);
	}
	
	// 부스수정
	public boolean modify_booth(int booth_id, String booth_name, String counting, String expected_time) throws Exception{
		return eventDao.modify_booth(booth_id, booth_name, counting, expected_time);
	}

	// 부스삭제
	public boolean delete_booth(int booth_id) throws Exception{
		return eventDao.delete_booth(booth_id);
	}

	// 행사명 get (부스등록시)
	public String getTitle(int event_id) throws Exception {
		return eventDao.getTitle(event_id);
	}

	// 행사상태 set
	public boolean setEventStatus(int event_id, int i) throws Exception {
		return eventDao.setEventStatus(event_id, i);
	}

	/* ---페이징처리--- */
	public int findAllCnt() {
		return eventDao.findAllCnt();
	}

	public List<EventVo> findListPaging(int startIndex, int pageSize) {
		return eventDao.findListPaging(startIndex, pageSize);
	}

	public List<EventVo> event_searchList(String searchKeyword, int startIndex, int pageSize) {
		return eventDao.event_searchList(searchKeyword, startIndex, pageSize);
	}

	public int searchCnt(String searchKeyword) {
		return eventDao.searchCnt(searchKeyword);
	}

	public int searchCnt_date(String startDate, String endDate) {
		return eventDao.searchCnt_date(startDate, endDate);
	}
	
	public List<EventVo> event_searchList_date(String startDate, String endDate, int startIndex, int pageSize){
		return eventDao.event_searchList_date(startDate, endDate, startIndex, pageSize);
	}

	public int searchCnt_keydate(String startDate, String endDate, String searchKeyword) {
		return eventDao.searchCnt_keydate(startDate, endDate, searchKeyword);
	}

	public List<EventVo> event_searchList_keydate(String startDate, String endDate, String searchKeyword, int startIndex, int pageSize) {
		return eventDao.event_searchList_keydate(startDate, endDate, searchKeyword, startIndex, pageSize);
	}

	public List<BoothVo> booth_list_paging(int event_id, int startIndex, int pageSize) {
		return eventDao.booth_list_paging(event_id, startIndex, pageSize);
	}

	public int booth_list_AllCnt(int event_id) {
		return eventDao.booth_list_AllCnt(event_id);
	}

	public int booth_searchCnt(int event_id, String searchKeyword) {
		return eventDao.booth_searchCnt(event_id, searchKeyword);
	}

	public List<BoothVo> booth_searchList(int event_id, String searchKeyword, int startIndex, int pageSize) {
		return eventDao.booth_searchList(event_id, searchKeyword, startIndex, pageSize);
	}

	public List<EventVo> findDownloadList() {
		return eventDao.findDownloadList();
	}

	public List<EventVo> event_Downloaddate(String startDate, String endDate) {
		return eventDao.event_Downloaddate(startDate, endDate);
	}

	public List<EventVo> event_Downloadkey(String searchKeyword) {
		return eventDao.event_Downloadkey(searchKeyword);
	}

	public List<EventVo> event_Downloadkeydate(String startDate, String endDate, String searchKeyword) {
		return eventDao.event_Downloadkeydate(startDate, endDate, searchKeyword);
	}
	
//	public List<BoothVo> findBoothDownloadList(){
//		return eventDao.findBoothDownloadList();
//	}
	
	public List<BoothVo> findBoothDownloadList(int event_id, String searchKeyword, String startDate, String endDate){
		return eventDao.findBoothDownloadList(event_id , searchKeyword, startDate, endDate);
	}

}