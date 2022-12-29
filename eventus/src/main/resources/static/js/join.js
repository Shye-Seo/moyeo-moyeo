var now = new Date();
var startYear = now.getFullYear();
var endYear = startYear - 100;

$(function() {

    var certifinum = "1"; // 인증번호

    // 약관동의
    // 전체 동의 체크 박스
    $('#all_chk').click(function(){
        if($("#all_chk").is(':checked')){
            $('.normal').prop('checked', true);
        }else{
            $('.normal').prop('checked', false);
        }
    });
    
    // 전체 동의 안 되어 있으면 회원가입 버튼 비활성화
    $(".join_btn").click(function() {
        if($("#all_chk").prop("checked") || ($("#chk1").prop("checked") && $("#chk2").prop("checked"))) {
            // join2.html로 이동
            location.href = "/join2";
        }
        else {
            alert("이용약관 및 개인정보 처리 및 이용에 동의해주세요.");
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

    
    
    
    
    // 회원정보 입력
    // 아이디 입력했을 때 중복확인
    $("#member input[name=user_id]").keyup(function() {
        $.ajax({
            type: "GET",
            url: "/idchk",
            data: {user_id: $(this).val()},
        }).done(function(data) {
            if(data == 1) {
                $("#id_check").text("이미 사용중인 아이디입니다.");
                $("#id_check").css("color", "red");
            }
            else {
                $("#id_check").text("사용 가능한 아이디입니다.");
                $("#id_check").css("color", "green");
            }
        });
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

    // 인증번호 발송
    $('#certifinum_submit').click(function() {
        if($('#member input[name=user_phone]').val() == '') {
            alert('휴대번호를 입력해주세요.');
            $('#member input[name=user_phone]').focus();
            return false;
        }
        else {
            alert("인증번호가 발송되었습니다. 인증번호를 입력해주세요.");
            $("#certifinum_submit").attr("disabled", true);
            setTimeout(function(){
                $("#certifinum_submit").attr("disabled", false);
            }, 60000);

            // ajax를 이용하여 인증번호를 요청한다.
            $.ajax({
                url: "/phoneCheck",
                type: "GET",
                data: { user_phone: $("#member input[name=user_phone]").val() },
                dataType: "text",
            }).done(function(data){
                certifinum = data;
            });
        }
    });

    // 인증번호 비교
    $('#certifinum_check').click(function() {
        // certifinum과 인증번호가 같으면
        if(certifinum == $('#member #certifinum').val()) {
            alert("인증번호가 일치합니다.");
        }
        else {
            alert("인증번호가 일치하지 않습니다.");
            // 회원가입 버튼 활성화
            $(".join_complete_btn").attr("disabled", true);
        }
    });

    // 회원가입
    $('.join_complete_btn').click(function(){
        // 아이디 빈칸인지 확인
        if($('#member input[name=user_id]').val() == '') {
            alert('아이디를 입력해주세요.');
            $('#member input[name=user_id]').focus();
            return false;
        }
        // 비밀번호 빈칸인지 확인
        else if($('#member input[name=user_pw]').val() == '') {
            alert('비밀번호를 입력해주세요.');
            $('#member input[name=user_pw]').focus();
            return false;
        }
        // 비밀번호 확인 빈칸인지 확인
        else if($('#member #checked_pw').val() == '') {
            alert('비밀번호 확인을 입력해주세요.');
            $('#checked_pw').focus();
            return false;
        }
        // 이름 빈칸인지 확인
        else if($('#member input[name=user_name]').val() == '') {
            alert('이름을 입력해주세요.');
            $('#member input[name=user_name]').focus();
            return false;
        }
        // 이메일주소 빈칸인지 확인
        else if($('#member input[name=user_email]').val() == '') {
            alert('이메일주소를 입력해주세요.');
            $('#member input[name=user_email]').focus();
            return false;
        }
        // 생일 년도, 월, 일 빈칸인지 확인
        else if($('#member #birth_year').val() == '년도' || $('#member #birth_month').val() == '월' || $('#member #birth_day').val() == '일') {
            alert('생일을 입력해주세요.');
            return false;
        }

        // 성별 체크되어 있는지 확인
        else if($('#member input[name=user_gender]:checked').val() == undefined) {
            alert('성별을 선택해주세요.');
            return false;
        }

        // 휴대번호 빈칸인지 확인
        else if($('#member input[name=user_phone]').val() == '') {
            alert('휴대번호를 입력해주세요.');
            $('#member input[name=user_phone]').focus();
            return false;
        }

        // 회원가입 진행
        else {

        }
    });
});