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

    public List<MasterVo> getListMemberApp(String user_id) {
        return masterDao.getListMemberApp(user_id);
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
    
    public int report_work_time_update(String work_start_time, String work_end_time, String work_outing_time, String work_comeback_time, int staff_id) {
        return masterDao.report_work_time_update(work_start_time, work_end_time, work_outing_time, work_comeback_time, staff_id);
    }
    
    public List<MasterVo> report_work_list_Staff_main(int staff_id) {
    	//띄어쓰기 여기에서 없애기
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
    
    public List<ApplicationVo> select_app_info(int staff_id){
    	return masterDao.select_app_info(staff_id);
    }
    
    public List<EventVo> select_app_manage(){
    	return masterDao.select_app_manage();
    }
    public List<String> app_profile_list(int event_id){
    	return masterDao.app_profile_list(event_id);
    }
    public List<WorkLogVo> selet_work_log(){
    	return masterDao.selet_work_log();
    }
    
    
}
