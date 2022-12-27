var now = new Date();
var startYear = now.getFullYear();
var endYear = startYear - 100;

$(function() {

    // 전체 동의 체크 박스
    $('#all_chk').click(function(){
        if($("#all_chk").is(':checked')){
            $('.normal').prop('checked', true);
        }else{
            $('.normal').prop('checked', false);
        }
    });

    $('.normal').click(function(){
        // chk1, chk2 체크박스 선택 시 전체 동의 체크박스 체크
        if($("#chk1").is(':checked') && $("#chk2").is(':checked')){
            $('#all_chk').prop('checked', true);
        }else{
            $('#all_chk').prop('checked', false);
        }

        if($("#all_chk").is(':checked')) {
            $('.normal').prop('checked', true);
        }
    });

    // chk1, chk2 체크박스 선택 시 전체 동의 체크박스 체크
    $('.normal').click(function(){
        if($(this).is(':checked')){
            $(this).prop('checked', true);
        }

        // radio unchecked 상태에서 click 시 checked
        else{
            $(this).prop('checked', false);
        }

        // radio 체크된 상태에서 click 시 unchecked
        if($(this).is(':checked')){
            $(this).removeAttr('checked');
        }

        // radio unchecked 상태에서 click 시 checked
        else{
            $(this).prop('checked', true);
        }

        if($("#chk1").is(':checked') && $("#chk2").is(':checked')){
            $('#all_chk').prop('checked', true);
        }else{
            $('#all_chk').prop('checked', false);
        }
    });

    // 전체 동의 안 되어 있으면 회원가입 버튼 비활성화
    $(".join_btn").click(function() {
        if($("#all_chk").prop("checked") || ($("#chk1").prop("checked") && $("#chk2").prop("checked"))) {
            // join2.html로 이동
            location.href = "/join2";
        }
        else {
            alert("이용약관 및 개인정보 수집 및 이용에 동의해주세요.");
        }
    });


    // 생년 월일 select box
    for (var i = startYear; i >= endYear; i--) { // 년도
        $('#birth_year').append('<option value="' + i + '">' + i + '</option>');
    }

    for (var i = 1; i <= 12; i++) { // 월
        $('#birth_month').append('<option value="' + i + '">' + i + '</option>');
    }

    for (var i = 1; i <= 31; i++) { // 일
        $('#birth_day').append('<option value="' + i + '">' + i + '</option>');
    }
});