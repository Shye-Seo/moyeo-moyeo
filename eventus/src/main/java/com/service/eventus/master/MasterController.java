package com.service.eventus.master;


import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.service.eventus.event.ApplicationVo;
import com.service.eventus.event.EventVo;
import com.service.eventus.resume.ResumeVo;

import jakarta.servlet.http.HttpSession;

import javax.inject.Inject;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Date;
import java.util.HashMap;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Controller
public class MasterController {

    @Inject
    private MasterService masterService;

    List<MasterVo> staff_list;
    List<MasterVo> career_list;
    List<MasterVo> report_work_list;
    List<MasterVo> report_work_list_Staff;
    
    @RequestMapping("/main")
    public ModelAndView main(HttpSession session) throws Exception {
        ModelAndView mav = new ModelAndView();
        String user_id = (String) session.getAttribute("user_id");
        int staff_id = masterService.getUserId(user_id);
        
     // 오늘 날짜
     		LocalDate now = LocalDate.now();
     		DayOfWeek dayOfWeek = now.getDayOfWeek();
     		String today = now.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
     		today += " ("+dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.KOREAN)+")";
     	

        // 최근행사
        List<EventVo> event_list = masterService.select_event_info();
        // 대기중인지원
        Map<Integer, List<String>> profile_map = new HashMap<Integer, List<String>>();
        List<EventVo> app_list = masterService.select_app_manage();
        for( EventVo eventVo : app_list) {
        	List<String> profile_list = masterService.app_profile_list(eventVo.getId());
        	profile_map.put( eventVo.getId(), profile_list );
        }
        //직원근무기록
        List<WorkLogVo> workLog_list = masterService.selet_work_log();
        
