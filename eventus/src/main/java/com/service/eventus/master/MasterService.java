package com.service.eventus.master;

import com.service.eventus.event.ApplicationVo;
import com.service.eventus.event.EventVo;
import com.service.eventus.mappers.MasterDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class MasterService {

    @Autowired
    private MasterDao masterDao;

    public List<MasterVo> getListMemberApp(String user_id, int startIndex, int pageSize) {
        return masterDao.getListMemberApp(user_id, startIndex, pageSize);
    }

    public List<MasterVo> getListUserApp(String user_id) {
        return masterDao.getListUserApp(user_id);
    }

    public List<MasterVo> report_work_list() {
        return masterDao.report_work_list();
    }

    public int update_work_total_time(String total_time, int staff_id) {
        return masterDao.update_work_total_time(total_time, staff_id);
    } 
    
    public List<MasterVo> report_work_list_Staff(int staff_id) {
        return masterDao.report_work_list_Staff(staff_id);
    }
    
    public List<MasterVo> report_work_list_Staff_main(int staff_id) {
    	return masterDao.report_work_list_Staff_main(staff_id);
    }
    
    public MasterVo getEventInfo(int id) {
        return masterDao.getEventInfo(id);
    }

    public void insert_contract(MasterVo masterVo) {
        masterDao.insert_contract(masterVo);
    }

    public int checkStaffPasser(MasterVo masterVo) {
        return masterDao.checkStaffPasser(masterVo);
    }

    public int checkContractFile(MasterVo masterVo) {
        return masterDao.checkContractFile(masterVo);
    }


    public int getUserId(String user_id) {
        return masterDao.getUserId(user_id);
    }

    public int getEventId(String event_title) {
        return masterDao.getEventId(event_title);
    }

    public MasterVo getContractInfo(MasterVo masterVo) { return masterDao.getContractInfo(masterVo); }
    
    public List<EventVo> select_event_info(){
    	return masterDao.select_event_info();
    }
    
    List<ApplicationVo> select_app_info(int staff_id){
    	return masterDao.select_app_info(staff_id);
    }
    
    List<EventVo> select_app_manage(){
    	return masterDao.select_app_manage();
    }
    List<String> app_profile_list(int event_id){
    	return masterDao.app_profile_list(event_id);
    }

	public int findAllCnt(String user_id) {
		return masterDao.fintAllCnt(user_id);
	}

	public int searchCnt_date(String user_id, String startDate, String endDate) {
		return masterDao.searchCnt_date(user_id, startDate, endDate);
	}

	public List<MasterVo> staff_searchList_date(String user_id, String startDate, String endDate, int startIndex, int pageSize) {
		return masterDao.staff_searchList_date(user_id, startDate, endDate, startIndex, pageSize);
	}

	public int searchCnt(String user_id, String searchKeyword) {
		return masterDao.searchCnt(user_id, searchKeyword);
	}

	public List<MasterVo> staff_searchList(String user_id, String searchKeyword, int startIndex, int pageSize) {
		return masterDao.staff_searchList(user_id, searchKeyword, startIndex, pageSize);
	}

	public int searchCnt_keydate(String user_id, String startDate, String endDate, String searchKeyword) {
		return masterDao.searchCnt_keydate(user_id, startDate, endDate, searchKeyword);
	}

	public List<MasterVo> staff_searchList_keydate(String user_id, String startDate, String endDate, String searchKeyword, int startIndex, int pageSize) {
		return masterDao.staff_searchList_keydate(user_id, startDate, endDate, searchKeyword, startIndex, pageSize);
	}
    
    
}
