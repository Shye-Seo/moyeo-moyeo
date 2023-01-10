package com.service.eventus;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class MainController {

	// 최초 진입 시 실행 페이지
	@GetMapping(value="/")
	public String home() {
		return "login";
	}

	// 관리자 페이지 링크
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

	// 관리자 페이지 링크
	@GetMapping(value="/main")
	public String Main() {
		return "main";
	}

	/*
	@GetMapping(value="/manage_event")
	public String manage_event(ModelMap model) {
		List<EventVo> event_list = eventService.event_list();
		model.addAttribute("event_list", event_list);
		return "manage_event";
	}


	@GetMapping(value="/manage_staff")
	public String manage_staff() {
		return "manage_staff";
	}

	@GetMapping(value="/manage_career_ForStaff")
	public String manage_career_ForStaff() {
		return "manage_career_forstaff";
	}
	*/

//	@GetMapping(value="/report_work")
//	public String report_work() {
//		return "report_work";
//	}


	// 사용자 페이지 링크
	@GetMapping(value="/main_ForStaff")
	public String Main_ForStaff() {
		return "main_ForStaff";
	}

//	@GetMapping(value="/eventList_ForStaff")
//	public String eventList_ForStaff() {
//		return "eventList_ForStaff";
//	}

//	@GetMapping(value="/resume_register")
//	public String resume_register() {
//		return "resume_register";
//	}

	@GetMapping(value="/report_work_ForStaff")
	public String report_work_ForStaff() {
		return "report_work_ForStaff";
	}

	@GetMapping(value="/contract_file")
	public String contract_file() {
		return "contract_file";
	}
}