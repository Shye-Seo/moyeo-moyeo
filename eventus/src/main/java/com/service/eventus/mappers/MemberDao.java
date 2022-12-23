package com.service.eventus.mappers;

import com.service.eventus.member.MemberVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Mapper
@Repository
public interface MemberDao {


    MemberVo viewMember(HashMap<String, Object> map);  // 로그인할 때 회원정보 가져올 때


    int idChk(String user_id); // 아이디 중복 체크


    int loginCheck(HashMap<String, Object> map); // 로그인할 때 회원확인(세션등록)


}