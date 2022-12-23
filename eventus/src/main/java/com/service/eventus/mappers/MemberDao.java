package com.service.eventus.mappers;

import com.service.eventus.member.MemberVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface MemberDao {

    @Select("select * from staff_table where staff_id = #{staff_id} and staff_pw = #{staff_pw}")
    MemberVo viewMember(MemberVo memberVo);  // 로그인할 때 회원정보 가져올 때

    @Select("select count(*) from staff_table where staff_id=#{staff_id}")
    int idChk(String user_id); // 아이디 중복 체크

    @Select("select * from staff_table where staff_id = #{staff_id} and staff_pw = #{staff_pw}")
    boolean loginCheck(MemberVo memberVo); // 로그인할 때 회원확인(세션등록)


}