package com.service.eventus.member;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

@Controller
public class MemberController {

    @Inject
    private MemberService memberService;

    @RequestMapping("MemInsertProc") // 회원가입 처리
    public String meminsertProc(MemberVo memberVo) {
        Map<String, String> map = new HashMap<>();

        map.put("user_id", memberVo.getUser_id());
        map.put("user_name", memberVo.getUser_name());
        map.put("user_pass", memberVo.getUser_pw());
        map.put("user_phone", memberVo.getUser_phone());
        map.put("user_mail_id", memberVo.getUser_mail_id());
        map.put("user_mail_domain", memberVo.getUser_mail_domain());

        memberService.memInsert(map);

        return "login";
    }
}
