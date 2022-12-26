package com.service.eventus.event;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.service.eventus.mappers.EventDao;

import jakarta.validation.OverridesAttribute;

@Service
public class EventService {

	@Autowired
	private EventDao eventDao;
	 
	// 행사현황 리스트
	public List<EventVo> event_list() throws Exception {
		return eventDao.event_list();
	}
}
