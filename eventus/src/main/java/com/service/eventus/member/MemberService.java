package com.service.eventus.member;

import com.service.eventus.mappers.MemberDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
public class MemberService {

    @Autowired
    private MemberDao memberDao;

    // 아이디 중복체크
    public int idChk(String user_id) {
        return memberDao.idChk(user_id);
    }

    // 로그인할 때 회원확인
    public boolean loginCheck(MemberVo memberVo) {
        boolean result = memberDao.loginCheck(memberVo);

        if(result) { // true일 경우 세션에 등록
            MemberVo memberVo1 = viewMember(memberVo);


        }
        return result;
    }

    // 로그인할때 회원정보 가져올때
    public MemberVo viewMember(MemberVo memberVo) {
        return memberDao.viewMember(memberVo);
    }
}
