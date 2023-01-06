package com.service.eventus.event;

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
import com.service.eventus.member.MemberVo;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
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
	
	@GetMapping(value="/manage_event")
	public String event_list(@ModelAttribute EventVo eventVo, ModelMap model) throws Exception{
		 List<EventVo> event_list = eventService.event_list();
	     model.addAttribute("event_list", event_list);
	     for(EventVo vo : event_list) {
	    	 if(vo.getEvent_status() == 0) {
	    		 vo.setApplication_count(eventService.application_count(vo.getId()));
	    	 }else if(vo.getEvent_status() == 1) {
	    		 vo.setStaff_count(eventService.staff_count(vo.getId()));
	    	 }else if(vo.getEvent_status() == 2) {
	    		 vo.setStaff_count(eventService.staff_count(vo.getId()));
	    	 }
	    	 vo.setBooth_count(eventService.booth_count(vo.getId()));
	     }
	     return "manage_event";
	}
	
	@RequestMapping(value="/manage_event_register", method=RequestMethod.GET)
	public String event_insert(ModelMap model) throws Exception{
		 
	     return "manage_event_register";
	}
	
	//행사상세조회
	@RequestMapping(value="/eventDetail", method=RequestMethod.GET)
	public ModelAndView eventDetail(@RequestParam("id") int event_id) throws Exception{
		ModelAndView mav = new ModelAndView();
		EventVo detailVo = eventService.viewEventDetail(event_id);
		
		List<EventFileVo> eventFileList = eventService.viewEventFileDetail(event_id);
		
		// update랑 똑같이 고치기
//		포지션별로 잘라 저장
		Map<String, String> positionMap = new HashMap<>();
		
		if(detailVo.getEvent_position() != null) {
			String[] position = detailVo.getEvent_position().split(",");
			String[] position_conut = detailVo.getEvent_position_count().split(",");
			
			for(int i=0; i<position.length;i++) {
				positionMap.put(position[i], position_conut[i]);
			}
		}
		
		mav.addObject("event", detailVo);
		mav.addObject("positionMap", positionMap);
		mav.addObject("positionMap", positionMap);
		mav.addObject("eventFileList", eventFileList);

		mav.setViewName("staff_eventDetail");
		return mav;
	}

	
