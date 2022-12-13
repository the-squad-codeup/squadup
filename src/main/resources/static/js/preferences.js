$(function() {
    console.log("Inside preferences.js");

    const MyPreferences = {
        initialize() {
            Events.initialize();
            Print.form();
        },
        packagePreferencesObject() {
            const preferencesObject = {
                bio: $("#bio").val(),
                location: $("#location").find(":selected").text(),
                language: $("#language").find(":selected").text(),
                mature_language: $("#mature-language").is(":checked"),
                game_age_rating: $("#game-rating").find(":selected").text()
            }
            return preferencesObject;
        }
    }

    const Fetch = {
        Get: {
            async all(type) {
                return await fetch(`${type}/all`).then(res => res);
            },
            async currentUser() {
                return await fetch('user/get').then(res => res);
            }
        },
        Post: {
            async updatedPreferences(preferencesObject) {

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

            }
        }
    }

    const Print = {
        async form() {
            let user = await Fetch.Get.currentUser().then(res => res);
            this.locationSelectElement(user);
            this.languageSelectElement(user);
        },
        async locationSelectElement(user) {
            let locations = await Fetch.Get.all("location").then(res => res);
            let $locations = $("#location");
            $locations.empty();
            for(let location in locations) {
                //check if location matches user location
                // append option element with "selected" if match
            }
        },
        async languageSelectElement(user) {
            let languages = await Fetch.Get.all("language").then(res => res);
            let $languages = $("#language");
            for(let language in languages) {

            }
        }
    }

    const Events = {
        initialize() {
            $(document).on("click", "#edit-preferences-submit-button", async function() {
                Fetch.Post.updatedPreferences(MyPreferences.packagePreferencesObject());
            });
        }
    }

    MyPreferences.initialize();

});