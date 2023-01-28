import { Utils } from "./utils.js";
$(function () {

    // MyGames object with globals/methods
    const MyGames = {
        // initializes event handlers and prints favorite game
        initialize() {
            Events.initialize();
            Print.myFavoriteGame(this.myFavoriteGameDiv);
            // Print.myGames();
        },
        baseUrl: Utils.url(),
        csrfToken: $("meta[name='_csrf']").attr("content"),
        addGameDiv: $("#games-div").find(".track"),
        myGamesDiv: $("#my-games"),
        myFavoriteGameDiv: $("#my-favorite-game"),
        // sorts games by year most recent to oldest
        sortGamesByYear(games) {
            if(Array.isArray(games)) {
                games = games.sort((prev, current) => parseInt(prev.year) - parseInt(current.year));
            }
            return games;
        }
    };

    // Print object with methods
    const Print = {
        // appends favorite game elements. uses a placeholder if no favorite game
        async myFavoriteGame() {
            let favoriteGame = await Fetch.Get.myFavoriteGame().then(res => res);
            if(favoriteGame.id != null) {
                MyGames.myFavoriteGameDiv.empty().append(`
                    <div class="div-card col-3 m-3" data-game-id="${favoriteGame.id}">
                        <div class="card game-card">
                            <img src="${favoriteGame.artwork}" class="card-img all-games-img">
                            <img src="/Icons/favorite.png" alt="" id="favorite-icon">
                        </div>  
                    </div>
                `);
            } else{
                MyGames.myFavoriteGameDiv.empty().append(`
                    <div class="div-card col-3 m-3" data-game-id="${favoriteGame.id}">
                        <div class="card game-card">
                            <img src="/Icons/favDefault.jpg" class="card-img all-games-img">
                            <img src="/Icons/favorite.png" alt="" id="favorite-icon">
                        </div>  
                    </div>
                `);
            }
        },
        // prints a single game in MyGame element
        async singleMyGame(data, div) {
            let game = await data;
            div.prepend(`
                    <div class="card" data-game-id="${game.id}" style="background-image: url(${game.artwork});">
                        <div class="buttons-div d-flex justify-content-between">
                            <img class="favorite-game-button" src="Icons/favorite.png">
                            <img class="remove-game-button" src="/Icons/trash.png">
                        </div>
                    </div>
            `);
        },
        // prints game results from search query
        async gameResults(data) {
            let games = await MyGames.sortGamesByYear(await data);
            MyGames.addGameDiv.empty();
            if(games.length > 4) {
                next2.classList.remove("hidden");
            }
            for(let game of games) {
                this.singleSearchedGame(game, MyGames.addGameDiv);
            }
            return games;
        },
        // prints single searched game
        async singleSearchedGame(data, div) {
            let game = await data;
            div.prepend(`
                <div class="card" data-game-id="${game.id}" style="background-image: url(${game.artwork});">
                        <div class="buttons-div d-flex justify-content-between">
                            <img class="add-game-button clickable" src="/Icons/add.png">
                        </div>
                    </div>
            `);
        }
    };

    // Fetch object with methods
    const Fetch = {
        // Get methods
        Get: {
            // gets keys from backend
            async keys() {
                let results = await fetch(`${MyGames.baseUrl}keys`);
                let data = await results.json();
                return data;
            },
            // gets current user games
            async myGames() {
                let data = await fetch(`${MyGames.baseUrl}game/user`).then(res => res.json());
                return data;
            },
            // gets current user favorite game
            async myFavoriteGame() {
                let data = await fetch(`${MyGames.baseUrl}game/favorite`).then(res => res.json());
                return data;
            }
        },
        // Post methods
        Post: {
            // posts query for backend API call
            async backendGameSearch(query) {
                const fetchOptions = {
                    method: 'POST',
                    headers: {
                        'X-CSRF-TOKEN' : MyGames.csrfToken
                    },
                    body: query
                }
                let results = await fetch(`${MyGames.baseUrl}game/search`, fetchOptions);
                let data = await results.json();
                return data;
            },
            // posts to add game to user game list by game id
            async addGame(id) {
                const fetchOptions = {
                    method: 'POST',
                    headers: {
                        'X-CSRF-TOKEN' : MyGames.csrfToken
                    }
                };
                let data = await fetch(`${MyGames.baseUrl}game/${id}/add`, fetchOptions).then(res => res.json());
                return data;
            },
            // posts to remove game from user game list by game id
            async removeGame(id) {
                const fetchOptions = {
                    method: 'POST',
                    headers: {
                        'X-CSRF-TOKEN' : MyGames.csrfToken
                    }
                };
                let data = await fetch(`${MyGames.baseUrl}game/${id}/remove`, fetchOptions).then(res => res.json());
                return data;
            },
            // posts to set user favorite game by game id
            async favoriteGame(id) {
                const fetchOptions = {
                    method: 'POST',
                    headers: {
                        'X-CSRF-TOKEN' : MyGames.csrfToken
                    }
                };
                let data = await fetch(`${MyGames.baseUrl}game/${id}/favorite`, fetchOptions).then(res => res.json());
                return data;
            }
        }
    };

    // event handlers
    const Events = {
        initialize() {
            $(document)
                // when game search button clicked, prints all results
                // sets styling for carousel
                .on("click", "#game-search-button", async function() {
                    $(".search-track").css("transform", "translateX(0vw)");
                    index2 = 0;
                    prev2.classList.add("hidden");
                    loading.classList.add("block");
                    let gameResults = await Print.gameResults(Fetch.Post.backendGameSearch($("#game-search-input").val()), $("#games-div"));
                    setMaxIndex2(gameResults);
                    loading.classList.remove("block");

                    // Fetch.Get.gameSearch($("#game-search-input").val());
                })
                // searches games when user hits enter on game search field
                .on("keyup", function(e) {
                    if($("#game-search-input").is(":focus") && e.key === "Enter") {
                        $("#game-search-button").trigger("click");
                    }
                })
                // adds game to user list when clicked
                // changes some styling variables to keep carousel working corrently with new game added to list
                .on("click", ".add-game-button", async function() {
                    let userGames = await fetch(`${Utils.url()}game/user`).then(res => res.json());
                    if(userGames == 0) {
                        MyGames.myGamesDiv.find(".track").empty();
                    }
                    setMaxIndex(userGames);
                    let gameId = $(this).parent().parent().attr("data-game-id");
                    let addedGame = await Fetch.Post.addGame(gameId);
                    let gameIds = [...MyGames.myGamesDiv.find(".card")].map(game => parseInt(game.attributes[1].value));
                    if (!gameIds.includes(parseInt(addedGame.id))) {
                        await Print.singleMyGame(addedGame, MyGames.myGamesDiv.find(".track"));
                    }
                })
                // removes game from user list when clicked
                .on("click", ".remove-game-button", async function() {
                    await Fetch.Post.removeGame($(this).parent().parent().attr("data-game-id"));
                    // await Print.myGames();
                    $(this).parent().parent().remove();
                })
                // changes favorite game when clicked
                .on("click", ".favorite-game-button", async function() {
                    await Fetch.Post.favoriteGame($(this).parent().parent().attr("data-game-id"));
                    await Print.myFavoriteGame(MyGames.myFavoriteGameDiv);
                })
            ;
        }
    };

    // initializes event handlers
    MyGames.initialize();
});

