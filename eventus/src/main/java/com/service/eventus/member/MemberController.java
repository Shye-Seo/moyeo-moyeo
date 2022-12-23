package com.service.eventus.member;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

@Controller
public class MemberController {

    @Inject
    private MemberService memberService;

    // 아이디 중복 체크
    @ResponseBody
    @PostMapping(value="idChk")
    public int idChk(@RequestParam("staff_id") String staff_id) {
        return memberService.idChk(staff_id);
    }

    // 로그인 처리
    @RequestMapping("/LoginProc")
    public ModelAndView loginCheck(@RequestParam("staff_id") String staff_id, @RequestParam("staff_pw") String staff_pw) throws Exception {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("staff_id", staff_id);
        map.put("staff_pw", staff_pw);
        int result = memberService.loginCheck(map);
        ModelAndView mav = new ModelAndView();

        if(result!=0) { // 로그인 성공
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

}