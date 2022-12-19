package com.service.eventus.member;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class MemberDao {

    @Autowired
    private SqlSession sqlSession;

    // 회원가입
    public int memInsert(Map<String, String> map) {
        return sqlSession.insert("user.insertUser",map);
    }

    // 아이디 중복 체크
    public int idChk(String user_id) {
        return sqlSession.selectOne("user.idChk", user_id);
    }

    // 로그인할 때 회원확인
    public boolean loginCheck(MemberVo memberVo) {
        String name = sqlSession.selectOne("user.selectLoginMem",memberVo);

        return  (name == null) ? false : true;
    }

    // 로그인할 때 회원정보 가져올 때
    public MemberVo viewMember(MemberVo memberVo) {
        return sqlSession.selectOne("user.viewMember",memberVo);
    }

    // 아이디 찾기
    public MemberVo findId(String user_phone) throws Exception {
        return sqlSession.selectOne("user.findId",user_phone);
    }

    public int findIdCheck(String user_phone) throws Exception {
        return sqlSession.selectOne("user.findIdCheck",user_phone);
    }

    // 비밀번호 변경
    public int updatePw(MemberVo memberVo) throws Exception {
        return sqlSession.update("user.updatePw",memberVo);
    }

    // 비밀번호 찾기
    public int findPwCheck(MemberVo memberVo) throws Exception {
        return sqlSession.selectOne("user.findPwCheck", memberVo);
    }

    public int findPw(String user_mail, String user_id, String user_pass) throws Exception {
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("user_mail", user_mail);
        map.put("user_id", user_id);
        map.put("user_pass", user_pass);

        return sqlSession.update("user.findPw", map);
    }

    // 마이페이지
    public MemberVo mypage(String user_id) {
        return sqlSession.selectOne("user.mypage",user_id);
    }

    public void mypageUpdate(MemberVo memberVo) {
        sqlSession.update("user.mypageUpdate",memberVo);
    }
}
