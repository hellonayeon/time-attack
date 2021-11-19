var targetId;

function openClose() {
    if ($("#post-box").css("display") == "block") {
        $("#post-box").hide();
        $("#btn-post-box").text("포스팅 박스 열기");
    } else {
        $("#post-url").val('');
        $("#post-comment").val('');
        $("#post-box").show();
        $("#btn-post-box").text("포스팅 박스 닫기");
    }
}

function showArticles() {
    $.ajax({
        type: "GET",
        url: `/articles`,
        success: function (response) {
            for(let i=0; i < response.length; i++) {
                makeArticle(response);
            }
        }
    })
}

function makeArticle(articles) {
    $('#list-post').empty();
    for(let i = 0; i < articles.length; i++) {
        console.log(articles[i].id);
        let tmpHtml = `<tr>
                            <td>${i+1}</td> 
                            <td onclick="showArticle(${articles[i].id})"><a>${articles[i].title}</a></td>
                            <td>${articles[i].commentList.length}</td> 
                            <td>${articles[i].createdAt}</td>
                       </tr>`
        $('#list-post').append(tmpHtml);
    }
}

function showArticle(articleId) {
    targetId = articleId;
    console.log(targetId);

    window.location.href = "view.html"
}

function saveArticle() {
    let title = $('#title').val();
    let content = $('#content').val();
    console.log(title, content);

    $.ajax({
        type: "POST",
        url: `/articles`,
        contentType: "application/json",
        data: JSON.stringify({title: title, content: content}),
        success: function (response) {
            showArticles();
        }
    })
}


/* 특정 기사 가져오기 */
function getArticle() {
    console.log("here")
    console.log(targetId);
    $.ajax({
        type: "GET",
        url: `/articles/${targetId}`,
        success: function (response) {
            $('#title').val(response.title);
            $('#content').val(response.content);
        }
    })
}

/* 기사 댓글 가져오기 */
function showComment() {
    $.ajax({
        type: "GET",
        url: `/articles/${targetId}/comments`,
        success: function (response) {
            makeComments(response);
        }
    })
}

function makeComments(comments) {
    $('#comment-list').empty();
    for(let i = 0; i < comments.length; i++) {
        let tmpHtml = `<div>${comments[i].content}</div>`
        $('#comment-list').append(tmpHtml);
    }
}

/* 댓글 저장 */
function saveComment() {
    let content = $('#comment').val();

    $.ajax({
        type: "POST",
        url: `/articles/${targetId}/comments`,
        contentType: "application/json",
        data: JSON.stringify({content: content}),
        success: function (response) {
            showComment();
        }
    })
}