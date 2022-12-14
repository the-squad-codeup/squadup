$(function() {

    const MyPreferences = {
        initialize() {
            Events.initialize();
            Print.form();
        },
        packageLanguageOptions($div) {
            let options = [];
            for(let child of $div.children()) {
                if(child.selected) {
                    options.push({language: child.value});
                }
            }
            return options;
        },
        packagePlatformOptions($div) {
            let options = [];
            for(let child of $div.children()) {
                if(child.selected) {
                    options.push({type: child.value});
                }
            }
            return options;
        },
        packagePreferencesObject() {
            const preferencesObject = {
                bio: $("#bio").val(),
                location: {
                    timezone: $("#locations").find(":selected").text()
                },
                languages: MyPreferences.packageLanguageOptions($("#languages")),
                mature_language: $("#mature-language").is(":checked"),
                game_age_rating: {
                    rating: $("#game-ratings").find(":selected").text()
                },
                platforms: MyPreferences.packagePlatformOptions($("#platforms")),
                gamertag: $("#gamertag").text()
            };
            return preferencesObject;
        },
        baseUrl: $("#base-url").text()
    }

    const Fetch = {
        Get: {
            async all(type) {
                let res = await fetch(`${MyPreferences.baseUrl}${type}/all`).then(res => res);
                let data = await res.json();
                return data;
            },
            async currentUser() {
                let res = await fetch(`${MyPreferences.baseUrl}user/get`).then(res => res);
                let data = await res.json();
                return data;
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
                let results = await fetch(`/profile/preferences/${$("#hidden-preferences-id").text()}/edit`, postOptions).then(res => res);

            }
        }
    }

    const Print = {
        async form() {
            let user = await Fetch.Get.currentUser().then(res => res);
            console.log(user);
            await this.locationSelectElement(user);
            await this.languageSelectElement(user);
            await this.gameRatingSelectElement(user);
            await this.platformSelectElement(user);
        },
        async locationSelectElement(user) {
            // user = await user.then(res => res);
            let locations = await Fetch.Get.all("location").then(res => res);
            let $locations = $("#locations");
            $locations.empty();
            for(let location of locations) {
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
            let $languages = $("#languages");
            for(let language of languages) {
                // check if language matches user language
                // append option element with "selected" if match
                if(user.preferences.language != null && user.preferences.languages.includes(language)) {
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
            let $gameRatings = $("#game-ratings");
            for(let gameRating of gameRatings) {
                //check if gameRating matches user gameRating
                // append option element with "selected" if match
                if(user.preferences.game_age_rating != null && user.preferences.game_age_rating.rating === gameRating.rating) {
                    $gameRatings.append(`
                        <option value="${gameRating.rating}">${gameRating.rating}</option>
                    `);
                } else {
                    $gameRatings.append(`
                        <option value="${gameRating.rating}" selected>${gameRating.rating}</option>
                    `);
                }
            }
        },
        async platformSelectElement(user) {
            let platforms = await Fetch.Get.all("platform").then(res => res);
            let $platforms = $("#platforms");
            for(let platform of platforms) {
                //check if platform matches user platform
                //append option element with "selected" if match
                if(user.preferences.platforms != null && user.preferences.platforms.includes(platform)) {
                    $platforms.append(`
                        <option value="${platform.type}">${platform.type}</option>
                    `);
                } else {
                    $platforms.append(`
                        <option value="${platform.type}" selected>${platform.type}</option>
                    `);
                }
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

    const Utils = {
        containsString(array, string) {

        }
    }

    MyPreferences.initialize();

});