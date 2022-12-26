package com.service.eventus.event;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class EventController {
	
	@Inject
    private EventService eventService;
	
	@RequestMapping(value="/manage_event", method=RequestMethod.GET)
	public String event_list(ModelMap model) throws Exception{
		 List<EventVo> event_list = eventService.event_list();
		 
		 
	     model.addAttribute("event_list", event_list);
	     return "manage_event";
	}
	
	@RequestMapping("/manage_event_register")
	public String event_insert(ModelMap model) throws Exception{
		 
		 
	     return "manage_event_register";
	}
	
	
	//행사상세조회
	@RequestMapping(value="/eventDetail", method=RequestMethod.GET)
	public ModelAndView eventDetail(@RequestParam("id") int event_id) throws Exception{
		ModelAndView mav = new ModelAndView();
		
		EventVo detailVo = eventService.viewEventDetail(event_id);
		System.out.println(detailVo);
		
		mav.addObject("event", detailVo);
		
		mav.setViewName("manage_eventDetail");
		return mav;
	}
	
	//행사등록
	@RequestMapping(value="/eventAdd", method=RequestMethod.POST)
	public String eventAdd(@ModelAttribute EventVo eventVo) throws Exception{
		System.out.println(eventVo);
		boolean result = eventService.insertEvent(eventVo);
		
		
		return "redirect:manage_event";
	}
	
	
}