//	@RequestMapping(value="/eventAdd", method=RequestMethod.POST)
//	public String eventAdd(@ModelAttribute EventVo eventVo, @RequestAttribute List<MultipartFile> library_file) throws Exception{
//		int id =eventService.insertEvent(eventVo);
//		return "redirect:eventDetail?id="+id;
//	}
	
	//행사등록
	@ResponseBody
	@RequestMapping(value="/eventAdd", method=RequestMethod.POST)
	public String eventAdd(@ModelAttribute EventVo eventVo, @RequestAttribute List<MultipartFile> event_file) throws Exception{
		
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
		if(detailVo.getEvent_position() != null) {
			positions = detailVo.getEvent_position().split(",");
			positions_conut = detailVo.getEvent_position_count().split(",");
		}
		
		
		mav.addObject("event", detailVo);
		mav.addObject("positions", positions);
		mav.addObject("positions_conut", positions_conut);
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
	@RequestMapping(value="/application_modal", method=RequestMethod.GET)
	public String application_list(@RequestParam("id") int event_id, ModelMap model) throws Exception{
		
		int applicant_count = eventService.application_count(event_id);
		model.addAttribute("event_id", event_id);
		model.addAttribute("applicant_count", applicant_count);
		
		System.out.println("=============> id:"+event_id);
		
		List<MemberVo> application_list = eventService.application_list(event_id);
		if (application_list != null) {
			for (MemberVo memberVo : application_list) {
				//경력 count
				int staff_career = eventService.staff_career(memberVo.getId());
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
				
				//수락여부(합격/불합격) check
				String result = eventService.getResult(event_id, memberVo.getId());
				memberVo.setResult(result);
			}
		}
	    model.addAttribute("application_list", application_list);
		return "application_modal";
	}
	
	// 지원자 수락
	@RequestMapping(value="accept_applicant", method=RequestMethod.POST)
	public String record_start(@RequestParam("staff_id") int staff_id, @RequestParam("event_id") int event_id, ModelMap model) throws Exception{
		eventService.accept_applicant(event_id, staff_id);
		return "application_modal";
	}
	
	// 지원자 수락해제
	@RequestMapping(value="accept_applicant_cancel", method=RequestMethod.POST)
	public String applicant_accept_cancel(@RequestParam("staff_id") int staff_id, @RequestParam("event_id") int event_id, ModelMap model) throws Exception{
		eventService.accept_applicant_cancel(event_id, staff_id);
		return "application_modal";
	}
	
	// 지원자 불합격처리
	@RequestMapping(value="reject_applicant", method=RequestMethod.POST)
	public String reject_applicant(@RequestParam("staff_id") int staff_id, @RequestParam("event_id") int event_id, ModelMap model) throws Exception{
		eventService.reject_applicant(event_id, staff_id);
		return "application_modal";
	}
	
	// 지원현황(진행중) 모달창
	@RequestMapping(value="/workRecord_modal", method=RequestMethod.GET)
	public String workStaff_list(@RequestParam("id") int event_id, ModelMap model) throws Exception{
		
		int staff_count = eventService.staff_count(event_id);
		model.addAttribute("event_id", event_id);
		model.addAttribute("staff_count", staff_count);
		
		// 오늘 날짜
        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int month = now.getMonthValue();
        int day = now.getDayOfMonth();
        String today = year + "년 " + month + "월 " + day + "일";
        model.addAttribute("today", today);
        
        String work_date = year+"-"+month+"-"+day;
        if(month < 10 && day > 10) {
        	String month_0 = "0" + month;
        	work_date = year+"-"+month_0+"-"+day;
        }else if(day < 10 && month > 10) {
        	String day_0 = "0" + day;
        	work_date = year+"-"+month+"-"+day_0;
        }else if(month < 10 && day < 10) {
        	String month_0 = "0" + month;
        	String day_0 = "0" + day;
        	work_date = year+"-"+month_0+"-"+day_0;
        }else if(month > 10 && day > 10) {
        	work_date = year+"-"+month+"-"+day;
        }
        model.addAttribute("work_date", work_date);
 
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
				
				//당일 근무기록
				List<WorkRecordVo> workTime_list = eventService.getWorkTime(memberVo.getId(), event_id, work_date);
				if(workTime_list != null) {
					for(WorkRecordVo recordVo : workTime_list) {
						memberVo.setRecordVo(recordVo);
					}
				}
			}
		}
	    model.addAttribute("workStaff_list", workStaff_list);
		return "workRecord_modal";
	}
	
	// 근무기록(출근)
	@RequestMapping(value="/record_start", method=RequestMethod.POST)
	public String record_startTime(@RequestParam("staff_id") int staff_id, @RequestParam("event_id") int event_id, 
		@RequestParam("work_date") String work_date, @RequestParam("record_id") int record_id, ModelMap model) throws Exception{
		
		// 현재 시간
		LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH : mm");
        String start_time = now.format(formatter);
        
		eventService.record_startTime(record_id, event_id, staff_id, work_date, start_time);
		return "workRecord_modal";
	}
	
	// 근무기록(외출)
	@RequestMapping(value="/record_out", method=RequestMethod.POST)
	public String record_outTime(@RequestParam("staff_id") int staff_id, @RequestParam("event_id") int event_id, 
		@RequestParam("work_date") String work_date, @RequestParam("record_id") int record_id, ModelMap model) throws Exception{
		
		// 현재 시간
		LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH : mm");
        String out_time = now.format(formatter);
        
		eventService.record_outTime(record_id, event_id, staff_id, work_date, out_time);
		return "workRecord_modal";
	}
	
	// 근무기록(복귀)
	@RequestMapping(value="/record_back", method=RequestMethod.POST)
	public String record_backTime(@RequestParam("staff_id") int staff_id, @RequestParam("event_id") int event_id, 
		@RequestParam("work_date") String work_date, @RequestParam("record_id") int record_id, ModelMap model) throws Exception{
		
		// 현재 시간
		LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH : mm");
        String back_time = now.format(formatter);
        
		eventService.record_backTime(record_id, event_id, staff_id, work_date, back_time);
		return "workRecord_modal";
	}
	
	// 근무기록(퇴근)
	@RequestMapping(value="/record_end", method=RequestMethod.POST)
	public String record_endTime(@RequestParam("staff_id") int staff_id, @RequestParam("event_id") int event_id, 
		@RequestParam("work_date") String work_date, @RequestParam("record_id") int record_id, ModelMap model) throws Exception{
		
		// 현재 시간
		LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH : mm");
        String end_time = now.format(formatter);
        
		eventService.record_endTime(record_id, event_id, staff_id, work_date, end_time);
		return "workRecord_modal";
	}
	
	// 당일 근무기록 없을 때,
	// 근무기록(출근)
	@RequestMapping(value="/record_start_new", method=RequestMethod.POST)
	public String record_startTime_new(@RequestParam("staff_id") int staff_id, @RequestParam("event_id") int event_id, 
		@RequestParam("work_date") String work_date, ModelMap model) throws Exception{
		
		// 현재 시간
		LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH : mm");
        String start_time = now.format(formatter);
        
		eventService.record_startTime_new(event_id, staff_id, work_date, start_time);
		return "workRecord_modal";
	}
	
	// 근무기록(외출)
	@RequestMapping(value="/record_out_new", method=RequestMethod.POST)
	public String record_outTime_new(@RequestParam("staff_id") int staff_id, @RequestParam("event_id") int event_id, 
		@RequestParam("work_date") String work_date, ModelMap model) throws Exception{
		
		// 현재 시간
		LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH : mm");
        String out_time = now.format(formatter);
        
		eventService.record_outTime_new(event_id, staff_id, work_date, out_time);
		return "workRecord_modal";
	}
	
	// 근무기록(복귀)
	@RequestMapping(value="/record_back_new", method=RequestMethod.POST)
	public String record_backTime_new(@RequestParam("staff_id") int staff_id, @RequestParam("event_id") int event_id, 
		@RequestParam("work_date") String work_date, ModelMap model) throws Exception{
		
		// 현재 시간
		LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH : mm");
        String back_time = now.format(formatter);
        
		eventService.record_backTime_new(event_id, staff_id, work_date, back_time);
		return "workRecord_modal";
	}
	
	// 근무기록(퇴근)
	@RequestMapping(value="/record_end_new", method=RequestMethod.POST)
	public String record_endTime_new(@RequestParam("staff_id") int staff_id, @RequestParam("event_id") int event_id, 
		@RequestParam("work_date") String work_date, ModelMap model) throws Exception{
		
		// 현재 시간
		LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH : mm");
        String end_time = now.format(formatter);
        
		eventService.record_endTime_new(event_id, staff_id, work_date, end_time);
		return "workRecord_modal";
	}
	
	// 부스현황 리스트
	@GetMapping(value="/manage_event_booth")
	public String event_booth_list(@RequestParam("id") int event_id, ModelMap model) throws Exception{
		
		model.addAttribute("event_id", event_id);
		String Title = eventService.getTitle(event_id);
		model.addAttribute("Title", Title);
		
		List<BoothVo> booth_list = eventService.booth_list(event_id);
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
		model.addAttribute("booth_list", booth_list);
	    return "manage_event_booth";
	}
	
	//부스등록
	@ResponseBody
	@RequestMapping(value="/register_booth", method=RequestMethod.POST)
	public String register_booth(@RequestParam("event_id") int event_id, @RequestParam("booth_name") String booth_name, 
								 @RequestParam("counting") int counting, @RequestParam("expected_time") int expected_time) throws Exception{
		
		boolean check = eventService.register_booth(event_id, booth_name, counting, expected_time);
		System.out.println("register ok==============>"+check);
			
		return "manage_event_booth?id="+event_id;
	}
	
	//부스수정
	@ResponseBody
	@RequestMapping(value="/modify_booth", method=RequestMethod.POST)
	public String modify_booth(@RequestParam("event_id") int event_id, @RequestParam("booth_id") int booth_id,  @RequestParam("booth_name") String booth_name, 
							   @RequestParam("counting") int counting, @RequestParam("expected_time") int expected_time) throws Exception{
			
		boolean check = eventService.modify_booth(booth_id, booth_name, counting, expected_time);
		System.out.println("register ok==============>"+check);
				
		return "manage_event_booth?id="+event_id;
	}
	
	//부스삭제
	@ResponseBody
	@RequestMapping(value="/delete_booth", method=RequestMethod.POST)
	public String delete_booth(@RequestParam("event_id") int event_id, @RequestParam("booth_id") int booth_id) throws Exception{
				
		boolean check = eventService.delete_booth(booth_id);
		System.out.println("register ok==============>"+check);
					
		return "manage_event_booth?id="+event_id;
	}
}
