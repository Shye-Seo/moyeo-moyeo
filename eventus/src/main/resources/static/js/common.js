

// 제이쿼리 바로밑에 불러주세요!

$(function(){

    // input, textarea 맞춤법 검사 한번에 끄기
    $('textarea').attr('spellcheck','false');
    $('input[type="text"]').attr('spellcheck','false');

    //숫자만 입력받기 ( 클래스 onlyNum 추가하면 작동 )
    $('.onlyNum').on("input", function() {
        $(this).val($(this).val().replace(/[^0-9]/g,""));
    });

})


//전화번호에 - 표시 넣기 
function chkItemPhone(phonenum) {
    let number = phonenum.replace(/[^0-9]/g, "");
    let phone = "";
    if (number.length < 9) {
        return number;
    } else if (number.length < 10) {
        phone += number.substr(0, 2);
        phone += "-";
        phone += number.substr(2, 3);
        phone += "-";
        phone += number.substr(5);
    } else if (number.length < 11) {
        phone += number.substr(0, 3);
        phone += "-";
        phone += number.substr(3, 3);
        phone += "-";
        phone += number.substr(6);
    } else {
        phone += number.substr(0, 3);
        phone += "-";
        phone += number.substr(3, 4);
        phone += "-";
        phone += number.substr(7,4);
    }
    return phone;
}

//전송 시 - 표시 빼기
function replacePhone(phonenum){
    return phonenum.replaceAll("-","");
}

//2023.01.01 형식으로 출력하기
function chkDate(in_birth){
    let births = in_birth.split('-');

    if(String(births[1]).length == 1){
        births[1]= '0' + births[1];
    }
    if(String(births[2]).length == 1){
        births[2]= '0' + births[2];
    }

    return `${births[0]}.${births[1]}.${births[2]}`;
}

let flag = 0;
//textarea 글 작성시 자동 크기조정 
//조정할 textarea에 onkeydown="resize(font_size, min_size,this)" 넣기
function resize(font_size, min_size, obj) {
    resizeHeight = ((font_size*flag) + obj.scrollHeight);
    if(resizeHeight>(min_size + font_size)){
        obj.style.height = '1px';
        obj.style.height =((font_size*flag) + obj.scrollHeight)+ 'px';
    }else{
        obj.style.height = '100px';
        flag=1
    }
}

//금액 콤마찍기
//costText(적용할 클래스,아이디);
function costText(obj){
	cost = obj.innerText;
	let costtext = cost.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");
	console.log(costtext)
	obj.innerHTML =costtext;
}