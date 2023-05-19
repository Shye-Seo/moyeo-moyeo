package com.service.eventus.member;

import java.util.HashMap;
import java.util.Map;



import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.service.eventus.mappers.MemberDao;

import jakarta.servlet.http.HttpSession;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;

@Service
public class MemberService {

    @Autowired
    private MemberDao memberDao;


    // 회원가입
    public int insertUser(MemberVo memberVo) {
        return memberDao.insertUser(memberVo);
    }

    // 아이디 중복체크(회원가입)
    public int idChk(String user_id) {
        return memberDao.idChk(user_id);
    }

    // 로그인할 때 회원확인
    public int loginCheck(MemberVo memberVo, HttpSession session) {
        int result = memberDao.loginCheck(memberVo);

        if(result==1) { // true일 경우 세션에 등록
            MemberVo memberVo1 = viewMember(memberVo);
            
            int id = memberVo1.getId();
            String myProfile = memberDao.select_myProfile(id);
            
            session.setAttribute("user_id", memberVo1.getUser_id());
            session.setAttribute("user_name", memberVo1.getUser_name());
            session.setAttribute("id",id);
            session.setAttribute("myProfile", myProfile);
            session.setAttribute("authority", memberVo1.getUser_authority());

        }
        return result;
    }
    
    

    // 아이디 찾기(user_id 반환)
    public Map findId(MemberVo memberVo) throws Exception {
        return memberDao.findId(memberVo);
    }

    // 비밀번호 변경을 위한 아이디 찾기(user_id 반환)
    public String findIdForPw(MemberVo memberVo) throws Exception {
        return memberDao.findIdForPw(memberVo);
    }

    // 비밀번호 변경
    public int updatePw(MemberVo memberVo) throws Exception {
        return memberDao.updatePw(memberVo);
    }
    
    // 비밀번호 변경을 위한 비밀번호 조회
    public String selectPw(MemberVo memberVo) throws Exception {
        return memberDao.selectPw(memberVo);
    }

    // 휴대전화 번호 변경
    public void updatePhone(MemberVo memberVo) throws Exception {
        memberDao.updatePhone(memberVo);
    }

    // 로그인할때 회원정보 가져올때
    public MemberVo viewMember(MemberVo memberVo) {
        return memberDao.viewMember(memberVo);
    }

    // coolsms에 정보를 보내 회원가입시 인증번호를 보낸 후, 인증번호를 return 한다.
    public void sendSms(String user_phone, int randomNumber) throws CoolsmsException {
        String api_key = "NCS9HI923SUSM5VF";
        String api_secret = "XLQEOJRXNGAIUHIFRGX5VUTCHEJV7D8N";

        Message coolsms = new Message(api_key, api_secret);

        // 4 params(to, from, type, text) are mandatory. must be filled
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("to", user_phone);
        params.put("from", "010-9878-0502"); // 발신전화번호. 테스트시에는 발신, 수신 둘다 본인 번호로 하면됨
        params.put("type", "SMS");
        params.put("text", "[모여모여] 인증번호는" + "["+randomNumber+"]" + "입니다."); // 문자 내용 입력

        // send() 를 통해 전송
        try {
            JSONObject obj = (JSONObject) coolsms.send(params);
            System.out.println(obj.toString());
        }catch(CoolsmsException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCode());
        }
    }
}
