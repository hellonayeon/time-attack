/* 포스팅 박스 열기 닫기 */
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

/* 모든 기사 조회 */
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
                            <td><a href="view.html" onclick="saveArticleId(${articles[i].id})">${articles[i].title}</a></td>
                            <td>${articles[i].commentList.length}</td> 
                            <td>${articles[i].createdAt}</td>
                       </tr>`
        $('#list-post').append(tmpHtml);
    }
}

function saveArticleId(articleId) {
    localStorage.setItem("articleId", articleId);
    return true;
}

/* 기사 저장 */
function saveArticle() {
    let title = $('#title').val();
    let content = $('#content').val();

    $.ajax({
        type: "POST",
        url: `/articles`,
        contentType: "application/json",
        data: JSON.stringify({title: title, content: content}),
        success: function (response) {
            $('#title').val('');
            $('#content').val('');

            showArticles();
        }
    })
}