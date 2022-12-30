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
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

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
	
	
	
	//지원현황(모집중) 모달창
	@RequestMapping(value="/application_modal", method=RequestMethod.GET)
	public String application_list(@RequestParam("id") int event_id, ModelMap model) throws Exception{
		
		List<MemberVo> application_list = eventService.application_list(event_id);
		if (application_list != null) {
			for (MemberVo memberVo : application_list) {
				int staff_career = eventService.staff_career(memberVo.getId());
				model.addAttribute("career_count", staff_career);
			}
		}
		System.out.println("=============>list:"+application_list);
	    model.addAttribute("application_list", application_list);
		return "application_modal";
	}
	
	//행사 파일 다운로드
	@RequestMapping({"/event_download"})
	@ResponseBody
	public ResponseEntity<byte[]> download(@RequestParam String filename) throws IOException {
		return s3Service.getObject_event(filename);
	}
}
