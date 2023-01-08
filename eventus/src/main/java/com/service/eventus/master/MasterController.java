package com.service.eventus.master;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MasterController {


    @Inject
    private MasterService masterService;

    @RequestMapping("/manage_staff")
    public ModelAndView manage_staff() {
        // 세션에 저장된 user_id를 가져온다.
        String user_id = "cdcd05g";
        int staff_num;
        ModelAndView mav = new ModelAndView();
        List<MasterVo> staff_list = masterService.getListMemberApp(user_id);
        staff_num = staff_list.size();

        mav.addObject("staff_list", staff_list);
        mav.addObject("staff_num", staff_num);
        return mav;
    }

    @RequestMapping("/manage_career_forstaff")
    public ModelAndView manage_career_ForStaff() {
        // 세션에 저장된 user_id를 가져온다.
        String user_id = "test5";
        int career_num;
        ModelAndView mav = new ModelAndView();
        List<MasterVo> career_list = masterService.getListUserApp(user_id);
        career_num = career_list.size();

        mav.addObject("career_list", career_list);
        mav.addObject("career_num", career_num);
        return mav;
    }

    @RequestMapping("/contract_check")
    public ModelAndView contract_check(@ModelAttribute MasterVo masterVo) {

        ModelAndView mav = new ModelAndView();

        mav.setViewName("/contract_file");
        mav.addObject("masterVo", masterVo);
        return mav;
    }
}
