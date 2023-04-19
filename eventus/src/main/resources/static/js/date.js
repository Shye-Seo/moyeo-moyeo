/**
 * 
 */
$(document).ready(function() {
	$("#s_calendar,#e_calendar").datepicker({

		showOtherMonths: true,
		selectOtherMonths: true,

		/*         changeMonth: true,
				 changeYear: true,
				 showMonthAfterYear: true,*/
		/*		dayNamesMin: ['SUN', 'MON', 'TUE', 'WED', 'THU', 'FRI', 'SAT'],*/
		dateFormat: 'yy.mm.dd',

	});

	$('#s_calendar').datepicker("option", "maxDate", $("#e_calendar").val());
	$('#s_calendar').datepicker("option", "onClose", function(selectedDate) {
		$("#e_calendar").datepicker("option", "minDate", selectedDate);
	});

	$('#e_calendar').datepicker();
	$('#e_calendar').datepicker("option", "minDate", $("#s_calendar").val());
	$('#e_calendar').datepicker("option", "onClose", function(selectedDate) {
		$("#s_calendar").datepicker("option", "maxDate", selectedDate);
	});


});

$(document).ready(function() {

	$("[id='update_btn']").click(function() {

		var num = $(this).attr('value');
		var update_btn_num = "li." + num;

		$(update_btn_num).siblings(".list_start_area").css({ "display": "none" });
		$(update_btn_num).siblings(".list_outing_area").css({ "display": "none" });
		$(update_btn_num).siblings(".list_comeback_area").css({ "display": "none" });
		$(update_btn_num).siblings(".list_end_area").css({ "display": "none" });
		$(update_btn_num).siblings(".time_update").css({ "display": "inline-block" });

		$(update_btn_num).siblings(".input_wrap").css({ "display": "flex" });

		$(update_btn_num).children().children().children(".pen_btn").css({ "display": "none" });

		$(update_btn_num).children("#check_btn").css({ "display": "inline" });
		$(update_btn_num).children("#close_btn").css({ "display": "inline" });


	});

	$("[id='close_btn']").click(function() {

		var num = $(this).attr('value');
		var update_btn_num = "li." + num;

		/*$(update_btn_num).siblings().children("#start_area_input").css({ "display": "none" });
		$(update_btn_num).siblings().children("#outing_area_input").css({ "display": "none" });
		$(update_btn_num).siblings().children("#comeback_area_input").css({ "display": "none" });
		$(update_btn_num).siblings().children("#end_area_input").css({ "display": "none" });*/
		$(update_btn_num).siblings(".time_update").css({ "display": "none" });
		$(update_btn_num).siblings(".list_start_area").css({ "display": "inline-block" });
		$(update_btn_num).siblings(".list_outing_area").css({ "display": "inline-block" });
		$(update_btn_num).siblings(".list_comeback_area").css({ "display": "inline-block" });
		$(update_btn_num).siblings(".list_end_area").css({ "display": "inline-block" });

		$(update_btn_num).siblings(".input_wrap").css({ "display": "flex" });

		$(update_btn_num).children().children().children(".pen_btn").css({ "display": "inline" });

		$(update_btn_num).children("#check_btn").css({ "display": "none" });
		$(update_btn_num).children("#close_btn").css({ "display": "none" });


	});

	$("[id='check_btn']").click(function() {
		var num = $(this).attr('value');
		var update_btn_num = "li." + num;

		var start = $(update_btn_num).siblings().children("#start_area_input").val();
		var out = $(update_btn_num).siblings().children("#outing_area_input").val();
		var back = $(update_btn_num).siblings().children("#comeback_area_input").val();
		var end = $(update_btn_num).siblings().children("#end_area_input").val();

		// 출근시간, 퇴근시간 필수 입력 값
		if (start == null || start == "") {
			alert("출근시간을 기록해 주세요.")
			return false;
		}
	
		
		//시간 형식 
		if (start != null || start != "") {
			if (start.indexOf(":") == -1 || start.length < 5) {
				alert("출근시간 형식이 알맞지 않습니다.")
				return false;
			}
		}
		if (end.length > 0) {
			if (end.indexOf(":") == -1 || end.length < 5) {
				alert(end.length);
				alert("퇴근시간 형식이 알맞지 않습니다.")
				return false;
			}
		}
		if (out.length > 0) {
			if (out.indexOf(":") == -1 || out.length < 5) {
				alert("외출시간 형식이 알맞지 않습니다.")
				return false;
			} 
		}

		if (back.lenth > 0) {
			if (back.indexOf(":") == -1 || back.length < 5) {
				alert("복귀시간 형식이 알맞지 않습니다.")
				return false;
			}
		}

		$.ajax({
			url: "/update_reportwork_time",
			type: "POST",
			data: {
				numb: num,
				start: start,
				end: end,
				out: out,
				back: back
			},
			success: function(data) {
				alert("입력 완료");
				window.location.reload();
				//				$('.area').load(location.href+' .area');
			}
		});

		return true;

	});


});

function inputTimeColon(time) {

	// 먼저 기존에 들어가 있을 수 있는 콜론(:)기호를 제거한다.
	var replaceTime = time.value.replace(/\:/g, "");

	// 글자수가 4 ~ 5개 사이일때만 동작하게 고정한다.
	if (replaceTime.length >= 4 && replaceTime.length < 5) {

		// 시간을 추출
		var hours = replaceTime.substring(0, 2);

		// 분을 추출
		var minute = replaceTime.substring(2, 4);

		// 시간은 24:00를 넘길 수 없게 세팅
		if (hours + minute > 2400) {
			alert("시간은 24시를 넘길 수 없습니다.");
			time.value = "24 : 00";
			return false;
		}

		// 분은 60분을 넘길 수 없게 세팅
		if (minute > 60) {
			alert("분은 60분을 넘길 수 없습니다.");
			time.value = hours + " : 00";
			return false;
		}

		// 콜론을 넣어 시간을 완성하고 반환한다.
		time.value = hours + " : " + minute;
	}
}