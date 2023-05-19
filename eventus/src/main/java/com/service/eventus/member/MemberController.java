package com.service.eventus.member;

import net.nurigo.java_sdk.exceptions.CoolsmsException;

import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;


@Controller
public class MemberController {

    @Inject
    private MemberService memberService;

    // 회원가입
    @RequestMapping(value = "/insertUser")
    @ResponseBody
    public int insertUser(@ModelAttribute MemberVo memberVo) throws Exception {
        return memberService.insertUser(memberVo);
    }

    // 아이디 중복 체크
    @ResponseBody
    @RequestMapping(value="/idchk")
    public int idChk(@RequestParam("user_id") String user_id) {
        return memberService.idChk(user_id);
    }

    // 로그인 처리
    @RequestMapping("/LoginProc")
    public ModelAndView loginCheck(HttpServletResponse response, @ModelAttribute MemberVo memberVo, HttpSession session) throws Exception {
        int result = memberService.loginCheck(memberVo, session);
        MemberVo memberVo2 = memberService.viewMember(memberVo);
        ModelAndView mav = new ModelAndView();

        if(result==1) { // 로그인 성공
            // main으로 이동
            if(memberVo2.getUser_authority()==0) {
                // 관리자 페이지 이동
                mav.setViewName("redirect:/main");
            }
            else {
                // 스태프 페이지 이동
                mav.setViewName("redirect:/main_ForStaff");
            }
        }
        else {
            // 로그인 실패
            mav.setViewName("/login");
            mav.addObject("msg", "failure");
        }

        return mav;
    }
    
    // 로그아웃로직
    @RequestMapping("/LogoutProc")
    public String Logout (HttpSession session) throws Exception{
    	session.invalidate();
    	return "/login";
    }

    @RequestMapping(value="/changePW")
    public ModelAndView changePW(HttpSession session) {
        ModelAndView mav = new ModelAndView();
        String user_id = (String) session.getAttribute("user_id");
        MemberVo memberVo = new MemberVo();
        memberVo.setUser_id(user_id);
        memberVo = memberService.viewMember(memberVo);

        mav.addObject("memberVo", memberVo);
        mav.setViewName("/changePW");
        return mav;
    }
    

    // 아이디 찾기
    @RequestMapping("/findId")
    @ResponseBody
    public Map findId(@ModelAttribute MemberVo memberVo) throws Exception {
    	
    	Map result = memberService.findId(memberVo);
    	
        if(MapUtils.isEmpty(result)) {
        	result = new HashMap<>();
            result.put("user_id", "failure");
        }
        return result;
    }

    // 비밀번호 변경을 위한 아이디 찾기
    @RequestMapping("/findIdForPw")
    @ResponseBody
    public String findIdForPw(@ModelAttribute MemberVo memberVo) throws Exception {
        String result = memberService.findIdForPw(memberVo);
        if(result != null) {
            return result;
        }else {
            return "failure";
        }
    }

    // 비밀번호 변경
    @RequestMapping("/updatePw")
    @ResponseBody
    public String updatePw(@ModelAttribute MemberVo memberVo) throws Exception {
    	int result = memberService.updatePw(memberVo);
        if(result == 1) {
            return "success";
        }else {
            return "failure";
        }
    }
    
    // 회원정보 페이지에서 비밀번호 변경
    @RequestMapping("/updatePw_modify")
    @ResponseBody
    public String updatePw_modify(@ModelAttribute MemberVo memberVo, 
    		@RequestParam("old_pw") String oldPw) throws Exception {	
    	int result = 0;
    	String presentPw = memberService.selectPw(memberVo);
    	if(presentPw.equals(oldPw)) {
    		result = memberService.updatePw(memberVo);
    	}else {
    		result = 2;
    	}
    	if(result == 1) {
    		return "success";
    	}else if(result == 2) {
    		return "different";
    	}else {
    		return "failure";
    	}
    }
    
    //내 정보 설정 변경 페이지
    @RequestMapping("/user_modify")
    public ModelAndView modifyUserInfo(HttpSession session) {
    	ModelAndView mav = new ModelAndView();
        String user_id = (String) session.getAttribute("user_id");
        MemberVo memberVo = new MemberVo();
        memberVo.setUser_id(user_id);
        memberVo = memberService.viewMember(memberVo);

        mav.addObject("memberVo", memberVo);
        mav.setViewName("/user_modify");
        return mav;
    }

    // 휴대전화 번호 변경
    @RequestMapping("/updatePhone")
    @ResponseBody
    public void updatePhone(@ModelAttribute MemberVo memberVo) throws Exception {
    	System.out.println(memberVo);
        memberService.updatePhone(memberVo);
    }

    // 휴대폰인증
    @RequestMapping(value = "phoneCheck", method = RequestMethod.GET)
    @ResponseBody
    public String sendSMS(@RequestParam("user_phone") String userPhoneNumber) throws CoolsmsException { // 휴대폰 문자보내기
        int randomNumber = (int)((Math.random() * (9999 - 1000 +1)) + 1000); // 난수 생성

//        memberService.sendSms(userPhoneNumber, randomNumber);
        System.out.println(randomNumber);
        return Integer.toString(randomNumber);
    }

}
