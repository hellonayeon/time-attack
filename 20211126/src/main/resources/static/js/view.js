let articleId;

$(document).ready(function() {
    articleId = getUrlParam("id");
    showArticle();
})

function getUrlParam(name) {
    var results = new RegExp('[\?&amp;]' + name + '=([^&amp;#]*)').exec(window.location.href);
    return results[1] || 0;
}

/* 기사 가져오기 */
function showArticle() {
    $.ajax({
        type: "GET",
        url: `${WEB_SERVER_DOMAIN}/articles/${articleId}`,
        success: function (response) {
            $('#title').text(response.title);
            $('#content').text(response.content);
            makeComments(response.comments);
        }
    })
}

function makeComments(comments) {
    $('#comment-list').empty();
    for(let i = 0; i < comments.length; i++) {
        let tmpHtml = `<div class="comment">${comments[i].content}</div>`
        $('#comment-list').append(tmpHtml);
    }
}

/* 댓글 저장 */
function saveComment() {
    let content = $('#comment').val();

    $.ajax({
        type: "POST",
        url: `${WEB_SERVER_DOMAIN}/articles/${articleId}/comments`,
        contentType: "application/json",
        data: JSON.stringify({content: content}),
        success: function (response) {
            $('#comment').val(''); // textarea clear
            showArticle();
        }
    })
}