
//현재 열려있는 이벤트 id
let now_event_id_for_app = 0;
let now_event_id_for_work = 0;

//모달창 활성화 여부
let modal_L_state = false;
let modal_R_state = false;

//오늘날짜
let today_for_work = "";


$(function(){
    
})


//모달창 종료 (내용 비우기)
function modal_inact(){
    $('.total').empty();
    $('.today').empty();
    $('#modal_content_wrap').empty();
    $('#modal_content_wrap').removeClass("app_Con");
    $("#modal_wrap").hide();
    $('.modal_con_L').hide();
    $(".confirm_wrap").hide();
    accept_staff_list = [];
}

//모달창 종료_이력서만
function modal_inact_resume(){
	$('#resume_wrap').empty();
    $('.modal_con_L').hide();
}

//지원자 모달 act
function modal_act_application(thisId,chk){

    $.ajax({
		url : "/get_application_list",
		type : 'get',
		data : {id:thisId},
		success: function(data){
            const list = data.application_list;
            const positions = data.position_list;
            now_event_id_for_app = data.event_id;
            if(list.length <=0){
                alert("현재 지원자가 없습니다.");
                return false;
            }

			$("#modal_wrap").show();
            if(chk == 0){
                $(".confirm_wrap").css('display','flex');
                $('#modal_content_wrap').addClass("app_Con");
            }
            $('.total').append(`총 지원자 수 <span>${list.length}</span>명`);

            for(i=0; i<positions.length; i++){
                let position_wrap = $("<div>");
                position_wrap.addClass("position_wrap");
                position_wrap.append(`<div class="position_title"><p>${positions[i]}</p><div></div></div>`)
                for(j=0; j<list.length; j++){
                    if(list[j].staff_position == positions[i]){
                        let app_list = {list:list[j], chk:chk};
                        position_wrap.append($.templates("#application_list").render(app_list)) ;
                    }
                }
                $('#modal_content_wrap').append(position_wrap);
            }
		}
	});
}


function app_btn(status,staff_id,obj){

    $.ajax({
		url : "/set_application_status",
		type : 'POST',
		data : {
            status:status,
            staff_id:staff_id,
            event_id:now_event_id_for_app
        },
		success: function(data){

            if(status === 0){// 완료,취소 > 대기중
                $(obj).remove();
                $(`#app_list_${staff_id}`).removeClass('list_rejected');
                $(`#app_list_${staff_id} .trash_icon`).show();
                $(`#app_list_${staff_id} .btn_div`).prepend(`<button type="button" class="accept_btn actbtn" onclick="app_btn(1,${staff_id},this)">수락</button>`);
            }else if(status === 1){ //대기중 > 완료
                $(obj).remove();
                $(`#app_list_${staff_id} .trash_icon`).hide();
                $(`#app_list_${staff_id} .btn_div`).prepend(`<button type="button" class="complete_btn actbtn" onclick="app_btn(0,${staff_id},this)">완료</button>`);
            }else if(status === 2){ //쓰레기통 > 취소
                $(obj).hide();
                $(`#app_list_${staff_id}`).addClass('list_rejected');
                $(`#app_list_${staff_id} .actbtn`).remove();
                $(`#app_list_${staff_id} .btn_div`).prepend(`<button type="button" class="undo_btn actbtn" onclick="app_btn(0,${staff_id},this)">취소</button>`);
            }

        }});
}


//모집완료
function application_send(){
    $.ajax({
		url : "/set_application",
		type : 'post',
		data : {
            event_id:now_event_id_for_app
        },
		success: function(data){
            alert(`총 ${data}명의 지원자를 합격처리 하셨습니다.`);
            modal_act_application(now_event_id_for_app,1);
        }});
}



//이력서 모달
function resume_act(thisId){
	
	 $.ajax({
		url : "/get_resume_file",
		type : 'get',
		data : {id:thisId},
		success: function(data){
            const info = data.staff_info;
            const resume = data.staff_resume;
            const school = data.school_info;
            const career = data.career_info;
//            now_event_id_for_app = data.event_id;

			$("#modal_wrap").show();
			$('.modal_con_L').show();
			let app_list = {info:info, resume:resume, school:school, career:career};
			$('#resume_wrap').append($.templates("#resume_file").render(app_list));
		}
	});
}
// applicant end


//근무자 모달
function modal_act_workRecord(thisId){

    $.ajax({
        url : "/get_workStaff_list",
        type : 'get',
        data : {id:thisId},
        success: function(data){
            const list = data.workStaff_list;

            now_event_id_for_work = data.event_id;
            today_for_work = data.work_date;

            if(list.length <=0){
                alert("현재 근무자가 없습니다.");
                return false;
            }

            $("#modal_wrap").show();

            let html = "";

            $('.total').append(`총 지원자 수 <span>${list.length}</span>명`);
            $('.today').append(`${data.today}`);

            for(i=0; i<list.length; i++){
                
                let times = list[i].recordVo;
                let work_list = {list:list[i], time:times};
                html += $.templates("#worker_list").render(work_list);
            }
            $('#modal_content_wrap').append(html);
        }
    });
}

//work record
function record_Time(staff_id, record_id, obj, action)  { //출근기록
  $.ajax({
		url : action,
		type : 'post',
		data : {
			staff_id:staff_id,
			event_id:now_event_id_for_work,
			work_date:today_for_work,
			record_id:record_id
		},
		success: function(data){
            $(obj).parent().append(`<button type="button" class="workTime_text" >${data}</button>`);
			$(obj).remove();
		}
	});
}

/* 기록없을때  */
function no_record_Time(staff_id, obj,action)  {
  
  $.ajax({
		url : action,
		type : 'post',
		data : {
            staff_id:staff_id,
			event_id:now_event_id_for_work,
			work_date:today_for_work
		},
		success: function(){
            $('.total').empty();
            $('.today').empty();
            $('#modal_content_wrap').empty();
            modal_act_workRecord(now_event_id_for_work);
		}
	});
}
