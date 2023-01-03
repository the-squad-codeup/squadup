import { Utils } from "./utils.js";

$(function () {
    console.log("Inside games.js");

    const MyGames = {
        initialize() {
            console.log("inside Games.initialize()");
            Events.initialize();
            Print.myGames($("#my-games"));
        },
        baseUrl: Utils.url(),
        csrfToken: $("meta[name='_csrf']").attr("content"),
        addGameDiv: $("#games-div"),
        myGamesDiv: $("#my-games"),
        sortGamesByYear(games) {
            return games.sort((prev, current) => parseInt(prev.year) - parseInt(current.year));
        }
    };

    const Print = {
        async myGames() {
            let userGames = await Fetch.Get.myGames().then(res => res);
            console.log("Inside Print.myGames(). userGames: ");
            console.log(userGames);
            MyGames.myGamesDiv.empty();
            for(let game of userGames) {
                this.singleGame(game, MyGames.myGamesDiv);
            }
        },
        async gameResults(data) {
            let games = MyGames.sortGamesByYear(await data);
            console.log(games);
            MyGames.addGameDiv.empty();
            for(let game of games) {
                this.singleGame(game, MyGames.addGameDiv);
            }
        },
        async singleGame(data, div) {
            let game = await data;
            div.prepend(`
                <div class="div-card col-3" data-game-id="${game.id}">
                    <div class="card game-card border-0">
                        <img src="${game.artwork}" class="card-img all-games-img">
                    </div>
                </div>
            `);
        }
    };

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
            },
            async myGames() {
                let data = await fetch(`${MyGames.baseUrl}game/user`).then(res => res.json());
                console.log("Inside Fetch.Get.myGames(). Data returned:");
                console.log(data);
                return data;
            }
        },
        Post: {
            async backendGameSearch(query) {
                console.log("Inside backendGameSearch. query: ");
                console.log(query);
                const fetchOptions = {
                    method: 'POST',
                    headers: {
                        'X-CSRF-TOKEN' : MyGames.csrfToken
                    },
                    body: query
                }
                let results = await fetch(`${MyGames.baseUrl}game/search`, fetchOptions);
                let data = await results.json();
                console.log(data);
                return data;
            },
            async addGame(id) {
                console.log("inside addGame. Id: ");
                console.log(id);
                const fetchOptions = {
                    method: 'POST',
                    headers: {
                        'X-CSRF-TOKEN' : MyGames.csrfToken
                    }
                };
                let data = await fetch(`${MyGames.baseUrl}game/${id}/add`, fetchOptions).then(res => res.json());
                console.log(data);
                return data;
            },
            async removeGame(id) {
                console.log("inside removeGame. Id: ");
                console.log(id);
                const fetchOptions = {
                    method: 'POST',
                    headers: {
                        'X-CSRF-TOKEN' : MyGames.csrfToken
                    }
                };
                let data = await fetch(`${MyGames.baseUrl}game/${id}/remove`, fetchOptions).then(res => res.json());
                console.log(data);
                return data;
            }
        }
    };

    const Events = {
        initialize() {
            console.log("Inside Events.initialize()");
            $(document)
                .on("click", "#game-search-button", function() {
                    console.log("button has been clicked. Value in input: ");
                    console.log($("#game-search-input").val());
                    Print.gameResults(Fetch.Post.backendGameSearch($("#game-search-input").val()), $("#games-div"));
                    // Fetch.Get.gameSearch($("#game-search-input").val());
                })
                .on("keyup", function(e) {
                    if($("#game-search-input").is(":focus") && e.key === "Enter") {
                        $("#game-search-button").trigger("click");
                    }
                })
                .on("click", "#games-div .game-card", async function() {
                    console.log("Add Game Card clicked");
                    let addedGame = await Fetch.Post.addGame($(this).parent().attr("data-game-id"));
                    await Print.singleGame(addedGame, MyGames.myGamesDiv);
                })
                .on("click", "#my-games .game-card", async function() {
                    console.log("Remove Game Card clicked");
                    await Fetch.Post.removeGame($(this).parent().attr("data-game-id"));
                    await Print.myGames();
                })
            ;
        }
    };

    MyGames.initialize();
});