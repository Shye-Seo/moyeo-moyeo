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