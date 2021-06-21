function post() {
    var questionId = $("#question_id").val();
    var content = $("#comment_content").val();

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
                $("#comment_section").hide();
            } else {
                alert(response.message);
            }
            console.log(response);
        },

        dataType: "json",//返回格式
        contentType: "application/json"
    });

    console.log(questionId);
    console.log(content);
}