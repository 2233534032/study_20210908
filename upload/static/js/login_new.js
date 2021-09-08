/**/


/*页面加载完成后执行*/
$(function(){
    /*获取可视窗口的高*/
    var height =  window.innerHeight;
    /*获取可视窗口的宽*/
    var width  = window.innerWidth;
    console.log("height:"+height+" width:"+width);
    /*设置元素的宽高*/
    $(".login_body_div").css("height",height).css('width',width);
    /*用户名框失去焦点时*/
    $("#username").live('blur',function(){
        var username = $("#username").val();
        if(username==""||username==null){
            $(".error_msg_div").css("display",'block');
            $(".error_msg").html("用户名不能为空");
            $("#username").css("border","1px solid red");
            return;
        } else{
            $(".error_msg_div").css("display",'none');
            $(".error_msg").html("");
            $("#username").css("border","1px solid #00FF7F");
        }

    })
    /*密码框失去焦点时*/
    $("#password").live('blur',function(){
        var password = $("#password").val();
        if(password==""||password==null){
            $(".error_msg_div").css("display",'block');
            $(".error_msg").html("密码不能为空");
            $("#password").css("border","1px solid red");
        }else{
            $(".error_msg_div").css("display",'none');
            $(".error_msg").html("");
            $("#password").css("border","1px solid #00FF7F");
        }
    })

    /*登录按钮按钮的登录事件*/
    $("#login_button").live("click",function(){
        isUsernameAndPassword();
    })

   function  isUsernameAndPassword(){
       var username = $("#username").val();
       if(username==""||username==null){
           $(".error_msg_div").css("display",'block');
           $(".error_msg").html("用户名不能为空");
           $("#username").css("border","1px solid red");
           return;
       }else{
           $(".error_msg_div").css("display",'none');
           $(".error_msg").html("");
           $("#username").css("border","1px solid #00FF7F");
       }
       var password = $("#password").val();
       if(password==""||password==null){
           $(".error_msg_div").css("display",'block');
           $(".error_msg").html("密码不能为空");
           $("#password").css("border","1px solid red");
           return;
       }else{
           $(".error_msg_div").css("display",'none');
           $(".error_msg").html("");
           $("#password").css("border","1px solid #00FF7F");
       }
       /*登录事件*/
       login(username,password);

   }

    /*登录事件的ajax请求*/
    function login(username,password){
        $.ajax({
            type:'post',
            url:'login',
            data:{
                'username':username,
                'password':password
            }
        }).success(function(result){
            console.log(result);
            if(result.code===501){
                $(".error_msg_div").css("display",'block');
                $(".error_msg").html(result.msg);
                $("#email_code").css("border","1px solid red");
            }else{
                window.location.replace(result.location_r);
            }
        })
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
        },2000);
    }
/*监听用户的回车键操作*/
    $(document).keyup(function(event){
        if(event.keyCode==13){
            isUsernameAndPassword();
        }
    })

})