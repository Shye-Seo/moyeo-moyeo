package com.service.eventus.master;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MasterController {


    @Inject
    private MasterService masterService;

    @RequestMapping("/manage_staff")
    public ModelAndView manage_staff(@RequestParam(value="page", required=false, defaultValue = "1") int page) {
        // 세션에 저장된 user_id를 가져온다.
        String user_id = "cdcd05g";
        int staff_size=0; // 페이지 개수 세기(페이징 처리)
        int staff_num; // 페이지 개수 세기(번호 붙히는 용도)
        ModelAndView mav = new ModelAndView();
        List<MasterVo> staff_list = masterService.getListMemberApp(user_id);
        List<MasterVo> staff_list_forview = new ArrayList<>();
        staff_num=staff_list.size();

        // list 객체인 staff_list를 반복문 돌려서 근로계약서 등록했는지 확인
        for(MasterVo staff: staff_list) {
            staff.setList_no(staff_num);
            staff_num--;
            staff_size++;
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
        // totalPage는 staff_list의 크기를 10으로 나눈 몫에 1을 더한 값
        int totalPage = (staff_size / 10) + 1;
        // 시작 페이지
        int startPage;

        // 보이는 페이지 번호 변경
        if(page % 10 != 0) { startPage = (page / 10) * 10 + 1; }
        else { startPage = ((page / 10) - 1) * 10 + 1; }


        if(page_str*10>=staff_size) {
            for(int j=(page_str*10)-9;j<=staff_size;j++) { // 하나의 게시물에 10개의 정보가 들어간다.
                // 자료형이 MasterVo인 리스트 staff_list에서 j번째 요소를 자료형이 MasterVo인 리스트 staff_list_forview에 추가
                staff_list_forview.add(staff_list.get(j-1));
            }
        }
        else {
            for(int j=(page_str*10)-9;j<=page_str*10;j++) { // 하나의 게시물에 10개의 정보가 들어간다.
                // 자료형이 MasterVo인 리스트 staff_list에서 j번째 요소를 자료형이 MasterVo인 리스트 staff_list_forview에 추가
                staff_list_forview.add(staff_list.get(j-1));
            }
        }


        mav.addObject("staff_list", staff_list_forview);
        mav.addObject("totalPage", totalPage);
        mav.addObject("page", page_str);
        mav.addObject("startPage", startPage);

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
