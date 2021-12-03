$(document).ready(function () {
    getArticles();
});

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

function postingArticle() {
    var data = new FormData();
    data.append( "title", $("#title").val() );
    data.append( "content", $("#content").val() );
    data.append( "tags", $("#tags").val() );
    data.append( "image", $("#image")[0].files[0] );

    $.ajax({
        type: "POST",
        url: `${WEB_SERVER_DOMAIN}/article`,
        processData: false,
        contentType: false,
        data: data,
        success: function (response) {
            alert("포스팅 업데이트 성공!");
            window.location.reload();
        }
    })
}

function getArticles() {
    let tag = $("#searchTag").val();

    $.ajax({
        type: "GET",
        url: `${WEB_SERVER_DOMAIN}/articles?searchTag=${tag}`,
        contentType: 'application/json; charset=utf-8',
        success: function (response) {
            $("#list-post").empty();
            // $("#searchTag").val('');
            for (let i = 0; i < response.length; i++) {
                num = response.length - i;
                makeListPost(response[i], num);
            }
        }
    })
}

function makeListPost(article, index) {
    let tags = '';
    for(let i = 0 ;  i < article['tags'].length ; i++){
        console.log(article['tags'][i]);
        tags += " #"+ article['tags'][i]['name'];
    }

    let tempHtml = ` <tr>
                      <th scope="row">${index}</th>
                      <td><a href="view.html?id=${article['id']}">${article['title']}</td>
                      <td>${article['comments'].length}</td>
                      <td>${tags}</td>
                      <td>${article['createdAt']}</td>
                      </tr>
                    `;
    $("#list-post").append(tempHtml);
}