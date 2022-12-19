$(function() {
    // On logout click link, submits logout form to log user out
    $("#site-bar-logout-link").on("click", function() {
        $("#site-bar-logout-form").submit();
    });

    //Contact us Submit button feel free to move this if need be//
    $("#emailSend").on('click', function(){
        alert("Your message ha been sent to HQ");
    });

    // $("contactUs")

});