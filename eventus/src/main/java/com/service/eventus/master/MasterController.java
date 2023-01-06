package com.service.eventus.master;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.service.eventus.event.EventVo;


import javax.inject.Inject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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

    //근무기록 리스트(관리자)
    @GetMapping(value="/report_work")
    public String report_work(@ModelAttribute EventVo eventVo, ModelMap model) throws Exception{
        List<MasterVo> report_work_list = masterService.report_work_list();

        for(int i = 0; i<=report_work_list.size()-1; i++) {
            String start_time = report_work_list.get(i).getWork_start_time();
            String outing_time = report_work_list.get(i).getWork_outing_time();
            String comeback_time = report_work_list.get(i).getWork_comeback_time();
            String end_time = report_work_list.get(i).getWork_end_time();
            int staff_id = report_work_list.get(i).getStaff_id();


            if(start_time != null && end_time != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("HH : mm");

                Date start = sdf.parse(start_time);
                Date end = sdf.parse(end_time);

                long timeMil_start = start.getTime();
                long timeMil_end = end.getTime();


                long diff = timeMil_end - timeMil_start;

                long diffSec = diff / 1000;

                if(outing_time != null && comeback_time != null ) {
                    Date outing = sdf.parse(outing_time);
                    Date comeback = sdf.parse(comeback_time);

                    long timeMil_outing = outing.getTime();
                    long timeMil_comeback = comeback.getTime();

                    long diff2 = timeMil_comeback - timeMil_outing;

                    long diffSec_1 = diff / 1000;
                    long diffSec2 = diff2 / 1000;

                    long Sum = diffSec_1 - diffSec2;

                    int hour = (int)Sum/(60*60);
                    int minute = (int)Sum/60-(hour*60);
                    String total = hour + " : "+ minute;
                    masterService.update_work_total_time(total, staff_id);

                } else {
                    int hour = (int)diffSec/(60*60);
                    int minute = (int)diffSec/60-(hour*60);

                    String total = hour + " : "+ minute;
                    masterService.update_work_total_time(total, staff_id);
                }
            }

        }
        model.addAttribute("report_work_list", report_work_list);
        return "report_work";
    }
}
