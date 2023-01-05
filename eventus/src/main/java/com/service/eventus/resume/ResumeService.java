package com.service.eventus.resume;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.service.eventus.mappers.ResumeDao;
import com.service.eventus.member.MemberVo;

@Service
public class ResumeService {
	
	@Autowired
	ResumeDao resumeDao;
	
	// 이력서 페이지를 위한 유저정보조회
	public MemberVo viewMember_forResume(String user_id) {
		return resumeDao.viewMember_forResume(user_id);
	}
	// 이력서 등록
	public boolean insertResume(ResumeVo resumeVo) {
		return resumeDao.insertResume(resumeVo);
	}
	
	// 이력서 프로필 등록
	boolean insertProfile(int staff_id ,String file_name) {
		return resumeDao.insertProfile(staff_id, file_name);
	}
}