package com.service.eventus.event;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.service.eventus.event.EventDao;

import jakarta.validation.OverridesAttribute;

@Service
public class EventService {

	@Autowired
	private EventDao eventDao;
	 
	// 행사현황 리스트
	public List<EventVo> event_list() throws Exception {
		return eventDao.event_list();
	}
	
	// 행사 상세 조회
	public EventVo viewEventDetail(int event_id) {
		return eventDao.viewEventDetail(event_id);
	}
	
	//행사등록
	public int insertEvent(EventVo eventVo) {
		eventDao.insertEvent(eventVo);
		int id = eventDao.maxEventId();
		return id;
	}
}
