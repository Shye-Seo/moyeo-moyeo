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


	@GetMapping(value="/contract_file")
	public String contract_file() {
		return "contract_file";
	}

	@GetMapping(value="/contract_file_fowview")
	public String contract_file_fowview() {
		return "contract_file_forview";
	}
}