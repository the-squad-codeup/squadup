$(function () {
    console.log("Inside games.js");

    const MyGames = {
        initialize() {
            console.log("inside Games.initialize()");
            Events.initialize();
            Fetch.Get.gameSearch("hollow knight");
        },
        baseUrl: $("#base-url").text()
    }

    const Print = {
        gameResults(games) {

        }
    }

    const Fetch = {
        Get: {
            async keys() {
                let results = await fetch(`${MyGames.baseUrl}keys`);
                let data = await results.json();
                return data;
            },
            async gameSearch(query) {
                let keys = await this.keys();
                console.log(keys);
                let body = `search "${query}"; fields name,cover.image_id,age_ratings.rating,age_ratings.category,genres.name,platforms.name`;
                const fetchOptions = {
                    method: 'POST',
                    headers: {
                        'Client-ID' : keys.igdb_CLIENT_ID,
                        'Authorization' : `Bearer ${keys.igdb_ACCESS_TOKEN}`,
                        'x-api-key' : keys.igdb_PROXY_KEY
                    },
                    body: body
                };
                console.log(fetchOptions);
                let results = await fetch(`${keys.igdb_PROXY_URL}games`, fetchOptions);
                let data = await results.json();
                console.log(data);
            }
        },
        Post: {

        }
    }

    const Events = {
        initialize() {
            console.log("Inside Events.initialize()");
            $(document)
                .on("click", "#game-search-button", function() {
                    console.log("button has been clicked. Value in input: ");
                    console.log($("#game-search-input").val());
                })
                .on("keyup", function(e) {
                    if($("#game-search-input").is(":focus") && e.key === "Enter") {
                        $("#game-search-button").trigger("click");
                    }
                })
            ;
        }
    }

    MyGames.initialize();
});