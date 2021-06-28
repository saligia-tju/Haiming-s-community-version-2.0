/**
 * 基于jQuery获取data属性
 * */

/**
 * 提交回复
 */
function post() {
    var questionId = $("#question_id").val();
    var content = $("#comment_content").val();

    commentToTarget(questionId, 1, content);
}


function comment(e) {
    var commentId = e.getAttribute("data-id");
    var content = $("#input-" + commentId).val();

    commentToTarget(commentId, 2, content);

}

function commentToTarget(targetId, type, content) {
    if (!content) {
        alert("不能回复空内容~");
        return;
    }

    //jQuery官方的post方法
    $.ajax({
        type: "POST",
        url: "/comment",
        /*我们可以使用 JSON.stringify() 方法将 JavaScript 对象转换为字符串。*/
        data: JSON.stringify({
            "parentId": targetId,
            "content": content,
            "type": type,

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
}

/**
 * 展开二级评论
 */
function collapseComments(e) {
    /**/
    var id = e.getAttribute("data-id");
    var comments = $("#comment-" + id);
    /**toggleClass() 对设置或移除被选元素的一个或多个类进行切换。
     comments.toggleClass("in");
     */
        //获取二级评论展开状态
    var collapse = e.getAttribute("data-collapse");
    if (collapse) {
        //折叠二级评论
        comments.removeClass("in");
        e.removeAttribute("data-collapse");
        e.classList.remove("active");
    } else {

        var subCommentContainer = $("#comment-" + id);

        //判断是第几次加载，若容器大于1
        if (subCommentContainer.children().length != 1) {
            //展开二级评论
            comments.addClass("in");
            //标记二级评论展开状态
            e.setAttribute("data-collapse", "in");
            e.classList.add("active");
        } else {
            $.getJSON("/comment/" + id, function (data) {

                $.each(data.data.reverse(), function (index, comment) {
                    var mediaLeftElement = $("<div/>", {
                        "class": "media-left"
                    }).append($("<img/>", {
                        "class": "media-object2",
                        "src": comment.user.avatarUrl
                    }));

                    var mediaBodyElement = $("<div/>", {
                        "class": "media-body",
                    }).append($("<h5/>", {
                        "class": "media-heading",
                        "html": comment.user.name
                    })).append($("<div/>", {
                        "html": comment.content
                    })).append($("<div/>", {
                        "class": "menu",
                    })).append($("<div/>", {
                        "class": "pull-right menu-date",
                        "html": moment(comment.gmtCreate).format('YYYY-MM-DD')
                    }));

                    var mediaElement = $("<div/>", {
                        "class": "media"
                    }).append(mediaLeftElement).append(mediaBodyElement);

                    var commentElement = $("<div/>", {
                        "class": "col-lg-12 col-md-12 col-sm-12 col-xs-12 comments",
                    }).append(mediaElement);

                    subCommentContainer.prepend(commentElement);
                });
                //展开二级评论
                comments.addClass("in");
                //标记二级评论展开状态
                e.setAttribute("data-collapse", "in");
                e.classList.add("active");
            });
        }
    }
}

/*
    else {
        comments.addClass("in");
        //标记二级评论展开状态
        e.setAttribute("data-collapse", "in");
        e.classList.add("active");
    }
}*/

function selectTag(e) {
    var value = e.getAttribute("data-tag");
    var previous = $("#tag").val();
    //
    if (previous.indexOf(value) == -1) {
        if (previous) {
            $("#tag").val(previous + ',' + value);
        } else {
            $("#tag").val(value);
        }
    }
}

function showSelectTag() {
    $("#select-tag").show();
}