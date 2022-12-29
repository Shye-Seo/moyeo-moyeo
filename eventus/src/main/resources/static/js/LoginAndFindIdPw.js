
$(function() {
    // 변수
    var certifinum = "1"; // 인증번호
    var getid = "1"; // 아이디

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
    // section id가 tab1_content인 경우
    $("#find_id .ask_certifinum").click(function(){
         // 이름 입력되어 있는지 확인
         if($("#find_id input[name=user_name]").val() == ""){
            alert("이름을 입력해주세요.");
            $("#find_id input[name=user_name]").focus();
            return false;
         }
        else if($("#find_id input[name=user_phone]").val() == ""){
            alert("전화번호를 입력해주세요.");
            $("#find_id input[name=user_phone]").focus();
            return false;
        }
         // 아니라면
         else {
            // 전화번호를 입력한 상태에서 인증번호 요청 버튼을 누르면
            alert("인증번호가 발송되었습니다. 인증번호를 입력해주세요.");
            // 인증번호 요청 버튼을 비활성화 시키고
            $(".ask_certifinum").attr("disabled", true);
            // 인증번호 요청 버튼을 60초 뒤에 활성화 시킨다.
            setTimeout(function(){
                $(".ask_certifinum").attr("disabled", false);
            }, 60000);

            // ajax를 이용하여 인증번호를 요청한다.
            $.ajax({
                url: "/phoneCheck",
                type: "GET",
                data: { user_phone: $("#find_id input[name=user_phone]").val() },
                dataType: "text",
            }).done(function(data){
                certifinum = data;
            });
         }
    });

    // 인증번호 비교
    $("#find_id  .asked_certifinum").click(function(){
         if($("#find_id .certifi_num").val() == certifinum){
            alert("인증번호가 일치합니다.");
            // 클래스가 find_id_btn 버튼 활성화하고 클릭시
            $(".find_id_btn").attr("disabled", false);
            $(".find_id_btn").click(function(){
                // user_name이 비어있을 경우 경고창을 띄우고
                if($("#find_id input[name=user_name]").val() == ""){
                    alert("이름을 입력해주세요.");
                    $("#find_id input[name=user_name]").focus();
                    return false;
                }

                $.ajax({
                    url: "/findId",
                    type: "GET",
                    data: {
                        user_name: $("#find_id input[name=user_name]").val(),
                        user_phone: $("#find_id input[name=user_phone]").val()
                    },
                    dataType: "text",
                }) // 성공했을 때
                .done(function(data){
                    if(data == "failure"){
                        alert("등록된 회원이 아닙니다.");
                    }
                    else {
                        $("#getid").text(data);
                        $(".tab1_content").css('display', 'none');
                        $(".found_id").css('display', 'inline');
                    }
                });
            });
         }
        else {
            alert("인증번호가 일치하지 않습니다.");
        }
    });

    // section id가 tab2_content인 경우
    $("#find_pw .ask_certifinum").click(function(){
        // 아이디 입력되어 있는지 확인
        if($("#find_pw input[name=user_id]").val() == ""){
            alert("아이디를 입력해주세요.");
            $("#find_pw input[name=user_id]").focus();
            return false;
        }
        else if($("#find_pw input[name=user_phone]").val() == ""){
            alert("전화번호를 입력해주세요.");
            $("#find_pw input[name=user_phone]").focus();
            return false;
        }
        // 아니라면
        else {
            // 전화번호를 입력한 상태에서 인증번호 요청 버튼을 누르면
            alert("인증번호가 발송되었습니다. 인증번호를 입력해주세요.");
            // 인증번호 요청 버튼을 비활성화 시키고
            $(".ask_certifinum").attr("disabled", true);
            // 인증번호 요청 버튼을 60초 뒤에 활성화 시킨다.
            setTimeout(function(){
                $(".ask_certifinum").attr("disabled", false);
            }, 60000);

            // ajax를 이용하여 인증번호를 요청한다.
            $.ajax({
                url: "/phoneCheck",
                type: "GET",
                data: { user_phone: $("#find_pw input[name=user_phone]").val() },
                dataType: "text",
            }).done(function(data){
                certifinum = data;
            });
        }
    });

    // 인증번호 비교
    $("#find_pw  .asked_certifinum").click(function(){
        if($("#find_pw .certifi_num").val() == certifinum){
            alert("인증번호가 일치합니다.");
            // 클래스가 find_pw_btn 버튼 활성화하고 클릭시
            $(".find_pw_btn").attr("disabled", false);
            $(".find_pw_btn").click(function(){
                // user_name이 비어있을 경우 경고창을 띄우고
                if($("#find_pw input[name=user_id]").val() == ""){
                    alert("아이디를 입력해주세요.");
                    $("#find_pw input[name=user_id]").focus();
                    return false;
                }
                
                // 등록된 회원이 있는지 확인
                $.ajax({
                    url: "/findIdForPw",
                    type: "GET",
                    data: {
                        user_id: $("#find_pw input[name=user_id]").val(),
                        user_phone: $("#find_pw input[name=user_phone]").val()
                    },
                    dataType: "text",
                }) // 성공했을 때
                    .done(function(data){
                        if(data == "failure"){
                            alert("등록된 회원이 아닙니다.");
                        }
                        else {
                            getid = data;
                            $(".tab2_content").css('display', 'none');
                            $(".changing_pw").css('display', 'inline');
                        }
                    });
            });
        }
        else {
            alert("인증번호가 일치하지 않습니다.");
        }
    });

    // 비밀번호 변경
    $(".changing_pw .change_pw_btn").click(function(){
        // 비밀번호가 영문, 숫자 특수기호 중 2가지 이상 조합, 10자~16자 인지 확인
        var regExp = /^(?!((?:[A-Za-z]+)|(?:[~!@#$%^&*()_+=]+)|(?:[0-9]+))$)[A-Za-z\d~!@#$%^&*()_+=]{10,16}$/;
        if(!regExp.test($(".changing_pw input[name=user_pw]").val())){
            alert("비밀번호는 영문, 숫자, 특수기호 중 2가지 이상 조합, 10자~16자로 입력해주세요.");
            $(".changing_pw input[name=user_pw]").focus();
            return false;
        }
        // 비밀번호 확인이 비밀번호와 일치하는지 확인
        else if($(".changing_pw input[name=user_pw]").val() != $(".changing_pw #checked_new_pw").val()){
            alert("비밀번호가 일치하지 않습니다.");
            $(".changing_pw input[name=user_pw_check]").focus();
            return false;
        }
        // 아니라면
        else {
            // 비밀번호 변경
            $.ajax({
                url: "/updatePw",
                type: "POST",
                data: {
                    user_id: getid,
                    user_pw: $(".changing_pw input[name=user_pw]").val() },
                dataType: "text",
            }).done(function(data){
                $(".changing_pw").css('display', 'none');
                $(".changed_complete").css('display', 'inline');
            });
        }
    });

    // display 설정
    $("#tab1").on("click", function() {
        $(".tab1_content").css('display', 'inline');
        $(".tab2_content").css('display', 'none');
        $(".found_id").css('display', 'none');
        $(".changing_pw").css('display', 'none');
    });

    $("#tab2").on("click", function() {
        $(".tab1_content").css('display', 'none');
        $(".tab2_content").css('display', 'inline');
        $(".found_id").css('display', 'none');
        $(".changing_pw").css('display', 'none');
    });
});