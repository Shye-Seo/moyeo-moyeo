
$(function() {
    // login 관련 javascript
    // loginform_check() : 로그인 폼 체크
    $(".login_btn").click(function(){
        if($("#loginform input[name=user_id]").val() == ""){
            alert("아이디를 입력해주세요.");
            $("#loginform input[name=user_id]").focus();
            return false;
        }
        if($("#loginform input[name=user_pw]").val() == ""){
            alert("비밀번호를 입력해주세요.");
            $("#loginform input[name=user_pw]").focus();
            return false;
        }
        $("#loginform").submit();
    });

    // find_id_pw 관련 javascript
    // tab1, tab2 display 설정
    $("#tab1").on("click", function() {
        $(".tab1_content").css('display', 'inline');
        $(".tab2_content").css('display', 'none');
    });

    $("#tab2").on("click", function() {
        $(".tab1_content").css('display', 'none');
        $(".tab2_content").css('display', 'inline');
    });
    
    // 빈칸에 대하여 경고창 띄우기
    $(".ask_certifinum").click(function(){
        if($("#find_id_form input[name=user_phone]").val() == ""){
            alert("전화번호를 입력해주세요.");
            $("#find_id_form input[name=user_phone]").focus();
            return false;
        }
    });

    $(".asked_certifinum").click(function(){
        if($("#find_id_form input[name=user_phone]").val() == ""){
            alert("인증번호를 입력해주세요.");
            $("#find_id_form input[name=user_phone]").focus();
            return false;
        }
        else {
            location.href="/login";
        }
    });

});