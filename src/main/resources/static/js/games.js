import { Utils } from "./utils.js";

$(function () {
    console.log("Inside games.js");
    const MyGames = {
        initialize() {
            console.log("inside Games.initialize()");
            Events.initialize();
            Print.myFavoriteGame(this.myFavoriteGameDiv);
            // Print.myGames();
        },
        baseUrl: Utils.url(),
        csrfToken: $("meta[name='_csrf']").attr("content"),
        addGameDiv: $("#games-div").find(".track"),
        myGamesDiv: $("#my-games"),
        myFavoriteGameDiv: $("#my-favorite-game"),
        sortGamesByYear(games) {
            if(Array.isArray(games)) {
                games = games.sort((prev, current) => parseInt(prev.year) - parseInt(current.year));
            }
            return games;
        }
    };

    const Print = {
        async myFavoriteGame() {
            console.log("Inside favorite games");
            let favoriteGame = await Fetch.Get.myFavoriteGame().then(res => res);
            console.log(favoriteGame);
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
        // async myGames() {
        //     let userGames = await Fetch.Get.myGames().then(res => res);
        //     console.log("Inside Print.myGames(). userGames: ");
        //     console.log(userGames);
        //     MyGames.myGamesDiv.empty();
        //     for(let game of userGames) {
        //         this.singleMyGame(game, MyGames.myGamesDiv.find(".track"));
        //     }
        // },



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
        async gameResults(data) {
            let games = MyGames.sortGamesByYear(await data);
            MyGames.addGameDiv.empty();
            for(let game of games) {
                this.singleSearchedGame(game, MyGames.addGameDiv);
            }
        },
        async singleSearchedGame(data, div) {
            let game = await data;
            div.prepend(`
                <div class="card" data-game-id="${game.id}" style="background-image: url(${game.artwork});">
                        <div class="buttons-div d-flex justify-content-between">
                            <img class="add-game-button" src="/Icons/add.png">
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
                .on("click", "#game-search-button", async function() {
                    console.log("button has been clicked. Value in input: ");
                    console.log($("#game-search-input").val());
                    loading.classList.add("block");
                    await Print.gameResults(Fetch.Post.backendGameSearch($("#game-search-input").val()), $("#games-div"));
                    loading.classList.remove("block");

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
                    let gameIds = [...MyGames.myGamesDiv.find(".card")].map(game => parseInt(game.attributes[1].value));

                    if(!gameIds.includes(parseInt(addedGame.id))){
                        await Print.singleMyGame(addedGame, MyGames.myGamesDiv.find(".track"));
                    }
                })
                .on("click", ".remove-game-button", async function() {
                    console.log("Remove Game Button clicked");
                    await Fetch.Post.removeGame($(this).parent().parent().attr("data-game-id"));
                    // await Print.myGames();
                    $(this).parent().parent().remove();
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

//
// let song = {
//     title: spotifyName.name,
//     artist: {
//         artistName: spotifyArtist.name
//         genres: genreObjects
//     }
// }
//
// let postOptions = {
//     method: 'POST',
//     headers: {
//
//     },
//     body: JSON.stringify(song)
// }
/*<!--        Stroke inducing carousel -->*/

async function getUserGames() {
    let userGames = await fetch(`${Utils.url()}game/user`).then(res => res.json());
    console.log("Array of user's games")
    console.log(userGames)
    for(let game of userGames){
        // $('.my-games').append(`
        //     <div class="game" style="background-image: url(${game.artwork});">
        //     </div>
        // `)
        $('#my-games').find(".track").append(`
<!--                <div class="card-container">-->
                    <div class="card" data-game-id="${game.id}" style="background-image: url(${game.artwork});">
                        <div class="buttons-div d-flex justify-content-between">
                            <img class="favorite-game-button" src="Icons/favorite.png">
                            <img class="remove-game-button" src="/Icons/trash.png">
                        </div>
                    </div>

<!--                </div>-->
             `)
    }

}
getUserGames()

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
window.addEventListener("resize", function () {
    width = carousel.offsetWidth;
});
next.addEventListener("click", function (e) {
    console.log("inside event listener for next button click");
    e.preventDefault();
    index = index + 1;
    prev.classList.add("show");
    track.style.transform = "translateX(" + index * -70 + "vw)";
    if (track.offsetWidth - index * width < (index -1) * width / 6) {
        next.classList.add("hide");
    }
});
prev.addEventListener("click", function () {
    index = index - 1;
    next.classList.remove("hide");
    if (index === 0) {
        prev.classList.remove("show");
    }
    track.style.transform = "translateX(" + index * -70 + "vw)";
});
next2.addEventListener("click", function (e) {
    console.log("inside event listener for next button click");
    e.preventDefault();
    index2 = index2 + 1;
    prev2.classList.add("show");
    searchTrack.style.transform = "translateX(" + index2 * -70 + "vw)";
    if (searchTrack.offsetWidth - index2 * width < (index2 -1) * width / 6) {
        next2.classList.add("hide");
    }
});
prev2.addEventListener("click", function () {
    index2 = index2 - 1;
    next2.classList.remove("hide");
    if (index2 === 0) {
        prev2.classList.remove("show");
    }
    searchTrack.style.transform = "translateX(" + index2 * -70 + "vw)";
});

const loading = document.querySelector(".load-gif");
$(window).on("load", function () {
    loading.classList.add("none");
});
