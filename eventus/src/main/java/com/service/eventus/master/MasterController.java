package com.service.eventus.master;


import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.service.eventus.event.EventVo;

import jakarta.servlet.http.HttpSession;

import javax.inject.Inject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class MasterController {

    @Inject
    private MasterService masterService;

    List<MasterVo> staff_list;
    List<MasterVo> report_work_list;

    // 메인(관리자)
    @RequestMapping("/main")
    public ModelAndView main(HttpSession session) throws Exception {
        ModelAndView mav = new ModelAndView();


        return mav;
    }

    // 메인(스태프)
    @RequestMapping("/main_ForStaff")
    public ModelAndView main_ForStaff(HttpSession session) throws Exception {
        ModelAndView mav = new ModelAndView();
        String user_id = (String) session.getAttribute("user_id");
        int staff_id = masterService.getUserId(user_id);

        // 근무기록 리스트
        List<MasterVo> report_work_list = masterService.report_work_list_Staff(staff_id);
        mav.addObject("report_work_list", report_work_list);

        return mav;
    }

    @RequestMapping("/manage_staff")
    public ModelAndView manage_staff(@RequestParam(value="page", required=false, defaultValue = "1") int page, HttpSession session) throws Exception {
        // 세션에 저장된 user_id를 가져온다.
        String user_id = (String) session.getAttribute("user_id");
        int staff_size=0; // 페이지 개수 세기(페이징 처리)
        int staff_num; // 페이지 개수 세기(번호 붙히는 용도)
        ModelAndView mav = new ModelAndView();
        staff_list = masterService.getListMemberApp(user_id);
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
    public ModelAndView manage_career_ForStaff(@RequestParam(value="page", required=false, defaultValue = "1") int page, HttpSession session) {
        // 세션에 저장된 user_id를 가져온다.
        String user_id = (String) session.getAttribute("user_id");
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
    public ModelAndView contract_check(@ModelAttribute MasterVo masterVo, HttpSession session) {
        ModelAndView mav = new ModelAndView();
        if(masterVo.getYear() != 0) {
            // 세션에 저장되어 있는 유저 아이디의 id값을 masterVo에 저장
            String user_id = (String) session.getAttribute("user_id");
            int id = masterService.getUserId(user_id);
            masterVo.setStaff_id(id);
            // masterVo의 staff_address 띄어쓰기를 +로 바꾸어 저장
            // masterVo.setStaff_address(masterVo.getStaff_address().replace(" ", "+"));
            mav.addObject("masterVo", masterVo);
            mav.setViewName("/contract_file");
            System.out.println(1);
        }
        else {
            MasterVo masterVo1 = masterService.getContractInfo(masterVo);
            // masterVo.contract_date를 year, month, day로 나누어 int형으로 저장
            String[] contract_date = masterVo1.getContract_date().split("-");

            // contract_date[0~2]을 int형으로 형변환하여 각각 year, month, day에 저장
            int year = Integer.parseInt(contract_date[0]);
            int month = Integer.parseInt(contract_date[1]);
            int day = Integer.parseInt(contract_date[2]);

            masterVo1.setYear(year);
            masterVo1.setMonth(month);
            masterVo1.setDay(day);

            mav.addObject("masterVo", masterVo1);
            mav.setViewName("/contract_file_forview");
            System.out.println(2);
        }


        System.out.println(3);
        return mav;
    }

    // 근로계약서 관련 데이터 저장
    @RequestMapping("/add_contract")
    public ModelAndView add_contract(@ModelAttribute MasterVo masterVo, HttpSession session) {
        ModelAndView mav = new ModelAndView();
        // session에 저장되어 있는 id를 가져와 masterVo에 저장
        String user_id = (String) session.getAttribute("user_id");
        masterVo.setStaff_id(masterService.getUserId(user_id));

        // event_title을 가지고 event_id를 가져와 masterVo에 저장
        // masterVo.setEvent_id(masterService.getEventId(masterVo.getEvent_title()));
        mav.setViewName("/manage_career_forstaff");
        return mav;
    }

    //근무기록 리스트(관리자)
    @GetMapping(value="/report_work")
    public String report_work(@ModelAttribute EventVo eventVo, ModelMap model) throws Exception{
        report_work_list = masterService.report_work_list();

        for(int i = 0; i<=report_work_list.size()-1; i++) {
            String start_time = report_work_list.get(i).getWork_start_time();
            String outing_time = report_work_list.get(i).getWork_outing_time();
            String comeback_time = report_work_list.get(i).getWork_comeback_time();
            String end_time = report_work_list.get(i).getWork_end_time();
            int staff_id = report_work_list.get(i).getId();


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
            } else {
            	 String total = 0 + " : "+ 0;
                 masterService.update_work_total_time(total, staff_id);
            }

        }
        model.addAttribute("report_work_list", report_work_list);
        return "report_work";
    }
    
    @GetMapping(value="/report_work_ForStaff")
	public String report_work_ForStaff(ModelMap model, HttpSession session) {
    	 int id = (int)session.getAttribute("id");
    	 List<MasterVo> report_work_list_Staff = masterService.report_work_list_Staff(id);
    	 
    	 model.addAttribute("report_work_list", report_work_list_Staff);
    	 
		return "report_work_ForStaff";
	}
    // 이력서 등록
    @RequestMapping("/insert_contract")
    @ResponseBody
    public void insert_contract(@ModelAttribute MasterVo masterVo) {
        masterService.insert_contract(masterVo);
    }

    // 직원관리 엑셀 다운로드
    @RequestMapping(value="/staff_excel", method= RequestMethod.GET)
    @ResponseBody
    public void staff_excel(HttpServletResponse response) throws IOException {

        // 엑셀 파일명
        String filename = "staff_list.xlsx";
        // 엑셀 파일 생성(xlsx 확장자)
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("staff_list");
        Row row;
        Cell cell;
        int rowNo = 0;
        int i=1;

        // 헤더 정보 구성
        row = sheet.createRow(rowNo++);
        cell = row.createCell(0);
        cell.setCellValue("No");
        cell = row.createCell(1);
        cell.setCellValue("행사명");
        cell = row.createCell(2);
        cell.setCellValue("이름");
        cell = row.createCell(3);
        cell.setCellValue("성별");
        cell = row.createCell(4);
        cell.setCellValue("생년월일");
        cell = row.createCell(5);
        cell.setCellValue("연락처");
        cell = row.createCell(6);
        cell.setCellValue("가입일자");
        cell = row.createCell(7);
        cell.setCellValue("합격여부");

        // 데이터 부분 생성
        for(MasterVo staff : staff_list) {
            row = sheet.createRow(rowNo++);
            cell = row.createCell(0);
            cell.setCellValue(i++);
            cell = row.createCell(1);
            cell.setCellValue(staff.getEvent_title());
            cell = row.createCell(2);
            cell.setCellValue(staff.getUser_name());
            cell = row.createCell(3);
            cell.setCellValue(staff.getUser_gender());
            cell = row.createCell(4);
            cell.setCellValue(staff.getUser_birth());
            cell = row.createCell(5);
            cell.setCellValue(staff.getUser_phone());
            cell = row.createCell(6);
            cell.setCellValue(staff.getUser_date_join());
            cell = row.createCell(7);

            int pass_check = masterService.checkStaffPasser(staff);
            if(pass_check == 1) {
                cell.setCellValue("합격");
            }
            else {
                cell.setCellValue("불합격");
            }
        }

        // 컨텐트 타입과 파일명 지정
        response.setContentType("ms-vnd/excel");
        response.setHeader("Content-Disposition", "attachment;filename="+filename);

        // 엑셀 출력
        workbook.write(response.getOutputStream());
        workbook.close();
    }
    
    // 근무기록 엑셀 다운로드
    @RequestMapping(value="/report_work_excel", method= RequestMethod.GET)
    @ResponseBody
    public void report_excel_excel(HttpServletResponse response) throws IOException {
        // 엑셀 파일명
        String filename = "report_excel_list.xlsx";
        // 엑셀 파일 내용
        List<MasterVo> report_work_list = masterService.report_work_list();
        // 엑셀 파일 생성(xlsx 확장자)
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("report_work_list");
        Row row;
        Cell cell;
        int rowNo = 0;
        int i=1;

        // 헤더 정보 구성
        row = sheet.createRow(rowNo++);
        cell = row.createCell(0);
        cell.setCellValue("No");
        cell = row.createCell(1);
        cell.setCellValue("일자");
        cell = row.createCell(2);
        cell.setCellValue("행사명");
        cell = row.createCell(3);
        cell.setCellValue("이름");
        cell = row.createCell(4);
        cell.setCellValue("연락처");
        cell = row.createCell(5);
        cell.setCellValue("출근");
        cell = row.createCell(6);
        cell.setCellValue("외출");
        cell = row.createCell(7);
        cell.setCellValue("복귀");
        cell = row.createCell(8);
        cell.setCellValue("퇴근");
        cell = row.createCell(9);
        cell.setCellValue("소계시간");

        // 데이터 부분 생성
        for(MasterVo report_work : report_work_list) {
            row = sheet.createRow(rowNo++);
            cell = row.createCell(0);
            cell.setCellValue(i++);
            cell = row.createCell(1);
            cell.setCellValue(report_work.getWork_date());
            cell = row.createCell(2);
            cell.setCellValue(report_work.getEvent_title());
            cell = row.createCell(3);
            cell.setCellValue(report_work.getUser_name());
            cell = row.createCell(4);
            cell.setCellValue(report_work.getUser_phone());
            cell = row.createCell(5);
            cell.setCellValue(report_work.getWork_start_time());
            cell = row.createCell(6);
            cell.setCellValue(report_work.getWork_outing_time());
            cell = row.createCell(7);
            cell.setCellValue(report_work.getWork_comeback_time());
            cell = row.createCell(8);
            cell.setCellValue(report_work.getWork_end_time());
            cell = row.createCell(9);
            cell.setCellValue(report_work.getWork_total_time());
        }

        // 컨텐트 타입과 파일명 지정
        response.setContentType("ms-vnd/excel");
        response.setHeader("Content-Disposition", "attachment;filename="+filename);

        // 엑셀 출력
        workbook.write(response.getOutputStream());
        workbook.close();
    }
}
