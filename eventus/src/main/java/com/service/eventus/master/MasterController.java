package com.service.eventus.master;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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
    public ModelAndView manage_staff(@RequestParam(value="page", defaultValue = "1") int page) {
        // 세션에 저장된 user_id를 가져온다.
        String user_id = "cdcd05g";
        int staff_num;
        ModelAndView mav = new ModelAndView();
        List<MasterVo> staff_list = masterService.getListMemberApp(user_id);
        List<MasterVo> staff_list_forview = null;
        staff_num = staff_list.size();
        // list 객체인 staff_list를 반복문 돌려서 근로계약서 등록했는지 확인
        for(MasterVo staff: staff_list) {
            int i=1;
            staff.setList_no(i);
            i++;
            int pass_check = masterService.checkStaffPasser(staff);
            if(pass_check == 1) {
                int contract_check = masterService.checkContractFile(staff);
                if(contract_check == 1) {
                    staff.setContract_check(1);
                } else {
                    staff.setContract_check(0);
                }
            }
            else {
                staff.setContract_check(-1);
            }
        }

        // 페이징 처리
        // String 형인 변수 page를 int형으로 변환하여 page_str에 저장
        int page_str = Integer.parseInt(String.valueOf(page));
        for(int j=(page_str*10)-9;j<page_str*10;j++) { // 하나의 게시물에 10개의 정보가 들어간다.
            // staff_list의 no가 j인 객체를 staff_list_forview에 넣는다.
            staff_list_forview.add(staff_list.get(j));
        }

        mav.addObject("staff_list", staff_list_forview);
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
        // list 객체인 career_list를 반복문 돌려서 행사 모집중 | 합격, 불합격 | 이력서 등록, 미등록 여부 확인
        for (MasterVo career : career_list) {
            int i=1;
            career.setList_no(i);
            i++;

            if(career.getEvent_check() == 1) {
                int pass_check = masterService.checkStaffPasser(career);
                if(pass_check == 1) { // 있으면
                    career.setPass_check(1);
                    int contract_check = masterService.checkContractFile(career);
                    if(contract_check == 1) { // 있으면
                        career.setContract_check(1);
                    }
                    else {
                        career.setContract_check(0);
                    }
                }
                else {
                    career.setPass_check(0);
                    career.setContract_check(-1);
                }

            }
            else {
                career.setPass_check(-1);
                career.setContract_check(-1);
            }

        }


        career_num = career_list.size();

        mav.addObject("career_list", career_list);
        mav.addObject("career_num", career_num);
        return mav;
    }

    // 근로계약서 작성을 위한 이벤트 정보 가져오기
    @RequestMapping("/getEventInfo")
    @ResponseBody
    public MasterVo getEventInfo(@RequestParam("id") int id) {
        MasterVo masterVo = masterService.getEventInfo(id);
        return masterVo;
    }

    // 근로확인서 및 보안각서 최종 확인 및 저장하기
    @RequestMapping("/contract_check")
    public ModelAndView contract_check(@ModelAttribute MasterVo masterVo) {
        ModelAndView mav = new ModelAndView();
        // 세션에 저장되어 있는 유저 아이디의 id값을 masterVo에 저장
        masterVo.setStaff_id(18);
        mav.setViewName("/contract_file");
        mav.addObject("masterVo", masterVo);
        return mav;
    }

    // 이력서 등록
    @RequestMapping("/insert_contract")
    @ResponseBody
    public void insert_contract(@ModelAttribute MasterVo masterVo) {
        masterService.insert_contract(masterVo);
    }
}
