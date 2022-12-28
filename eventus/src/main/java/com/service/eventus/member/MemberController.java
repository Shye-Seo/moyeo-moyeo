package com.service.eventus.member;

import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

@Controller
public class MemberController {

    @Inject
    private MemberService memberService;

    // 아이디 중복 체크
    @ResponseBody
    @PostMapping(value="idChk")
    public int idChk(@RequestParam("user_id") String user_id) {
        return memberService.idChk(user_id);
    }

    // 로그인 처리
    @RequestMapping("/LoginProc")
    public ModelAndView loginCheck(@ModelAttribute MemberVo memberVo) throws Exception {
        int result = memberService.loginCheck(memberVo);
        ModelAndView mav = new ModelAndView();

        if(result==1) { // 로그인 성공
            // main으로 이동
            System.out.println("로그인성공");
            mav.setViewName("/main");
            mav.addObject("msg", "success");
        }else { // 로그인 실패
            System.out.println("로그인실패");
            mav.setViewName("/login");
            mav.addObject("msg", "failure");
        }

        return mav;
    }

    // 아이디 찾기
    @RequestMapping("/findId")
    public ModelAndView findId(@ModelAttribute MemberVo memberVo) throws Exception {
        ModelAndView mav = new ModelAndView();
        String result = memberService.findId(memberVo);
        if(result != null) {
            mav.setViewName("/login");
            mav.addObject("msg", "success");
            mav.addObject("user_id", result);
        }else {
            mav.setViewName("/find_id_pw");
            mav.addObject("msg", "failure");
        }
        return mav;
    }

    // 휴대폰인증
    @RequestMapping(value = "phoneCheck", method = RequestMethod.GET)
    @ResponseBody
    public String sendSMS(@RequestParam("user_phone") String userPhoneNumber) throws CoolsmsException { // 휴대폰 문자보내기
        int randomNumber = (int)((Math.random() * (9999 - 1000 +1)) + 1000); // 난수 생성

        memberService.sendSms(userPhoneNumber, randomNumber);

        return Integer.toString(randomNumber);
    }

}