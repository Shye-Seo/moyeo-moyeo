package com.service.eventus.resume;

import java.util.List;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.service.eventus.aws.AwsS3Service;
import com.service.eventus.event.EventFileVo;
import com.service.eventus.event.EventVo;
import com.service.eventus.member.MemberVo;

import jakarta.servlet.http.HttpSession;

@Controller
public class ResumeController {
	@Autowired
	AwsS3Service s3Service;
	
	@Inject
	private ResumeService resumeService;

	
	//이력서 등록 페이지
	@GetMapping(value="/resume_register")
	public ModelAndView resume_register(HttpSession session) {
		
		ModelAndView mav = new ModelAndView();
		//세션의 아이디를 받아온다
		String id = (String) session.getAttribute("user_id");
		
		MemberVo memberVo = resumeService.viewMember_forResume(id);
		
		mav.addObject("memberInfo",memberVo);
		mav.setViewName("resume_register");
		return mav;
	}
	
	@ResponseBody
	@RequestMapping(value="/resumeAdd", method=RequestMethod.POST)
	public String resumeAdd(MultipartHttpServletRequest multipartRequest, @ModelAttribute ResumeVo resumeVo) throws Exception{
		
		
		MultipartFile profileImg = multipartRequest.getFile("profile_img");
		String filename = s3Service.upload_profile(profileImg, Integer.toString(resumeVo.getStaff_id()));
		resumeService.insertResume(resumeVo);
		resumeService.insertProfile(resumeVo.getStaff_id(), filename);
		
		
		return "manage_career_forstaff";
	}
	
}
