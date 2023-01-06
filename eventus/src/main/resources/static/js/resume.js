
let career_num =1;
const max_num =5;

//경력추가
function add_career(){
    $(".career_wrap:first").clone(true).appendTo($(".careers_div"));
    $('.career_wrap:last input[name="input_staff_career_eventName"]').val('');
    $('.career_wrap:last input[name="input_staff_career_eventName"]').attr('name','input_staff_career_eventName'+career_num);
    $('.career_wrap:last input[name="input_staff_career_businessName"]').val('');
    $('.career_wrap:last input[name="input_staff_career_businessName"]').attr('name','input_staff_career_businessName'+career_num);
    $('.career_wrap:last input[name="input_staff_career_position"]').val('');
    $('.career_wrap:last input[name="input_staff_career_position"]').attr('name','input_staff_career_position'+career_num);
    $('.career_wrap:last input[name="input_staff_career_workday"]').val('');
    $('.career_wrap:last input[name="input_staff_career_workday"]').attr('name','input_staff_career_workday'+career_num);
    $('.career_wrap:last').append('<div class="remove_career"  style="--i :'+career_num+' ;" onclick="remove_career(this)"></div>');

    career_num++;
    if(career_num>1){
        $("#add_career").css('right','-90px')
    }else{
        $("#add_career").css('right','-50px')
    }
    if(career_num == max_num){
        $("#add_career").hide();
    }
}

//경력추가 update
function add_career_forUpdate(eventName, businessName, position, workday){
    $(".career_wrap:first").clone(true).appendTo($(".careers_div"));
    $('.career_wrap:last input[name="input_staff_career_eventName"]').val(eventName);
    $('.career_wrap:last input[name="input_staff_career_eventName"]').attr('name','input_staff_career_eventName'+career_num);
    $('.career_wrap:last input[name="input_staff_career_businessName"]').val(businessName);
    $('.career_wrap:last input[name="input_staff_career_businessName"]').attr('name','input_staff_career_businessName'+career_num);
    $('.career_wrap:last input[name="input_staff_career_position"]').val(position);
    $('.career_wrap:last input[name="input_staff_career_position"]').attr('name','input_staff_career_position'+career_num);
    $('.career_wrap:last input[name="input_staff_career_workday"]').val(workday);
    $('.career_wrap:last input[name="input_staff_career_workday"]').attr('name','input_staff_career_workday'+career_num);
    $('.career_wrap:last').append('<div class="remove_career"  style="--i :'+career_num+' ;" onclick="remove_career(this)"></div>');

    career_num++;
    if(career_num>1){
        $("#add_career").css('right','-90px')
    }else{
        $("#add_career").css('right','-50px')
    }
    if(career_num == max_num){
        $("#add_career").hide();
    }
}

//경력삭제
function remove_career(obj){
    let remove_num= obj.style.getPropertyValue("--i")*1;
    if(remove_num == career_num-1){
    }else{
        $('.c_en').each(function(){
            let i = $(this).attr('name').charAt($(this).attr('name').length-1);
            if(i>remove_num){
                $(this).attr('name','input_staff_career_eventName'+ (i-1));
            }
        })
        $('.c_bn').each(function(){
            let i = $(this).attr('name').charAt($(this).attr('name').length-1);
            if(i>remove_num){
                $(this).attr('name','input_staff_career_businessName'+ (i-1));
            }
        })
        $('.c_p').each(function(){
            let i = $(this).attr('name').charAt($(this).attr('name').length-1);
            if(i>remove_num){
                $(this).attr('name','input_staff_career_position'+ (i-1));
            }
        })
        $('.c_wd').each(function(){
            let i = $(this).attr('name').charAt($(this).attr('name').length-1);
            if(i>remove_num){
                $(this).attr('name','input_staff_career_workday'+ (i-1));
            }
        })
        $('.remove_career').each(function(){
            let i = $(this).css('--i')*1;
            if(i>remove_num){
                $(this).css('--i',(i-1));
            }
        })
    }
    $(obj).parent().remove();
    career_num--;

    if($("#add_career").css('display')=='none'){
        $("#add_career").show();
    }

    if(career_num>1){
        $("#add_career").css('right','-90px')
    }else{
        $("#add_career").css('right','-50px')
    }
    if(career_num == max_num){
        $("#add_career").hide();
    }
}

function chkSchool(obj){
    let shoolDate = "";
    let text = obj.val();

    if(!text.includes('.')){
        if(text.length<5){
            obj.val(text);
        }else if(text.length<8){
            shoolDate += text.substr(0, 4);
            shoolDate += "."
            shoolDate += text.substr(4);
            obj.val(shoolDate.substr(0,7));
        }
    }
}

