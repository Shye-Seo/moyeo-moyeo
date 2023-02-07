package com.service.eventus.mappers;

import com.service.eventus.event.ApplicationVo;
import com.service.eventus.event.EventVo;
import com.service.eventus.master.MasterVo;
import com.service.eventus.master.WorkLogVo;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface MasterDao {

    // 주최한 행사에 지원해서 합격한 근로자들 목록 전부 출력
    @Select("select event_id, staff_id, resume_id, event_title, user_name, user_gender, user_birth, user_phone, user_date_join, event_startDate, event_endDate from event e" +
            "    inner join staff_application sa on e.id = sa.event_id" +
            "    inner join user u on sa.staff_id = u.id" +
            "    where e.user_id = #{user_id}" +
            "    order by e.event_startDate desc, sa.id desc limit #{startIndex}, #{pageSize}")
    List<MasterVo> getListMemberApp(String user_id, int startIndex, int pageSize);

    // 이력관리 - 지원현황 : 유저 아이디를 가지고 지원한 이벤트 목록 가져오기
    @Select ("select staff_id, event_id, event_title, event_startDate, event_endDate, event_check from event e\n" +
            "inner join staff_application sa on e.id=sa.event_id\n" +
            "inner join user u on sa.staff_id=u.id\n" +
            "where u.user_id=#{user_id}\n" +
            "order by e.event_startDate desc limit #{startIndex}, #{pageSize}")
    List<MasterVo> getListUserApp(String user_id, int startIndex, int pageSize);

    //근무기록 리스트(관리자)
    @Select("select a.id, staff_id, work_date, event_title, user_name, user_phone, work_start_time, work_outing_time,"
            + " work_comeback_time, work_end_time, work_total_time from staff_work_record a "
            + "left JOIN user u ON a.staff_id= u.id left join event e ON a.event_id = e.id")
    List<MasterVo> report_work_list();

    //근무기록 소계시간(관리자)
    @Update("update staff_work_record set work_total_time = '${total_time}' where id = ${staff_id}")
    int update_work_total_time(String total_time, int staff_id);

    //근무기록 리스트(스태프)
    @Select("select event_title, work_date, work_start_time, work_end_time, work_outing_time, work_comeback_time, work_total_time from eventusdb.staff_work_record swr " +
            "inner join eventusdb.event e on swr.event_id=e.id " +
            "where staff_id=#{staff_id} " +
    		"order by swr.id desc limit #{startIndex}, #{pageSize}")
    List<MasterVo> report_work_list_Staff(int staff_id, int startIndex, int pageSize);
    
    //근무기록 리스트(스태프)_메인용
    @Select("select event_title, work_date, work_start_time, work_end_time, work_outing_time, work_comeback_time, work_total_time from eventusdb.staff_work_record swr\n" +
    		"inner join eventusdb.event e on swr.event_id=e.id\n" +
    		"where staff_id=#{staff_id} ORDER BY work_date DESC limit 8")
    List<MasterVo> report_work_list_Staff_main(int staff_id);
    
    //근무기록 시간 수정(관리자)
    @Update("update staff_work_record set work_start_time = #{work_start_time}, work_end_time = #{work_end_time}, "
			+ "work_outing_time = #{work_outing_time}, work_comeback_time = #{work_comeback_time} where id = #{staff_id}")
	int report_work_time_update (String work_start_time, String work_end_time, String work_outing_time, String work_comeback_time, int staff_id);
    
    @Select("select id from user where user_id = #{user_id}")
    int getUserId(String user_id);

    @Select("select id from event where event_title = #{event_title}")
    int getEventId(String event_title);


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

    // 근로계약서 정보 가져오기
    @Select("select * from contract_file cf inner join event e on cf.event_id=e.id where event_id=#{event_id} and staff_id=#{staff_id}")
    MasterVo getContractInfo(MasterVo masterVo);
    
    
    //메인 이벤트 정보_staff
	@Select("SELECT id, event_title, event_venue, event_status FROM event order by event_status, created_at DESC limit 10")
	List<EventVo> select_event_info();
	//메인 지원 정보_staff
	@Select("SELECT e.id, e.event_title, staff_result, app_created_at FROM staff_application s INNER JOIN event e on s.event_id = e.id where staff_id = #{user_id} order by staff_result, app_created_at desc limit 4")
	List<ApplicationVo> select_app_info(int staff_id);
	//메인 대기중인지원_manage
	@Select("SELECT e.id, e.event_title, e.event_deadline, count(a.staff_id) as staff_count FROM event e left join staff_application a on a.event_id = e.id where event_status=0 GROUP BY a.event_id order by event_deadline limit 4")
	List<EventVo> select_app_manage();
	@Select("SELECT f.file_name FROM staff_file f inner join staff_application a on f.resume_id = a.resume_id where a.event_id=#{event_id} limit 4")
	List<String> app_profile_list(int event_id);
	//메인 직원근무기록_manage
	@Select("SELECT l.staff_id, l.staff_name, l.event_name, l.work_time, l.work_state, f.file_name FROM work_log l left join (SELECT a.event_id, a.staff_id, f.file_name from staff_application a left join staff_file f on a.resume_id = f.resume_id and a.staff_id = f.staff_id) f "
			+ "on l.staff_id=f.staff_id and l.event_id = f.event_id where created_at > curdate() order by created_at desc limit 8")
	List<WorkLogVo> selet_work_log();

	/* 관리자 ----------------- 직원관리 페이징처리 */
	//검색 전 기본상태
	@Select("select count(*) from event e" +
            "    inner join staff_application sa on e.id = sa.event_id" +
            "    inner join user u on sa.staff_id = u.id" +
            "    where e.user_id = #{user_id}")
	int fintAllCnt(String user_id);

	//날짜 검색
	@Select("select count(*) from event e" +
            "    inner join staff_application sa on e.id = sa.event_id" +
            "    inner join user u on sa.staff_id = u.id" +
            "    where e.user_id = #{user_id} and (#{startDate} <= e.event_startDate and e.event_startDate <= #{endDate}) or (#{startDate} <= e.event_endDate and e.event_endDate <= #{endDate})")
	int searchCnt_date(String user_id, String startDate, String endDate);

	@Select("select event_id, staff_id, resume_id, event_title, user_name, user_gender, user_birth, user_phone, user_date_join, event_startDate, event_endDate from event e" +
            "    inner join staff_application sa on e.id = sa.event_id" +
            "    inner join user u on sa.staff_id = u.id" +
            "    where e.user_id = #{user_id} and (#{startDate} <= e.event_startDate and e.event_startDate <= #{endDate}) or (#{startDate} <= e.event_endDate and e.event_endDate <= #{endDate})" +
            "    order by e.event_startDate desc, sa.id desc limit #{startIndex}, #{pageSize}")
	List<MasterVo> staff_searchList_date(String user_id, String startDate, String endDate, int startIndex, int pageSize);

	//키워드 검색 (행사명 또는 스태프이름)
	@Select("select count(*) from event e" +
            "    inner join staff_application sa on e.id = sa.event_id" +
            "    inner join user u on sa.staff_id = u.id" +
            "    where e.user_id = #{user_id} and (e.event_title like concat('%','${searchKeyword}','%') or u.user_name like concat('%','${searchKeyword}','%'))")
	int searchCnt(String user_id, String searchKeyword);

	@Select("select * from event e" +
            "    inner join staff_application sa on e.id = sa.event_id" +
            "    inner join user u on sa.staff_id = u.id" +
            "    where e.user_id = #{user_id} and (e.event_title like concat('%','${searchKeyword}','%') or u.user_name like concat('%','${searchKeyword}','%'))" +
			"    order by e.event_startDate desc, sa.id desc limit #{startIndex}, #{pageSize}")
	List<MasterVo> staff_searchList(String user_id, String searchKeyword, int startIndex, int pageSize);

	//동시 검색
	@Select("select count(*) from event e" +
            "    inner join staff_application sa on e.id = sa.event_id" +
            "    inner join user u on sa.staff_id = u.id" +
            "    where (e.user_id = #{user_id} and (e.event_title like concat('%','${searchKeyword}','%') or u.user_name like concat('%','${searchKeyword}','%'))" +
            "	 and ((#{startDate} <= e.event_startDate and e.event_startDate <= #{endDate}) or (#{startDate} <= e.event_endDate and e.event_endDate <= #{endDate})))")
	int searchCnt_keydate(String user_id, String startDate, String endDate, String searchKeyword);

	@Select("select * from event e" +
            "    inner join staff_application sa on e.id = sa.event_id" +
            "    inner join user u on sa.staff_id = u.id" +
            "    where (e.user_id = #{user_id} and (e.event_title like concat('%','${searchKeyword}','%') or u.user_name like concat('%','${searchKeyword}','%'))" +
            "	 and ((#{startDate} <= e.event_startDate and e.event_startDate <= #{endDate}) or (#{startDate} <= e.event_endDate and e.event_endDate <= #{endDate})))"+
			"    order by e.event_startDate desc, sa.id desc limit #{startIndex}, #{pageSize}")
	List<MasterVo> staff_searchList_keydate(String user_id, String startDate, String endDate, String searchKeyword,	int startIndex, int pageSize);

	/* -----------사용자 이력관리 페이징------- */
	@Select ("select count(*) from event e" +
            "	 inner join staff_application sa on e.id = sa.event_id" +
            "	 inner join user u on sa.staff_id = u.id" +
            "	 where u.user_id = #{user_id}")
	int findAllCnt_user(String user_id);

	//날짜 검색
	@Select ("select count(*) from event e" +
            "	 inner join staff_application sa on e.id = sa.event_id" +
            "	 inner join user u on sa.staff_id = u.id" +
            "	 where u.user_id = #{user_id} and ((#{startDate} <= e.event_startDate and e.event_startDate <= #{endDate}) or (#{startDate} <= e.event_endDate and e.event_endDate <= #{endDate}))")
	int searchUserCnt_date(String user_id, String startDate, String endDate);

	@Select ("select * from event e" +
            "	 inner join staff_application sa on e.id = sa.event_id" +
            "	 inner join user u on sa.staff_id = u.id" +
            "	 where u.user_id = #{user_id} and ((#{startDate} <= e.event_startDate and e.event_startDate <= #{endDate}) or (#{startDate} <= e.event_endDate and e.event_endDate <= #{endDate}))" +
            "	 order by e.event_startDate desc limit #{startIndex}, #{pageSize}")
	List<MasterVo> user_searchList_date(String user_id, String startDate, String endDate, int startIndex, int pageSize);

	//키워드 검색
	@Select ("select count(*) from event e" +
            "	 inner join staff_application sa on e.id = sa.event_id" +
            "	 inner join user u on sa.staff_id = u.id" +
            "	 where u.user_id = #{user_id} and e.event_title like concat('%','${searchKeyword}','%')")
	int searchUserCnt(String user_id, String searchKeyword);

	@Select ("select * from event e" +
            "	 inner join staff_application sa on e.id = sa.event_id" +
            "	 inner join user u on sa.staff_id = u.id" +
            "	 where u.user_id = #{user_id} and e.event_title like concat('%','${searchKeyword}','%')" +
			"	 order by e.event_startDate desc limit #{startIndex}, #{pageSize}")
	List<MasterVo> user_searchList(String user_id, String searchKeyword, int startIndex, int pageSize);

	//동시 검색
	@Select("select count(*) from event e" +
            "    inner join staff_application sa on e.id = sa.event_id" +
            "    inner join user u on sa.staff_id = u.id" +
            "    where u.user_id = #{user_id} and e.event_title like concat('%','${searchKeyword}','%')" +
            "	 and ((#{startDate} <= e.event_startDate and e.event_startDate <= #{endDate}) or (#{startDate} <= e.event_endDate and e.event_endDate <= #{endDate}))")
	int searchUserCnt_keydate(String user_id, String startDate, String endDate, String searchKeyword);

	@Select("select * from event e" +
            "    inner join staff_application sa on e.id = sa.event_id" +
            "    inner join user u on sa.staff_id = u.id" +
            "    where u.user_id = #{user_id} and e.event_title like concat('%','${searchKeyword}','%')" +
            "	 and ((#{startDate} <= e.event_startDate and e.event_startDate <= #{endDate}) or (#{startDate} <= e.event_endDate and e.event_endDate <= #{endDate}))" +
			"	 order by e.event_startDate desc limit #{startIndex}, #{pageSize}")
	List<MasterVo> user_searchList_keydate(String user_id, String startDate, String endDate, String searchKeyword, int startIndex, int pageSize);

	/* 근무기록 페이징 (스태프) */
	@Select("select count(*) from eventusdb.staff_work_record swr " +
            "inner join eventusdb.event e on swr.event_id=e.id " +
            "where staff_id= #{staff_id}")
	int CntAll_staffwork(int staff_id);

	//날짜 검색 - 근무일자 기준
	@Select("select count(*) from eventusdb.staff_work_record swr " +
            "inner join eventusdb.event e on swr.event_id=e.id " +
            "where staff_id= #{staff_id} " +
			"and (#{startDate} <= swr.work_date and swr.work_date <= #{endDate})")
	int worksearchCnt_date(int staff_id, String startDate, String endDate);

	@Select("select * from eventusdb.staff_work_record swr " +
            "inner join eventusdb.event e on swr.event_id=e.id " +
            "where staff_id= #{staff_id} " +
			"and (#{startDate} <= swr.work_date and swr.work_date <= #{endDate}) " +
            "order by swr.id desc limit #{startIndex}, #{pageSize}")
	List<MasterVo> staffwork_searchList_date(int staff_id, String startDate, String endDate, int startIndex, int pageSize);

	//키워드 검색
	@Select("select count(*) from eventusdb.staff_work_record swr " +
            "inner join eventusdb.event e on swr.event_id=e.id " +
            "where staff_id= #{staff_id} " +
            "and e.event_title like concat('%','${searchKeyword}','%')")
	int worksearchCnt_key(int staff_id, String searchKeyword);

	@Select("select * from eventusdb.staff_work_record swr " +
            "inner join eventusdb.event e on swr.event_id=e.id " +
            "where staff_id= #{staff_id} " +
            "and e.event_title like concat('%','${searchKeyword}','%') " +
			"order by swr.id desc limit #{startIndex}, #{pageSize}")
	List<MasterVo> staffwork_searchList(int staff_id, String searchKeyword, int startIndex, int pageSize);

	//동시 검색
	@Select("select count(*) from eventusdb.staff_work_record swr " +
            "inner join eventusdb.event e on swr.event_id=e.id " +
            "where staff_id= #{staff_id} " +
            "and e.event_title like concat('%','${searchKeyword}','%') " +
            "and (#{startDate} <= swr.work_date and swr.work_date <= #{endDate})")
	int worksearchCnt_keydate(int staff_id, String startDate, String endDate, String searchKeyword);

	@Select("select * from eventusdb.staff_work_record swr " +
            "inner join eventusdb.event e on swr.event_id=e.id " +
            "where staff_id= #{staff_id} " +
            "and e.event_title like concat('%','${searchKeyword}','%') " +
            "and (#{startDate} <= swr.work_date and swr.work_date <= #{endDate}) " +
			"order by swr.id desc limit #{startIndex}, #{pageSize}")
	List<MasterVo> staffwork_searchList_keydate(int staff_id, String startDate, String endDate, String searchKeyword, int startIndex, int pageSize);

	/* ---------------관리자 근무기록 페이징------------- */
	@Select("select count(*) from staff_work_record a "
            + "left JOIN user u ON a.staff_id= u.id left join event e ON a.event_id = e.id")
	int CntAll_work();

	@Select("select a.id, staff_id, work_date, event_title, user_name, user_phone, work_start_time, work_outing_time,"
            + " work_comeback_time, work_end_time, work_total_time from staff_work_record a "
            + "left JOIN user u ON a.staff_id= u.id left join event e ON a.event_id = e.id "
            + "order by work_date desc limit #{startIndex}, #{pageSize}")
	List<MasterVo> report_work_list_paging(int startIndex, int pageSize);

	//날짜 검색
	@Select("select count(*) from staff_work_record a "
            + "left JOIN user u ON a.staff_id= u.id left join event e ON a.event_id = e.id "
			+ "where (#{startDate} <= work_date and work_date <= #{endDate})")
	int WorkSearchCnt_date(String startDate, String endDate);

	@Select("select a.id, staff_id, work_date, event_title, user_name, user_phone, work_start_time, work_outing_time,"
            + " work_comeback_time, work_end_time, work_total_time from staff_work_record a "
            + "left JOIN user u ON a.staff_id= u.id left join event e ON a.event_id = e.id "
            + "where (#{startDate} <= work_date and work_date <= #{endDate}) " 
            + "order by work_date desc limit #{startIndex}, #{pageSize}")
	List<MasterVo> Work_SearchList_date(String startDate, String endDate, int startIndex, int pageSize);

	//키워드 검색
	@Select("select count(*) from staff_work_record a "
            + "left JOIN user u ON a.staff_id= u.id left join event e ON a.event_id = e.id "
            + "where e.event_title like concat('%','${searchKeyword}','%') or u.user_name like concat('%','${searchKeyword}','%')")
	int WorkSearchCnt_key(String searchKeyword);

	@Select("select a.id, staff_id, work_date, event_title, user_name, user_phone, work_start_time, work_outing_time,"
            + " work_comeback_time, work_end_time, work_total_time from staff_work_record a "
            + "left JOIN user u ON a.staff_id= u.id left join event e ON a.event_id = e.id "
            + "where e.event_title like concat('%','${searchKeyword}','%') or u.user_name like concat('%','${searchKeyword}','%') " 
            + "order by work_date desc limit #{startIndex}, #{pageSize}")
	List<MasterVo> Work_SearchList(String searchKeyword, int startIndex, int pageSize);

	//동시 검색
	@Select("select count(*) from staff_work_record a "
            + "left JOIN user u ON a.staff_id= u.id left join event e ON a.event_id = e.id "
			+ "where (#{startDate} <= work_date and work_date <= #{endDate}) "
            + "and (e.event_title like concat('%','${searchKeyword}','%') or u.user_name like concat('%','${searchKeyword}','%'))")
	int WorkSearchCnt_keydate(String startDate, String endDate, String searchKeyword);

	@Select("select a.id, staff_id, work_date, event_title, user_name, user_phone, work_start_time, work_outing_time,"
			+ " work_comeback_time, work_end_time, work_total_time from staff_work_record a "
            + "left JOIN user u ON a.staff_id= u.id left join event e ON a.event_id = e.id "
			+ "where (#{startDate} <= work_date and work_date <= #{endDate}) "
            + "and (e.event_title like concat('%','${searchKeyword}','%') or u.user_name like concat('%','${searchKeyword}','%')) "
            + "order by work_date desc limit #{startIndex}, #{pageSize}")
	List<MasterVo> Work_SearchList_keydate(String startDate, String endDate, String searchKeyword, int startIndex, int pageSize);

	@Select("select event_id, staff_id, resume_id, event_title, user_name, user_gender, user_birth, user_phone, user_date_join, event_startDate, event_endDate from event e " +
			"inner join staff_application sa on e.id = sa.event_id " +
			"inner join user u on sa.staff_id = u.id " +
			"where e.user_id = #{user_id} " +
			"order by e.event_startDate asc, sa.id asc")
	List<MasterVo> staff_findDownloadList(String user_id);

	@Select("select event_id, staff_id, resume_id, event_title, user_name, user_gender, user_birth, user_phone, user_date_join, event_startDate, event_endDate from eventusdb.event e " +
			"inner join eventusdb.staff_application sa on e.id = sa.event_id " +
			"inner join eventusdb.user u on sa.staff_id = u.id " +
			"where ((#{startDate} <= event_startDate and event_startDate <= #{endDate}) or (#{startDate} <= event_endDate and event_endDate <= #{endDate})) and e.user_id = #{user_id} " +
			"order by e.event_startDate asc, sa.id asc")
	List<MasterVo> staff_Downloaddate(String user_id, String startDate, String endDate);

	@Select("select event_id, staff_id, resume_id, event_title, user_name, user_gender, user_birth, user_phone, user_date_join, event_startDate, event_endDate from eventusdb.event e " +
			"inner join eventusdb.staff_application sa on e.id = sa.event_id " +
			"inner join eventusdb.user u on sa.staff_id = u.id " +
			"where (event_title like concat('%','${searchKeyword}','%')) and e.user_id = #{user_id} " +
			"order by e.event_startDate asc, sa.id asc")
	List<MasterVo> staff_Downloadkey(String user_id, String searchKeyword);

	@Select("select event_id, staff_id, resume_id, event_title, user_name, user_gender, user_birth, user_phone, user_date_join, event_startDate, event_endDate from eventusdb.event e " +
			"inner join eventusdb.staff_application sa on e.id = sa.event_id " +
			"inner join eventusdb.user u on sa.staff_id = u.id " +
			"where ((#{startDate} <= event_startDate and event_startDate <= #{endDate}) or (#{startDate} <= event_endDate and event_endDate <= #{endDate})) and event_title like concat('%','${searchKeyword}','%') and e.user_id = #{user_id} " +
			"order by e.event_startDate asc, sa.id asc")
	List<MasterVo> staff_Downloadkeydate(String user_id, String startDate, String endDate, String searchKeyword);

	@Select("select a.id, staff_id, work_date, event_title, user_name, user_phone, work_start_time, work_outing_time, work_comeback_time, work_end_time, work_total_time from staff_work_record a left JOIN user u ON a.staff_id= u.id left join event e ON a.event_id = e.id")
	List<MasterVo> report_work_findDownloadList();

	@Select("select a.id, staff_id, work_date, event_title, user_name, user_phone, work_start_time, work_outing_time, work_comeback_time, work_end_time, work_total_time from staff_work_record a \n" +
			"left JOIN user u ON a.staff_id= u.id \n" +
			"left join event e ON a.event_id = e.id\n" +
			"where (#{startDate} <= event_startDate and event_startDate <= #{endDate}) or (#{startDate} <= event_endDate and event_endDate <= #{endDate})")
	List<MasterVo> report_work_Downloaddate(String startDate, String endDate);

	@Select("select a.id, staff_id, work_date, event_title, user_name, user_phone, work_start_time, work_outing_time, work_comeback_time, work_end_time, work_total_time from staff_work_record a \n" +
			"left JOIN user u ON a.staff_id= u.id \n" +
			"left join event e ON a.event_id = e.id\n" +
			"where (event_title like concat('%','${searchKeyword}','%'))")
	List<MasterVo> report_work_Downloadkey(String searchKeyword);

	@Select("select a.id, staff_id, work_date, event_title, user_name, user_phone, work_start_time, work_outing_time, work_comeback_time, work_end_time, work_total_time from staff_work_record a \n" +
			"left JOIN user u ON a.staff_id= u.id \n" +
			"left join event e ON a.event_id = e.id\n" +
			"where ((#{startDate} <= event_startDate and event_startDate <= #{endDate}) or (#{startDate} <= event_endDate and event_endDate <= #{endDate})) and event_title like concat('%','${searchKeyword}','%')")
	List<MasterVo> report_work_Downloadkeydate(String startDate, String endDate, String searchKeyword);
}