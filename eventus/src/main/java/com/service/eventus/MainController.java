package com.service.eventus;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.service.eventus.event.EventService;
import com.service.eventus.event.EventVo;

@Controller
public class MainController {

    // 최초 진입 시 실행 페이지
	@GetMapping(value="/")
	public String home() {
		return "login";
	}

	@GetMapping(value="/login")
	public String login() {
		return "login";
	}

	// 회원가입 약관동의 페이지
	@GetMapping(value="/join1")
	public String join() {
		return "join1";
	}

	@GetMapping(value="/join2")
	public String join2() { return "join2"; }

	@GetMapping(value="/join3")
	public String join3() { return "join3"; }

	// 아이디 비멀번호 관련 페이지
	@GetMapping(value="/find_id_pw")
	public String find_id_pw() { return "find_id_pw"; }

	@GetMapping(value="/changing_pw")
	public String changing_pw() { return "changing_pw"; }

	@GetMapping(value="/changed_pw")
	public String changed_pw() { return "changed_pw"; }

	// main 페이지
	@GetMapping(value="/main")
	public String Main() {
		return "main";
	}

//	@GetMapping(value="/manage_event")
//	public String manage_event(ModelMap model) {
//		List<EventVo> event_list = eventService.event_list();    
//	    model.addAttribute("event_list", event_list);
//		return "manage_event";
//	}

	@GetMapping(value="/manage_staff")
	public String manage_staff() {
		return "manage_staff";
	}

	@GetMapping(value="report_work")
	public String report_work() {
		return "report_work";
	}

}