        mav.addObject("today", today);
        mav.addObject("event_list", event_list);
        mav.addObject("app_list", app_list);
        mav.addObject("profile_map", profile_map);
        mav.addObject("workLog_list", workLog_list);
        return mav;
    }

    // 메인(스태프)
    @RequestMapping("/main_ForStaff")
    public ModelAndView main_ForStaff(HttpSession session) throws Exception {
        ModelAndView mav = new ModelAndView();
        String user_id = (String) session.getAttribute("user_id");
        int staff_id = masterService.getUserId(user_id);

        // 근무기록 리스트
        List<MasterVo> report_work_list = masterService.report_work_list_Staff_main(staff_id);
        // 최근행사
        List<EventVo> event_list = masterService.select_event_info();
        // 대기중인지원
        List<ApplicationVo> app_list = masterService.select_app_info(staff_id);
        mav.addObject("report_work_list", report_work_list);
        mav.addObject("event_list", event_list);
        mav.addObject("app_list", app_list);

        return mav;
    }

    @GetMapping("/manage_staff")
    public ModelAndView manage_staff(@RequestParam(defaultValue = "1") int page, HttpSession session, String searchKeyword, String startDate, String endDate, String searchDate) throws Exception {
        // 세션에 저장된 user_id를 가져온다.
        String user_id = (String) session.getAttribute("user_id");
        
        // 오늘 날짜
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        Calendar cal = Calendar.getInstance();
        String today = dateFormat.format(cal.getTime());
        
        int staff_size=0; // 페이지 개수 세기(페이징 처리)
        int staff_num; // 페이지 개수 세기(번호 붙히는 용도)
        ModelAndView mav = new ModelAndView();
        
        //총 게시물 수
        int totalListCnt = masterService.findAllCnt(user_id);
        
        // 생성인자로  총 게시물 수, 현재 페이지를 전달
	    PagingVo pagination = new PagingVo(totalListCnt, page);

	    // DB select start index
	    int startIndex = pagination.getStartIndex();
	    // 페이지 당 보여지는 게시글의 최대 개수
	    int pageSize = pagination.getPageSize();

        staff_list = masterService.getListMemberApp(user_id, startIndex, pageSize);
        
        if(searchKeyword == null && startDate == null && endDate == null) { //키워드&날짜 null (기본상태)
        	
        	staff_list = masterService.getListMemberApp(user_id, startIndex, pageSize);
        	
        	staff_num = staff_list.size();

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
            
        	mav.addObject("pagination", pagination);
	    	
	    }else if(searchKeyword == null && startDate != null && endDate != null) { //날짜검색, 키워드는 null
	    	
	    	totalListCnt = masterService.searchCnt_date(user_id, startDate,endDate);
	    	pagination = new PagingVo(totalListCnt, page);
	    	startIndex = pagination.getStartIndex();
	    	pageSize = pagination.getPageSize();
	    	staff_list = masterService.staff_searchList_date(user_id, startDate, endDate, startIndex, pageSize);
	    	
	    	staff_num = staff_list.size();

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
	        
	    	mav.addObject("pagination", pagination);
	    	mav.addObject("startDate", startDate);
	    	mav.addObject("endDate", endDate);
	    	
	    }else if(searchKeyword != null && searchDate != null){ 
	    	startDate = searchDate.substring(0, 10);
	    	endDate = searchDate.substring(13, 23);
    		
	    	if(startDate.equals(today) && endDate.equals(today)) { //키워드검색, 날짜null처리
	    		
	    		totalListCnt = masterService.searchCnt(user_id, searchKeyword);
	    		pagination = new PagingVo(totalListCnt, page);
	    		startIndex = pagination.getStartIndex();
	    		pageSize = pagination.getPageSize();
	    		staff_list = masterService.staff_searchList(user_id, searchKeyword, startIndex, pageSize);
	    		
	    		staff_num = staff_list.size();

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
	            
	    		mav.addObject("pagination", pagination);
	    		mav.addObject("searchKeyword", searchKeyword);
	    		mav.addObject("searchDate", searchDate);
	    		mav.addObject("today", today);
	    		
	    	}else { // 키워드&날짜 동시검색
	    		System.out.println("=============");
	    		
	    		mav.addObject("searchDate", searchDate);
	    		totalListCnt = masterService.searchCnt_keydate(user_id, startDate, endDate, searchKeyword);
	    		pagination = new PagingVo(totalListCnt, page);
	    		startIndex = pagination.getStartIndex();
	    		pageSize = pagination.getPageSize();
	    		staff_list = masterService.staff_searchList_keydate(user_id, startDate, endDate, searchKeyword, startIndex, pageSize);
	    		
	    		staff_num = staff_list.size();

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
	            
	    		mav.addObject("pagination", pagination);
	    		mav.addObject("startDate", startDate);
	    		mav.addObject("endDate", endDate);
	    		mav.addObject("searchKeyword", searchKeyword);
	    		
	    	}
	    }
	    mav.addObject("searchDate", searchDate);
        
//        List<MasterVo> staff_list_forview = new ArrayList<>();
//        staff_num = staff_list.size();
//
//        // list 객체인 staff_list를 반복문 돌려서 근로계약서 등록했는지 확인
//        for(MasterVo staff: staff_list) {
//            staff.setList_no(staff_num);
//            staff_num--;
//            staff_size++;
//            int pass_check = masterService.checkStaffPasser(staff);
//            if(pass_check == 1) {
//                int contract_check = masterService.checkContractFile(staff);
//                if(contract_check == 1) {
//                    staff.setContract_check(1);
//                } else {
//                    staff.setContract_check(0);
//                }
//            }
//            else {
//                staff.setContract_check(-1);
//            }
//        }

//        // 페이징 처리
//        // String 형인 변수 page를 int형으로 변환하여 page_str에 저장
//        int page_str = Integer.parseInt(String.valueOf(page));
//        // totalPage는 staff_list의 크기를 10으로 나눈 몫에 1을 더한 값
//        int totalPage = (staff_size / 10) + 1;
//        // 시작 페이지
//        int startPage;
//
//        // 보이는 페이지 번호 변경
//        if(page % 10 != 0) { startPage = (page / 10) * 10 + 1; }
//        else { startPage = ((page / 10) - 1) * 10 + 1; }
//
//
//        if(page_str*10>=staff_size) {
//            for(int j=(page_str*10)-9;j<=staff_size;j++) { // 하나의 게시물에 10개의 정보가 들어간다.
//                // 자료형이 MasterVo인 리스트 staff_list에서 j번째 요소를 자료형이 MasterVo인 리스트 staff_list_forview에 추가
//                staff_list_forview.add(staff_list.get(j-1));
//            }
//        }
//        else {
//            for(int j=(page_str*10)-9;j<=page_str*10;j++) { // 하나의 게시물에 10개의 정보가 들어간다.
//                // 자료형이 MasterVo인 리스트 staff_list에서 j번째 요소를 자료형이 MasterVo인 리스트 staff_list_forview에 추가
//                staff_list_forview.add(staff_list.get(j-1));
//            }
//        }


//        mav.addObject("staff_list", staff_list_forview);
//        mav.addObject("totalPage", totalPage);
//        mav.addObject("page", page_str);
//        mav.addObject("startPage", startPage);
        mav.addObject("staff_list", staff_list);
		mav.addObject("nowpage", page);

        return mav;
    }

    @GetMapping("/manage_career_forstaff")
    public ModelAndView manage_career_ForStaff(@RequestParam(defaultValue = "1") int page, HttpSession session, String searchKeyword, String startDate, String endDate, String searchDate) {
        // 세션에 저장된 user_id를 가져온다.
        String user_id = (String) session.getAttribute("user_id");
        
        // 오늘 날짜
        LocalDate now = LocalDate.now();
        
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        
        String today = dateFormat.format(cal.getTime());
        
        int career_size = 0; // 페이지 개수 세기(페이징 처리)
        int career_num; // 페이지 개수 세기(번호 붙히는 용도)
        ModelAndView mav = new ModelAndView();
        
        //총 게시물 수
        int totalListCnt = masterService.findAllCnt_user(user_id);
        
        // 생성인자로  총 게시물 수, 현재 페이지를 전달
	    PagingVo pagination = new PagingVo(totalListCnt, page);

	    // DB select start index
	    int startIndex = pagination.getStartIndex();
	    // 페이지 당 보여지는 게시글의 최대 개수
	    int pageSize = pagination.getPageSize();
	    
        career_list = masterService.getListUserApp(user_id, startIndex, pageSize);

        if(searchKeyword == null && startDate == null && endDate == null) { //키워드&날짜 null (기본상태)
        	
        	career_list = masterService.getListUserApp(user_id, startIndex, pageSize);
        	
        	career_num = career_list.size();
        	
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
                        }else {
                            career.setContract_check(0);
                        }
                    }else {
                        career.setPass_check(0);
                        career.setContract_check(-1);
                    }
                }else {
                    career.setPass_check(-1);
                    career.setContract_check(-1);
                }
            }
            
        	mav.addObject("pagination", pagination);
	    	
	    }else if(searchKeyword == null && startDate != null && endDate != null) { //날짜검색, 키워드는 null
	    	
	    	totalListCnt = masterService.searchUserCnt_date(user_id, startDate,endDate);
	    	pagination = new PagingVo(totalListCnt, page);
	    	startIndex = pagination.getStartIndex();
	    	pageSize = pagination.getPageSize();
	    	career_list = masterService.user_searchList_date(user_id, startDate, endDate, startIndex, pageSize);
	    	
	    	career_num = career_list.size();
	    	
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
	                    }else {
	                        career.setContract_check(0);
	                    }
	                }else {
	                    career.setPass_check(0);
	                    career.setContract_check(-1);
	                }
	            }else {
	                career.setPass_check(-1);
	                career.setContract_check(-1);
	            }
	        }
	    	
	    	mav.addObject("pagination", pagination);
	    	mav.addObject("startDate", startDate);
	    	mav.addObject("endDate", endDate);
	    	
	    }else if(searchKeyword != null && searchDate != null){ 
	    	startDate = searchDate.substring(0, 10);
	    	endDate = searchDate.substring(13, 23);
    		
	    	if(startDate.equals(today) && endDate.equals(today)) { //키워드검색, 날짜null처리
	    		
	    		totalListCnt = masterService.searchUserCnt(user_id, searchKeyword);
	    		pagination = new PagingVo(totalListCnt, page);
	    		startIndex = pagination.getStartIndex();
	    		pageSize = pagination.getPageSize();
	    		career_list = masterService.user_searchList(user_id, searchKeyword, startIndex, pageSize);
	    		
	    		career_num = career_list.size();
	    		
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
	                        }else {
	                            career.setContract_check(0);
	                        }
	                    }else {
	                        career.setPass_check(0);
	                        career.setContract_check(-1);
	                    }
	                }else {
	                    career.setPass_check(-1);
	                    career.setContract_check(-1);
	                }
	            }
	    		
	    		mav.addObject("pagination", pagination);
	    		mav.addObject("searchKeyword", searchKeyword);
	    		mav.addObject("searchDate", searchDate);
	    		mav.addObject("today", today);
	    		
	    	}else { // 키워드&날짜 동시검색
	    		
	    		mav.addObject("searchDate", searchDate);
	    		totalListCnt = masterService.searchUserCnt_keydate(user_id, startDate, endDate, searchKeyword);
	    		pagination = new PagingVo(totalListCnt, page);
	    		startIndex = pagination.getStartIndex();
	    		pageSize = pagination.getPageSize();
	    		career_list = masterService.user_searchList_keydate(user_id, startDate, endDate, searchKeyword, startIndex, pageSize);
	    		
	    		career_num = career_list.size();
	    		
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
	                        }else {
	                            career.setContract_check(0);
	                        }
	                    }else {
	                        career.setPass_check(0);
	                        career.setContract_check(-1);
	                    }
	                }else {
	                    career.setPass_check(-1);
	                    career.setContract_check(-1);
	                }
	            }
	    		
	    		mav.addObject("pagination", pagination);
	    		mav.addObject("startDate", startDate);
	    		mav.addObject("endDate", endDate);
	    		mav.addObject("searchKeyword", searchKeyword);
	    		
	    	}
	    }
	    mav.addObject("searchDate", searchDate);
        
        // list 객체인 career_list를 반복문 돌려서 행사 모집중 | 합격, 불합격 | 이력서 등록, 미등록 여부 확인
