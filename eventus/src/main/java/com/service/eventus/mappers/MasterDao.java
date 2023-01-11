package com.service.eventus.mappers;

import com.service.eventus.master.MasterVo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface MasterDao {

    // 주최한 행사에 지원해서 합격한 근로자들 목록 전부 출력
    @Select("select event_id, staff_id, event_title, user_name, user_gender, user_birth, user_phone, user_date_join from event e" +
            "    inner join staff_application sa on e.id = sa.event_id" +
            "    inner join user u on sa.staff_id = u.id" +
            "    where e.user_id = #{user_id}" +
            "    order by e.event_startDate desc")
    List<MasterVo> getListMemberApp(String user_id);

    // 이력관리 - 지원현황 : 유저 아이디를 가지고 지원한 이벤트 목록 가져오기
    @Select ("select staff_id, event_id, event_title, event_startDate, event_endDate, event_check from event e\n" +
            "inner join staff_application sa on e.id=sa.event_id\n" +
            "inner join user u on sa.staff_id=u.id\n" +
            "where u.user_id='test5'\n" +
            "order by e.event_startDate desc;")
    List<MasterVo> getListUserApp(String user_id);

    // 이벤트 정보 가져오기
    @Select("select * from event where id=#{id}")
    MasterVo getEventInfo(int id);

    // 근로계약서 정보 입력
    @Insert("insert into contract_file (event_id, staff_id, user_name, user_phone, staff_address, identification_number, contract_date) values (#{event_id}, #{staff_id}, #{user_name}, #{user_phone}, #{staff_address}, #{identification_number}, #{contract_date})")
    void insert_contract(MasterVo masterVo);

    // 행사 모집 합격자인지 확인
    @Select("select count(*) from staff_passer where event_id=#{event_id} and staff_id=#{staff_id}")
    int checkStaffPasser(MasterVo masterVo);

    // 근로계약서 등록되어 있는지 확인
    @Select("select count(*) from contract_file where event_id=#{event_id} and staff_id=#{staff_id}")
    int checkContractFile(MasterVo masterVo);
}
