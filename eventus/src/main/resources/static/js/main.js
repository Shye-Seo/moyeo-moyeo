const box_count =  $(".event_box_in").children().length;
        const inbox_width = box_count * 250 - 800;
        $(function(){
            $(".date_set").each(function(){
                let date = $(this).text();
                date = date.substring(0,10);
                let result = date.replace(/-/g, '.');
                $(this).text(result);
            })

            $('.time_set').each(function(){
                let time = $(this).text();
                let result = time.replace(/ /g, '');
                if(result==""){
                    $(this).text("00:00");
                }else{
                    $(this).text(result);
                }
            })

            $('.event_box_in').draggable({
                "axis": "x",
                stop: function() {
                    if($(this).position().left > 0){
                        $(this).css('left','0')
                    }
                    if($(this).position().left < -inbox_width){
                        $(this).css('left',-inbox_width+'px')
                    }
                }
            });
            
        })
        
        
        // 최근행사 버튼
        function list_next(){
            let left_num = $('.event_box_in').position().left;
            if(left_num > -inbox_width){
                left_num -= 750;
                if(left_num < -inbox_width){
                    left_num = -inbox_width
                }
            }else{
                return false;
            }
            $('.event_box_in').animate({left : left_num+'px'},300);
        }
        function list_pre(){
            let left_num = $('.event_box_in').position().left;
            if(left_num < 0){
                left_num += 750;
                if(left_num > 0){
                    left_num = 0;
                }
            }else{
                return false;
            }
            $('.event_box_in').animate({left : left_num+'px'},300);
        }