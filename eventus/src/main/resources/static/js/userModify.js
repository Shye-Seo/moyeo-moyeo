//인증번호
var certinum = 0;
// 인증번호 확인 여부
var certifi_checked = "0"; 

$(function(){

    //인증번호 발송
    $("#auth_bt").click(()=>{
        const newPhoneNum = $("#input_phone").val();
        const oldPhoneNum = $("#input_phone").attr("data-num");
        if(newPhoneNum == "" || newPhoneNum == undefined) {
            alert("휴대전화 번호를 입력해주세요.");
            return false;
        }else if(newPhoneNum == oldPhoneNum){
            alert("현재 전화번호와 같습니다.");
            return false;
        }
        // 인증번호 요청
        else {
            $.ajax({
                url: "/phoneCheck",
                type: "GET",
                data: {
                    user_phone: newPhoneNum
                },
                dataType: "text",
            }).done(function(date) {
                certinum = date;
                alert("인증번호가 발송되었습니다.");
                $(".phone_bt_box").toggle();
                $(".phone_ck_box").toggle();
                $("#certifinum").attr("disabled", false);
                timeOut()
            })
        }
    })

    // 재인증
    $('#resend').click(function() {
        const newPhoneNum = $("#input_phone").val();

        $.ajax({
            url: "/phoneCheck",
            type: "GET",
            data: {
                user_phone: newPhoneNum
            },
            dataType: "text",
        }).done(function(date) {
            alert("인증번호가 재발송되었습니다.");
            $("#certifinum").attr("disabled", false);
            certinum = date;
            timeOut()
        })
    })

    // 인증번호 일치하면 '재인증'을 '변경'으로
    $('#certifinum').keyup(function() {
        if($('#certifinum').val() == certinum && certinum!=0 && certifi_checked=="1") {
            $('#resend').hide();
            $('#submit_btn').show();
        }
    })

     // 휴대전화 번호 변경
     $('#submit_btn').click(function() {
        const newPhoneNum = $("#input_phone").val();
        const userId = $("#input_id").val();
        $.ajax({
            url: "/updatePhone",
            type: "GET",
            data: {
                user_id: userId,
                user_phone: newPhoneNum
            },
        }).done(function() {
            alert("휴대전화 번호가 변경되었습니다.");
            // history.back();
            location.reload();
        })
    })

    // 비밀번호 변경
    $("#change_pw_button").click(function() {
        if($("#old_pw").val() == "") {
            alert("현재 비밀번호를 입력해주세요.");
            return false;
        }
        else if($("#new_pw").val() == "" || $("#new_pw_ck").val() == "") {
            alert("변경할 비밀번호를 입력해주세요.");
            return false;
        }
        else if($("#new_pw").val().length < 10 || $("#new_pw").val().length > 16) {
            alert("비밀번호는 10-16자로 입력해주세요.");
            return false;
        }
        else if(!/^(?!((?:[A-Za-z]+)|(?:[~!@#$%^&*()_+=]+)|(?:[0-9]+))$)[A-Za-z\d~!@#$%^&*()_+=]{10,16}$/.test($("#new_pw").val())) {
            alert("비밀번호는 영문 대소문자/숫자/특수문자 중 2가지 이상 조합해주세요.");
            return false;
        }
        // 비밀번호가 다를 경우
        else if($("#new_pw").val() != $("#new_pw_ck").val()) {
            alert("변경할 비밀번호가 일치하지 않습니다.");
            return false;
        }

        // ajax로 /updatePw 호출
        $.ajax({
            url: "/updatePw_modify",
            type: "POST",
            data: {
                user_id: $("input[name=id]").val(),
                user_pw: $("#new_pw").val(),
                old_pw: $("#old_pw").val(),
            },
            success: function(data) {
                if(data === "success"){
                    alert("비밀번호가 변경되었습니다.");
                    location.href='/LogoutProc';
                }else if(data === "different"){
                    alert("현재비밀번호가 일치하지 않습니다.");
                }else{
                    console.log("비밀번호 변경실패")
                }
            }
        })
    })

})

//번호 변경 버튼이벤트 
function click_ckBt(state){
    $("#phone_check_bt").toggle();
    $("#phone_cancel_bt").toggle();
    
    certinum = 0;
    certifi_checked = "0"; 

    //취소버튼
    if(state === 0){
        $(".phone_ck_box").hide();
        $(".phone_bt_box").hide();
        
        $("#certifinum").val("");
        if(typeof timer !== "undefined"){
            clearInterval(timer);
        }
        $("#input_phone").attr('disabled',true);
        $("#input_phone").removeAttr('style');
        $("#input_phone").val($("#input_phone").attr("data-num"))

        $('#certifinum_check').css({'background':'#DDDDDD'});
        $('#certifinum_check').css({'font-size':'14px'});
        $('#certifinum_check').attr('value','✓');
        certifi_checked = "0";
        
        $("#certifinum_submit").removeAttr("disabled");
        $('#resend').show();
        $('#submit_btn').hide();
    
    //변경하기버튼
    }else if(state === 1){

        $(".phone_bt_box").show();

        $("#input_phone").removeAttr('disabled');
        $("#input_phone").css("background-color","#fff");
    }
    
}


function timeOut(){
    // 3분 타이머
    certifi_checked = "0";
    var time = 180;
    timer = setInterval(function() {
        var min = Math.floor(time / 60);
        var sec = time % 60;
        $("#certifi_time").text(min + "분 " + sec + "초");
        time--;
        if(time < 0) {
            alert("인증시간이 만료되었습니다. 다시 시도해주세요.");
            clearInterval(timer);
            $("#certifi_time").text("시간 초과");
            $("#certifinum").val("");
            $("#certifinum").attr("disabled", true);

            $('#certifinum_check').css({'background':'#DDDDDD'});
            $('#certifinum_check').css({'font-size':'14px'});
            $('#certifinum_check').attr('value','✓');
            certifi_checked = "0";
        }
    }, 1000);
}

// 인증번호 비교
function certifinum_checking(input_num){
    if(input_num.length >= 4){
        if(certinum == input_num){
            $('#certifinum_check').css({'background':'#00DE3C'});
            $('#certifinum_check').css({'font-size':'14px'});
            clearInterval(timer)
            $("#certifi_time").text("");
            certifi_checked = "1";
            $('#certifinum').attr('disabled','true');
        }else{
            $('#certifinum_check').css({'background':'#FF7E93'});
            $('#certifinum_check').css({'font-size':'10px'});
            $('#certifinum_check').attr('value','✕');
            certifi_checked = "0";
        }
    }else{
        $('#certifinum_check').css({'background':'#DDDDDD'});
        $('#certifinum_check').css({'font-size':'14px'});
        $('#certifinum_check').attr('value','✓');
        certifi_checked = "0";
    }
}


