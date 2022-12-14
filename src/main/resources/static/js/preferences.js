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
        },
        baseUrl: $("#base-url").text()
    }

    const Fetch = {
        Get: {
            async all(type) {
                let res = await fetch(`${MyPreferences.baseUrl}${type}/all`).then(res => res);
                return res.json();
            },
            async currentUser() {
                let res = await fetch(`${MyPreferences.baseUrl}user/get`).then(res => res);
                return res.json();
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
                let results = await fetch(`/profile/preferences/${$("#hidden-preferences-id").text()}/edit`, postOptions).then(res => res);

            }
        }
    }

    const Print = {
        async form() {
            let user = await Fetch.Get.currentUser().then(res => res);
            console.log("Currently logged in user: ");
            console.log(user);
            this.locationSelectElement(user);
            this.languageSelectElement(user);
            this.gameRatingSelectElement(user);
            this.platformSelectElement(user);
        },
        async locationSelectElement(user) {
            console.log("inside locationSelectElement. User in here: ");
            console.log(user);
            // user = await user.then(res => res);
            let locations = await Fetch.Get.all("location").then(res => res);
            console.log("All locations get results:");
            console.log(locations);
            let $locations = $("#locations");
            $locations.empty();
            for(let location of locations) {
                console.log("inside the for-in loop to print out select options. Single location object: ");
                console.log(location);
                console.log(typeof location);
                //check if location matches user location
                // append option element with "selected" if match
                if(user.preferences.location != null && user.preferences.location.timezone === location.timezone) {
                    $locations.append(`
                        <option value="${location.timezone}">${location.timezone}</option>
                    `);
                } else {
                    $locations.append(`
                        <option value="${location.timezone}" selected>${location.timezone}</option>
                    `);
                }
            }
        },
        async languageSelectElement(user) {
            let languages = await Fetch.Get.all("language").then(res => res);
            console.log("All languages get results:");
            console.log(languages);
            let $languages = $("#languages");
            for(let language of languages) {
                // check if language matches user language
                // append option element with "selected" if match
                if(user.preferences.language != null && user.preferences.language.language === language.language) {
                    $languages.append(`
                        <option value="${language.language}">${language.language}</option>
                    `);
                } else {
                    $languages.append(`
                        <option value="${language.language}" selected>${language.language}</option>
                    `);
                }
            }
        },
        async gameRatingSelectElement(user) {
            let gameRatings = await Fetch.Get.all("rating").then(res => res);
            console.log("All ratings get results:");
            console.log(gameRatings);
            let $gameRatings = $("#game-ratings");
            for(let gameRating of gameRatings) {
                //check if gameRating matches user gameRating
                // append option element with "selected" if match
            }
        },
        async platformSelectElement(user) {
            let platforms = await Fetch.Get.all("platform").then(res => res);
            console.log("All platforms get results:");
            console.log(platforms);
            let $platforms = $("#platforms");
            for(let platform of platforms) {
                //check if platform matches user platform
                //append option element with "selected" if match
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