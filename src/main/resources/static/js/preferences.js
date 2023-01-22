import { Utils } from "./utils.js";
import { Overlays } from "./pref-overlays.js";

//print form not working

$(function() {

    const MyPreferences = {
        initialize() {
            Events.initialize();
            Print.form();
        },
        arrayIncludesLanguage(array, language) {
            for(let e of array) {
                if(e.language === language.language) {
                    return true;
                }
            }
            return false;
        },
        arrayIncludesPlatform(array, platform) {
            for(let e of array) {
                if(e.type === platform.type) {
                    return true;
                }
            }
            return false;
        },
        packageLanguageOptions: function ($div) {
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
        formValidated(preferencesObject) {
            let isLocationValid = this.locationValidated(preferencesObject.location);
            let areLanguagesValid = this.languagesValidated(preferencesObject.languages);
            let isRatingValid = this.ratingValidated(preferencesObject.rating);
            let arePlatformsValid = this.platformsValidated(preferencesObject.platforms);
            let isGamertagValid = this.gamertagValidated(preferencesObject.gamertag);
            let isBioValid = this.bioValidated(preferencesObject.gamertag);
            if(
                isLocationValid &&
                areLanguagesValid &&
                isRatingValid &&
                arePlatformsValid &&
                isGamertagValid &&
                isBioValid
            ) {
                return true;
            }
            Print.formNotValid();
            return false;
        },
        locationValidated(location) {
            let isValid = true;
            if(location == null) {
                isValid = false;
            }
            Print.locationValidation(isValid);
            return isValid;
        },
        languagesValidated(languages) {
            let isValid = true;
            if(languages.length < 1) {
                isValid = false;
            }
            Print.languagesValidation(isValid);
            return isValid;
        },
        ratingValidated(rating) {
            let isValid = true;
            if(rating == null) {
                isValid = false;
            }
            Print.ratingValidation(isValid);
            return isValid;
        },
        platformsValidated(platforms) {
            let isValid = true;
            if(platforms.length < 1) {
                isValid = false;
            }
            Print.platformsValidation(isValid);
            return isValid;
        },
        gamertagValidated(gamertag) {
            let isValid = true;
            if(gamertag === "" || !this.gamertagValidDiscordName(gamertag)) {
                isValid = false;
            }
            Print.gamertagValidation(isValid);
            return isValid;
        },
        gamertagValidDiscordName(gamertag) {
            let gamertagSplit = gamertag.split("#");
            if(gamertagSplit.length !== 2 || isNaN(gamertagSplit[1]) || gamertagSplit[1].length !== 4) {
                return false;
            }
            return true;
        },
        bioValidated(bio) {
            let isValid = true;
            if(bio === "") {
                isValid = false;
            }
            Print.bioValidation(isValid);
            return isValid;
        },
        packagePreferencesObject() {
            const preferencesObject = {
                bio: $("#bio").val(),
                //bio is updating table and saving to page
                location: {
                    timezone: $("#location").find(":selected").val()
                },
                languages: MyPreferences.packageLanguageOptions($("#languages")),
                matureLanguage: $("#mature-language").is(":checked"),
                // mature language is updating table

                // $("#game-ratings").multiselect();
                rating: {
                    rating: $("#game-ratings").find(":selected").val()
                    // rating: $("#game-ratings").is(":checked")
                },
                platforms: MyPreferences.packagePlatformOptions($("#platforms")),

                gamertag: $("#gamertag").val()
                // gamertag is updating the table and saving to page
            };
            return preferencesObject;
        },
        baseUrl: $("#base-url").text()
    };

    const Fetch = {
        Get: {
            async all(type) {
                let res = await fetch(`${Utils.url()}${type}/all`).then(res => res);
                let data = await res.json();
                return data;
            },
            async currentUser() {
                let res = await fetch(`${Utils.url()}user/get`).then(res => res);
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
                let results = await fetch(`${Utils.url()}profile/preferences/edit`, postOptions).then(res => res);
            }
        }
    }

    const Print = {
        async form() {
            let user = await Fetch.Get.currentUser().then(res => res);
            await this.locationSelectElement(user);
            await this.languageSelectElement(user);
            await this.gameRatingSelectElement(user);
            await this.platformSelectElement(user);
            await this.matureLanguageCheckboxElement(user);
            await this.gamertagElement(user);
            await this.bioElement(user);
        },
        async matureLanguageCheckboxElement(user){
            if(user.preferences.matureLanguage) {
                $("#mature-language").attr("checked", "checked");
            }
        },
        async gamertagElement(user) {
            $("#gamertag").val(`${user.preferences.gamertag}`);
        },
        async bioElement(user) {
            $("#bio").val(`${user.preferences.bio}`);
        },
        async locationSelectElement(user) {
            let locations = await Fetch.Get.all("location").then(res => res);
            let $locations = $("#location");
            $locations.empty();
            $locations.append(`<option value=""></option>`);

            for(let location of locations) {
                if(user.preferences.location != null && user.preferences.location.timezone === location.timezone) {
                    $locations.append(`
                        <option value="${location.timezone}" selected>${location.timezone}</option>
                    `);
                } else {
                    $locations.append(`
                        <option value="${location.timezone}">${location.timezone}</option>
                    `);
                }
            }
            Overlays.toggleOverlay();
        },
        async languageSelectElement(user) {
            let languages = await Fetch.Get.all("language").then(res => res);
            let $languages = $("#languages");
            $languages.empty();
            $languages.append(`<option value=""></option>`);
            for(let language of languages) {
                if(user.preferences.languages != null && MyPreferences.arrayIncludesLanguage(user.preferences.languages, language)) {
                    $languages.append(`
                        <option value="${language.language}" selected>${language.language}</option>
                    `);
                } else {
                    $languages.append(`
                        <option value="${language.language}">${language.language}</option>
                    `);
                }
            }
        },
        async gameRatingSelectElement(user) {
            let gameRatings = await Fetch.Get.all("rating").then(res => res);
            let $gameRatings = $("#game-ratings");
            $gameRatings.empty();
            $gameRatings.append(`<option value=""></option>`);
            for(let gameRating of gameRatings) {
                if(user.preferences.rating != null && user.preferences.rating.rating === gameRating.rating) {
                    $gameRatings.append(`
                        <option value="${gameRating.rating}" selected>${gameRating.rating}</option>
                    `);
                } else {
                    $gameRatings.append(`
                        <option value="${gameRating.rating}">${gameRating.rating}</option>
                    `);
                }
            }
        },
        async platformSelectElement(user) {
            let platforms = await Fetch.Get.all("platform").then(res => res);
            let $platforms = $("#platforms");
            $platforms.empty();
            $platforms.append(`<option value=""></option>`);
            for(let platform of platforms) {
                if(user.preferences.platforms != null && MyPreferences.arrayIncludesPlatform(user.preferences.platforms, platform)) {
                    $platforms.append(`
                        <option value="${platform.type}" selected>${platform.type}</option>
                    `);
                } else {
                    $platforms.append(`
                        <option value="${platform.type}">${platform.type}</option>
                    `);
                }
            }
        },
        locationValidation(isValid) {
            let locationInput = $("#select2-location-container");
            isValid ? locationInput.css("background", "") : locationInput.css("background", "#fc3503");
        },
        languagesValidation(isValid) {
            let languagesInput = $("#languages").parent().find(".select2-selection");
            isValid ? languagesInput.css("background", "") : languagesInput.css("background", "#fc3503");
        },
        ratingValidation(isValid) {
            let ratingInput = $("#select2-game-ratings-container");
            isValid ? ratingInput.css("background", "") : ratingInput.css("background", "#fc3503");
        },
        platformsValidation(isValid) {
            let platformsInput = $("#platforms").parent().find(".select2-selection");
            isValid ? platformsInput.css("background", "") : platformsInput.css("background", "#fc3503");
        },
        gamertagValidation(isValid) {
            let gamertagInput = $("#gamertag");
            isValid ? gamertagInput.css("background", "") : gamertagInput.css("background", "#fc3503");
        },
        bioValidation(isValid) {
            let bioInput = $("#bio");
            isValid ? bioInput.css("background", "") : bioInput.css("background", "#fc3503");
        },
        formNotValid() {
            $("#preferences-form").find("#validation-prepend").remove();
            $("#preferences-form").prepend(`
                <p id="validation-prepend">
                    <span>All fields are required. Please enter correct values.</span>
                </p>
            `);
        }
    };

    const Events = {
        initialize() {
            $(document)
                .on("click", "#edit-preferences-submit-button", async function() {
                    let preferencesObject = MyPreferences.packagePreferencesObject();
                    if(MyPreferences.formValidated(preferencesObject)) {
                        await Fetch.Post.updatedPreferences(preferencesObject);
                        window.location.replace(`/hq`);
                    } else {
                        document.body.scrollTop = document.documentElement.scrollTop = 0;
                    }
                })
                .on("change", "#location", function() {
                    Overlays.toggleOverlay();
                })
            ;
            $(document).ready(function() {
                $("#location").select2({
                    placeholder: "select a timezone",
                });
                $("#languages").select2({
                    placeholder: "select languages",
                });
                $("#game-ratings").select2({
                    placeholder: "select a rating",
                });
                $("#platforms").select2({
                    placeholder: "select platforms",
                });
                Overlays.hideOverlays();
            });
        }
    }

    MyPreferences.initialize();


});