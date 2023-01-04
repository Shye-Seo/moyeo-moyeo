package com.service.eventus.resume;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.service.eventus.member.MemberVo;

import jakarta.servlet.http.HttpSession;

@Controller
public class ResumeController {
	
	@Inject
	private ResumeService resumeService;

	@GetMapping(value="/resume_register")
	public ModelAndView resume_register(HttpSession session) {
		
		ModelAndView mav = new ModelAndView();
		String id = (String) session.getAttribute("user_id");
		
		MemberVo memberVo = resumeService.viewMember_forResume(id);
		
		mav.addObject("memberInfo",memberVo);
		mav.setViewName("resume_register");
		return mav;
	}
}
