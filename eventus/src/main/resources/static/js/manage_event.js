
//현재 열려있는 이벤트 id
let now_event_id_for_app = 0;
let now_event_id_for_work = 0;

//모달창 활성화 여부
let modal_L_state = false;
let modal_R_state = false;

//오늘날짜
let today_for_work = "";

//
let accept_staff_list = [];

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
}

//지원자 모달 act
function modal_act_application(thisId){

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
            $(".confirm_wrap").css('display','flex');
            $('#modal_content_wrap').addClass("app_Con");
            $('.total').append(`총 지원자 수 <span>${list.length}</span>명`);

            for(i=0; i<positions.length; i++){
                let position_wrap = $("<div>");
                position_wrap.addClass("position_wrap");
                position_wrap.append(`<div class="position_title"><p>${positions[i]}</p><div></div></div>`)
                for(j=0; j<list.length; j++){
                    if(list[j].staff_position == positions[i]){
                        let app_list = {list:list[j]};
                        position_wrap.append($.templates("#application_list").render(app_list)) ;
                    }
                }
                $('#modal_content_wrap').append(position_wrap);
            }
		}
	});
}

function accept_staff(staff_id,obj){
    accept_staff_list.push(staff_id);
    console.log(accept_staff_list)
    $(obj).remove();
    $(`#app_list_${staff_id} .trash_icon`).hide();
    $(`#app_list_${staff_id} .btn_div`).prepend(`<button type="button" class="complete_btn" onclick="undo(${staff_id},this)">완료</button>`);
}


// application
function acceptApplicant(staff_id,obj)  { //합격 (수락 > 완료)
  $.ajax({
		url : "/accept_applicant",
		type : 'post',
		data : {
			staff_id:staff_id,
			event_id:now_event_id_for_app
		},
		success: function(){
            $(obj).remove();
            $(`#app_list_${staff_id} .trash_icon`).hide();
            $(`#app_list_${staff_id} .btn_div`).prepend(`<button type="button" class="complete_btn" onclick="undo(${staff_id},this)">완료</button>`);
		}
	});
}

function undo(staff_id,obj)  { //대기중 (완료,취소 > 수락)
    var event_id = $("#event_id").val();
    
    $.ajax({
        url : "/accept_applicant_cancel",
        type : 'post',
        data : {
            staff_id:staff_id,
            event_id:now_event_id_for_app
        },
        success: function(){
            $(obj).remove();
            $(`#app_list_${staff_id}`).removeClass('list_rejected');
            $(`#app_list_${staff_id} .trash_icon`).show();
            $(`#app_list_${staff_id} .btn_div`).prepend(`<button type="button" class="accept_btn" onclick="acceptApplicant(${staff_id},this)">수락</button>`);
        }
    });
}

function rejectAccept(staff_id,obj)  { //불합격 (쓰레기통 > 취소)
	  var event_id = $("#event_id").val();
	  
	  $.ajax({
			url : "/reject_applicant",
			type : 'post',
			data : {
				staff_id:staff_id,
				event_id:now_event_id_for_app
			},
			success: function(){
                $(obj).hide();
                $(`#app_list_${staff_id}`).addClass('list_rejected');
                $(`#app_list_${staff_id} .accept_btn`).remove();
				$(`#app_list_${staff_id} .btn_div`).prepend(`<button type="button" class="undo_btn" onclick="undo(${staff_id},this)">취소</button>`);
			}
		});
	}

    //이력서 예정
    function resume_act(){
        $('.modal_con_L').show();
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
                alert("현재 지원자가 없습니다.");
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