/*<!--        Stroke inducing carousel -->*/

// method to get current user's games
async function getUserGames() {
    let userGames = await fetch(`${Utils.url()}game/user`).then(res => res.json());
    // setting up carousel for mobile and regular view
    if(userGames.length > 0) {
        $("#my-games").find(".track").empty();
        setMaxIndex(userGames)
        if(mobileView.matches){
            if(userGames.length > 3) {
                next.classList.remove("hidden");
            }
        } else {
            if(userGames.length > 6) {
                next.classList.remove("hidden");
            }
        }
        // loops through game list and prints every game to carousel
        for (let game of userGames) {
            $('#my-games').find(".track").append(`
                <div class="card" data-game-id="${game.id}" style="background-image: url(${game.artwork});">
                    <div class="buttons-div d-flex justify-content-between">
                        <img class="favorite-game-button clickable" src="Icons/favorite.png">
                        <img class="remove-game-button clickable" src="/Icons/trash.png">
                    </div>
                </div>
            `);
        }
    }

}
// calls method created above
getUserGames();

// global variables used for carousels
const prev = document.querySelector(".prev");
const next = document.querySelector(".next");
const prev2 = document.querySelector(".prev2");
const next2 = document.querySelector(".next2");
const carousel = document.querySelector(".game-carousel");
const track = document.querySelector(".track");
const searchTrack = document.querySelector(".search-track");

