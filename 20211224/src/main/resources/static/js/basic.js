<!-- set JWT token in http request header -->
$.ajaxPrefilter(function( options, originalOptions, jqXHR ) {
    if(localStorage.getItem('access_token')) {
        jqXHR.setRequestHeader('Authorization', 'Bearer ' + localStorage.getItem('access_token'));
    }
});