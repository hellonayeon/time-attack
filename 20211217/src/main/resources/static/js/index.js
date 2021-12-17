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

function loginButtonToggle() {
    if ($("#btn-logout").css("display") == "none") {
        $("#btn-logout").show();
        $("#btn-login").hide();
        $("#btn-signup").hide();
    } else {
        $("#btn-logout").hide();
        $("#btn-login").show();
        $("#btn-signup").show();
    }
}

function modalToggle(action) {
    $('.modal').modal('show');

    switch(action) {
        case '회원가입':
            $('#modal-title').val('회원가입');
            $('#modal-btn-ok').html('회원가입');
            $('#modal-btn-ok').attr("onclick", "").unbind("click").on("click", function () {
                signup();
                $('.modal').modal('hide');
            });
            break;
        case '로그인':
            $('#modal-title').val('로그인');
            $('#modal-btn-ok').html('로그인');
            $('#modal-btn-ok').attr("onclick", "").unbind("click").on("click", function () {
                login();
                $('.modal').modal('hide');
            });
            break;
    }
}

function clearInput() {
    $('#id').val('');
    $('#password').val('');
}

function signup() {
    let id = $('#id').val();
    let password = $('#password').val();

    $.ajax({
        type: "POST",
        url: `${WEB_SERVER_DOMAIN}/signup`,
        contentType: "application/json",
        data: JSON.stringify({userId: id, password: password}),
        success: function (response) {
            clearInput();
            alert("로그인 해주세요!");
        }
    })
}

function login() {
    let id = $('#id').val();
    let password = $('#password').val();

    $.ajax({
        type: "POST",
        url: `${WEB_SERVER_DOMAIN}/login`,
        contentType: "application/json",
        data: JSON.stringify({userId: id, password: password}),
        success: function (response) {
            clearInput();
            loginButtonToggle();
            localStorage.setItem("access_token", response.accessToken);
            localStorage.setItem("user_id", response.userId);
        }
    })
}

function logout() {
    loginButtonToggle();
    localStorage.removeItem("access_token");
    localStorage.removeItem("user_id");
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
            console.log(response);
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
        tags += " #"+ article['tags'][i]['name'];
    }

    let tempHtml = ` <tr>
                      <th scope="row">${index}</th>
                      <td><a href="view.html?id=${article['id']}">${article['title']}</td>
                      <td>${article.user.userId}</td>
                      <td>${article['comments'].length}</td>
                      <td>${tags}</td>
                      <td>${article['createdAt']}</td>
                      </tr>
                    `;
    $("#list-post").append(tempHtml);
}