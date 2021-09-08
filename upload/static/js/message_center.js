
/*message_cneter的js代码*/
$(function(){

    /*请求热搜榜*/
    topSearch();


    var scrollHeight = $(document).height();//当前页面的总高度
    $(".content_left").css("height",scrollHeight);

    /*请求未读点赞消息的方法*/
    findUserUnReadMe();
    /*获取是否有未读的点赞信息,有则显示提示信息*/
    isGiveUnRead();
    /*请求是否有未读的评论信息*/
    isCommentUnRead();

    /*发起请求未读点赞消息的方法*/
    function  findUserUnReadMe(){
        $.ajax({
            type:'post',
            url:'findUserUnReadMessage',
            data:{'page':1}
        }).success(function(result){
            console.log(result);
            /*判断用户的身份*/
            if(result.user.vip==1){
                $(".user_head_nav_img_vip").css("display","block");
            }else{
                $(".user_head_nav_img_vip").css("display","none");
            }
            $("#index_user_HeadImg_nav").attr("src",result.user.userHead);
            var state = result.state;
            if(state=='有信息'){
                /*有未读消息的情况,删除页面的信息*/
                deleteItem();
                /*绘制新数据到页面*/
                $.each(result.data,function(i,value){
                    appendMessage(value);
                })
                /*清除提示信息的红点*/
                $(".message_nav_4_span").css("display",'none');
                clearUnReadMessageHint();
                /*再次请求历史信息（填充页面，加载完成未读提示后）*/
                findUserReadGiveMessageTwo();
            }else{
                /*没有未读消息的情况,请求历史数据*/
                /* $(".message_center_zhuijia_div").append("<div class=\"message_lishixinxi_div\"><div class=\"lishixinxi_div\">以下是历史信息</div></div>");*/
                findUserReadGiveMessage();
            }
        })
    }
    /*请求历史的点赞信息*/
    function findUserReadGiveMessage(){
        console.log("没有未读点赞的情况，请求历史数据");
        $.ajax({
            type:'post',
            url:'findUserReadGive',
            data:{'post':1}
        }).success(function(result){
            if(result.state==="有信息"){
                /*清除页面的数据*/
                deleteItem();
                $(".message_center_zhuijia_div").append("<div class=\"message_lishixinxi_div\"><div class=\"lishixinxi_div\">历史信息</div></div>");
                $.each(result.data,function(i,value){
                    appendMessage(value);
                })
            }else{
                //无历史信息的情况,显示默认的信息
                /*删除历史信息的提示框*/
                deleteItem();
                appendDefault();

            }
        })
    }
    /*请求历史点赞信息的方法重载（主要是做新消息和旧消息拼接的）*/
    function findUserReadGiveMessageTwo(){
        $.ajax({
            type:'post',
            url:'findUserReadGive',
            data:{'post':1}
        }).success(function(result){
            if(result.state==="有信息"){
                /*清除页面的数据*/
                $(".message_center_zhuijia_div").append("<div class=\"message_lishixinxi_div\"><div class=\"lishixinxi_div\">历史信息</div></div>");
                $.each(result.data,function(i,value){
                    appendMessage(value);
                })
            }else{
                /*  //无历史信息的情况,显示默认的信息
                  /!*删除历史信息的提示框*!/
                  deleteItem();
                  appendDefault();*/

            }
        })
    }
    /*请求是否有未读的点赞信息*/
    function isGiveUnRead(){
        $.ajax({
            type:'post',
            url:'unReadMessageCount',
            data:{"page":1}
        }).success(function(result){
            if(result.code==500){
                console.log(result.msg);
                $(".message_nav_4_span").css("display",'none');
            }else{
                /* $(".message_nav_4_span").css("display",'inline-block');*/
                /*发起清除未读点赞信息的请求*/
                clearUnReadMessageHint();
            }
        })
    }
    /*点击赞 标签的 的点击方法*/
    $(".message_left_click_zan").live("click",function(){
        findUserUnReadMe();
    })

    /*提示框信息*/
    function hint(msg){
        /*获取提示框对象*/
        var hint =  $("#hint_msg");
        /*设置提示框的显示*/
        hint.css("display",'block');
        /*设置提示信息*/
        $(".msg_span_1").html(msg);
        console.log("调用了");
        /*设置多长时间自动消失*/
        setTimeout(function(){
            hint.hide();
        },1000);

    }

    /*-------------------------------------------------------------------------------------------*/
    /*请求评论信息的点击事件*/
    $(".message_left_a_click").live("click",function(){
        console.log("请求未读评论中");
        $.ajax({
            type:'post',
            url:'findUserUnReadComment',
            data:{'page':1}
        }).success(function(result){
            /* console.log(result);*/
            if(result.state==='无数据'){
                console.log("没有新的评论");
                /*没有未读的评论,清除页面的数据*/
                deleteItem();
                clearReadComment();
                $(".message_nav_1_span").css("display","none");
                /*获取旧的评论*/
                findUserReadComment();
            }else{
                console.log("有新的评论的情况");
                //有未读评论的情况
                deleteItem();
                $.each(result.data,function(i,value){
                    /*循环将数据展示到页面上*/
                    appendCommentItem(value);
                })
                console.log("清除红点");
                /*点击后将提示红点取消掉*/
                $(".message_nav_1_span").css("display","none");
                /*请求历史数据*/
                findUserReadCommentTwo();
                /*发起清除已读评论的请求*/
                clearReadComment();
            }
        })

    })
    /*请求历史的评论信息*/
    function findUserReadComment(){
        console.log("没有未读评论的情况，请求历史数据");
        $.ajax({
            type:'post',
            url:'findUserReadComment',
            data:{'post':1}
        }).success(function(result){
            console.log(result);
            if(result.state==="有数据"){
                /*清除页面的数据*/
                deleteItem();
                $(".message_center_zhuijia_div").append("<div class=\"message_lishixinxi_div\"><div class=\"lishixinxi_div\">历史信息</div></div>");
                $.each(result.data,function(i,value){
                    appendCommentItem(value);
                })
            }else{
                //无历史信息的情况，提示无信息的提示
                appendDefault();
            }
        })
    }
    /*请求历史评论信息的重载（主要是做拼接新旧数据使用是）*/
    function findUserReadCommentTwo(){
        $.ajax({
            type:'post',
            url:'findUserReadComment',
            data:{'post':1}
        }).success(function(result){
            console.log(result);
            if(result.state==="有数据"){
                console.log("11");
                /*更新提示信息*/
                $(".message_center_zhuijia_div").append("<div class=\"message_lishixinxi_div\"><div class=\"lishixinxi_div\">历史信息</div></div>");
                $.each(result.data,function(i,value){
                    appendCommentItem(value);
                })
            }else{
                /*   //无历史信息的情况，提示无信息的提示
                   appendDefault();*/
            }
        })
    }


    /*热搜榜数据************************************************/
    function topSearch(){
        $.ajax({
            type:'post',
            url:'findTopSearchData',
            data:{'page':1}
        }).success(function(result){
            if(result.code===200){
                /*循环将热搜数据绘制到页面*/
                $.each(result.data,function(i,value){
                    appendTopSearch(value);
                })
            }
        })
    }
    /*绘制热搜榜数据到页面的函数*/
    function appendTopSearch(value){
        var item=" <div><span id='"+value.blogId+"' class='color_hover_orange   topSearch_page'>"+value.mainBody+"</span></div>";
        $(".hos_list").append(item);
    }


    /*删除页面的数据的函数*/
    function deleteItem(){
        /*正文内容框*/
        $(".message_center_content_div").remove();
        /*没有数据情况的默认提示框*/
        $(".message_not_unread_message").remove();
        /*历史信息提示框*/
        $(".message_lishixinxi_div").remove();
    }
    /*默认的提示信息（没有任何数据的情况下显示）*/
    function appendDefault(){
        var item =" <div class='message_not_unread_message'>\n" +
            "                        <span><i class='iconfont icon-gantanhao gantanhao'></i>暂无数据</span>\n" +
            "                </div>";
        $(".message_center_zhuijia_div").append(item);
    }
    /* 清除未读的点赞信息*/
    function clearUnReadMessageHint(){
        console.log('发起了清除点赞信息的请求');
        $.ajax({
            type:'post',
            url:'clearMessage',
            data:{'page':1}
        }).success(function(result){
            console.log(result.msg);
        })
    }
    /*请求是否有未读的评论信息*/
    function isCommentUnRead(){
        $.ajax({
            type:'post',
            url:'isCommentUnRead',
            data:{'page':1}
        }).success(function(result){
            if(result.code==200){
                /*有未读的评论信息*/
                $(".message_nav_1_span").css("display","inline-block");
            }else{
                $(".message_nav_1_span").css("display","none");
            }
        })
    }

    /*清除已经被点击了的评论数据*/
    function clearReadComment(){
        $.ajax({
            type:'post',
            url:'clearReadComment',
            data:{'page':1}
        }).success(function(result){
            console.log(result);
        })
    }


    /*将获取到的数据绘制到页面的方法*/
    function appendMessage(value){
        var item = "<!--单个小内容-->\n" +
            "                <div class='message_center_content_div'>\n" +
            "                    <div class='message_center_single_div' >\n" +
            "                        <!--用户头像的div-->\n" +
            "                        <div class='height_error message_center_user_message_head'>\n" +
            "                            <div class='message_center_dianzan_userHead user_head_img_yuanxing message_center_float_left'>\n" +
            "                                <a href=\"\"><img class='user_head_img_yuanxing ' src='"+value.giveUserHead+"' alt=\"\"></a>\n" +
            "                                <span class=' user_head_vip_span_message_center user_head_vip_span_message_center_"+value.userId+"'><i class='iconfont icon-v user_comment_head_vip_logo'></i></span>\n"+
            "                            </div>\n" +
            "                            <!--点赞用户的昵称 点赞时间-->\n" +
            "                            <div class =' message_center_user_nick_data_div  message_center_float_left'>\n" +
            "                                <p class='message_center_user_nick '>"+value.giveUsername+"</p>\n" +
            "                                <p class='message_center_user_dianzan_date'>"+value.giveDate+"</p>\n" +
            "                            </div>\n" +
            "                        </div>\n" +
            "                        <!--赞了这条说说-->\n" +
            "                        <div>\n" +
            "                            <p class='message_center_content_p'>点赞了这条说说</p>\n" +
            "                        </div>\n" +
            "                        <!--被点赞的内容-->\n" +
            "                        <div class='message_center_beidianzande_neiRong'>\n" +
            "                            <!--图片-->\n" +
            "                            <div class='message_center_float_left'>\n" +
            "                                <a href=\"\"><img class='message_center_beidianzan_img' src='"+value.img+"' alt=\"\"></a>\n" +
            "                            </div>\n" +
            "                            <!--昵称和内容-->\n" +
            "                            <div class='message_center_float_left nick_and_contetn_div'>\n" +
            "                                <a href=\"\">\n" +
            "                                    <p class='message_center_beidianzan_nice'>@"+value.username+"</p>\n" +
            "                                </a>\n" +
            "                                <a href=\"\">\n" +
            "                                    <p class='message_center_beidianzan_content'>"+value.content+"</p>\n" +
            "                                </a>\n" +
            "                            </div>\n" +
            "                        </div>\n" +
            "                    </div>\n" +
            "                </div>"
        $(".message_center_zhuijia_div").append(item);
        userIsVipUser(value.userId);

    }
    /*判断当前用户的身份*/
    function userIsVipUser(userId){
        $.ajax({
            type:'post',
            url:'userIsVipUser',
            data:{'userId':userId}
        }).success(function (result) {
            if(result.vip==0){
                $(".user_head_vip_span_message_center_"+userId).css("display","none");
            }else{
                $(".user_head_vip_span_message_center_"+userId).css("display","block");
            }
        })
    }
    /*追加评论信息到页面的方法*/
    function appendCommentItem(value){
        var item = "<!--单个小内容-->\n" +
            "                <div class='message_center_content_div'>\n" +
            "                    <div class='message_center_single_div' >\n" +
            "                        <!--用户头像的div-->\n" +
            "                        <div class='height_error message_comment_user_div'>\n" +
            "                            <div class='message_center_dianzan_userHead user_head_img_yuanxing message_center_float_left'>\n" +
            "                                <a href=\"\"><img class='user_head_img_yuanxing ' src='"+value.commentUserHead+"' alt=\"\"></a>\n" +
            "                                <span class=' user_head_vip_span_message_center user_head_vip_span_message_center_"+value.userId+"'><i class='iconfont icon-v user_comment_head_vip_logo'></i></span>\n"+
            "                            </div>\n" +
            "                            <!-- 评论用户的昵称 评论时间-->\n" +
            "                            <div class =' message_center_user_nick_data_div  message_center_float_left'>\n" +
            "                                <p class='message_center_user_nick '>"+value.commentUsername+"</p>\n" +
            "                                <p class=\"message_center_user_dianzan_date \">"+value.commentDate+"</p>\n" +
            "                            </div>\n" +
            "                        </div>\n" +
            "                        <!--评论这条说说-->\n" +
            "                        <div>\n" +
            "                            <p class='message_center_content_p'>"+value.commentContent+"</p>\n" +
            "                        </div>\n" +
            "                        <!--被评论的内容-->\n" +
            "                        <div class='message_center_beidianzande_neiRong'>\n" +
            "                            <!--图片-->\n" +
            "                            <div class='message_center_float_left'>\n" +
            "                                <a href=\"\"><img class='message_center_beidianzan_img' src='"+value.img+"' alt=\"\"></a>\n" +
            "                            </div>\n" +
            "                            <!--昵称和内容-->\n" +
            "                            <div class='message_center_float_left nick_and_contetn_div'>\n" +
            "                                <a href=\"\">\n" +
            "                                    <p class='message_center_beidianzan_nice'>"+value.username+"</p>\n" +
            "                                </a>\n" +
            "                                <a href=\"\">\n" +
            "                                    <p class='message_center_beidianzan_content'>"+value.content+"</p>\n" +
            "                                </a>\n" +
            "                            </div>\n" +
            "                        </div>\n" +
            "                        <!--回复点赞信息和点赞给功能-->\n" +
            "                        <div class=\"message_center_huifu_dianzan_he_dianzan\">\n" +
            "                            <ul>\n" +
            "                                <li><span  class=\"huidu_dianzan_a color_font_hover\" onclick='comment_to_sonComment(this)' id='"+value.id+"' name='"+value.commentUsername+"'><i class=\"iconfont icon-xinxi  huifu_i_tubiao color_font_hover\"></i><span class=\"user_huifu_xinxi color_font_hover\">回复</span></span></li>\n" +
            "                                <li><span class='color_font_hover'><i class=\"iconfont icon-dianzan huifu_i_tubiao\"></i></span></li>\n" +
            "                            </ul>\n" +
            "                        </div>\n" +
            "                    </div>\n" +
            "                </div>"
        $(".message_center_zhuijia_div").append(item);
        userIsVipUser(value.userId);
    }



    /*关闭评论的=窗口*/
    $(".cancel").click(function(){
       /* document.getElementById("windows").style.display="none";*/
        document.getElementById("shadow").style.display="none";

        document.getElementById("windows_comment").style.display="none";
    })
    /*点击评论评论的函数*/
    comment_to_sonComment = function(obj){
        /*评论的id*/
        var commentId =  $(obj).attr("id");
        var username = $(obj).attr("name");
        console.log(commentId);
        document.getElementById("windows_comment").style.display="block";
        document.getElementById("shadow").style.display="block";
        /*给选中的元素设置一个自定义的属性*/
        $(".windows_comment_input").attr("commentId",commentId);
        $(".comment_title_div>h3").html("回复@"+username);
    }
    /*隐藏的评论栏的提交事件*/
    $(".comment_button_tijiao").live("click",function(){
        var content = $(".windows_comment_input>textarea").val();
        if(content==null||content==''||content<=0){
            return;
        }
        /*提交评论*/
        var commentId = $(".windows_comment_input").attr("commentId");
        $.ajax({
            type:'post',
            url:'comment_son_comment',
            data:{'commentId':commentId,
                'content':content
            }
        }).success(function(result){
            if(result.code==200){
                /*关闭弹窗*/
                document.getElementById("shadow").style.display="none";
                document.getElementById("windows_comment").style.display="none";
                hint("回复成功");
                /*调用获取子评论的函数，将评论绘制到页面*/
                getSonComment(commentId);
                $(".windows_comment_input").load(location.href+" .windows_comment_input");
            }
        })

    })

})