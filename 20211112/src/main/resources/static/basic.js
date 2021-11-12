$(document).ready(function() {
    showArticles();
})

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
    $('#article').empty();
    for(let i = 0; i < articles.length; i++) {
        let tmpHtml = `<p> ${articles[i].content} </p>`
        $('#article').append(tmpHtml);
    }
}

function saveArticle() {
    let content = $('#content').val();

    $.ajax({
        type: "POST",
        url: `/article`,
        contentType: "application/json",
        data: JSON.stringify({content: content}),
        success: function (response) {
            confirmArticleDisplay(response);
        }
    })
}

function confirmArticleDisplay(articleId) {
    if (confirm("저장된 메시지를 확인하시겠어요?") == true){    //확인
        setArticleDisplay(articleId, true);
    }else{   //취소
        setArticleDisplay(articleId, false);
    }
}

function setArticleDisplay(articleId, display) {
    $.ajax({
        type: "PUT",
        url: `/article/${articleId}`,
        contentType: "application/json",
        data: JSON.stringify({display: display}),
        success: function (response) {
            window.location.reload();
        }
    })
}