package com.service.eventus.master;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.service.eventus.event.EventVo;


import javax.inject.Inject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    public ModelAndView manage_career_ForStaff(@RequestParam(value="page", required=false, defaultValue = "1") int page) {
        // 세션에 저장된 user_id를 가져온다.
        String user_id = "test5";
        int career_size=0; // 페이지 개수 세기(페이징 처리)
        int career_num; // 페이지 개수 세기(번호 붙히는 용도)
        ModelAndView mav = new ModelAndView();
        List<MasterVo> career_list = masterService.getListUserApp(user_id);
        List<MasterVo> career_list_forview = new ArrayList<>();
        career_num=career_list.size();

        // list 객체인 career_list를 반복문 돌려서 행사 모집중 | 합격, 불합격 | 이력서 등록, 미등록 여부 확인
        for (MasterVo career : career_list) {
            career.setList_no(career_num);
            career_num--;
            career_size++;
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

        // 페이징 처리
        // String 형인 변수 page를 int형으로 변환하여 page_str에 저장
        int page_str = Integer.parseInt(String.valueOf(page));
        // totalPage는 staff_list의 크기를 10으로 나눈 몫에 1을 더한 값
        int totalPage = (career_size / 10) + 1;
        // 시작 페이지
        int startPage;

        // 보이는 페이지 번호 변경
        if(page % 10 != 0) { startPage = (page / 10) * 10 + 1; }
        else { startPage = ((page / 10) - 1) * 10 + 1; }

        if(page_str*10>=career_size) {
            for(int j=(page_str*10)-9;j<=career_size;j++) { // 하나의 게시물에 10개의 정보가 들어간다.
                // 자료형이 MasterVo인 리스트 career_list에서 j번째 요소를 자료형이 MasterVo인 리스트 career_list_forview에 추가
                career_list_forview.add(career_list.get(j-1));
            }
        }
        else {
            for(int j=(page_str*10)-9;j<=page_str*10;j++) { // 하나의 게시물에 10개의 정보가 들어간다.
                // 자료형이 MasterVo인 리스트 career_list에서에서 j번째 요소를 자료형이 MasterVo인 리스트 career_list_forview에 추가
                career_list_forview.add(career_list.get(j-1));
            }
        }

        mav.addObject("career_list", career_list_forview);
        mav.addObject("totalPage", totalPage);
        mav.addObject("page", page_str);
        mav.addObject("startPage", startPage);
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

    // 근로계약서 관련 데이터 저장
    @RequestMapping("/add_contract")
    public ModelAndView add_contract(@ModelAttribute MasterVo masterVo) {
        ModelAndView mav = new ModelAndView();
        // session에 저장되어 있는 id를 가져와 masterVo에 저장
        masterVo.setStaff_id(masterService.getUserId("test5"));

        // event_title을 가지고 event_id를 가져와 masterVo에 저장
        // masterVo.setEvent_id(masterService.getEventId(masterVo.getEvent_title()));
        mav.setViewName("/manage_career_forstaff");
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
    
    @GetMapping(value="/report_work_ForStaff")
	public String report_work_ForStaff(ModelMap model) {
    	
    	 List<MasterVo> report_work_list = masterService.report_work_list();
    	 model.addAttribute("report_work_list", report_work_list);
    	 
		return "report_work_ForStaff";
	}
    // 이력서 등록
    @RequestMapping("/insert_contract")
    @ResponseBody
    public void insert_contract(@ModelAttribute MasterVo masterVo) {
        masterService.insert_contract(masterVo);
    }
}
