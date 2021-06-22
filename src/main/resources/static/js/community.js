function post() {
    var questionId = $("#question_id").val();
    var content = $("#comment_content").val();

    if(!content){
        alert("不能回复空内容~");
        return;
    }

    //jQuery官方的post方法
    $.ajax({
        type: "POST",
        url: "/comment",
        /*我们可以使用 JSON.stringify() 方法将 JavaScript 对象转换为字符串。*/
        data: JSON.stringify({
            "parentId": questionId,
            "content": content,
            "type": 1,

        }),
        success: function (response) {
            if (response.code == 200) {
                window.location.reload();
                //$("#comment_section").hide();
            } else {
                if (response.code == 2003) {
                    //弹一个确认框
                    var isAccepted = confirm(response.message); //返回错误提示信息
                    //如果isAccepted == true，说明点击了确认,跳转页面到登录页
                    if (isAccepted) {
                        window.open("https://github.com/login/oauth/authorize?client_id=2dbeb9ea261b20041f31&redirect_uri=http://localhost:8887/callback&scope=user&state=1");
                        window.localStorage.setItem("closable", true);

                    }

                } else {
                    alert(response.message);
                }
            }
            console.log(response);
        },

        dataType: "json",//返回格式
        contentType: "application/json"
    });

    console.log(questionId);
    console.log(content);
}