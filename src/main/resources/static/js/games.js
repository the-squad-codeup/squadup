$(function () {
    console.log("Inside games.js");

    const MyGames = {
        initialize() {
            console.log("inside Games.initialize()");
            Events.initialize();
        },
        baseUrl: $("#base-url").text(),
        igdbGamesUrl: "https://api.igdb.com/v4/games"
    }

    const Print = {
        gameResults(games) {

        }
    }

    const Fetch = {
        Get: {
            async gameSearch(query) {
                const postOptions = {
                    method: 'POST',
                    headers: {

                    }
                };
                let results = await fetch(`${MyGames.igdbGamesUrl}`)
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