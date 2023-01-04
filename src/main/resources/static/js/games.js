import { Utils } from "./utils.js";

$(function () {
    console.log("Inside games.js");

    const MyGames = {
        initialize() {
            console.log("inside Games.initialize()");
            Events.initialize();
            Print.myFavoriteGame(this.myFavoriteGameDiv);
            Print.myGames(this.myGamesDiv);
        },
        baseUrl: Utils.url(),
        csrfToken: $("meta[name='_csrf']").attr("content"),
        addGameDiv: $("#games-div"),
        myGamesDiv: $("#my-games"),
        myFavoriteGameDiv: $("#my-favorite-game"),
        sortGamesByYear(games) {
            return games.sort((prev, current) => parseInt(prev.year) - parseInt(current.year));
        }
    };

    const Print = {
        async myFavoriteGame() {
            console.log("Inside favorite games");
            let favoriteGame = await Fetch.Get.myFavoriteGame().then(res => res);
            console.log(favoriteGame);
            if(favoriteGame.id != null) {
                MyGames.myFavoriteGameDiv.empty().append(`
                    <div class="div-card col-3" data-game-id="${favoriteGame.id}">
                        <div class="card game-card border-0">
                            <img src="${favoriteGame.artwork}" class="card-img all-games-img">
                        </div>
                    </div>
                `);
            }
        },
        async myGames() {
            let userGames = await Fetch.Get.myGames().then(res => res);
            console.log("Inside Print.myGames(). userGames: ");
            console.log(userGames);
            MyGames.myGamesDiv.empty();
            for(let game of userGames) {
                this.singleMyGame(game, MyGames.myGamesDiv);
            }
        },
        async singleMyGame(data, div) {
            let game = await data;
            div.prepend(`
                <div class="div-card col-3" data-game-id="${game.id}">
                    <div class="card game-card border-0">
                        <img src="${game.artwork}" class="card-img all-games-img">
                    </div>
                    <div class="buttons-div d-flex justify-content-between">
                        <button class="favorite-game-button">Favorite</button>
                        <button class="remove-game-button">Remove</button>
                    </div>
                </div>
            `);
        },
        async gameResults(data) {
            let games = MyGames.sortGamesByYear(await data);
            console.log(games);
            MyGames.addGameDiv.empty();
            for(let game of games) {
                this.singleSearchedGame(game, MyGames.addGameDiv);
            }
        },
        async singleSearchedGame(data, div) {
            let game = await data;
            div.prepend(`
                <div class="div-card col-3" data-game-id="${game.id}">
                    <div class="card game-card border-0">
                        <img src="${game.artwork}" class="card-img all-games-img">
                    </div>
                    <div class="buttons-div d-flex justify-content-between">
                        <button class="add-game-button">Add</button>
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
            async myGames() {
                let data = await fetch(`${MyGames.baseUrl}game/user`).then(res => res.json());
                console.log("Inside Fetch.Get.myGames(). Data returned:");
                console.log(data);
                return data;
            },
            async myFavoriteGame() {
                let data = await fetch(`${MyGames.baseUrl}game/favorite`).then(res => res.json());
                console.log("Inside Fetch.Get.myFavoriteGame(). Data returned:");
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
            },
            async favoriteGame(id) {
                console.log("inside favoriteGame. Id: ");
                console.log(id);
                const fetchOptions = {
                    method: 'POST',
                    headers: {
                        'X-CSRF-TOKEN' : MyGames.csrfToken
                    }
                };
                let data = await fetch(`${MyGames.baseUrl}game/${id}/favorite`, fetchOptions).then(res => res.json());
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
                .on("click", ".add-game-button", async function() {
                    console.log("Add Game Button clicked");
                    let gameId = $(this).parent().parent().attr("data-game-id");
                    let addedGame = await Fetch.Post.addGame(gameId);
                    let gameIds = [...MyGames.myGamesDiv.children()].map(game => parseInt(game.attributes[1].value));
                    if(!gameIds.includes(parseInt(addedGame.id))){
                        await Print.singleMyGame(addedGame, MyGames.myGamesDiv);
                    }
                })
                .on("click", ".remove-game-button", async function() {
                    console.log("Remove Game Button clicked");
                    await Fetch.Post.removeGame($(this).parent().parent().attr("data-game-id"));
                    await Print.myGames();
                })
                .on("click", ".favorite-game-button", async function() {
                    console.log("Favorite Game Button clicked");
                    await Fetch.Post.favoriteGame($(this).parent().parent().attr("data-game-id"));
                    await Print.myFavoriteGame(MyGames.myFavoriteGameDiv);
                })
            ;
        }
    };

    MyGames.initialize();
});