function checkFileName(str){
//확장자 체크
var ext =  str.split('.').pop().toLowerCase();
if($.inArray(ext, ['bmp', 'jpg', 'png', 'jpeg', 'gif']) == -1) {
    alert(ext+'파일은 업로드 하실 수 없습니다.');
    return false;
}

//파일명 길이 체크
if(str.length>=70){
    alert('파일명이 너무 길어 업로드 하실 수 없습니다.');
    return false;
}
return true;
}

//프로필 이미지 미리보기
function readURL(input) {
    //파일이 있으면 실행
    if (input.files && input.files[0]) {
        //파일경로를 (input[type=file]의 값) fileName에 저장
        var fileName = $(input).val();
        var reader = new FileReader();

        //경로를 뺀 파일이름이 저장
        var realfileName = fileName.split('\\').pop().toLowerCase();
        
        //checkFileName 실행 후 값이 true이면 실행
        if(checkFileName(realfileName)){
            reader.onload = function (e) { 
                $('#preview').attr('src', e.target.result);
                $('#preview').addClass("in_profile");
            }
            reader.readAsDataURL(input.files[0]);
        }else{
            //checkFileName false면 받아온 파일 값 지우기
            $(input).val("");
            $('#preview').attr('src', "");
            $('#preview').removeClass("in_profile");
        }
    }
} 


// form submit
function submitDate(actionUrl){

if (confirm("등록 하시겠습니까?") == true){

    //학교
    
    let staff_school = $('input[name="staff_school1"]').val();
    let staff_major = $('input[name="staff_major1"]').val();
    let staff_school_start = $('input[name="staff_school_start1"]').val();
    let staff_school_end = $('input[name="staff_school_end1"]').val();
    let staff_school_state = $('select[name="staff_school_state1"]').val();

    if($('input[name="staff_school2"]').val()!=""){//학교명2 없음 바로 submit
        staff_school += ',' + $('input[name="staff_school2"]').val();
        staff_major += ',' + $('input[name="staff_major2"]').val();
        staff_school_start += ',' + $('input[name="staff_school_start2"]').val();
        staff_school_end += ',' + $('input[name="staff_school_end2"]').val();
        staff_school_state += ',' + $('select[name="staff_school_state2"]').val();
    }

    $('.area2').append(`<input type="text" name="staff_school" value="${staff_school}" hidden/>`);
    $('.area2').append(`<input type="text" name="staff_major" value="${staff_major}" hidden/>`);
    $('.area2').append(`<input type="text" name="staff_school_start" value="${staff_school_start}" hidden/>`);
    $('.area2').append(`<input type="text" name="staff_school_end" value="${staff_school_end}" hidden/>`);
    $('.area2').append(`<input type="text" name="staff_school_state" value="${staff_school_state}" hidden/>`);



    //경력
    let staff_career_eventName = $('input[name="input_staff_career_eventName"]').val();
    let staff_career_businessName = $('input[name="input_staff_career_businessName"]').val();
    let staff_career_position = $('input[name="input_staff_career_position"]').val();
    let staff_career_workday = $('input[name="input_staff_career_workday"]').val();

    $('.area3').append('<input type="text" name="staff_career_eventName" hidden/>');
    $('.area3').append('<input type="text" name="staff_career_businessName" hidden/>');
    $('.area3').append('<input type="text" name="staff_career_position" hidden/>');
    $('.area3').append('<input type="text" name="staff_career_workday" hidden/>');

    
    if(career_num>1){ //추가 없을시 바로 submit
        for(let i=1;career_num>i;i++){ 
            staff_career_eventName += ','+$('input[name="input_staff_career_eventName'+i+'"]').val();
            staff_career_businessName += ','+$('input[name="input_staff_career_businessName'+i+'"]').val();
            staff_career_position += ','+$('input[name="input_staff_career_position'+i+'"]').val();
            staff_career_workday += ','+$('input[name="input_staff_career_workday'+i+'"]').val();
        }
    }

    // hidden input요소에 합친 값을 저장
    $('input[name="staff_career_eventName"]').val(staff_career_eventName);
    $('input[name="staff_career_businessName"]').val(staff_career_businessName);
    $('input[name="staff_career_position"]').val(staff_career_position);
    $('input[name="staff_career_workday"]').val(staff_career_workday);

    
    var form = $('form[name="resumeForm"]');
    var formData = new FormData(form[0]);
    $.ajax({
        url : actionUrl,
        data : formData,
        type : 'POST',
        enctype : 'multipart/form-data',
        processData : false,
        contentType : false,
        cache : false,
        success : function (result) {
            window.location.href= result;
        }
    });
    } else {
        return false;
    }


}

