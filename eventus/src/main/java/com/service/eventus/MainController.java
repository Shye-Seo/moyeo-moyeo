package com.service.eventus;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    // 최초 진입 시 실행 페이지
	@GetMapping(value="/")
	public String page() {
		return "main";
	}

}
