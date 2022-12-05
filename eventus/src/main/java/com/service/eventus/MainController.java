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

	@GetMapping(value="/manage_event")
	public String manage_event() {
		return "manage_event";
	}

	@GetMapping(value="/manage_staff")
	public String manage_staff() {
		return "manage_staff";
	}

	@GetMapping(value="report_work")
	public String report_work() {
		return "report_work";
	}
}