let width = carousel.offsetWidth;
let index = 0;
let index2 = 0;
let maxIndex = 0;
let maxIndex2 = 0;
const loading = document.querySelector(".load-gif");

//-----------------------------------------------carousel event listeners-----------------------------------------------
// adjusts carousel width variable when window resized
window.addEventListener("resize", function () {
    width = carousel.offsetWidth;
});

$(window).on("load", function () {
    loading.classList.add("none");
});

//media query
let mobileView = window.matchMedia("(max-width: 480px)")

//-----------------------------------------event listeners for carousel buttons-----------------------------------------
next.addEventListener("click", function (e) {
    e.preventDefault();
    index += 1;
    if (index >= maxIndex -1) {
        next.classList.add("hidden");
    }
    prev.classList.remove("hidden");
    if(mobileView.matches){
        track.style.transform = "translateX(" + index * -60 + "vw)";
    }else{
        track.style.transform = "translateX(" + index * -70 + "vw)";
    }
});
prev.addEventListener("click", function () {
    index = index - 1;
    next.classList.remove("hidden");
    if (index === 0) {
        prev.classList.add("hidden");
    }
    if(mobileView.matches){
        track.style.transform = "translateX(" + index * -60 + "vw)";
    }else{
        track.style.transform = "translateX(" + index * -70 + "vw)";
    }
});
next2.addEventListener("click", function (e) {
    e.preventDefault();
    index2 += 1;
    if (index2 >= maxIndex2 - 1) {
        next2.classList.add("hidden");
    }
    prev2.classList.remove("hidden");
    if(mobileView.matches){
        searchTrack.style.transform = "translateX(" + index2 * -60 + "vw)";
    }else{
        searchTrack.style.transform = "translateX(" + index2 * -70 + "vw)";
    }
});
prev2.addEventListener("click", function () {
    index2 = index2 - 1;
    next2.classList.remove("hidden");
    if (index2 === 0) {
        prev2.classList.add("hidden");
    }
    if(mobileView.matches){
        searchTrack.style.transform = "translateX(" + index2 * -60 + "vw)";
    }else{
        searchTrack.style.transform = "translateX(" + index2 * -70 + "vw)";
    }
});

// sets index for first carousel to indicate when to show/hide next/prev buttons
function setMaxIndex(userGames) {
    if(mobileView.matches) {
        maxIndex = Math.ceil(userGames.length / 3);
        if(userGames.length > 3) {
            next.classList.remove("hidden");
        }
    } else {
        if(userGames.length > 6) {
            next.classList.remove("hidden");
        }
        maxIndex = Math.ceil(userGames.length / 6);
    }
}

// sets index for second carousel to indicate when to show/hide next/prev buttons
function setMaxIndex2(gameResults) {
    if(mobileView.matches) {
        maxIndex2 = Math.ceil(gameResults.length / 4);
        if(gameResults.length > 4) {
            next2.classList.remove("hidden");
        }
    } else {
        if(gameResults.length > 6) {
            next2.classList.remove("hidden");
        }
        maxIndex2 = Math.ceil(gameResults.length / 6);
    }
}