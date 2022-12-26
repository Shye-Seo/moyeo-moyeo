package com.service.eventus.event;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.service.eventus.member.MemberVo;

import java.util.List;

import javax.inject.Inject;
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
}
