$(function() {
    console.log("Inside preferences.js");

    $(document).on("click", "#edit-preferences-submit-button", async function() {
        const preferencesObject = {
            bio: $("#bio").val(),
            location: $("#location").find(":selected").text(),
            language: $("#language").find(":selected").text(),
            mature_language: $("#mature-language").is(":checked"),
            game_age_rating: $("#game-rating").find(":selected").text()
        }

        const postOptions = {
            method: 'POST',
            headers: {
                'Content-Type' : 'application/json',
                'X-CSRF-TOKEN' : $("meta[name='_csrf']").attr("content")
            },
            body: JSON.stringify(preferencesObject)
        }
        console.log(preferencesObject);
        console.log(postOptions);
        let results = await fetch(`/profile/${$("#hidden-preferences-id").text()}/edit`, postOptions).then(res => res);

    });

});