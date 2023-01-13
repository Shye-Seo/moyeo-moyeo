
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
    accept_staff_list = [];
}

//모달창 종료_이력서만
function modal_inact_resume(){
	$('#resume_wrap').empty();
    $('.modal_con_L').hide();
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


// application
//수락 누를 시
function accept_staff(staff_id,obj){
    accept_staff_list.push(staff_id);
    console.log(accept_staff_list)
    $(obj).remove();
    $(`#app_list_${staff_id} .trash_icon`).hide();
    $(`#app_list_${staff_id} .btn_div`).prepend(`<button type="button" class="complete_btn" onclick="undo_staff(${staff_id},this)">완료</button>`);
}

//쓰레기통 누를 시
function reject_staff(staff_id,obj){
    remove_num(staff_id);
    console.log(accept_staff_list)
    $(obj).hide();
    $(`#app_list_${staff_id}`).addClass('list_rejected');
    $(`#app_list_${staff_id} .accept_btn`).remove();
    $(`#app_list_${staff_id} .btn_div`).prepend(`<button type="button" class="undo_btn" onclick="undo_staff(${staff_id},this)">취소</button>`);
}

//완료,취소 누를 시(되돌리기)
function undo_staff(staff_id,obj){
    remove_num(staff_id);
    console.log(accept_staff_list)
    $(obj).remove();
    $(`#app_list_${staff_id}`).removeClass('list_rejected');
    $(`#app_list_${staff_id} .trash_icon`).show();
    $(`#app_list_${staff_id} .btn_div`).prepend(`<button type="button" class="accept_btn" onclick="accept_staff(${staff_id},this)">수락</button>`);
}

//배열에 번호빼기
function remove_num(num){
    for(let i = 0; i < accept_staff_list.length; i++){ 
        if (accept_staff_list[i] === num) { 
            accept_staff_list.splice(i, 1);
            i--;
        }
    }
}

//모집완료
function application_send(){
    $.ajax({
		url : "/set_application",
		type : 'post',
		data : {
            event_id:now_event_id_for_app,
            passer_list:accept_staff_list
        },
		success: function(data){
            alert(`총 ${data}명의 지원자를 합격처리 하셨습니다.`);
            // 합불합 표시
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

function resume_download(thisId){
	
	 let element = document.getElementById('resume_wrapper');
     let opt = {
            margin:       0.5,
            filename:     '이력서.pdf',
            image:        { type: 'jpeg', quality: 0.98 },
            html2canvas: { // html2canvas 옵션
                useCORS: true, // 영역 안에 로컬 이미지를 삽입 할 때 옵션 필요
                scrollY: 0, // 스크롤 이슈 때문에 필수
                scale: 1, // browsers device pixel ratio
                dpi: 300,
                letterRendering: true,
                allowTaint: false, //useCORS를 true로 설정 시 반드시 allowTaint를 false처리 해주어야함
            },
            jsPDF:        { unit: 'in', format: 'letter', orientation: 'portrait' }
     };
     html2pdf().from(element).set(opt).save();
     
     // 5초 뒤에 location.href로 이동
     alert("다운로드 후 자동으로 페이지 이동됩니다.\n확인 버튼 클릭 후, 조금만 기다려주세요!");

     setTimeout(function() {
     	window.close();
     }, 3000);
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
