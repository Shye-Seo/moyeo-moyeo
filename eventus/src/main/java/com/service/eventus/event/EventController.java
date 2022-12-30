package com.service.eventus.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.service.eventus.aws.AwsS3Service;
import com.service.eventus.member.MemberVo;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
		
		mav.addObject("event", detailVo);
		mav.addObject("eventFileList", eventFileList);

		mav.setViewName("manage_eventDetail");
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
	
	//행사 파일 다운로드
	@RequestMapping({"/event_download"})
	@ResponseBody
	public ResponseEntity<byte[]> download(@RequestParam String filename) throws IOException {
		return s3Service.getObject_event(filename);
	}
	
	//지원현황(모집중) 모달창
	@RequestMapping(value="/application_modal", method=RequestMethod.GET)
	public String application_list(@RequestParam("id") int event_id, ModelMap model) throws Exception{
		
		int applicant_count = eventService.application_count(event_id);
		model.addAttribute("event_id", event_id);
		model.addAttribute("applicant_count", applicant_count);
		
		List<MemberVo> application_list = eventService.application_list(event_id);
		if (application_list != null) {
			for (MemberVo memberVo : application_list) {
				int staff_career = eventService.staff_career(memberVo.getId());
				model.addAttribute("career_count", staff_career); //경력 count
				
				String user_address = eventService.getAddress(memberVo.getId());
				model.addAttribute("user_address", user_address); //주소 get
				
				String user_age = eventService.getUserAge(memberVo.getUser_birth());
				model.addAttribute("user_age", user_age); //만 나이 계산
				
				String regEx = "(\\d{3})(\\d{3,4})(\\d{4})";
				String user_phone = memberVo.getUser_phone().replaceAll(regEx, "$1-$2-$3");
				model.addAttribute("user_phone", user_phone); //휴대폰번호 형태
				
				// 수락여부 check
				String result = eventService.getResult(event_id, memberVo.getId());
				model.addAttribute("result", result);
			}
		}
	    model.addAttribute("application_list", application_list);
		return "application_modal";
	}
	
	// 지원자 수락
	@RequestMapping(value="accept_applicant", method=RequestMethod.POST)
	public String applicant_accept(@RequestParam("staff_id") int staff_id, @RequestParam("event_id") int event_id, ModelMap model) throws Exception{
		
		int result = eventService.accept_applicant(event_id, staff_id);
		System.out.println("=========>결과:"+result);
		
		return "application_modal";
	}
	
	// 지원자 수락해제
	@RequestMapping(value="accept_applicant_cancel", method=RequestMethod.POST)
	public String applicant_accept_cancel(@RequestParam("staff_id") int staff_id, @RequestParam("event_id") int event_id, ModelMap model) throws Exception{
		
		int result = eventService.accept_applicant_cancel(event_id, staff_id);
		System.out.println("=========>결과:"+result);
			
		return "application_modal";
	}
	
	// 지원자 불합격처리
	@RequestMapping(value="reject_applicant", method=RequestMethod.POST)
	public String reject_applicant(@RequestParam("staff_id") int staff_id, @RequestParam("event_id") int event_id, ModelMap model) throws Exception{
			
		int result = eventService.reject_applicant(event_id, staff_id);
		System.out.println("=========>결과:"+result);
				
		return "application_modal";
	}
}