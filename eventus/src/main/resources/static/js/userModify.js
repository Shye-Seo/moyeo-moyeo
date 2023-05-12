
function click_ckBt(){
    $(".phone_bt_box").toggle();
    $("#phone_check_bt").toggle();
    $("#phone_cancel_bt").toggle();

    if($("#input_phone").attr('disabled')!=undefined){
        $("#input_phone").removeAttr('disabled');
        $("#input_phone").css("background-color","#fff");
    }else{
        $("#input_phone").attr('disabled',true);
        $("#input_phone").removeAttr('style');
    } 
    
}

function send_authNum(){
    const newPhoneNum = $("input_phone").val();

}