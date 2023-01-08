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
	
	// 이력서 조회
	public ResumeVo selectMyResume(int staff_id) {
		return resumeDao.selectMyResume(staff_id);
	}
	
	// 프로필 조회
	public String selectProfile(int resume_id) {
		return resumeDao.selectProfile(resume_id);
	}
	
	// 이력서 등록 (등록한 이력서 id 출력)
	public int insertResume(ResumeVo resumeVo) {
		resumeDao.insertResume(resumeVo);
		return resumeDao.selectResumeId(resumeVo.getStaff_id());
	}
	
	// 이력서 프로필 등록
	public boolean insertProfile(int staff_id, int resume_id, String file_name) {
		return resumeDao.insertProfile(staff_id, resume_id, file_name);
	}
	
	// 이전 이력서 비활성화
	public boolean disabledPreResume(int staff_id) {
		return resumeDao.disabledPreResume(staff_id);
	}
	
}
