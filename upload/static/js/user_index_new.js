

$(function(){
    /*起始页数*/
    var page = 1;
    /*总页数*/
    var countPage;
    /*页面加载完成后，执行以下的方法*/
    var loginUserId;
    getContent(page);
    /*页面加载完成，去数据库动态获取首页的内容部分*/
    function getContent(page){
        $.ajax({
            type:'get',
            url: 'findMyBlog',
            contentType:"application/json",
            data:{"page":page},
            dataType: 'json',
        }).success(function(result){
            /*更新右侧的个人信息*/
            updateRightLoginUser(result.user);
            loginUserId = result.user.id;
            /*更新隐藏的头像修改框的src值*/
            $("#user_index_img_none").attr("src",result.user.userHead);
            /*验证身份*/
            if(result.user.vip==1){
                $(".user_head_nav_img_vip").css("display","block");
            }else{
                $(".user_head_nav_img_vip").css("display","none");
            }
            console.log(result);
            /*设置总的页数*/
            countPage = result.pageCount;
            $.each(result.data,function(i,value){
                $(".user_index_default").remove();
                setHtmlContent(i,value);
                return;
            });
            $(".user_index_default>span").html("您还没有发表过，快去首页发表吧");
        });
    }
    /*获取评论*/
    window.getComment = function(obj) {
        var id = $(obj).attr("value");
        commentAjax(id);
    }
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

    /*监听用户是否上传新的图片*/
    $(".user_img_input_head").live("change",function(){
        var form =  new FormData(document.getElementById('user_index_upload_userHead'));
        $.ajax({
            type:'post',
            url:'upload_content_img',
            data:form,
            dataType:'json',
            async:true,
            cache: false,
            processData: false,
            contentType: false
        }).success(function(result){
            console.log(result);
            if(result.code==200){
                $("#user_index_img_none").attr("src",result.src);
            }
        })
    })
    /*更新头像的操作*/
    $(".user_index_update_isOk").live("click",function(){
        var path =  $("#user_index_img_none").attr("src");
        $.ajax({
            type:'post',
            url:'update_userHeadImg',
            data:{'path':path}
        }).success(function(result){
            if(result.code===200){
                document.getElementById("windows").style.display="none";
                document.getElementById("shadow").style.display="none";
                hint(result.msg);
                $(".user_index_user_center_head>img").attr("src",result.path);
                $(".user_img_head>a>img").attr("src",result.path);
            }else{
                hint(result.msg);
            }
        })
    })

    /*当前登录用户*/
    function loginUserIsVip(loginUser){
        $.ajax({
            type:'post',
            url:'userIsVipUser',
            data:{'userId':loginUser}
        }).success(function (result) {
            if(result.vip==0){
                $(".user_head_vip_span_userId_comment_input").css("display",'none');
            }else{
                $(".user_head_vip_span_userId_comment_input").css("display",'block');
            }
        })
    }

    /*删除blog*/
    window.deleteBlock = function(obj){
        var blogId  = $(obj).attr("blogId");
        $.ajax({
            type:'post',
            url:'userDeleteBlog',
            data:{'blogId':blogId}
        }).success(function(result){
            if(result.code===200){
                hint("删除成功");
                $(".main_body_blog_id_"+blogId).remove();
               /* getContent(1);*/
            }
        })
    }

    /*点赞的业务功能*/
    window.dianzan = function(obj){
        /*获取被点赞的博客id  通过this对象传递当前document对象，获取绑定在id上的博客id*/
        var blogId =  $(obj).attr("id");
        /*修改字体颜色*/
        $(".give_icon_"+blogId).addClass("font_color_orange");
        $(".give_span_"+blogId).addClass("font_color_orange");
        $.ajax({
            type:"post",
            url:"praise",
            data:{'blogId':blogId},
            dataType:'json'
        }).success(function(result){
            console.log(result);
            /*执行成功*/
            if(result.code==200){
                $(".give_span_"+result.blogId).html(result.count);
                hint(result.msg);
                /*执行失败*/
            }else{
                hint(result.msg);
            }
        })

    }
    /*用户添加评论的事件*/
    window.addComment = function(obj){
        /*获取当前博客id*/
        var blogId = $(obj).attr("name");
        /*获取用户评论框输入的值*/
        var comment = $(".comment_textarea_"+blogId).val();
        /*去除空格*/
        var comment =  comment =  comment.trim();
        /*判断是否为空*/
        if(comment==null||comment==""||comment.length==0){
            alert("请输入评论");
            return false;
        }
        $.ajax({
            type:'get',
            url:'userComment',
            data:{
                'blogId':blogId,
                'comment':comment
            },
            dataType: 'json',
            async:true,
            contentType:"application/json",
        }).success(function(result){
            if(result==501){
                alert("评论失败");
            }else{
                var comment = $(".comment_textarea_"+blogId).val("");
                /*重新回去评论并刷新到页面*/
                commentAjaxTo(blogId,'disp');
                /*获取评论的总数并刷新到页面*/
                getCommentCount(blogId);
                /*评论成功后，弹出提示信息*/
                hint("评论成功");
            }
        })
    }
    /*绘制中间部分的数据到页面的方法*/

    function setHtmlContent(i,value){
        var item = "<div class='main_body main_body_blog_id_"+value.blogId+"' >" +
            "                <div class='text_main_body' blog_id='"+value.blogId+"'>\n" +
            "                    <!--头像-->\n" +
            "                    <div class='user_img_head index_cursor' onclick='findUser(this)' userId='"+value.userId+"'>\n" +
            "                      <span> <img  src='"+value.userHead+"' alt=''></span>\n" +
            "                      <span class='user_head_vip_span user_head_vip_span_userId_"+value.userId+"'><i class='iconfont icon-v user_head_vip_logo'></i></span>\n"+
            "                    </div>\n" +
            "                    <!--内容区--昵称-发布时间-正文-图片等-->\n" +
            "                    <div class='main_body_content'>\n" +
            "                        <span class='color_hover_orange'><div class='user_user_nick' ><span>"+value.username+"</span>\n" +
            "                                <div class='index_user_guanzhu_div '>" +
            "                                       <input class='home_div_button cursor_hover index_user_attention_"+value.userId+"' onclick='deleteBlock(this)' type='button' value='删除' blogId='"+value.blogId+"'>" +
            "                                </div>" +
            "                               </div>" +
            "                       </a>\n" +
            "                        <div class='user_new_data'>"+value.newDate+"来自<span>xx手机型号</span></div>\n" +
            "                        <div class='user_user_content'>\n" +
            "                            <p >\n" +
            "                                "+value.mainBody+"\n" +
            "                            </p>\n" +
            "                        </div>\n" +
            "                        <div class='user_user_img'>\n" +
            "                            <ul id='content_img_ul_"+value.blogId+"'>\n" +
            "                            </ul>\n" +
            "                        </div>\n" +
            "                    </div>\n" +
            "                </div>\n" +
            "                <!--转发-点赞-->\n" +
            "                <div class='give_body'>\n" +
            "                    <ul class='height_error'>\n" +
            "                        <li><i class='iconfont icon-zhuanfafasong-3 all_margin_right iconfont_size' aria-hidden='true'></i>0</li>\n" +
            "                        <li class='comment_li' onclick='getComment(this)' value='"+value.blogId+"'><i  class=' iconfont icon-xinxi all_margin_right iconfont_size comment_message_"+value.blogId+"' aria-hidden='true'></i><span class='commentCount_span_"+value.blogId+"'>"+value.commentCount+"</span></li>\n" +
            "                        <li onclick='dianzan(this)' id='"+value.blogId+"' class=' '><i  class=' iconfont iconfont_size icon-dianzan all_margin_right give_icon_"+value.blogId+" "+isGive(value.blogId)+"  ' aria-hidden='true'></i ><span class='give_span_"+value.blogId+"'>"+value.giveCount+"</span></li>\n" +
            "                    </ul>\n" +
            "                    <!--评论的div-->\n" +
            "                    <div class='user_comment_div height_error user_comment_div_"+value.blogId+"'>\n" +
            "                        <!--评论区顶部的发表评论的输入框和按钮区域-->\n" +
            "                        <div  class=\"height_error input_btn\">\n" +
            "                            <div class=\"comment_div_input center_my \">\n" +
            "                                <!--头像-->\n" +
            "                                <div class='user_comment_img all_float'>\n" +
            "                                    <a href=\"\">\n" +
            "                                        <img class='user_comment_img' src="+value.loginUserHead+"  alt=''>\n" +
            "                                    </a>\n" +
            "                                    <span class=' user_head_vip_span_userId_comment_input'><i class='iconfont icon-v user_comment_head_vip_logo'></i></span>\n"+
            "                                </div>\n" +
            "                                <!--评论框-->\n" +
            "                               <div class='all_float comment_input'>\n" +
            "                                   <textarea class='comment_textarea_"+value.blogId+"' placeholder=\"发表你的看法\" value=''></textarea>\n" +
            "                               </div>\n" +
            "                                <!--发布按钮-->\n" +
            "                                <div class=\"all_float btn_ok_t\">\n" +
            "                                     <div class='comment_blog_div' value='"+value.blogId+"'></div> \n"+
            "                                    <input type=\"submit\" onclick='addComment(this)' class='comment_btn_"+value.blogId+"' name="+value.blogId+" value=\"评论\">\n" +
            "                                </div>\n" +
            "                            </div>\n" +
            "                        </div>\n" +
            "                        <!--评论区-->\n" +
            "                        <!--这个div是用于展示数据时的循环最外层的div-->\n" +
            "                        <div class='comment_all_"+value.blogId+"' >\n" +
            "                           \n" +
            "                        </div>\n" +
            "                    </div>\n" +
            "                </div>\n"+
            "              </div>";
        /*prepend():向被选中元素中插入一个子元素，位置在最前边*/
        $(".content_all").append(item);
        /*判断当图片是一张的情况，适配页面的大小*/
        if(value.imgs.length==1){
            $("#content_img_ul_"+value.blogId+">li").remove();
            $("#content_img_ul_"+value.blogId).append("<li><img class='content_img cursor_hover' onclick='loadingImg(this)' src='"+value.contentImg+"' alt=''></li")
            /*图片是两张的情况*/
        }else if(value.imgs.length==2){
            for(var i =0;i<value.imgs.length;i++){
                $("#content_img_ul_"+value.blogId).append("<li><img class='content_img_two cursor_hover'  onclick='loadingImg(this)' src='"+value.imgs[i]+"' alt=''></li")
            }
            /*图片是多张的情况*/
        }else if(value.imgs.length>=3){
            for(var i =0;i<value.imgs.length;i++){
                $("#content_img_ul_"+value.blogId).append("<li><img  class='content_img_ss cursor_hover'  onclick='loadingImg(this)' src='"+value.imgs[i]+"' alt=''></li")
            }
        }

        /*判断当前用户是什么身份？*/
        userIsVipUser(value.userId);
    }
    /*获取当前用户的vip身份*/
    function userIsVipUser(userId){
        $.ajax({
            type:'post',
            url:'userIsVipUser',
            data:{'userId':userId}
        }).success(function (result) {
            if(result.vip==0){
                $(".user_head_vip_span_userId_"+userId).css("display","none");
            }else{
                $(".user_head_vip_span_userId_"+userId).css("display","block");
            }
        })
    }
    /*中间评论数据绘制到页面方法*/
    /*id: 是当前书籍的id值，通过id可以有效的避免class重复的问题，主要是通过拼接找到需要将评论绘制到那个div下
    * value: 是当前循环的对象
    * */
    /*这个方法绘制的评论的*/
    function setComment(id,value){
        console.log(value);
        var item =
            "                                <div class='index_comment_content_div height_error comment_div_text_"+id+"'>\n" +
            "                                    <div class='index_comment_userHead_nick_dateAll'>\n" +
            "                                        <!--头像-->\n" +
            "                                        <div class=\"index_comment_user_head all_float\">\n" +
            "                                            <a href='#'>\n" +
            "                                                <img class='index_comment_user_head_img' src='"+value.userHead+"' alt=''>\n" +
            "                                            </a>\n"+
            "                                            <span class='user_comment_head_vip_span user_head_vip_span_userId_"+value.userId+"'><i class='iconfont icon-v user_comment_head_vip_logo'></i></span>\n"+
            "                                        </div>\n" +
            "                                        <!--包裹评论信息和昵称和时间的div-->\n" +
            "                                        <div class='index_comment_content_div_nick_date all_float'>\n" +
            "                                            <!--用户昵称和评论-->\n" +
            "                                            <div class='index_comment_user_nick '>\n" +
            "                                                <span>"+value.username+"</span>\n" +
            "                                                <span class='index_comment_content_span_1'>:"+value.comment+"</span>\n" +
            "                                            </div>\n" +
            "                                            <!--评论的时间-->\n" +
            "                                            <div class='index_comment_comment_date all_float'>\n" +
            "                                                <span>"+value.commentDate+"</span>\n" +
            "                                            </div>\n" +
            "                                            <!--点赞和评论的图标-->\n" +
            "                                            <div class='index_comment_user_give_and_comment'>\n" +
            "                                                <span class='index_comment_comment_i' onclick='comment_to_sonComment(this)' id='"+value.id+"' name='"+value.username+"'><i class='iconfont icon-xinxi cursor_hover_color_orange'></i></span>\n" +
            "                                                <span class='index_comment_give_i cursor_hover_color_orange index_comment_give_span_font_color_"+value.id+"' onclick='comment_toGive(this)' commentId='"+value.id+"'><i class='iconfont icon-dianzan cursor_hover_color_orange'></i><span class='index_comment_give_span_"+value.id+"' style='font-size: 12px'>"+value.give+"</span></span>\n" +
            "                                            </div>\n" +
            "                                        </div>\n" +
            "                                        <div class='index_comment_user_son_comment_div all_float  son_comment_div_commentId_"+value.id+"'>\n"+
            "                                        </div>\n"+
            "                                 </div>\n" +
            "                               </div>";
        $(".comment_all_"+id).append(item);
        /*判断该条评论当前用户是否点赞过*/
        commentIsGive(value.id)
        /*获取该条评论有没有子评论*/
        getSonComment(value.id);
        /*判断当前用户的身份（普通用户还是认证用户）*/
        userIsVipUser(value.userId);
    }
    /*绘制子评论到页面的方法*/
    function setSonComment(value){
        console.log(value);
        var item =" <div class=\"index_comment_son_comment_div_zi height_error\">\n" +
            "                                                   <div class='index_comment_content_div_nick_date all_float'>\n" +
            "                                                       <!--用户昵称和评论-->\n" +
            "                                                       <div class='index_comment_user_nick '>\n" +
            "                                                           <span>"+value.sonCommentUsername+"</span>\n" +
            "                                                           <span class='index_comment_content_span_1'>:"+value.content+"</span>\n" +
            "                                                       </div>\n" +
            "                                                       <!--评论的时间-->\n" +
            "                                                       <div class='index_comment_comment_date all_float'>\n" +
            "                                                           <span>"+value.commentDate+"</span>\n" +
            "                                                       </div>\n" +
            "                                                       <!--点赞和评论的图标-->\n" +
            "                                                       <div class='index_comment_user_give_and_comment'>\n" +
            "                                                           <!--评论-->\n" +
            "                                                           <span class='index_comment_comment_i'><i class='iconfont icon-xinxi cursor_hover_color_orange'></i></span>\n" +
            "                                                           <!--点赞-->\n" +
            "                                                           <span class='index_comment_give_i cursor_hover_color_orange ' onclick=\"sonGive(this)\"><i class='iconfont icon-dianzan cursor_hover_color_orange'></i><span  style=\"font-size: 12px\">"+value.give+"</span></span>\n" +
            "                                                       </div>\n" +
            "                                                   </div>\n" +
            "                                               </div>";
        $(".son_comment_div_commentId_"+value.commentId).append(item);
    }

    /*ajax获取子评论的函数*/
    function getSonComment(commentId){
        $.ajax({
            type:'post',
            url:'findSonComment',
            data:{'commentId':commentId}
        }).success(function(result){
            console.log(result);
            /*有子评论的情况*/
            if(result.code==200){
                $(".son_comment_div_commentId_"+commentId).css("display","block");
                /*追加子评论的方法*/
                $.each(result.sonComment,function(i,value){
                    setSonComment(value);
                })
            }
        })
    }
    /*ajax获取评论*/
    function commentAjax(id){
        /*将元素显示到页面*/
        var div =  $(".user_comment_div_"+id);
        /*获取点赞图标*/
        var comment_i = $(".comment_message_"+id);
        /*获取显示评论总数的span标签*/
        var comment_span = $(".commentCount_span_"+id);
        var v = div.css("display");
        console.log(v);
        if(v=='none'){
            /*显示评论隐藏的框*/
            div.attr("id","disp")
            /*将评论的图标修改为黄色*/
            comment_i.attr("id",'orange_font')
            /*将评论数修改为黄色*/
            comment_span.attr('id','orange_font1');

        }else{
            div.attr("id",'nsp');
            comment_i.attr('id',id);
            comment_span.attr("id",id);
        }
        /*ajax获取评论，以及将评论绘制到页面*/
        $.ajax({
            type: 'get',
            url:'getComment',
            contentType:"application/json",
            data:{"id":id},
            dataType: 'json'
        }).success(function(result){
            loginUserIsVip(loginUserId);
            console.log(result);
            if(result.length==0){
                $(".comment_all_"+id).append("<div class='comment_div_text'>暂无评论</div>");
                return;
            }
            $(".comment_div_text_"+id).remove();
            $.each(result.commentList,function(i,value){
                setComment(id,value);
            });
        })
    }
    /*ajax获取评论的重载*/
    function commentAjaxTo(id,disp){
        console.log("读取方法中");
        /*将元素显示到页面*/
        var div =  $(".user_comment_div_"+id);
        div.attr("id",disp);
        /*ajax获取评论，以及将评论绘制到页面*/
        $.ajax({
            type: 'get',
            url:'getComment',
            contentType:"application/json",
            data:{"id":id},
            dataType: 'json'
        }).success(function(result){
            if(result.length==0){
                $(".comment_all_"+id).append("<div class='comment_div_text'>暂无评论</div>");
                return;
            }
            $(".comment_div_text").remove();
            $(".comment_div_text_"+id).remove();
            $.each(result,function(i,value){
                setHtmlComment(id,value);
            });
        })
    }
    /*判断是否已经点赞过，如果已经点赞过，
    * 方法返回isGive 不是 0 表示已经点赞
    * */
    function isGive(blogId){
        $.ajax({
            type:'post',
            url:'userIsGive',
            data:{'blogId':blogId},
            dataType:'json'
        }).success(function(result){
            if(result.code==200){
                if(result.isGive!=0){
                    $(".give_icon_"+blogId).addClass("font_color_orange");
                    $(".give_span_"+blogId).addClass("font_color_orange");
                }
            }
        })
    }
    /*请求评论的总数*/
    function getCommentCount(blogId){
        $.ajax({
            type:'post',
            url:'getCommentCount',
            data:{'blogId':blogId}
        }).success(function(result){
            console.log(result);
            if(result.code==200){
                $(".commentCount_span_"+blogId).html(result.count);
            }
        })
    }
    /*判断用户是否点赞过该评论*/
    function commentIsGive(commentId){
        $.ajax({
            type:'post',
            url:'commentIsGive',
            data:{'commentId':commentId}
        }).success(function(result){
            console.log(result);
            if(result.code===500){
                $(".index_comment_give_span_font_color_"+commentId).attr("class",'font_color_orange');
            }
        })
    }


    /*更新右侧用户信息*/
    function updateRightLoginUser(user){
        $(".user_index_right_user_msg>*").remove();
        var item = "                <!--头像-->\n" +
            "                <div id='titleOn' class='user_index_user_center_head titleOn'><img class='publicshubiao_ok' src='"+user.userHead+"'/></div>\n" +
            "                <!--用户信息-->\n" +
            "                <div class='user_index_username user_index_right_style'><p>昵称: <span class='font_color_orange user_index_right_user_style'>"+user.username+"</span></p></div>\n" +
            "                <div class='user_index_gender user_index_right_style'><p>性别:<span class='font_color_orange user_index_right_user_style'>"+user.gender+"</span></p></div>\n" +
            "                <div class='user_index_email user_index_right_style'><p>邮箱:<span class='font_color_orange user_index_right_user_style'>"+user.email+"</span></p></div>\n" +
            "                <div class='user_index_brief user_index_right_style'><p>简介:<span class='font_color_orange user_index_right_user_style'>"+user.brief+"</span></p></div>\n" +
            "                <div class='user_index_birth user_index_right_style'><p>生日:<span class='font_color_orange user_index_right_user_style'>"+user.birth+"</span></p></div>\n" +
            "                <div class='user_index_registry_date user_index_right_style'><p>注册日期:<span class='font_color_orange user_index_right_user_style'>"+user.registryDate+"</span></p></div>\n" +
            "                <div class='user_index_border_date user_index_right_style'><p></p></div>\n" +
            "                <div id='itemss' class='user_index_update_msg user_index_right_style'><p class='color_hover_orange publicshubiao_ok '>编辑资料 <i class=' iconfont icon-youjiantou'></i></p></div>"

        $(".user_index_right_user_msg").append(item);
    }

    /*监听滑动进度条到底部更新数据操作*/
    $(window).scroll(function(){
        var scrollTop = $(this).scrollTop(); //滚动条距离顶部的高度
        var scrollHeight = $(document).height();//当前页面的总高度
        var clientHeight = $(this).height();//当前可视的高度
        /*这里表示滑倒底部了*/
        if(scrollTop+clientHeight>=scrollHeight){
            console.log("countPage:"+countPage);
            if(page>countPage){
                return;
            }
            page++;
            console.log("page:"+page);
            getContent(page);
        }else if(scrollTop<=0){
            console.log("滑倒顶部");
        }
    })

    /*隐藏的修改头像的框*/
    $(".titleOn").live('click',function(){
        console.log("执行了");
        document.getElementById("windows").style.display="block";
        document.getElementById("shadow").style.display="block";
    })


    /*关闭修改头像的=窗口*/
    $(".cancel").click(function(){
        document.getElementById("windows").style.display="none";
        document.getElementById("shadow").style.display="none";
    })

    $("#itemss").click(function(){
        console.log("ok");
    })

})
