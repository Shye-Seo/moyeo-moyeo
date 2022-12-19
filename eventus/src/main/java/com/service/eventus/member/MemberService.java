package com.service.eventus.member;

import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.util.Map;

@Service
public class MemberService {

    // https://m.blog.naver.com/seek316/222116844258
    @Inject
    private MemberDao memberDao;

    // 회원 가입
    public int memInsert(Map<String, String> map) {
        return memberDao.memInsert(map);
    }

    // 아이디 중복체크
    public int idChk(String user_id) {
        return memberDao.idChk(user_id);
    }

    // 로그인할때 회원확인
    public boolean loginCheck(MemberVo memberVo, HttpSession session) {
        boolean result = memberDao.loginCheck(memberVo);

        if(result) { // true일 경우 세션에 등록
            MemberVo memberVo1 = viewMember(memberVo);

            session.setAttribute("user_id", memberVo1.getUser_id());
            session.setAttribute("id", memberVo1.getId());

        }
        return result;
    }

    // 로그인할때 회원정보 가져올때
    public MemberVo viewMember(MemberVo memberVo) {
        return memberDao.viewMember(memberVo);
    }


    // 아이디 찾기
    public MemberVo findId(String user_phone) throws Exception {
        return memberDao.findId(user_phone);
    }

    public int findIdCheck(String user_phone) throws Exception {
        return memberDao.findIdCheck(user_phone);
    }

    // 비밀번호 찾기 이메일 발송
    public void sendEmail(MemberVo memberVo, String div) throws Exception {

    }

    public void findPw(HttpServletResponse resp, MemberVo memberVo) throws Exception {
        resp.setContentType("text/html;charset=utf-8");
        MemberVo memberDto1 = memberDao.mypage(memberVo.getUser_id()); // mypage꺼 재사용
        PrintWriter out = resp.getWriter();
        // 가입된 아이디가 없으면
        if(memberDao.idChk(memberVo.getUser_id()) == 0) {
            out.print("등록되지 않은 아이디입니다.");
            out.close();
        }
        // 가입된 전화번호가 아니면
        else if(!memberVo.getUser_phone().equals(memberDto1.getUser_phone())) {
            out.print("등록되지 않은 전화번호입니다.");
            out.close();
        }else {
            // 임시 비밀번호 생성
            String pw = "";
            for (int i = 0; i < 12; i++) {
                pw += (char) ((Math.random() * 26) + 97);
            }
            memberVo.setUser_pw(pw);
            // 비밀번호 변경
            memberDao.updatePw(memberVo);
            // 비밀번호 변경 메일 발송
            sendEmail(memberVo, "findpw");

            out.print("이메일로 임시 비밀번호를 발송하였습니다.");
            out.close();
        }
    }

    // 마이페이지
    public MemberVo mypage(String user_id) {
        return memberDao.mypage(user_id);
    }

    public void mypageUpdate(MemberVo memberDto) {
        memberDao.mypageUpdate(memberDto);
    }


}
