package com.service.eventus.event;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.service.eventus.mappers.EventDao;
import com.service.eventus.member.MemberVo;

import jakarta.validation.OverridesAttribute;

@Service
public class EventService {

	@Autowired
	private EventDao eventDao;
	 
	// 행사현황 리스트
	public List<EventVo> event_list() throws Exception {
		return eventDao.event_list();
	}
	
	// 지원현황 count
	public int application_count(int event_id) throws Exception {
		return eventDao.application_count(event_id);
	}
	
	// 지원현황 지원자 리스트(모집중)
	public List<MemberVo> application_list(int event_id) throws Exception {
		return eventDao.application_list(event_id);
	}
	
	// 행사 상세 조회
	public EventVo viewEventDetail(int event_id) {
		return eventDao.viewEventDetail(event_id);
	}
}
