package com.service.eventus.member;

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
    public ModelAndView loginCheck(@ModelAttribute MemberVo memberVo, HttpSession session) throws Exception {
        boolean result = memberService.loginCheck(memberVo, session);
        ModelAndView mav = new ModelAndView();

        if(result) { // 로그인 성공
            // main으로 이동
            System.out.println("로그인성공");
            session.setAttribute("user_id", memberService.viewMember(memberVo).getStaff_id());
            mav.setViewName("/main");
            mav.addObject("msg", "success");
        }else { // 로그인 실패
            System.out.println("로그인실패");
            mav.setViewName("/login");
            mav.addObject("msg", "failure");
        }

        return mav;
    }

}