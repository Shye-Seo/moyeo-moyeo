package com.service.eventus.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.service.eventus.aws.AwsS3Service;
import com.service.eventus.member.MemberVo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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