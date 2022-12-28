package com.service.eventus.member;

import com.service.eventus.mappers.MemberDao;

import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class MemberService {

    @Autowired
    private MemberDao memberDao;

    // 아이디 중복체크(회원가입)
    public int idChk(String user_id) {
        return memberDao.idChk(user_id);
    }

    // 로그인할 때 회원확인
    public int loginCheck(MemberVo memberVo) {
        int result = memberDao.loginCheck(memberVo);

        if(result==1) { // true일 경우 세션에 등록
            MemberVo memberVo1 = viewMember(memberVo);


        }
        return result;
    }

    // 아이디 찾기(user_id 반환)
    public String findId(MemberVo memberVo) throws Exception {
        return memberDao.findId(memberVo);
    }


    // 로그인할때 회원정보 가져올때
    public MemberVo viewMember(MemberVo memberVo) {
        return memberDao.viewMember(memberVo);
    }

    // coolsms에 정보를 보내 회원가입시 인증번호를 보낸 후, 인증번호를 return 한다.
    public void sendSms(String user_phone, int randomNumber) throws CoolsmsException {
        String api_key = "NCSFGLWNPHJUTG1Q";
        String api_secret = "ZK5DEEDCXR0T6LXJDNQCVCAUYZ1BMHA6";

        Message coolsms = new Message(api_key, api_secret);

        // 4 params(to, from, type, text) are mandatory. must be filled
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("to", user_phone);
        params.put("from", "010-9878-0502"); // 발신전화번호. 테스트시에는 발신, 수신 둘다 본인 번호로 하면됨
        params.put("type", "SMS");
        params.put("text", "[TEST] 인증번호는" + "["+randomNumber+"]" + "입니다."); // 문자 내용 입력

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
