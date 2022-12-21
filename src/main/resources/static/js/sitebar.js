$(function() {
    // On logout click link, submits logout form to log user out
    $("#site-bar-logout-link").on("click", function() {
        $("#site-bar-logout-form").submit();
    });
});