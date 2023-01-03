var now = new Date();
var startYear = now.getFullYear();
var endYear = startYear - 100;

$(function() {
    var data = new Date();
    var year = data.getFullYear();
    var month = data.getMonth() + 1;
    var day = data.getDate();
    var today = year + "-" + month + "-" + day;

    var certifinum = "1"; // 인증번호
    var id_checked = "0"; // 아이디 중복확인 여부
    var pw_checked = "0" // 비밀번호 확인 여부
    var certifi_checked = "0"; // 인증번호 확인 여부
    var email_checked = "0"; // 이메일 중복 확인 여부

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
                id_checked = "0";
                $("#id_check").text("이미 사용중인 아이디입니다.");
                $("#id_check").css("color", "red");
            }
            else {
                id_checked = "1";
                $("#id_check").text("사용 가능한 아이디입니다.");
                $("#id_check").css("color", "green");
            }
        });
    });

    // 비밀번호가 영문, 숫자 특수기호 중 2가지 이상 조합, 10자~16자 인지 확인
    $("#member input[name=user_pw]").keyup(function() {
        var regExp = /^(?!((?:[A-Za-z]+)|(?:[~!@#$%^&*()_+=]+)|(?:[0-9]+))$)[A-Za-z\d~!@#$%^&*()_+=]{10,16}$/;
        if(!regExp.test($("#member input[name=user_pw]").val())){
            pw_checked = "0";
            $("#pw_check1").text("비밀번호 형식이 맞지 않습니다.");
            $("#pw_check1").css("color", "red");
        }
        else {
            pw_checked = "1";
            $("#pw_check1").text("사용 가능한 비밀번호입니다.");
            $("#pw_check1").css("color", "green");
        }
    });

    // 비밀번호 확인
    $("#member #checked_pw").keyup(function() {
        if($(this).val() != $("#member input[name=user_pw]").val()) {
            pw_checked = "0";
            $("#pw_check2").text("비밀번호가 일치하지 않습니다.");
            $("#pw_check2").css("color", "red");
        }
        else {
            pw_checked = "1";
            $("#pw_check2").text("비밀번호가 일치합니다.");
            $("#pw_check2").css("color", "green");
        }
    });

    // 이메일 주소 형식 확인
    $("#member input[name=user_email]").keyup(function() {
        var email = $(this).val();
        var emailReg = /^([\w-\.]+@([\w-]+\.)+[\w-]{2,4})?$/;
        if(emailReg.test(email)) {
            email_checked = "1";
            $("#email_check").text("사용 가능한 이메일입니다.");
            $("#email_check").css("color", "green");
        }
        else {
            email_checked = "0";
            $("#email_check").text("이메일 형식이 올바르지 않습니다.");
            $("#email_check").css("color", "red");
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

    // 인증번호 발송
    $('#certifinum_submit').click(function() {
        $("#certifinum_check").attr("disabled", false);
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

            // 3분 타이머
            certifi_checked = "0";
            var time = 180;
            var timer = setInterval(function() {
                var min = Math.floor(time / 60);
                var sec = time % 60;
                $("#certifi_time").text(min + "분 " + sec + "초");
                time--;
                if(time < 0) {
                    alert("인증시간이 만료되었습니다. 다시 시도해주세요.");
                    clearInterval(timer);
                    $("#certifi_time").text("시간 초과");
                    $("#certifinum_check").attr("disabled", true);
                }
            }, 1000);

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
            $("#certifi_time").text("인증 완료");
            certifi_checked = "1";
        }
        else {
            alert("인증번호가 일치하지 않습니다.");
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

        // 인증번호 빈칸인지 확인
        else if($('#member #certifinum').val() == '') {
            alert('인증번호를 입력해주세요.');
            $('#member #certifinum').focus();
            return false;
        }

        // 인증번호 확인했는지 확인
        else if(certifi_checked == "0") {
            alert("휴대번호 인증을 해주세요.");
            return false;
        }
        
        // 아이디 중복 여부 확인
        else if(id_checked == "0") {
            alert("아이디 중복확인을 해주세요.");
            return false;
        }
        
        // 비밀번호 형식 맞추었는지, 비밀번호와 비밀번호 확인이 같은지 확인
        else if(pw_checked == "0") {
            alert("비밀번호를 확인을 해주세요.");
            return false;
        }
        
        // 이메일 형식 갖추었는지 확인
        else if(email_checked == "0") {
            alert("이메일 형식을 확인을 해주세요.");
            return false;
        }

        // 회원가입 진행
        else {
            // 회원가입 진행
            $.ajax({
                url: "/insertUser",
                type: "GET",
                data: {
                    user_id: $('#member input[name=user_id]').val(),
                    user_pw: $('#member input[name=user_pw]').val(),
                    user_name: $('#member input[name=user_name]').val(),
                    user_email: $('#member input[name=user_email]').val(),
                    user_birth: $('#member #birth_year').val() + '-' + $('#member #birth_month').val() + '-' + $('#member #birth_day').val(),
                    user_gender: $("#member input[name=user_gender]:checked").val(),
                    user_phone: $('#member input[name=user_phone]').val(),
                    user_date_join: today,
                    user_authority:1,
                }
            }).done(function(data) {
                if(data==1) {
                    location.href = "/join3";
                }
                else {
                    alert("회원가입에 실패하였습니다.");
                    location.href = "/join2";
                }
            })
        }
    });
});