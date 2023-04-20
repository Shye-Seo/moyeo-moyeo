package com.service.eventus.event;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.service.eventus.aws.AwsS3Service;
import com.service.eventus.master.PagingVo;
import com.service.eventus.member.MemberVo;
import com.service.eventus.resume.ResumeService;
import com.service.eventus.resume.ResumeVo;

import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

@Controller
public class EventController {
	
	@Autowired
	AwsS3Service s3Service;
	  
	@Inject
    private EventService eventService;
	
	@Inject
	private ResumeService resumeService;

	List<EventVo> event_list;
	List<BoothVo> booth_list;
	
	@GetMapping(value="/manage_event")
	public String event_list(@ModelAttribute EventVo eventVo, ModelMap model, @RequestParam(defaultValue = "1") int page, String searchKeyword, String startDate, String endDate, String searchDate) throws Exception{
	    
		// 오늘 날짜
      LocalDate now = LocalDate.now();
      Calendar time = Calendar.getInstance();
      SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmm");
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
      String nowTime = format.format(time.getTime());
      
      Calendar cal = Calendar.getInstance();
      String today = dateFormat.format(cal.getTime());
      
      	// 총 게시물 수 
	    int totalListCnt = eventService.findAllCnt();

	    // 생성인자로  총 게시물 수, 현재 페이지를 전달
	    PagingVo pagination = new PagingVo(totalListCnt, page);

	    // DB select start index
	    int startIndex = pagination.getStartIndex();
	    // 페이지 당 보여지는 게시글의 최대 개수
	    int pageSize = pagination.getPageSize();

	    event_list = eventService.findListPaging(startIndex, pageSize);
	    
	    if(searchKeyword == null && startDate == null && endDate == null) { //키워드&날짜 null (기본상태)
	    	event_list = eventService.findListPaging(startIndex, pageSize);
	    	model.addAttribute("pagination", pagination);
	    	
	    }else if(searchKeyword == null && startDate != null && endDate != null) { //날짜검색, 키워드는 null
	    	totalListCnt = eventService.searchCnt_date(startDate,endDate);
	    	pagination = new PagingVo(totalListCnt, page);
	    	startIndex = pagination.getStartIndex();
	    	pageSize = pagination.getPageSize();
	    	event_list = eventService.event_searchList_date(startDate, endDate, startIndex, pageSize);
	    	model.addAttribute("pagination", pagination);
	    	model.addAttribute("startDate", startDate);
	    	model.addAttribute("endDate", endDate);
	    	
	    }else if(searchKeyword != null && searchDate != null){ 
	    	startDate = searchDate.substring(0, 10);
	    	endDate = searchDate.substring(13, 23);
    		
	    	if(startDate.equals(today) && endDate.equals(today)) { //키워드검색, 날짜null처리
	    		totalListCnt = eventService.searchCnt(searchKeyword);
	    		pagination = new PagingVo(totalListCnt, page);
	    		startIndex = pagination.getStartIndex();
	    		pageSize = pagination.getPageSize();
	    		event_list = eventService.event_searchList(searchKeyword, startIndex, pageSize);
	    		model.addAttribute("pagination", pagination);
	    		model.addAttribute("searchKeyword", searchKeyword);
	    		model.addAttribute("searchDate", searchDate);
	    		model.addAttribute("today", today);
	    		
	    	}else { // 키워드&날짜 동시검색
	    		model.addAttribute("searchDate", searchDate);
	    		totalListCnt = eventService.searchCnt_keydate(startDate, endDate, searchKeyword);
	    		pagination = new PagingVo(totalListCnt, page);
	    		startIndex = pagination.getStartIndex();
	    		pageSize = pagination.getPageSize();
	    		event_list = eventService.event_searchList_keydate(startDate, endDate, searchKeyword, startIndex, pageSize);
	    		model.addAttribute("pagination", pagination);
	    		model.addAttribute("startDate", startDate);
	    		model.addAttribute("endDate", endDate);
	    		model.addAttribute("searchKeyword", searchKeyword);
	    		
	    	}
	    }
	    model.addAttribute("searchDate", searchDate);
	    
	     for(EventVo vo : event_list) {
	    	 String startdate = vo.getEvent_startDate();
	    	 String year = startdate.substring(0, 4);
	    	 String month = startdate.substring(5, 7);
	    	 String day = startdate.substring(8, 10);
	    	 String date_s = year+month+day;
	    	 
	    	 String enddate = vo.getEvent_endDate();
	    	 String end_year = enddate.substring(0, 4);
	    	 String end_month = enddate.substring(5, 7);
	    	 String end_day = enddate.substring(8, 10);
	    	 String date_e = end_year+end_month+end_day;
	    	 
	    	 String deadline = vo.getEvent_deadline();
	    	 String d_year = deadline.substring(0, 4);
	    	 String d_month = deadline.substring(5, 7);
	    	 String d_day = deadline.substring(8, 10);
	    	 String d_hour = deadline.substring(11, 13);
	    	 String d_min = deadline.substring(14, 16);
	    	 String date_d = d_year+d_month+d_day+d_hour+d_min;
	    	 
	    	 LocalDate localdate_start = LocalDate.parse(date_s, DateTimeFormatter.ofPattern("yyyyMMdd"));
	    	 LocalDate localdate_end = LocalDate.parse(date_e, DateTimeFormatter.ofPattern("yyyyMMdd"));
	    	 LocalDate localdate = LocalDate.parse(date_d, DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
	    	 LocalDate localdate_now = LocalDate.parse(nowTime, DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
	    	 
	    	 //compareTo메서드를 통한 날짜비교
	    	 int compare = localdate_start.compareTo(now);
	    	 int compare_end = localdate_end.compareTo(now);
	    	 int compare_deadline = localdate.compareTo(localdate_now);
	    	 
	    	 //진행현황 check
	    	 int check = vo.getEvent_check();
	    	  
	    	 //현재날짜에 따라 event_status set
	    	 if(compare_deadline > 0 && check == 0) { //event_event_deadline > today, event_status:0, event_check:0 (모집중)
//	    	  	   eventService.setEventStatus(vo.getId(), 0);
	    	 }else if((compare >= 0 && check == 1) || (compare > 0 && compare_deadline <= 0)) { //event_startDate > today, event_status:9, event_check:1 (모집완료+진행전) -> 확정버튼 누를 때, status set
		    	   eventService.setEventStatus(vo.getId(), 1);
		     }else if(compare < 0 && compare_end >= 0 && check == 1) { //event_startDate < today < event_endDate, event_status:1, event_check:1 (모집완료+진행중)
		    	   eventService.setEventStatus(vo.getId(), 2);
	    	 }else if(compare_end < 0) { //event_endDate < today, event_status:2 (진행완료)
	    		   eventService.setEventStatus(vo.getId(), 3);
	    	 }
	    	 
	    	 if(vo.getEvent_status() == 0) {
	    		 vo.setApplication_count(eventService.application_count(vo.getId()));
	    	 }else if(vo.getEvent_status() == 1) {
	    		 vo.setApplication_count(eventService.application_count(vo.getId()));
	    	 }else if(vo.getEvent_status() == 2) {
	    		 vo.setStaff_count(eventService.staff_count(vo.getId()));
	    	 }else if(vo.getEvent_status() == 3) {
	    		 vo.setStaff_count(eventService.staff_count(vo.getId()));
	    	 }
	    	 vo.setBooth_count(eventService.booth_count(vo.getId()));
	     }
	     
		 model.addAttribute("event_list", event_list);
		 model.addAttribute("nowpage", page);
	     return "manage_event";
	}
	
	@RequestMapping(value="/manage_event_register", method=RequestMethod.GET)
	public String event_insert(ModelMap model) throws Exception{
		 
	     return "manage_event_register";
	}
	
	//행사상세조회_스테프
	@RequestMapping(value="/eventDetail_forStaff", method=RequestMethod.GET)
	public ModelAndView eventDetail_forStaff(@RequestParam("id") int event_id, HttpSession session) throws Exception{
		ModelAndView mav = new ModelAndView();
		EventVo detailVo = eventService.viewEventDetail(event_id);
		
		List<EventFileVo> eventFileList = eventService.viewEventFileDetail(event_id);
		
		String[] positions =null;
		String[] positions_conut =null;
		String[] position_startTime =null;
		String[] position_endTime =null;
		String[] position_pay =null;
		if(detailVo.getEvent_position() != null) {
			positions = detailVo.getEvent_position().split(",");
			positions_conut = detailVo.getEvent_position_count().split(",");
			position_startTime = detailVo.getEvent_position_startTime().split(",");
			position_endTime = detailVo.getEvent_position_endTime().split(",");
			position_pay = detailVo.getEvent_position_pay().split(",");
		}
		
		//세션의 아이디를 받아온다
		String id = (String) session.getAttribute("user_id");
		MemberVo memberVo = resumeService.viewMember_forResume(id);
		
		//이력서 조회
		ResumeVo resumeVo = resumeService.selectMyResume(memberVo.getId());
		
		int isResume = 0;
		if(resumeVo != null) {
			isResume =resumeVo.getId();
		}
		
		mav.addObject("event", detailVo);
		mav.addObject("positions", positions);
		mav.addObject("positions_conut", positions_conut);
		mav.addObject("position_startTime", position_startTime);
		mav.addObject("position_endTime", position_endTime);
		mav.addObject("position_pay", position_pay);
		mav.addObject("eventFileList", eventFileList);
		mav.addObject("isResume",isResume);
		mav.addObject("staff_id",memberVo.getId());
		mav.setViewName("staff_eventDetail");
		return mav;
	}
	
	//행사상세조회_관리자
	@RequestMapping(value="/eventDetail", method=RequestMethod.GET)
	public ModelAndView eventDetail(@RequestParam("id") int event_id) throws Exception{
		ModelAndView mav = new ModelAndView();
		EventVo detailVo = eventService.viewEventDetail(event_id);
		
		List<EventFileVo> eventFileList = eventService.viewEventFileDetail(event_id);
		
		String[] positions =null;
		String[] positions_conut =null;
		String[] position_startTime =null;
		String[] position_endTime =null;
		String[] position_pay =null;
		if(detailVo.getEvent_position() != null) {
			positions = detailVo.getEvent_position().split(",");
			positions_conut = detailVo.getEvent_position_count().split(",");
			position_startTime = detailVo.getEvent_position_startTime().split(",");
			position_endTime = detailVo.getEvent_position_endTime().split(",");
			position_pay = detailVo.getEvent_position_pay().split(",");
		}
		
		mav.addObject("event", detailVo);
		mav.addObject("positions", positions);
		mav.addObject("positions_conut", positions_conut);
		mav.addObject("position_startTime", position_startTime);
		mav.addObject("position_endTime", position_endTime);
		mav.addObject("position_pay", position_pay);
		mav.addObject("eventFileList", eventFileList);
		mav.setViewName("manage_eventDetail");
		return mav;
	}
	
	//지원자 행사지원
	@ResponseBody
	@RequestMapping(value="/application_event", method=RequestMethod.POST)
	public int application_event (@ModelAttribute ApplicationVo applicationVo) throws Exception {
		
		if(!eventService.isChkApplication(applicationVo)) {
			eventService.insertApplication(applicationVo);
		}else {
			return 1;
		}
		return 0;
	}
	
	//행사등록
	@ResponseBody
	@RequestMapping(value="/eventAdd", method=RequestMethod.POST)
	public String eventAdd(HttpSession session,@ModelAttribute EventVo eventVo, @RequestAttribute List<MultipartFile> event_file) throws Exception{
		
		String user_id = (String) session.getAttribute("user_id");
		eventVo.setUser_id(user_id);
		
		int id =eventService.insertEvent(eventVo);
		
		if(event_file != null) {
			List<String> filenames = s3Service.upload_eventFile(event_file);
			for(String name : filenames) {
				EventFileVo eventFileVo = new EventFileVo();
				eventFileVo.setEvent_id(id);
				eventFileVo.setFile_name(name);
				eventService.insertEventFile(eventFileVo);
			}
		}
		
		return "eventDetail?id="+id;
	}
	
	
	
	//행사 수정 조회
	@RequestMapping(value="/manage_event_update", method=RequestMethod.GET)
	public ModelAndView manage_event_update(@RequestParam("id") int event_id) throws Exception{
		ModelAndView mav = new ModelAndView();
		EventVo detailVo = eventService.viewEventDetail(event_id);
		
		List<EventFileVo> eventFileList = eventService.viewEventFileDetail(event_id);
		
		
		String[] positions =null;
		String[] positions_conut =null;
		String[] position_startTime =null;
		String[] position_endTime =null;
		String[] position_pay =null;
		if(detailVo.getEvent_position() != null) {
			positions = detailVo.getEvent_position().split(",");
			positions_conut = detailVo.getEvent_position_count().split(",");
			position_startTime = detailVo.getEvent_position_startTime().split(",");
			position_endTime = detailVo.getEvent_position_endTime().split(",");
			position_pay = detailVo.getEvent_position_pay().split(",");
		}
		
		
		mav.addObject("event", detailVo);
		mav.addObject("positions", positions);
		mav.addObject("positions_conut", positions_conut);
		mav.addObject("position_startTime", position_startTime);
		mav.addObject("position_endTime", position_endTime);
		mav.addObject("position_pay", position_pay);
		mav.addObject("eventFileList", eventFileList);

		mav.setViewName("manage_event_update");
		return mav;
	}
	
	//행사 수정
	@ResponseBody
	@RequestMapping(value="/eventUpdate", method=RequestMethod.POST)
	public String eventUpdate(MultipartHttpServletRequest multipartRequest, @ModelAttribute EventVo eventVo, @RequestAttribute("event_file") List<MultipartFile> event_file) throws Exception{
			
		//지울 파일 리스트
		String[] deleteFileNameList = multipartRequest.getParameterValues("deleteFileNameList");
		//수정 시 지운파일 삭제
		if(deleteFileNameList != null) {
			for( String name : deleteFileNameList ) {
				s3Service.delete_s3event(name);
				eventService.deleteFile(eventVo.getId(), name);
			}
		}
		//행사 내용 수정
		eventService.updateEvent(eventVo);
			
		//수정 시 추가한 파일 추가
		if(event_file != null) {
			List<String> filenames = s3Service.upload_eventFile(event_file);
			for(String name : filenames) {
				EventFileVo eventFileVo = new EventFileVo();
				eventFileVo.setEvent_id(eventVo.getId());
				eventFileVo.setFile_name(name);
				eventService.insertEventFile(eventFileVo);
			}
		}
			
		return "eventDetail?id="+eventVo.getId();
	}
	
	
	//지원현황(모집중) 모달창
	@ResponseBody
	@RequestMapping(value="/get_application_list", method=RequestMethod.GET)
	public Map get_application_list(@RequestParam("id") int event_id) throws Exception{
		
		Map applicationMap = new HashMap<>();
		applicationMap.put("event_id", event_id);
		
		List<MemberVo> application_list = eventService.application_list(event_id);
		if (application_list != null) {
			for (MemberVo memberVo : application_list) {
				//경력 count
				int staff_career = eventService.staff_career(memberVo.getId(), event_id);
				memberVo.setCareer_count(staff_career);
				
				//주소 set
				String user_address = eventService.getAddress(event_id, memberVo.getId());
				memberVo.setStaff_address(user_address); 
				
				//만 나이 계산
				String staff_age = eventService.getUserAge(memberVo.getUser_birth());
				memberVo.setStaff_age(staff_age);
				
				//휴대폰번호 형태
				String regEx = "(\\d{3})(\\d{3,4})(\\d{4})";
				String staff_phone = memberVo.getUser_phone().replaceAll(regEx, "$1-$2-$3");
				memberVo.setStaff_phone(staff_phone);
				
				//프로필이미지
				String resumeProfile = resumeService.selectProfile(memberVo.getResume_id());
				memberVo.setResume_profile(resumeProfile);
			}
		}
		List<String> position_list = eventService.application_position_count(event_id);
		
		applicationMap.put("application_list", application_list);
		applicationMap.put("position_list", position_list);
		
		return applicationMap;
	}
	
	
	
	//지원자 임시 합불합 상태 저장 + 지원자 합불합 등록
	@ResponseBody
	@RequestMapping(value="/set_application_status", method=RequestMethod.POST)
	public String set_application_status(@RequestParam("status") int status, @RequestParam("staff_id") int staff_id, @RequestParam("event_id") int event_id) throws Exception {
		
		eventService.updateStaffResult(status, event_id, staff_id);
		
		if(status == 1) {
			Map info = eventService.insertOnePasser(event_id, staff_id);
			//문자보내기
			eventService.sendSms_forPasser((String)info.get("user_phone"), (String)info.get("user_name"), (String)info.get("event_title"), (String)info.get("staff_position"));
		}
		return staff_id+"";
	}
	
	//모집종료
	@ResponseBody
	@RequestMapping(value="/set_application", method=RequestMethod.POST)
	public int set_application (@RequestParam("event_id") int event_id) throws Exception{
		
		return eventService.end_hire(event_id);
	}
	
	
	// 지원현황(진행중) 모달창
	@ResponseBody
	@RequestMapping(value="/get_workStaff_list", method=RequestMethod.GET)
	public Map get_workStaff_list(@RequestParam("id") int event_id) throws Exception{
		
		Map workRecordMap = new HashMap<>();
		
		workRecordMap.put("event_id", event_id);
		
		// 오늘 날짜
		LocalDate now = LocalDate.now();
		int year = now.getYear();
		int month = now.getMonthValue();
		int day = now.getDayOfMonth();
		String today = year + "년 " + month + "월 " + day + "일";
		
		workRecordMap.put("today", today);
	
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
	    Calendar cal = Calendar.getInstance();
	    String work_date = dateFormat.format(cal.getTime());
	    
		workRecordMap.put("work_date", work_date);
		
		List<MemberVo> workStaff_list = eventService.workStaff_list(event_id);
		if (workStaff_list != null) {
			for (MemberVo memberVo : workStaff_list) {
				
				//만 나이 계산
				String staff_age = eventService.getUserAge(memberVo.getUser_birth());
				memberVo.setStaff_age(staff_age);
				
				//휴대폰번호 형태
				String regEx = "(\\d{3})(\\d{3,4})(\\d{4})";
				String staff_phone = memberVo.getUser_phone().replaceAll(regEx, "$1-$2-$3");
				memberVo.setStaff_phone(staff_phone);
				
				//프로필이미지
				String resumeProfile = resumeService.selectProfile(memberVo.getResume_id());
				memberVo.setResume_profile(resumeProfile);
				
				//당일 근무기록
				List<WorkRecordVo> workTime_list = eventService.getWorkTime(memberVo.getId(), event_id, work_date);
				if(workTime_list != null) {
					for(WorkRecordVo recordVo : workTime_list) {
						memberVo.setRecordVo(recordVo);
					}
				}
			}
		}
		workRecordMap.put("workStaff_list", workStaff_list);
		return workRecordMap;
	}
	
	// 근무기록(출근)
	@ResponseBody
	@RequestMapping(value="/record_start", method=RequestMethod.POST)
	public String record_startTime(@RequestParam("staff_id") int staff_id, @RequestParam("staff_name") String staff_name, @RequestParam("event_id") int event_id, 
			@RequestParam("event_title") String event_title, @RequestParam("work_date") String work_date, @RequestParam("record_id") int record_id, ModelMap model) throws Exception{
		
		ZoneId zoneId = ZoneId.of("Asia/Seoul");
		LocalDateTime localDateTime = LocalDateTime.now();
		ZonedDateTime zdt = localDateTime.atZone( zoneId ) ;
		
		// 현재 시간
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH : mm");
		String start_time = zdt.format(formatter);
		
		eventService.record_startTime(record_id, event_id, staff_id, work_date, start_time);
		eventService.insert_work_log(staff_id, staff_name, event_id, event_title, start_time, 1);
		return start_time;
	}
	
	// 근무기록(외출)
	@ResponseBody
	@RequestMapping(value="/record_out", method=RequestMethod.POST)
	public String record_outTime(@RequestParam("staff_id") int staff_id, @RequestParam("staff_name") String staff_name, @RequestParam("event_id") int event_id, 
		@RequestParam("event_title") String event_title, @RequestParam("work_date") String work_date, @RequestParam("record_id") int record_id, ModelMap model) throws Exception{
		
		ZoneId zoneId = ZoneId.of("Asia/Seoul");
		LocalDateTime localDateTime = LocalDateTime.now();
		ZonedDateTime zdt = localDateTime.atZone( zoneId ) ;
		
		// 현재 시간
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH : mm");
        String out_time = zdt.format(formatter);
        
		eventService.record_outTime(record_id, event_id, staff_id, work_date, out_time);
		eventService.insert_work_log(staff_id, staff_name, event_id, event_title, out_time, 2);
		return out_time;
	}
	
	// 근무기록(복귀)
	@ResponseBody
	@RequestMapping(value="/record_back", method=RequestMethod.POST)
	public String record_backTime(@RequestParam("staff_id") int staff_id, @RequestParam("staff_name") String staff_name, @RequestParam("event_id") int event_id, 
			@RequestParam("event_title") String event_title, @RequestParam("work_date") String work_date, @RequestParam("record_id") int record_id, ModelMap model) throws Exception{
		
		ZoneId zoneId = ZoneId.of("Asia/Seoul");
		LocalDateTime localDateTime = LocalDateTime.now();
		ZonedDateTime zdt = localDateTime.atZone( zoneId ) ;
		
		// 현재 시간
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH : mm");
        String back_time = zdt.format(formatter);
        
		eventService.record_backTime(record_id, event_id, staff_id, work_date, back_time);
		eventService.insert_work_log(staff_id, staff_name, event_id, event_title, back_time, 3);
		return back_time;
	}
	
	// 근무기록(퇴근)
	@ResponseBody
	@RequestMapping(value="/record_end", method=RequestMethod.POST)
	public String record_endTime(@RequestParam("staff_id") int staff_id, @RequestParam("staff_name") String staff_name, @RequestParam("event_id") int event_id, 
			@RequestParam("event_title") String event_title, @RequestParam("work_date") String work_date, @RequestParam("record_id") int record_id, ModelMap model) throws Exception{
		
		ZoneId zoneId = ZoneId.of("Asia/Seoul");
		LocalDateTime localDateTime = LocalDateTime.now();
		ZonedDateTime zdt = localDateTime.atZone( zoneId ) ;
		
		// 현재 시간
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH : mm");
        String end_time = zdt.format(formatter);
        
		eventService.record_endTime(record_id, event_id, staff_id, work_date, end_time);
		eventService.insert_work_log(staff_id, staff_name, event_id, event_title, end_time, 4);
		return end_time;
	}
	
	// 당일 근무기록 없을 때,
	// 근무기록(출근)
	@ResponseBody
	@RequestMapping(value="/record_start_new", method=RequestMethod.POST)
	public String record_startTime_new(@RequestParam("staff_id") int staff_id, @RequestParam("staff_name") String staff_name, @RequestParam("event_id") int event_id, 
			@RequestParam("event_title") String event_title, @RequestParam("work_date") String work_date) throws Exception{
		
		ZoneId zoneId = ZoneId.of("Asia/Seoul");
		LocalDateTime localDateTime = LocalDateTime.now();
		ZonedDateTime zdt = localDateTime.atZone( zoneId ) ;
		
		// 현재 시간
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH : mm");
		String start_time = zdt.format(formatter);
		
		eventService.record_startTime_new(event_id, staff_id, work_date, start_time);
		eventService.insert_work_log(staff_id, staff_name, event_id, event_title, start_time, 1);
		return start_time;
	}
	
	// 근무기록(외출)
	@ResponseBody
	@RequestMapping(value="/record_out_new", method=RequestMethod.POST)
	public String record_outTime_new(@RequestParam("staff_id") int staff_id, @RequestParam("staff_name") String staff_name, @RequestParam("event_id") int event_id, 
			@RequestParam("event_title") String event_title, @RequestParam("work_date") String work_date) throws Exception{
		
		ZoneId zoneId = ZoneId.of("Asia/Seoul");
		LocalDateTime localDateTime = LocalDateTime.now();
		ZonedDateTime zdt = localDateTime.atZone( zoneId ) ;
		
		// 현재 시간
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH : mm");
        String out_time = zdt.format(formatter);
        
		eventService.record_outTime_new(event_id, staff_id, work_date, out_time);
		eventService.insert_work_log(staff_id, staff_name, event_id, event_title, out_time, 2);
		return out_time;
	}
	
	// 근무기록(복귀)
	@ResponseBody
	@RequestMapping(value="/record_back_new", method=RequestMethod.POST)
	public String record_backTime_new(@RequestParam("staff_id") int staff_id, @RequestParam("staff_name") String staff_name, @RequestParam("event_id") int event_id, 
			@RequestParam("event_title") String event_title, @RequestParam("work_date") String work_date) throws Exception{
		
		ZoneId zoneId = ZoneId.of("Asia/Seoul");
		LocalDateTime localDateTime = LocalDateTime.now();
		ZonedDateTime zdt = localDateTime.atZone( zoneId ) ;
		
		// 현재 시간
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH : mm");
        String back_time = zdt.format(formatter);
        
		eventService.record_backTime_new(event_id, staff_id, work_date, back_time);
		eventService.insert_work_log(staff_id, staff_name, event_id, event_title, back_time, 3);
		return back_time;
	}
	
	// 근무기록(퇴근)
	@ResponseBody
	@RequestMapping(value="/record_end_new", method=RequestMethod.POST)
	public String record_endTime_new(@RequestParam("staff_id") int staff_id, @RequestParam("staff_name") String staff_name, @RequestParam("event_id") int event_id, 
			@RequestParam("event_title") String event_title, @RequestParam("work_date") String work_date) throws Exception{
		
		ZoneId zoneId = ZoneId.of("Asia/Seoul");
		LocalDateTime localDateTime = LocalDateTime.now();
		ZonedDateTime zdt = localDateTime.atZone( zoneId ) ;
		
		// 현재 시간
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH : mm");
        String end_time = zdt.format(formatter);
        
		eventService.record_endTime_new(event_id, staff_id, work_date, end_time);
		eventService.insert_work_log(staff_id, staff_name, event_id, event_title, end_time, 4);
		return end_time;
	}
	
	// 부스현황 리스트
	@GetMapping(value="/manage_event_booth")
	public String event_booth_list(@RequestParam("id") int event_id, ModelMap model, @RequestParam(defaultValue = "1") int page, String searchKeyword) throws Exception{
		
		model.addAttribute("event_id", event_id);
		String Title = eventService.getTitle(event_id);
		model.addAttribute("Title", Title);
		
		// 총 게시물 수 
	    int totalListCnt = eventService.booth_list_AllCnt(event_id);

	    // 생성인자로  총 게시물 수, 현재 페이지를 전달
	    PagingVo pagination = new PagingVo(totalListCnt, page);

	    // DB select start index
	    int startIndex = pagination.getStartIndex();
	    // 페이지 당 보여지는 게시글의 최대 개수
	    int pageSize = pagination.getPageSize();
	    
	    booth_list = eventService.booth_list_paging(event_id, startIndex, pageSize);
	    
	    if(searchKeyword == null) { //키워드 null (기본상태)
	    	booth_list = eventService.booth_list_paging(event_id, startIndex, pageSize);
	    	model.addAttribute("pagination", pagination);
	    	
	    }else if(searchKeyword != null){ //키워드 검색 시,
	    	totalListCnt = eventService.booth_searchCnt(event_id, searchKeyword);
	    	pagination = new PagingVo(totalListCnt, page);
	    	startIndex = pagination.getStartIndex();
	    	pageSize = pagination.getPageSize();
	    	booth_list = eventService.booth_searchList(event_id, searchKeyword, startIndex, pageSize);
	    	model.addAttribute("pagination", pagination);
	    	model.addAttribute("searchKeyword", searchKeyword);
	    }
	    
		if (booth_list != null) {
			for (BoothVo vo : booth_list) {
				String event_title = eventService.getEventTitle(vo.getId());
				vo.setEvent_title(event_title);
				
				String startDate = eventService.getStartDate(vo.getId());
				vo.setEvent_startDate(startDate);
				
				String endDate = eventService.getEndDate(vo.getId());
				vo.setEvent_endDate(endDate);
			}
		}
		
		int booth_num = booth_list.size();
		model.addAttribute("booth_list", booth_list);
		model.addAttribute("booth_num", booth_num);
		model.addAttribute("nowpage", page);
	    return "manage_event_booth";
	}
	
	//부스등록
	@ResponseBody
	@RequestMapping(value="/register_booth", method=RequestMethod.POST)
	public String register_booth(@RequestParam("event_id") int event_id, @RequestParam("booth_name") String booth_name, 
								 @RequestParam("counting") String counting, @RequestParam("expected_time") String expected_time) throws Exception{
		
		eventService.register_booth(event_id, booth_name, counting, expected_time);
			
		return "manage_event_booth?id="+event_id;
	}
	
	//부스수정
	@ResponseBody
	@RequestMapping(value="/modify_booth", method=RequestMethod.POST)
	public String modify_booth(@RequestParam("event_id") int event_id, @RequestParam("booth_id") int booth_id,  @RequestParam("booth_name") String booth_name, 
							   @RequestParam("counting") String counting, @RequestParam("expected_time") String expected_time) throws Exception{
			
		eventService.modify_booth(booth_id, booth_name, counting, expected_time);
				
		return "manage_event_booth?id="+event_id;
	}
	
	//부스삭제
	@ResponseBody
	@RequestMapping(value="/delete_booth", method=RequestMethod.POST)
	public String delete_booth(@RequestParam("event_id") int event_id, @RequestParam("booth_id") int booth_id) throws Exception{
				
		eventService.delete_booth(booth_id);
					
		return "manage_event_booth?id="+event_id;
	}
	
	//사용자 페이지_행사목록
	@GetMapping(value="/eventList_ForStaff")
	public String event_list_staff(@ModelAttribute EventVo eventVo, ModelMap model, @RequestParam(defaultValue = "1") int page, String searchKeyword, String startDate, String endDate, String searchDate) throws Exception{
		
		// 오늘 날짜
        LocalDate now = LocalDate.now();
        Calendar time = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmm");
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        String nowTime = format.format(time.getTime());
        
        Calendar cal = Calendar.getInstance();
	    String today = dateFormat.format(cal.getTime());
        // 총 게시물 수 
	    int totalListCnt = eventService.findAllCnt();

	    // 생성인자로  총 게시물 수, 현재 페이지를 전달
	    PagingVo pagination = new PagingVo(totalListCnt, page);

	    // DB select start index
	    int startIndex = pagination.getStartIndex();
	    // 페이지 당 보여지는 게시글의 최대 개수
	    int pageSize = pagination.getPageSize();

	    event_list = eventService.findListPaging(startIndex, pageSize);
	    
	    if(searchKeyword == null && startDate == null && endDate == null) { //키워드&날짜 null (기본상태)
	    	event_list = eventService.findListPaging(startIndex, pageSize);
	    	model.addAttribute("pagination", pagination);
	    	
	    }else if(searchKeyword == null && startDate != null && endDate != null) { //날짜검색, 키워드는 null
	    	totalListCnt = eventService.searchCnt_date(startDate,endDate);
	    	pagination = new PagingVo(totalListCnt, page);
	    	startIndex = pagination.getStartIndex();
	    	pageSize = pagination.getPageSize();
	    	event_list = eventService.event_searchList_date(startDate, endDate, startIndex, pageSize);
	    	model.addAttribute("pagination", pagination);
	    	model.addAttribute("startDate", startDate);
	    	model.addAttribute("endDate", endDate);
	    	
	    }else if(searchKeyword != null && searchDate != null){ 
	    	startDate = searchDate.substring(0, 10);
	    	endDate = searchDate.substring(13, 23);
    		
	    	if(startDate.equals(today) && endDate.equals(today)) { //키워드검색, 날짜null처리
	    		totalListCnt = eventService.searchCnt(searchKeyword);
	    		pagination = new PagingVo(totalListCnt, page);
	    		startIndex = pagination.getStartIndex();
	    		pageSize = pagination.getPageSize();
	    		event_list = eventService.event_searchList(searchKeyword, startIndex, pageSize);
	    		model.addAttribute("pagination", pagination);
	    		model.addAttribute("searchKeyword", searchKeyword);
	    		model.addAttribute("searchDate", searchDate);
	    		model.addAttribute("today", today);
	    		
	    	}else { // 키워드&날짜 동시검색
	    		model.addAttribute("searchDate", searchDate);
	    		totalListCnt = eventService.searchCnt_keydate(startDate, endDate, searchKeyword);
	    		pagination = new PagingVo(totalListCnt, page);
	    		startIndex = pagination.getStartIndex();
	    		pageSize = pagination.getPageSize();
	    		event_list = eventService.event_searchList_keydate(startDate, endDate, searchKeyword, startIndex, pageSize);
	    		model.addAttribute("pagination", pagination);
	    		model.addAttribute("startDate", startDate);
	    		model.addAttribute("endDate", endDate);
	    		model.addAttribute("searchKeyword", searchKeyword);
	    		
	    	}
	    }
	    model.addAttribute("searchDate", searchDate);
		 
	     for(EventVo vo : event_list) {
	    	 String startdate = vo.getEvent_startDate();
	    	 String year = startdate.substring(0, 4);
	    	 String month = startdate.substring(5, 7);
	    	 String day = startdate.substring(8, 10);
	    	 String date_s = year+month+day;
	    	 
	    	 String enddate = vo.getEvent_endDate();
	    	 String end_year = enddate.substring(0, 4);
	    	 String end_month = enddate.substring(5, 7);
	    	 String end_day = enddate.substring(8, 10);
	    	 String date_e = end_year+end_month+end_day;
	    	 
	    	 String deadline = vo.getEvent_deadline();
	    	 String d_year = deadline.substring(0, 4);
	    	 String d_month = deadline.substring(5, 7);
	    	 String d_day = deadline.substring(8, 10);
	    	 String d_hour = deadline.substring(11, 13);
	    	 String d_min = deadline.substring(14, 16);
	    	 String date_d = d_year+d_month+d_day+d_hour+d_min;
	    	 
	    	 LocalDate localdate_start = LocalDate.parse(date_s, DateTimeFormatter.ofPattern("yyyyMMdd"));
	    	 LocalDate localdate_end = LocalDate.parse(date_e, DateTimeFormatter.ofPattern("yyyyMMdd"));
	    	 LocalDate localdate = LocalDate.parse(date_d, DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
	    	 LocalDate localdate_now = LocalDate.parse(nowTime, DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
	    	 
	    	 //compareTo메서드를 통한 날짜비교
	    	 int compare = localdate_start.compareTo(now);
	    	 int compare_end = localdate_end.compareTo(now);
	    	 int compare_deadline = localdate.compareTo(localdate_now);
	    	 
	    	 //진행현황 check
	    	 int check = vo.getEvent_check();
	    	  
	    	 //현재날짜에 따라 event_status set
	    	 if(compare_deadline > 0 && check == 0) { //event_event_deadline > today, event_status:0, event_check:0 (모집중)
	    	  	   eventService.setEventStatus(vo.getId(), 0);
	    	 }else if(compare > 0 && check == 1) { //event_startDate > today, event_status:1, event_check:1 (모집완료+진행전) -> 확정버튼 누를 때, status set
		    	   eventService.setEventStatus(vo.getId(), 1);
		     }else if(compare <= 0 && compare_end >= 0 && check == 1) { //event_startDate < today < event_endDate, event_status:2, event_check:1 (모집완료+진행중)
		    	   eventService.setEventStatus(vo.getId(), 2);
	    	 }else if(compare_end < 0) { //event_endDate < today, event_status:3 (진행완료)
	    		   eventService.setEventStatus(vo.getId(), 3);
	    	 }
	     }
	     
	     int event_num = event_list.size();
	     model.addAttribute("event_num", event_num);
	     model.addAttribute("event_list", event_list);
	     model.addAttribute("nowpage", page);
	     return "eventList_ForStaff";
	}
	
	//이력서 form
	@GetMapping(value="/resume_file")
	public String resume_file(ModelMap model) throws Exception{
		return "resume_file";
	}
	
	//행사 파일 다운로드
	@RequestMapping({"/event_download"})
	@ResponseBody
	public ResponseEntity<byte[]> download(@RequestParam String filename) throws IOException {
		return s3Service.getObject_event(filename);
	}
	
	//이력서 프로필 이미지
	@RequestMapping({"/profile_download"})
	@ResponseBody
	public ResponseEntity<byte[]> profile_download(@RequestParam String filename) throws IOException {
		if(filename.isEmpty() || filename.length()==0 || filename==null) {
			return null;
		}
		return s3Service.getObject_profile(filename);
	}
	
	//이력서 모달창
	@ResponseBody
	@RequestMapping(value="/get_resume_file", method=RequestMethod.GET)
	public Map get_resume_file(@RequestParam("id") int resume_id) throws Exception{
		
		Map resumeMap = new HashMap<>();
		resumeMap.put("resume_id", resume_id);
		System.out.println("resume_id : "+resume_id);
		
		int staff_id = resumeService.selectStaffId(resume_id);
		resumeMap.put("staff_id", staff_id);
		
		MemberVo staff_info = resumeService.getStaffInfo(staff_id);
		ResumeVo staff_resume = resumeService.getStaffResume(resume_id);
		
		//프로필이미지
		String resumeProfile = resumeService.selectProfile(resume_id);
		staff_info.setResume_profile(resumeProfile);
		
		//만 나이 계산
		String staff_age = eventService.getUserAge(staff_info.getUser_birth());
		staff_info.setStaff_age(staff_age);
		
		//휴대폰번호 형태
		String regEx = "(\\d{3})(\\d{3,4})(\\d{4})";
		String staff_phone = staff_info.getUser_phone().replaceAll(regEx, "$1-$2-$3");
		staff_info.setStaff_phone(staff_phone);
		
		//생년월일 형태
		String staff_birth = staff_info.getUser_birth();
		String birth[] = staff_birth.split("-");
		String birth_replace = birth[0]+"."+birth[1]+"."+birth[2];
		staff_info.setUser_birth(birth_replace);
		
		//학교처리..
		String[] nullArray = {"",""} ;
				
		Map<String, String[]> school_info = new HashMap<>();
		school_info.put("school", nullArray);
		school_info.put("major", nullArray);
		school_info.put("start", nullArray);
		school_info.put("end", nullArray);
		school_info.put("state", nullArray);
				
		if(staff_resume.getStaff_school() != null) {
			if(staff_resume.getStaff_school().split(",").length <2) {
				String[] school = {staff_resume.getStaff_school(), ""};
				school_info.put("school", school);
			}else {
				school_info.put("school", staff_resume.getStaff_school().split(","));
			}
			if(staff_resume.getStaff_major().split(",").length <2) {
				String[] major = {staff_resume.getStaff_major(), ""};
				school_info.put("major", major);
			}else {
				school_info.put("major", staff_resume.getStaff_major().split(","));
			}
			if(staff_resume.getStaff_school_start().split(",").length <2) {
				String[] start = {staff_resume.getStaff_school_start(), ""};
				school_info.put("start", start);
			}else {
				school_info.put("start", staff_resume.getStaff_school_start().split(","));
			}
			if(staff_resume.getStaff_school_end().split(",").length <2) {
				String[] end = {staff_resume.getStaff_school_end(), ""};
				school_info.put("end", end);
			}else {
				school_info.put("end", staff_resume.getStaff_school_end().split(","));
			}
			if(staff_resume.getStaff_major().split(",").length <2) {
				String[] state = {staff_resume.getStaff_school_state(), ""};
				school_info.put("state", state);
			}else {
				school_info.put("state", staff_resume.getStaff_school_state().split(","));
			}
		}
		
		//행사처리
		Map<String, String[]> career_info = new HashMap<>();
				
		if(staff_resume.getStaff_career_eventName() != null) {
			career_info.put("eventName", staff_resume.getStaff_career_eventName().split(","));
			career_info.put("businessName",staff_resume.getStaff_career_businessName().split(","));
			career_info.put("positions",staff_resume.getStaff_career_position().split(","));
			career_info.put("workday",staff_resume.getStaff_career_workday().split(","));
		}

		resumeMap.put("staff_info", staff_info);
		resumeMap.put("staff_resume", staff_resume);
		resumeMap.put("school_info", school_info);
		resumeMap.put("career_info", career_info);
		
		return resumeMap;
	}


	// 행사관리 엑셀 다운로드
	@RequestMapping(value="/event_excel", method= RequestMethod.GET)
	@ResponseBody
	public void event_excel(HttpServletResponse response, String searchKeyword, String startDate, String endDate, String searchDate) throws IOException {
		
		System.out.println(searchKeyword);
		System.out.println(startDate);
		System.out.println(endDate);
		System.out.println(searchDate);
		if(searchKeyword == "") {searchKeyword = null;}

		Workbook workbook = new XSSFWorkbook();
		
	//      엑셀스타일
      CellStyle style = workbook.createCellStyle();
      style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
      style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
      Font Bold = workbook.createFont();
      Bold.setBold(true);
      style.setFont(Bold);
	//      
		Sheet sheet = workbook.createSheet("event_list");
		Row row;
		Cell cell;
		int rowNo = 0;
		int i=1;

		// 헤더 정보 구성
		row = sheet.createRow(rowNo++);
		cell = row.createCell(0);
		cell.setCellValue("No");
        cell.setCellStyle(style);
		cell = row.createCell(1);
		cell.setCellValue("행사명");
        cell.setCellStyle(style);
		cell = row.createCell(2);
		cell.setCellValue("행사기간");
        cell.setCellStyle(style);
		cell = row.createCell(3);
		cell.setCellValue("진행현황");
        cell.setCellStyle(style);
		cell = row.createCell(4);
		cell.setCellValue("지원현황");
        cell.setCellStyle(style);
		cell = row.createCell(5);
		cell.setCellValue("부스현황");
        cell.setCellStyle(style);


        //열 길이 바꾸기
        for(int a =0; a<8;a++) {
        	sheet.autoSizeColumn(a);
        	if(a==0) {
        		sheet.setColumnWidth(a, (sheet.getColumnWidth(a))+(short)1000);
        	}else if(a==1) {
        		sheet.setColumnWidth(a, (sheet.getColumnWidth(a))+(short)7000);
        	}else if(a==2) {
        		sheet.setColumnWidth(a, (sheet.getColumnWidth(a))+(short)5000);
        	}else {
        		sheet.setColumnWidth(a, (sheet.getColumnWidth(a))+(short)3000);
        	}
        	
        }
        
		// 오늘 날짜 (오늘-오늘검색일 시)
	      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
	      Calendar cal = Calendar.getInstance();
	      String today = dateFormat.format(cal.getTime());
	      if(searchDate!=null && searchDate.equals(today+" - "+today)) {
	    	  searchDate=null;
	      }
	      
	   // 엑셀 파일명
		String filename = today+"_event_excel.xlsx";
	      
		 event_list = eventService.findDownloadList();
		
		 if(searchKeyword == null && searchDate == null) { //키워드&날짜 null (기본상태)
		    	event_list = eventService.findDownloadList();
//		    	System.out.println("기본");
		    	
		 }else if(searchKeyword == null && searchDate != null) { //날짜검색, 키워드는 null
			 	startDate = searchDate.substring(0, 10);
		    	endDate = searchDate.substring(13, 23);
		    	
		    	event_list = eventService.event_Downloaddate(startDate, endDate);
//		    	System.out.println("날짜");
		    	filename = today+"_event_excel_"+startDate+"_"+endDate+".xlsx";
		    	
		 }else if(searchKeyword != null && searchDate == null) { //키워드검색, 날짜null처리
//			 System.out.println("키워드");
	    		event_list = eventService.event_Downloadkey(searchKeyword);
	    		filename = new String((today+"_staff_excel_"+searchKeyword+".xlsx").getBytes("UTF-8"),"ISO-8859-1");
	    		
		 }else if(searchKeyword != null && searchDate != null){ // 키워드&날짜 동시검색
		    	startDate = searchDate.substring(0, 10);
		    	endDate = searchDate.substring(13, 23);
		    	
		    	event_list = eventService.event_Downloadkeydate(startDate, endDate, searchKeyword);
//		    	System.out.println("동시");
		    	filename =  new String((today+"_staff_excel_"+startDate+"_"+endDate+"_"+searchKeyword+".xlsx").getBytes("UTF-8"),"ISO-8859-1");
		 }
		
		for(EventVo event : event_list) {
			row = sheet.createRow(rowNo++);
			cell = row.createCell(0);
			cell.setCellValue(i++);
			cell = row.createCell(1);
			cell.setCellValue(event.getEvent_title());
			cell = row.createCell(2);
			cell.setCellValue(event.getEvent_startDate() + " - " + event.getEvent_endDate());
			cell = row.createCell(3);
			if(event.getEvent_status()==0 || event.getEvent_status()==1) {

				if(event.getEvent_status()==0) {
					cell.setCellValue("모집중");
				}
				else {
					cell.setCellValue("모집완료");
				}
				cell = row.createCell(4);
				cell.setCellValue(event.getApplication_count());
			}
			else if(event.getEvent_status()==2 || event.getEvent_status()==3) {

				if(event.getEvent_status()==2) {
					cell.setCellValue("진행중");
				} else {
					cell.setCellValue("진행완료");
				}
				cell = row.createCell(4);
				cell.setCellValue(event.getStaff_count());
			}
			cell = row.createCell(5);
			cell.setCellValue(event.getBooth_count());
		}

		// 컨텐트 타입과 파일명 지정
		response.setContentType("ms-vnd/excel");
		response.setHeader("Content-Disposition", "attachment;filename="+filename);

		// 엑셀 출력
		workbook.write(response.getOutputStream());
		workbook.close();
	}
	
	// 행사관리 부스 다운로드
		@RequestMapping(value="/booth_excel", method= RequestMethod.GET)
		@ResponseBody
		public void booth_excel(HttpServletResponse response, String id ,String searchKeyword, String startDate, String endDate, String searchDate) throws IOException {
			
			System.out.println(searchKeyword);
			System.out.println(startDate);
			System.out.println(endDate);
			System.out.println(searchDate);
			if(searchKeyword == "") {searchKeyword = null;}

			Workbook workbook = new XSSFWorkbook();
			
		//      엑셀스타일
	      CellStyle style = workbook.createCellStyle();
	      style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
	      style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	      Font Bold = workbook.createFont();
	      Bold.setBold(true);
	      style.setFont(Bold);
		//      
			Sheet sheet = workbook.createSheet("booth_list");
			Row row;
			Cell cell;
			int rowNo = 0;
			int i=1;

			// 헤더 정보 구성
			row = sheet.createRow(rowNo++);
			cell = row.createCell(0);
			cell.setCellValue("No");
	        cell.setCellStyle(style);
			cell = row.createCell(1);
			cell.setCellValue("행사명");
	        cell.setCellStyle(style);
			cell = row.createCell(2);
			cell.setCellValue("행사기간");
	        cell.setCellStyle(style);
			cell = row.createCell(3);
			cell.setCellValue("부스명");
	        cell.setCellStyle(style);
			cell = row.createCell(4);
			cell.setCellValue("카운팅");
	        cell.setCellStyle(style);
			cell = row.createCell(5);
			cell.setCellValue("소요시간");
	        cell.setCellStyle(style);


	        //열 길이 바꾸기
	        for(int a =0; a<8;a++) {
	        	sheet.autoSizeColumn(a);
	        	if(a==0) {
	        		sheet.setColumnWidth(a, (sheet.getColumnWidth(a))+(short)1000);
	        	}else if(a==1||a==3) {
	        		sheet.setColumnWidth(a, (sheet.getColumnWidth(a))+(short)7000);
	        	}else if(a==2) {
	        		sheet.setColumnWidth(a, (sheet.getColumnWidth(a))+(short)5000);
	        	}else {
	        		sheet.setColumnWidth(a, (sheet.getColumnWidth(a))+(short)1500);
	        	}
	        	
	        }
	        
			// 오늘 날짜 (오늘-오늘검색일 시)
		      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
		      Calendar cal = Calendar.getInstance();
		      String today = dateFormat.format(cal.getTime());
		      if(searchDate!=null && searchDate.equals(today+" - "+today)) {
		    	  searchDate=null;
		      }else if(searchDate!=null) {
		    	  startDate = searchDate.substring(0, 10);
		    	  endDate = searchDate.substring(13, 23);
		      }
		   // 엑셀 파일명
			String filename = today+"_booth_excel.xlsx";
		      
		      booth_list = eventService.findBoothDownloadList(Integer.parseInt(id) ,searchKeyword, startDate, endDate);
			
			 if(searchKeyword == null && searchDate == null) { //키워드&날짜 null (기본상태)
//			    	event_list = eventService.findDownloadList();
//			    	System.out.println("기본");
			    	
			 }else if(searchKeyword == null && searchDate != null) { //날짜검색, 키워드는 null
//				 	startDate = searchDate.substring(0, 10);
//			    	endDate = searchDate.substring(13, 23);
//			    	
//			    	event_list = eventService.event_Downloaddate(startDate, endDate);
//			    	System.out.println("날짜");
			    	filename = today+"_booth_excel_"+startDate+"_"+endDate+".xlsx";
			    	
			 }else if(searchKeyword != null && searchDate == null) { //키워드검색, 날짜null처리
//				 System.out.println("키워드");
//		    		event_list = eventService.event_Downloadkey(searchKeyword);
		    		filename = new String((today+"_booth_excel_"+searchKeyword+".xlsx").getBytes("UTF-8"),"ISO-8859-1");
		    		
			 }else if(searchKeyword != null && searchDate != null){ // 키워드&날짜 동시검색
//			    	startDate = searchDate.substring(0, 10);
//			    	endDate = searchDate.substring(13, 23);
//			    	
//			    	event_list = eventService.event_Downloadkeydate(startDate, endDate, searchKeyword);
//			    	System.out.println("동시");
			    	filename =  new String((today+"_booth_excel_"+startDate+"_"+endDate+"_"+searchKeyword+".xlsx").getBytes("UTF-8"),"ISO-8859-1");
			 }
			
			for(BoothVo booth : booth_list) {
				row = sheet.createRow(rowNo++);
				cell = row.createCell(0);
				cell.setCellValue(i++);
				cell = row.createCell(1);
				cell.setCellValue(booth.getEvent_title());
				cell = row.createCell(2);
				cell.setCellValue(booth.getEvent_startDate() + " - " + booth.getEvent_endDate());
				cell = row.createCell(3);
				cell.setCellValue(booth.getBooth_name());
				cell = row.createCell(4);
				cell.setCellValue(booth.getCounting());
				cell = row.createCell(5);
				cell.setCellValue(booth.getExpected_time());
			}

			// 컨텐트 타입과 파일명 지정
			response.setContentType("ms-vnd/excel");
			response.setHeader("Content-Disposition", "attachment;filename="+filename);

			// 엑셀 출력
			workbook.write(response.getOutputStream());
			workbook.close();
		}
}
