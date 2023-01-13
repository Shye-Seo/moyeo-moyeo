package com.service.eventus;

import com.service.eventus.master.MasterService;
import com.service.eventus.master.MasterVo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MainController {

	@Inject
	private MasterService masterService;

    // 최초 진입 시 실행 페이지
	@GetMapping(value="/")
	public String home() {
		return "login";
	}
	
	// 관리자 페이지 링크
	@GetMapping(value="/login")
	public String login() {
		return "login";
	}

	// 회원가입 약관동의 페이지
	@GetMapping(value="/join1")
	public String join() {
		return "join1";
	}

	@GetMapping(value="/join2")
	public String join2() { return "join2"; }

	@GetMapping(value="/join3")
	public String join3() { return "join3"; }

	// 아이디 비멀번호 관련 페이지
	@GetMapping(value="/find_id_pw")
	public String find_id_pw() { return "find_id_pw"; }

	// 관리자 페이지 링크
	@GetMapping(value="/main")
	public String Main() {
		return "main";
	}

	/*
	@GetMapping(value="/manage_event")
	public String manage_event(ModelMap model) {
		List<EventVo> event_list = eventService.event_list();
		model.addAttribute("event_list", event_list);
		return "manage_event";
	}


	@GetMapping(value="/manage_staff")
	public String manage_staff() {
		return "manage_staff";
	}

	@GetMapping(value="/manage_career_ForStaff")
	public String manage_career_ForStaff() {
		return "manage_career_forstaff";
	}
	*/

	@GetMapping(value="/report_work")
	public String report_work() {
		return "report_work";
	}


	// 사용자 페이지 링크
	@GetMapping(value="/main_ForStaff")
	public String Main_ForStaff() {
		return "main_ForStaff";
	}

	@GetMapping(value="/eventList_ForStaff")
	public String eventList_ForStaff() {
		return "eventList_ForStaff";
	}

//	@GetMapping(value="/resume_register")
//	public String resume_register() {
//		return "resume_register";
//	}

	@GetMapping(value="/report_work_ForStaff")
	public String report_work_ForStaff() {
		return "report_work_ForStaff";
	}

	@GetMapping(value="/contract_file")
	public String contract_file() {
		return "contract_file";
	}

	@GetMapping("/excel/download")
	public void excelDownload(HttpServletResponse response) throws IOException {
//        Workbook wb = new HSSFWorkbook();
		String user_id = "cdcd05g";
		// 엑셀 파일명
		String filename = "staff_list.xlsx";
		// 엑셀 파일 내용
		List<MasterVo> staff_list = masterService.getListMemberApp(user_id);
		// 엑셀 파일 생성(xlsx 확장자)
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("staff_list");
		Row row;
		Cell cell;
		int rowNo = 0;

		// 헤더 정보 구성
		row = sheet.createRow(rowNo++);
		cell = row.createCell(0);
		cell.setCellValue("행사명");
		cell = row.createCell(1);
		cell.setCellValue("이름");
		cell = row.createCell(2);
		cell.setCellValue("성별");
		cell = row.createCell(3);
		cell.setCellValue("생년월일");
		cell = row.createCell(4);
		cell.setCellValue("연락처");
		cell = row.createCell(5);
		cell.setCellValue("가입일자");
		cell = row.createCell(6);
		cell.setCellValue("합격여부");

		// 데이터 부분 생성
		for(MasterVo staff : staff_list) {
			row = sheet.createRow(rowNo++);
			cell = row.createCell(0);
			cell.setCellValue(staff.getEvent_title());
			cell = row.createCell(1);
			cell.setCellValue(staff.getUser_name());
			cell = row.createCell(2);
			cell.setCellValue(staff.getUser_gender());
			cell = row.createCell(3);
			cell.setCellValue(staff.getUser_birth());
			cell = row.createCell(4);
			cell.setCellValue(staff.getUser_phone());
			cell = row.createCell(5);
			cell.setCellValue(staff.getUser_date_join());
			cell = row.createCell(6);

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
}