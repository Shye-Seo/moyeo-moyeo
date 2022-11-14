package com.service.eventus;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping(value="/")
	public String page() {
		return "index.html";
	}

	@GetMapping(value="/Main")
	public String main() {
		return "main";
	}
}
