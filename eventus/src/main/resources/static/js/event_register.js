
        $(function () {
            $(".datepicker").datepicker({ dateFormat: "yy.mm.dd" });
            $(".datetimepicker").datetimepicker({ format: "Y.m.d H:i"});

            $("#input_file").bind('change', function() {
                selectFile(this.files);

                //파일 5개 이상일 시 버튼 비활성화
                if($('#file_names').children().length >=maxFileNum){
                    $('#input_file').attr("disabled","disabled");
                    $('.area3 label').css({'background-color':'#eee', 'cursor':'unset'});
                }
            });

            //필수값 체크
            $('#submit_bt').click(function(){
                let result = true;
                $('.required').each(function(){
                    if($(this).val() == ""){
                        $(this).focus();
                        result = false;
                        return false;
                    }
                })
                if(!result) return false;
                submitDate('/eventAdd');
            })
            $('#update_bt').click(function(){
                let result = true;
                $('.required').each(function(){
                    if($(this).val() == ""){
                        $(this).focus();
                        result = false;
                        return false;
                    }
                })
                if(!result) return false;
                submitDate('/eventUpdate');
            })
            
        });

        // datepicker setting
        $.datepicker.setDefaults({
            dateFormat: 'yy.mm.dd',
            //prevText: '이전 달',
            //nextText: '다음 달',
           //monthNames: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
            //monthNamesShort: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
            //dayNames: ['일', '월', '화', '수', '목', '금', '토'],
            //dayNamesShort: ['일', '월', '화', '수', '목', '금', '토'],
            //dayNamesMin: ['일', '월', '화', '수', '목', '금', '토'],
            showMonthAfterYear: true,
            //yearSuffix: '년'
            changeMonth: true,
            changeYear: true
        });
        
        // 포지션 추가
        let position_num =1;
        const max_num =5;

        function add_position(){
            $(".area4_pn:first").clone(true).appendTo($(".position_wrap"));

            $('.area4_pn:last input').val('');
            $('.area4_pn:last input').each(function(){
                let name =  $(this).attr('name');
                $(this).attr('name',name+position_num)
            })
            
            $('.area4_pn:last').append('<div class="remove_position"  style="--i :'+position_num+' ;" onclick="remove_position(this)"></div>');
            position_num++;
            if(position_num == max_num){
                $("#add_position").hide();
            }
        }

        function add_position_forUpdate(position, position_count, position_startTime, position_endTime, position_pay){
            $(".area4_pn:first").clone(true).appendTo($(".position_wrap"));
            $('.area4_pn:last input[name="input_event_position"]').val(position);
            $('.area4_pn:last input[name="input_event_position_count"]').val(position_count);
            $('.area4_pn:last input[name="input_event_position_startTime"]').val(position_startTime);
            $('.area4_pn:last input[name="input_event_position_endTime"]').val(position_endTime);
            $('.area4_pn:last input[name="input_event_position_pay"]').val(position_pay);

            $('.area4_pn:last input').each(function(){
                let name =  $(this).attr('name');
                $(this).attr('name',name+position_num)
            })
            $('.area4_pn:last').append('<div class="remove_position"  style="--i :'+position_num+' ;" onclick="remove_position(this)"></div>');
            position_num++;
            if(position_num == max_num){
                $("#add_position").hide();
            }
        }

        //포지션삭제
        function remove_position(obj){
            let remove_num= $(obj).css('--i')*1;
            if(remove_num == position_num-1){
            }else{
                $('.input_po').each(function(){
                    let i = $(this).attr('name').charAt($(this).attr('name').length-1);
                    if(i>remove_num){
                        $(this).attr('name','input_event_position'+ (i-1));
                    }
                })
                $('.input_pn').each(function(){
                    let i = $(this).attr('name').charAt($(this).attr('name').length-1);
                    if(i>remove_num){
                        $(this).attr('name','input_event_position_count'+ (i-1));
                    }
                })
                $('.input_st').each(function(){
                    let i = $(this).attr('name').charAt($(this).attr('name').length-1);
                    if(i>remove_num){
                        $(this).attr('name','input_event_position_startTime'+ (i-1));
                    }
                })
                $('.input_et').each(function(){
                    let i = $(this).attr('name').charAt($(this).attr('name').length-1);
                    if(i>remove_num){
                        $(this).attr('name','input_event_position_endTime'+ (i-1));
                    }
                })
                $('.input_pp').each(function(){
                    let i = $(this).attr('name').charAt($(this).attr('name').length-1);
                    if(i>remove_num){
                        $(this).attr('name','input_event_position_pay'+ (i-1));
                    }
                })
                $('.remove_position').each(function(){
                    let i = $(this).css('--i')*1;
                    if(i>remove_num){
                        $(this).css('--i',(i-1));
                    }
                })
            }
            $(obj).parent().remove();
            position_num--;
            if($("#add_position").css('display')=='none'){
                $("#add_position").show();
            }
        }

        // form submit
        function submitDate(actionUrl){
            if (confirm("등록 하시겠습니까?") == true){

                let event_position = $('input[name="input_event_position"]').val();
                let event_position_count = $('input[name="input_event_position_count"]').val();
                let event_position_startTime = $('input[name="input_event_position_startTime"]').val();
                let event_position_endTime = $('input[name="input_event_position_endTime"]').val();
                let event_position_pay = $('input[name="input_event_position_pay"]').val();

                $('.area4').append('<input type="text" name="event_position" hidden/>')
                $('.area4').append('<input type="text" name="event_position_count" hidden/>')
                $('.area4').append('<input type="text" name="event_position_startTime" hidden/>')
                $('.area4').append('<input type="text" name="event_position_endTime" hidden/>')
                $('.area4').append('<input type="text" name="event_position_pay" hidden/>')

                $('.area4').append('<input type="text" name="event_startDate" hidden/>')
                $('.area4').append('<input type="text" name="event_endDate" hidden/>')


                if(position_num>1){ //추가 없을시 바로 submit
                    for(let i=1;position_num>i;i++){ 
                        event_position += ','+$('input[name="input_event_position'+i+'"]').val();
                        event_position_count += ','+$('input[name="input_event_position_count'+i+'"]').val();
                        event_position_startTime += ','+$('input[name="input_event_position_startTime'+i+'"]').val();
                        event_position_endTime += ','+$('input[name="input_event_position_endTime'+i+'"]').val();
                        event_position_pay += ','+$('input[name="input_event_position_pay'+i+'"]').val();
                    }
                }

                //
                const searchDate = $("#searchDate").val();
                const searchDateArr = searchDate.split(' - ')

                $('input[name="event_startDate"]').val(searchDateArr[0])
                $('input[name="event_endDate"]').val(searchDateArr[1])
                

                // hidden input요소에 합친 값을 저장
                $('input[name="event_position"]').val(event_position);
                $('input[name="event_position_count"]').val(event_position_count);
                $('input[name="event_position_startTime"]').val(event_position_startTime);
                $('input[name="event_position_endTime"]').val(event_position_endTime);
                $('input[name="event_position_pay"]').val(event_position_pay);

                for(let i = 0; i < deleteFileNameList.length; i++){
                    $('form[name="eventAddForm"]').append("<input type='hidden' name='deleteFileNameList' value='"+deleteFileNameList[i]+"'/>")
                }

                // var uploadFileList = Object.keys(fileList);
                var form = $('form[name="eventAddForm"]');
                var formData = new FormData(form[0]);
                for (var i = 0; i < fileList.length; i++) {
                    formData.append('event_file', fileList[i]);
                    console.log(formData.get('event_file')) 
                    console.log(fileList[i]) 
                }

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

           // 파일 리스트 번호
            var fileIndex = 0;
            // 등록할 전체 파일 사이즈
            var totalFileSize = 0;
            // 파일 리스트
            var fileList = new Array();
            // 파일 사이즈 리스트
            var fileSizeList = new Array();
            // 등록 가능한 파일 사이즈 MB
            //    var uploadSize = 5;
            // 등록 가능한 총 파일 사이즈 MB
            //    var maxUploadSize = 500;
            
            var maxFileNum = 5;
            // 파일 최대갯수

            //행사수정 시 지울 파일 목록
            let deleteFileNameList = new Array();

            var files = new Array();

        // 파일 선택시
        function selectFile(fileObject) {
        //var files = null;
        //if (fileObject != null) {
           // 파일 Drag 이용하여 등록시
           //files = fileObject;
       //} else {
           // 직접 파일 등록시
           
            //}
            files = $('#input_file')[0].files;
            // 다중파일 등록
            if (files != null) {
           
           
           for (var i = 0; i < files.length; i++) {
               // 파일 이름
               var fileName = files[i].name;
               var fileNameArr = fileName.split("\.");
               // 확장자
               var ext = fileNameArr[fileNameArr.length - 1];
               
               var special_pattern = /[\{\}\[\]|<>\"]/gi;

               if(special_pattern.test($("input[name='file']").val())){
                   alert('파일명에 특수문자를 제거해주세요.');
                   return false;
               }
               
               var fileSize = files[i].size; // 파일 사이즈(단위 :byte)
               console.log("fileSize="+fileSize);
               if (fileSize <= 0) {
                   console.log("0kb file return");
                   return;
               }
               
               var fileSizeKb = fileSize / 1024; // 파일 사이즈(단위 :kb)
               var fileSizeMb = fileSizeKb / 1024;    // 파일 사이즈(단위 :Mb)
               
               var fileSizeStr = "";
               if ((1024*1024) <= fileSize) {    // 파일 용량이 1메가 이상인 경우 
                   console.log("fileSizeMb="+fileSizeMb.toFixed(2));
                   fileSizeStr = fileSizeMb.toFixed(2) + " Mb";
               } else if ((1024) <= fileSize) {
                   console.log("fileSizeKb="+parseInt(fileSizeKb));
                   fileSizeStr = parseInt(fileSizeKb) + " kb";
               } else {
                   console.log("fileSize="+parseInt(fileSize));
                   fileSizeStr = parseInt(fileSize) + " byte";
               }
				
                   // 전체 파일 사이즈
                   totalFileSize += fileSizeMb;
                   // 파일 배열에 넣기
                   fileList[fileIndex] = files[i];
                   // 파일 사이즈 배열에 넣기
                   fileSizeList[fileIndex] = fileSizeMb;
                   // 업로드 파일 목록 생성
                   addFileList(fileIndex, fileName, fileSizeStr);
                   // 파일 번호 증가
                   fileIndex++;
           }
       } else {
           alert("ERROR");
       }
   }

   // 업로드 파일 목록 생성
   function addFileList(fIndex, fileName, fileSizeStr) {
	   var html = "";
       html += "<div id='fileTr_" + fIndex + "'>";
       html += "    <div class='file_con'>";
       html += "		<div class='filename'>"+fileName + "</div>" 
               + "<img src='../imgs/material-cancel.svg' href='#' onclick='deleteFile(" + fIndex + "); return false;'/>"
       html += "    </div>"
       html += "</div>"
       
       $('#file_names').append(html);
   }
   // 업로드 파일 삭제
   function deleteFile(fIndex) {
       console.log("deleteFile.fIndex=" + fIndex);
       // 전체 파일 사이즈 수정
       totalFileSize -= fileSizeList[fIndex];
       // 파일 배열에서 삭제
       delete fileList[fIndex];
       // 파일 사이즈 배열 삭제
       delete fileSizeList[fIndex];
		
       // 업로드 파일 테이블 목록에서 삭제
       $("#fileTr_" + fIndex).remove();
       
       if($('#input_file').attr("disabled")=='disabled'){
        $('#input_file').removeAttr("disabled");
        $('.area3 label').css({'background-color':'#fff', 'cursor':'pointer'});
       }

        console.log("totalFileSize="+totalFileSize);
    }

    

    function deleteFile_for_update(fIndex, fileName){
        $("#fileTr_" + fIndex).remove();
        deleteFileNameList.push(fileName);

        if($('#input_file').attr("disabled")=='disabled'){
            $('#input_file').removeAttr("disabled");
            $('.area3 label').css({'background-color':'#fff', 'cursor':'pointer'});
        }
    }
   
   function deleteAll() {
	   for (var i = 0; i < files.length; i++) {
	   // 전체 파일 사이즈 수정
       totalFileSize -= fileSizeList[i];
       // 파일 배열에서 삭제
       delete fileList[i];
       // 파일 사이즈 배열 삭제
       delete fileSizeList[i];
		
       // 업로드 파일 테이블 목록에서 삭제
       $("#fileTr_" + i).remove();
	   }
	   
	}

    function chkTime(obj){
        let timeData = "";
        let text = $(obj).val();
    
        if(!text.includes('.')){
            if(text.length<3){
                $(obj).val(text);
            }else if(text.length<6){
                timeData += text.substr(0, 2);
                timeData += ":"
                timeData += text.substr(2);
                $(obj).val(timeData.substr(0,5));
            }
        }
    }