//        for (MasterVo career : career_list) {
//            career.setList_no(career_num);
//            career_num--;
//            career_size++;
//            if(career.getEvent_check() == 1) {
//                int pass_check = masterService.checkStaffPasser(career);
//                if(pass_check == 1) { // 있으면
//                    career.setPass_check(1);
//                    int contract_check = masterService.checkContractFile(career);
//                    if(contract_check == 1) { // 있으면
//                        career.setContract_check(1);
//                    }
//                    else {
//                        career.setContract_check(0);
//                    }
//                }
//                else {
//                    career.setPass_check(0);
//                    career.setContract_check(-1);
//                }
//            }
//            else {
//                career.setPass_check(-1);
//                career.setContract_check(-1);
//            }
//        }

        // 페이징 처리
        // String 형인 변수 page를 int형으로 변환하여 page_str에 저장
//        int page_str = Integer.parseInt(String.valueOf(page));
//        // totalPage는 staff_list의 크기를 10으로 나눈 몫에 1을 더한 값
//        int totalPage = (career_size / 10) + 1;
//        // 시작 페이지
//        int startPage;

        // 보이는 페이지 번호 변경
//        if(page % 10 != 0) { startPage = (page / 10) * 10 + 1; }
//        else { startPage = ((page / 10) - 1) * 10 + 1; }
//
//        if(page_str*10>=career_size) {
//            for(int j=(page_str*10)-9;j<=career_size;j++) { // 하나의 게시물에 10개의 정보가 들어간다.
//                // 자료형이 MasterVo인 리스트 career_list에서 j번째 요소를 자료형이 MasterVo인 리스트 career_list_forview에 추가
//                career_list_forview.add(career_list.get(j-1));
//            }
//        }
//        else {
//            for(int j=(page_str*10)-9;j<=page_str*10;j++) { // 하나의 게시물에 10개의 정보가 들어간다.
//                // 자료형이 MasterVo인 리스트 career_list에서에서 j번째 요소를 자료형이 MasterVo인 리스트 career_list_forview에 추가
//                career_list_forview.add(career_list.get(j-1));
//            }
//        }
//
//        mav.addObject("career_list", career_list_forview);
//        mav.addObject("totalPage", totalPage);
//        mav.addObject("page", page_str);
//        mav.addObject("startPage", startPage);
        mav.addObject("career_list", career_list);
		mav.addObject("nowpage", page);
        
        return mav;
    }

    // 근로계약서 작성을 위한 이벤트 정보 가져오기
    @RequestMapping("/getEventInfo")
    @ResponseBody
    public Map getEventInfo(@RequestParam("id") int event_id, HttpSession session) {
    	// 세션에 저장된 user_id를 가져온다.
        int user_id =  (int) session.getAttribute("id");
        
    	//이벤트 정보 가져오기
        MasterVo masterVo = masterService.getEventInfo(event_id);
        //스태프 지원정보 가져오기 (이력서번호, 지원포지션명)
        Map resumePosition = masterService.getResumeInfo(event_id,user_id);
        
        //이력서 내용가져오기 > resumeVo형식
        int resume_id = (int) resumePosition.get("resume_id");
        ResumeVo resumeData = masterService.selectResume(resume_id);
        
        //이벤트 정보 안 포지션, 시급정보 가져오기
        String[] pays = masterVo.getEvent_position_pay().split(",");
        String[] positions = masterVo.getEvent_position().split(",");
        //지원한 포지션정보
        String staff_position = (String) resumePosition.get("staff_position");
        
        //포지션 위치
        int myPositionsIndex = Arrays.asList(positions).indexOf(staff_position);
        //스태프의 포지션에 맞는 시급
        String myPay = pays[myPositionsIndex];
        
        Map wrapMap = new HashMap();
        wrapMap.put("eventInfo", masterVo);
        wrapMap.put("resumeData", resumeData);
        wrapMap.put("myPay", myPay);
        wrapMap.put("name", session.getAttribute("user_name"));
        return wrapMap;
    }

    // 근로확인서 및 보안각서 최종 확인 및 저장하기
    @RequestMapping(value="/contract_check", method= {RequestMethod.POST,RequestMethod.GET})
    public ModelAndView contract_check(@ModelAttribute MasterVo masterVo, HttpSession session) {
        ModelAndView mav = new ModelAndView();
        if(masterVo.getYear() != 0) {
            // 세션에 저장되어 있는 유저 아이디의 id값을 masterVo에 저장
            String user_id = (String) session.getAttribute("user_id");
            int id = masterService.getUserId(user_id);
            masterVo.setStaff_id(id);
            mav.addObject("masterVo", masterVo);
            mav.setViewName("/contract_file"); 
            System.out.println(1);
        }
        else {//조회용
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
    

    //근무기록 리스트(관리자) -> DB와 비교하기 위해 날짜형태(0000-00-00) 변경 필요
    @GetMapping(value="/report_work")
    public String report_work(@ModelAttribute EventVo eventVo, ModelMap model, @RequestParam(defaultValue = "1") int page, String searchKeyword, String startDate, String endDate, String searchDate) throws Exception{
    	
    	 // 오늘 날짜
        LocalDate now = LocalDate.now();
        
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String today = dateFormat.format(cal.getTime());
        if(searchDate!=null && searchDate.equals(today+" - "+today)) {
	    	  searchDate=null;
	      }
        
        
        //총 게시물 수
        int totalListCnt = masterService.CntAll_work();
        
        // 생성인자로  총 게시물 수, 현재 페이지를 전달
	    PagingVo pagination = new PagingVo(totalListCnt, page);

	    // DB select start index
	    int startIndex = pagination.getStartIndex();
	    // 페이지 당 보여지는 게시글의 최대 개수
	    int pageSize = pagination.getPageSize();
	    
        report_work_list = masterService.report_work_list_paging(startIndex, pageSize);

        if(searchKeyword == null && startDate == null && endDate == null) { //키워드&날짜 null (기본상태)
    		report_work_list = masterService.report_work_list_paging(startIndex, pageSize);
 	    	model.addAttribute("pagination", pagination);
 	    	
 	    }else if(searchKeyword == null && startDate != null && endDate != null) { //날짜검색, 키워드는 null
// 	    	startDate = startDate.substring(0, 4) + "-" + startDate.substring(5, 7) + "-" + startDate.substring(8, 10);
// 	    	endDate = endDate.substring(0, 4) + "-" + endDate.substring(5, 7) + "-" + endDate.substring(8, 10);
 	    	
 	    	totalListCnt = masterService.WorkSearchCnt_date(startDate, endDate);
 	    	pagination = new PagingVo(totalListCnt, page);
 	    	startIndex = pagination.getStartIndex();
 	    	pageSize = pagination.getPageSize();
 	    	report_work_list = masterService.Work_SearchList_date(startDate, endDate, startIndex, pageSize);
 	    	model.addAttribute("pagination", pagination);
 	    	model.addAttribute("startDate", startDate);
 	    	model.addAttribute("endDate", endDate);
 	    	
 	    }else if(searchKeyword != null && searchDate != null){ 
 	    	startDate = searchDate.substring(0, 10);
 	    	endDate = searchDate.substring(13, 23);
 	    	
 	    	if(startDate.equals(today.replace("-", ".")) && endDate.equals(today.replace("-", "."))) { //키워드검색, 날짜null처리
 	    		totalListCnt = masterService.WorkSearchCnt_key(searchKeyword);
 	    		pagination = new PagingVo(totalListCnt, page);
 	    		startIndex = pagination.getStartIndex();
 	    		pageSize = pagination.getPageSize();
 	    		report_work_list = masterService.Work_SearchList(searchKeyword, startIndex, pageSize);
 	    		model.addAttribute("pagination", pagination);
 	    		model.addAttribute("searchKeyword", searchKeyword);
 	    		model.addAttribute("searchDate", searchDate);
 	    		model.addAttribute("today", today);
 	    		
 	    	}else { // 키워드&날짜 동시검색
 	    		model.addAttribute("searchDate", searchDate);
 	    		totalListCnt = masterService.WorkSearchCnt_keydate(startDate, endDate, searchKeyword);
 	    		pagination = new PagingVo(totalListCnt, page);
 	    		startIndex = pagination.getStartIndex();
 	    		pageSize = pagination.getPageSize();
 	    		report_work_list = masterService.Work_SearchList_keydate(startDate, endDate, searchKeyword, startIndex, pageSize);
 	    		model.addAttribute("pagination", pagination);
 	    		model.addAttribute("startDate", startDate);
 	    		model.addAttribute("endDate", endDate);
 	    		model.addAttribute("searchKeyword", searchKeyword);
 	    		
 	    	}
 	    }
 	    model.addAttribute("searchDate", searchDate);
 	    
        for(int i = 0; i<=report_work_list.size()-1; i++) {
            String start_time = report_work_list.get(i).getWork_start_time();
            String outing_time = report_work_list.get(i).getWork_outing_time();
            String comeback_time = report_work_list.get(i).getWork_comeback_time();
            String end_time = report_work_list.get(i).getWork_end_time();
            int staff_id = report_work_list.get(i).getId();
            
            if(start_time != null && end_time != null && end_time != "") {
                SimpleDateFormat sdf = new SimpleDateFormat("HH : mm");

                Date start = sdf.parse(start_time);
                Date end = sdf.parse(end_time);

                long timeMil_start = start.getTime();
                long timeMil_end = end.getTime();


                long diff = timeMil_end - timeMil_start;

                long diffSec = diff / 1000;

                if(outing_time != null && outing_time != "" && comeback_time != null ) {
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
                    
                    String total = "";
                    if(hour < 10 && minute < 10) {
                    	total = "0"+hour+" : 0"+minute;
                    }else if(hour < 10 && minute >= 10) {
                    	total = "0"+hour+" : "+minute;
                    }else if(hour >= 10 && minute < 10) {
                    	total = ""+hour+" : 0"+minute;
                    }else { total = ""+hour+" : "+minute; }
//                    String total = hour + " : "+ minute;
                    masterService.update_work_total_time(total, staff_id);

                } else {
                    int hour = (int)diffSec/(60*60);
                    int minute = (int)diffSec/60-(hour*60);

                    String total = "";
                    if(hour < 10 && minute < 10) {
                    	total = "0"+hour+" : 0"+minute;
                    }else if(hour < 10 && minute >= 10) {
                    	total = "0"+hour+" : "+minute;
                    }else if(hour >= 10 && minute < 10) {
                    	total = ""+hour+" : 0"+minute;
                    }else { total = ""+hour+" : "+minute; }
//                    String total = hour + " : "+ minute;
                    masterService.update_work_total_time(total, staff_id);
                }
            } else {
            	 String total = "00 : 00";
                 masterService.update_work_total_time(total, staff_id);
            }

        }
        model.addAttribute("report_work_list", report_work_list);
        model.addAttribute("nowpage", page);
        return "report_work";
    }
    
//    근무기록 리스트(스태프) -> DB와 비교하기 위해 날짜형태(0000-00-00) 변경 필요
    @GetMapping(value="/report_work_ForStaff")
	public String report_work_ForStaff(ModelMap model, HttpSession session, @RequestParam(defaultValue = "1") int page, String searchKeyword, String startDate, String endDate, String searchDate) {
    	
    	 int id = (int)session.getAttribute("id");
    	 
    	 // 오늘 날짜
         LocalDate now = LocalDate.now();
         
         Calendar cal = Calendar.getInstance();
         SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
         String today = dateFormat.format(cal.getTime());
         
         //총 게시물 수
         int totalListCnt = masterService.CntAll_staffwork(id);
         
         // 생성인자로  총 게시물 수, 현재 페이지를 전달
 	     PagingVo pagination = new PagingVo(totalListCnt, page);

 	    // DB select start index
 	    int startIndex = pagination.getStartIndex();
 	    // 페이지 당 보여지는 게시글의 최대 개수
 	    int pageSize = pagination.getPageSize();
         
    	 report_work_list_Staff = masterService.report_work_list_Staff(id, startIndex, pageSize);
    	 
    	 if(searchKeyword == null && startDate == null && endDate == null) { //키워드&날짜 null (기본상태)
    		report_work_list_Staff = masterService.report_work_list_Staff(id, startIndex, pageSize);
 	    	model.addAttribute("pagination", pagination);
 	    	
 	    }else if(searchKeyword == null && startDate != null && endDate != null) { //날짜검색, 키워드는 null
 	    	startDate = startDate.substring(0, 4) + "-" + startDate.substring(5, 7) + "-" + startDate.substring(8, 10);
 	    	endDate = endDate.substring(0, 4) + "-" + endDate.substring(5, 7) + "-" + endDate.substring(8, 10);
 	    	
 	    	totalListCnt = masterService.worksearchCnt_date(id, startDate, endDate);
 	    	pagination = new PagingVo(totalListCnt, page);
 	    	startIndex = pagination.getStartIndex();
 	    	pageSize = pagination.getPageSize();
 	    	report_work_list_Staff = masterService.staffwork_searchList_date(id, startDate, endDate, startIndex, pageSize);
 	    	model.addAttribute("pagination", pagination);
 	    	model.addAttribute("startDate", startDate);
 	    	model.addAttribute("endDate", endDate);
 	    	
 	    }else if(searchKeyword != null && searchDate != null){ 
 	    	startDate = searchDate.substring(0, 10);
 	    	endDate = searchDate.substring(13, 23);
 	    	
 	    	startDate = startDate.substring(0, 4) + "-" + startDate.substring(5, 7) + "-" + startDate.substring(8, 10);
 	    	endDate = endDate.substring(0, 4) + "-" + endDate.substring(5, 7) + "-" + endDate.substring(8, 10);
     		
 	    	if(startDate.equals(today) && endDate.equals(today)) { //키워드검색, 날짜null처리
 	    		totalListCnt = masterService.worksearchCnt_key(id, searchKeyword);
 	    		pagination = new PagingVo(totalListCnt, page);
 	    		startIndex = pagination.getStartIndex();
 	    		pageSize = pagination.getPageSize();
 	    		report_work_list_Staff = masterService.staffwork_searchList(id, searchKeyword, startIndex, pageSize);
 	    		model.addAttribute("pagination", pagination);
 	    		model.addAttribute("searchKeyword", searchKeyword);
 	    		model.addAttribute("searchDate", searchDate);
 	    		model.addAttribute("today", today);
 	    		
 	    	}else { // 키워드&날짜 동시검색
 	    		model.addAttribute("searchDate", searchDate);
 	    		totalListCnt = masterService.worksearchCnt_keydate(id, startDate, endDate, searchKeyword);
 	    		pagination = new PagingVo(totalListCnt, page);
 	    		startIndex = pagination.getStartIndex();
 	    		pageSize = pagination.getPageSize();
 	    		report_work_list_Staff = masterService.staffwork_searchList_keydate(id, startDate, endDate, searchKeyword, startIndex, pageSize);
 	    		model.addAttribute("pagination", pagination);
 	    		model.addAttribute("startDate", startDate);
 	    		model.addAttribute("endDate", endDate);
 	    		model.addAttribute("searchKeyword", searchKeyword);
 	    		
 	    	}
 	    }
 	    model.addAttribute("searchDate", searchDate);
    	 
    	model.addAttribute("report_work_list", report_work_list_Staff);
    	model.addAttribute("nowpage", page);
    	
		return "report_work_ForStaff";
	}
    
    //  근무기록 리스트 시간 수정(관리자)
    @RequestMapping(value = "/update_reportwork_time")
	public String report_work_time_update(@RequestParam(value = "numb", required = false) String num,
			@RequestParam(value = "start", required = false) String work_start_time,
			@RequestParam(value = "end", required = false) String work_end_time,
			@RequestParam(value = "out", required = false) String work_outing_time,
			@RequestParam(value = "back", required = false) String work_comeback_time) {

		int staff_id = Integer.parseInt(num);
		
		if(work_start_time == "") {
			work_start_time = null;
		} else if(work_end_time == "") {
			work_end_time = null;
		} else if(work_outing_time == "") {
			work_outing_time = null;
		} else if(work_comeback_time == "") {
			work_comeback_time = null;
		}

		masterService.report_work_time_update(work_start_time, work_end_time, work_outing_time, work_comeback_time, staff_id);

		return "redirect:/report_work";
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
    public void staff_excel(HttpServletResponse response, HttpSession session, String searchKeyword, String startDate, String endDate, String searchDate) throws IOException {

		String user_id = (String) session.getAttribute("user_id");
        
        // 엑셀 파일 생성(xlsx 확장자)
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("staff_list");
        
//        엑셀스타일
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Font Bold = workbook.createFont();
        Bold.setBold(true);
        style.setFont(Bold);
//        
        
        Row row;
        Cell cell;
        int rowNo = 0;
        int i=1;

		if(searchKeyword == "") {searchKeyword = null;}
        // 헤더 정보 구성
        row = sheet.createRow(rowNo++);
        cell = row.createCell(0);
        cell.setCellValue("No");
        cell.setCellStyle(style); //스타일적용
        cell = row.createCell(1);
        cell.setCellValue("행사명");
        cell.setCellStyle(style);
        cell = row.createCell(2);
        cell.setCellValue("근무기간");
        cell.setCellStyle(style);
        cell = row.createCell(3);
        cell.setCellValue("이름");
        cell.setCellStyle(style);
        cell = row.createCell(4);
        cell.setCellValue("성별");
        cell.setCellStyle(style);
        cell = row.createCell(5);
        cell.setCellValue("생년월일");
        cell.setCellStyle(style);
        cell = row.createCell(6);
        cell.setCellValue("연락처");
        cell.setCellStyle(style);
        cell = row.createCell(7);
        cell.setCellValue("합격여부");
        cell.setCellStyle(style);
       
        
        //열 길이 바꾸기
        for(int a =0; a<8;a++) {
        	sheet.autoSizeColumn(a);
        	if(a==0) {
        		sheet.setColumnWidth(a, (sheet.getColumnWidth(a))+(short)1000);
        	}else if(a==1) {
        		sheet.setColumnWidth(a, (sheet.getColumnWidth(a))+(short)7000);
        	}else if(a==2) {
        		sheet.setColumnWidth(a, (sheet.getColumnWidth(a))+(short)5000);
        	}else {
        		sheet.setColumnWidth(a, (sheet.getColumnWidth(a))+(short)3000);
        	}
        	
        }
        

		// 오늘 날짜 (오늘-오늘검색일 시)
	      LocalDate now = LocalDate.now();
	      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
	      Calendar cal = Calendar.getInstance();
	      String today = dateFormat.format(cal.getTime());
	      if(searchDate!=null && searchDate.equals(today+" - "+today)) {
	    	  searchDate=null;
	      }
	   // 엑셀 파일명
	        String filename = today+"_staff_list.xlsx";

		staff_list = masterService.staff_findDownloadList(user_id);

		if(searchKeyword == null && searchDate == null) { //키워드&날짜 null (기본상태)
			staff_list = masterService.staff_findDownloadList(user_id);

		}else if(searchKeyword == null && searchDate != null) { //날짜검색, 키워드는 null
			startDate = searchDate.substring(0, 10);
			endDate = searchDate.substring(13, 23);

			staff_list = masterService.staff_Downloaddate(user_id, startDate, endDate);
			filename = today+"_staff_excel_"+startDate+"_"+endDate+".xlsx";

		}else if(searchKeyword != null && searchDate == null) { //키워드검색, 날짜null처리
			staff_list = masterService.staff_Downloadkey(user_id, searchKeyword);
			filename = new String((today+"_staff_excel_"+searchKeyword+".xlsx").getBytes("UTF-8"),"ISO-8859-1");

		}else if(searchKeyword != null && searchDate != null){ // 키워드&날짜 동시검색
			startDate = searchDate.substring(0, 10);
			endDate = searchDate.substring(13, 23);

			staff_list = masterService.staff_Downloadkeydate(user_id, startDate, endDate, searchKeyword);
			filename =  new String((today+"_staff_excel_"+startDate+"_"+endDate+"_"+searchKeyword+".xlsx").getBytes("UTF-8"),"ISO-8859-1");
		}

        // 데이터 부분 생성
        for(MasterVo staff : staff_list) {
            row = sheet.createRow(rowNo++);
            cell = row.createCell(0);
            cell.setCellValue(i++);
            cell = row.createCell(1);
            cell.setCellValue(staff.getEvent_title());
            cell = row.createCell(2);
            cell.setCellValue(staff.getEvent_startDate() + "-" +staff.getEvent_endDate());
            cell = row.createCell(3);
            cell.setCellValue(staff.getUser_name());
            cell = row.createCell(4);
            cell.setCellValue(staff.getUser_gender());
            cell = row.createCell(5);
            String birth = staff.getUser_birth().replaceAll("-", ".");
            cell.setCellValue(birth);
            cell = row.createCell(6);
            String regEx = "(\\d{3})(\\d{3,4})(\\d{4})";
			String staff_phone = staff.getUser_phone().replaceAll(regEx, "$1-$2-$3");
            cell.setCellValue(staff_phone);
            cell = row.createCell(7);

            int pass_check = masterService.checkStaffPasser(staff);
            int staff_result = masterService.getStaffResult(staff.getStaff_id(), staff.getEvent_id());
            System.out.println("staff_result : "+staff_result);
            
            if(pass_check == 1) {
                cell.setCellValue("합격");
            }
            else if(staff_result == 0){
                cell.setCellValue("대기중");
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
    public void report_excel_excel(HttpServletResponse response, String searchKeyword, String startDate, String endDate, String searchDate) throws IOException {
        
        // 엑셀 파일 내용
        // 엑셀 파일 생성(xlsx 확장자)
        Workbook workbook = new XSSFWorkbook();
        
	//      엑셀스타일
	      CellStyle style = workbook.createCellStyle();
	      style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
	      style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	      Font Bold = workbook.createFont();
	      Bold.setBold(true);
	      style.setFont(Bold);
	//      
	      
        Sheet sheet = workbook.createSheet("report_work_list");
        Row row;
        Cell cell;
        int rowNo = 0;
        int i=1;

		if(searchKeyword == "") {searchKeyword = null;}
        // 헤더 정보 구성
        row = sheet.createRow(rowNo++);
        cell = row.createCell(0);
        cell.setCellValue("No");
        cell.setCellStyle(style);
        cell = row.createCell(1);
        cell.setCellValue("일자");
        cell.setCellStyle(style);
        cell = row.createCell(2);
        cell.setCellValue("행사명");
        cell.setCellStyle(style);
        cell = row.createCell(3);
        cell.setCellValue("이름");
        cell.setCellStyle(style);
        cell = row.createCell(4);
        cell.setCellValue("연락처");
        cell.setCellStyle(style);
        cell = row.createCell(5);
        cell.setCellValue("출근");
        cell.setCellStyle(style);
        cell = row.createCell(6);
        cell.setCellValue("외출");
        cell.setCellStyle(style);
        cell = row.createCell(7);
        cell.setCellValue("복귀");
        cell.setCellStyle(style);
        cell = row.createCell(8);
        cell.setCellValue("퇴근");
        cell.setCellStyle(style);
        cell = row.createCell(9);
        cell.setCellValue("소계시간");
        cell.setCellStyle(style);
        
        //열 길이 바꾸기
        for(int a =0; a<=9;a++) {
        	sheet.autoSizeColumn(a);
        	if(a==0) {
        		sheet.setColumnWidth(a, (sheet.getColumnWidth(a))+(short)1000);
        	}else if(a==1) {
        		sheet.setColumnWidth(a, (sheet.getColumnWidth(a))+(short)4000);
        	}else if(a==2) {
        		sheet.setColumnWidth(a, (sheet.getColumnWidth(a))+(short)7000);
        	}else {
        		sheet.setColumnWidth(a, (sheet.getColumnWidth(a))+(short)2500);
        	}
        	
        }

		// 오늘 날짜 (오늘-오늘검색일 시)
	      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
	      Calendar cal = Calendar.getInstance();
	      String today = dateFormat.format(cal.getTime());
	      if(searchDate!=null && searchDate.equals(today+" - "+today)) {
	    	  searchDate=null;
	      }
	   // 엑셀 파일명
	    String filename = today+"_report_excel_list.xlsx";

		report_work_list = masterService.report_work_findDownloadList();

		if(searchKeyword == null && searchDate == null) { //키워드&날짜 null (기본상태)
			report_work_list = masterService.report_work_findDownloadList();
//		    	System.out.println("기본");

		}else if(searchKeyword == null && searchDate != null) { //날짜검색, 키워드는 null
			startDate = searchDate.substring(0, 10);
			endDate = searchDate.substring(13, 23);

			report_work_list = masterService.report_work_Downloaddate(startDate, endDate);
//		    	System.out.println("날짜");
			filename = "report_work_excel_"+startDate+"_"+endDate+".xlsx";

		}else if(searchKeyword != null && searchDate == null) { //키워드검색, 날짜null처리
//			 System.out.println("키워드");
			report_work_list = masterService.report_work_Downloadkey(searchKeyword);
			filename = new String(("staff_excel_"+searchKeyword+".xlsx").getBytes("UTF-8"),"ISO-8859-1");

		}else if(searchKeyword != null && searchDate != null){ // 키워드&날짜 동시검색
			startDate = searchDate.substring(0, 10);
			endDate = searchDate.substring(13, 23);

			report_work_list = masterService.report_work_Downloadkeydate(startDate, endDate, searchKeyword);
//		    	System.out.println("동시");
			filename =  new String(("staff_excel_"+startDate+"_"+endDate+"_"+searchKeyword+".xlsx").getBytes("UTF-8"),"ISO-8859-1");
		}

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
            String regEx = "(\\d{3})(\\d{3,4})(\\d{4})";
			String staff_phone = report_work.getUser_phone().replaceAll(regEx, "$1-$2-$3");
            cell.setCellValue(staff_phone);
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
