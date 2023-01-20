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
		var update_btn_num = "li."+num;
		
		$(update_btn_num).siblings(".list_start_area").css({ "display": "none" });
		$(update_btn_num).siblings(".list_outing_area").css({ "display": "none" });
		$(update_btn_num).siblings(".list_comeback_area").css({ "display": "none" });
		$(update_btn_num).siblings(".list_end_area").css({ "display": "none" });
		$(update_btn_num).siblings(".time_update").css({ "display": "inline" });
		
		$(update_btn_num).siblings(".input_wrap").css({ "display": "flex" });
		
		$(update_btn_num).children().children().children(".pen_btn").css({ "display": "none" });
		
		$(update_btn_num).children("#check_btn").css({ "display": "inline" });
		$(update_btn_num).children("#close_btn").css({ "display": "inline" });


	});
	
	$("[id='close_btn']").click(function() {
		
		var num = $(this).attr('value');
		var update_btn_num = "li."+num;
		
		/*$(update_btn_num).siblings().children("#start_area_input").css({ "display": "none" });
		$(update_btn_num).siblings().children("#outing_area_input").css({ "display": "none" });
		$(update_btn_num).siblings().children("#comeback_area_input").css({ "display": "none" });
		$(update_btn_num).siblings().children("#end_area_input").css({ "display": "none" });*/
		$(update_btn_num).siblings(".time_update").css({ "display": "none" });
		$(update_btn_num).siblings(".list_start_area").css({ "display": "inline" });
		$(update_btn_num).siblings(".list_outing_area").css({ "display": "inline" });
		$(update_btn_num).siblings(".list_comeback_area").css({ "display": "inline" });
		$(update_btn_num).siblings(".list_end_area").css({ "display": "inline" });
		
		$(update_btn_num).siblings(".input_wrap").css({ "display": "flex" });
		
		$(update_btn_num).children().children().children(".pen_btn").css({ "display": "inline" });
		
		$(update_btn_num).children("#check_btn").css({ "display": "none" });
		$(update_btn_num).children("#close_btn").css({ "display": "none" });


	});


});