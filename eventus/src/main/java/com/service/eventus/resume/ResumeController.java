package com.service.eventus.resume;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	
	//이력서 조회 / 등록 / 수정
	@GetMapping(value="/myresume_forStaff")
	public ModelAndView resume_register(HttpSession session) {
		
		ModelAndView mav = new ModelAndView();
		//세션의 아이디를 받아온다
		String id = (String) session.getAttribute("user_id");
		MemberVo memberVo = resumeService.viewMember_forResume(id);
		
		//이력서 조회
		ResumeVo resumeVo = resumeService.selectMyResume(memberVo.getId());
		String profile_fileName = "";
		int resume_id = 0;
		if(resumeVo == null) { 
			resumeVo = new ResumeVo(); 
		}else {
			profile_fileName = resumeService.selectProfile(resumeVo.getId());
			resume_id = resumeVo.getId();
		}
		
		
		//학교처리..
		String[] nullArray = {"",""} ;
		
		Map<String, String[]> school_info = new HashMap<>();
		school_info.put("school", nullArray);
		school_info.put("major", nullArray);
		school_info.put("start", nullArray);
		school_info.put("end", nullArray);
		school_info.put("state", nullArray);
		
		if(resumeVo.getStaff_school() != null) {
			if(resumeVo.getStaff_school().split(",").length <2) {
				String[] school = {resumeVo.getStaff_school(), ""};
				school_info.put("school", school);
			}else {
				school_info.put("school", resumeVo.getStaff_school().split(","));
			}
			if(resumeVo.getStaff_major().split(",").length <2) {
				String[] major = {resumeVo.getStaff_major(), ""};
				school_info.put("major", major);
			}else {
				school_info.put("major", resumeVo.getStaff_major().split(","));
			}
			if(resumeVo.getStaff_school_start().split(",").length <2) {
				String[] start = {resumeVo.getStaff_school_start(), ""};
				school_info.put("start", start);
			}else {
				school_info.put("start", resumeVo.getStaff_school_start().split(","));
			}
			if(resumeVo.getStaff_school_end().split(",").length <2) {
				String[] end = {resumeVo.getStaff_school_end(), ""};
				school_info.put("end", end);
			}else {
				school_info.put("end", resumeVo.getStaff_school_end().split(","));
			}
			if(resumeVo.getStaff_major().split(",").length <2) {
				String[] state = {resumeVo.getStaff_school_state(), ""};
				school_info.put("state", state);
			}else {
				school_info.put("state", resumeVo.getStaff_school_state().split(","));
			}
		}
		
		//행사처리
		Map<String, String[]> career_info = new HashMap<>();
		
		if(resumeVo.getStaff_career_eventName() != null) {
			career_info.put("eventName", resumeVo.getStaff_career_eventName().split(","));
			career_info.put("businessName",resumeVo.getStaff_career_businessName().split(","));
			career_info.put("positions",resumeVo.getStaff_career_position().split(","));
			career_info.put("workday",resumeVo.getStaff_career_workday().split(","));
		}

		mav.addObject("memberInfo",memberVo);
		mav.addObject("resumeInfo", resumeVo);
		mav.addObject("profile", profile_fileName);
		mav.addObject("resume_id", resume_id);
		mav.addObject("school_info", school_info);
		mav.addObject("career_info", career_info);
		
		mav.setViewName("resume_register");
		return mav;
	}
	
	//이력서 추가/수정
	@ResponseBody
	@RequestMapping(value="/resumeAdd", method=RequestMethod.POST)
	public String resumeAdd(MultipartHttpServletRequest multipartRequest, @ModelAttribute ResumeVo resumeVo) throws Exception{
		
		int OldResume_id = Integer.parseInt(multipartRequest.getParameter("OldResume_id"));
		String OldProfile = (String) multipartRequest.getParameter("OldProfile");
		MultipartFile profileImg = multipartRequest.getFile("profile_img");
		
		//먼저 등록한 이력서 비활성화
		resumeService.disabledPreResume(resumeVo.getStaff_id());
		
		//이력서 등록
		int resumeID = resumeService.insertResume(resumeVo);
		//프로필 등록
		if(profileImg.getOriginalFilename() != "") {
			String filename = s3Service.upload_profile(profileImg, Integer.toString(resumeVo.getStaff_id()), Integer.toString(resumeID));
			resumeService.insertProfile(resumeVo.getStaff_id(), resumeID, filename);
		}else if(OldResume_id != 0 ) {
			resumeService.insertProfile(resumeVo.getStaff_id(), resumeID, OldProfile);
		}
		
		return "manage_career_forstaff";
	}

	@GetMapping(value="/test_download")
	public String test_download() {
		return "test_download";
	}
	
    @RequestMapping("/download")
    public String test_download(MultipartHttpServletRequest multipartRequest) throws Exception{
    	MultipartFile profileImg = multipartRequest.getFile("file");
    	return "manage_career_forstaff";
    }
}
