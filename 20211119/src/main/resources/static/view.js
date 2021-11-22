let articleId;

/* 기사 가져오기 */
function showArticle() {
    $.ajax({
        type: "GET",
        url: `http://localhost:8080/articles/${articleId}`,
        success: function (response) {
            $('#title').text(response.title);
            $('#content').text(response.content);
            makeComments(response.commentList);
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
        url: `http://localhost:8080/articles/${articleId}/comments`,
        contentType: "application/json",
        data: JSON.stringify({content: content}),
        success: function (response) {
            $('#comment').val(''); // textarea clear
            showArticle();
        }
    })
}