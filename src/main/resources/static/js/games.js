import { Utils } from "./utils.js";
$(function () {
    const MyGames = {
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
        sortGamesByYear(games) {
            if(Array.isArray(games)) {
                games = games.sort((prev, current) => parseInt(prev.year) - parseInt(current.year));
            }
            return games;
        }
    };

    const Print = {
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
            console.log("inside gameresults")
            let games = await MyGames.sortGamesByYear(await data);
            MyGames.addGameDiv.empty();
            console.log(games);
            console.log(games.length);
            if(games.length > 4) {
                next2.classList.remove("hidden");
            }
            for(let game of games) {
                this.singleSearchedGame(game, MyGames.addGameDiv);
            }
            return games;
        },
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

    const Fetch = {
        Get: {
            async keys() {
                let results = await fetch(`${MyGames.baseUrl}keys`);
                let data = await results.json();
                return data;
            },
            async myGames() {
                let data = await fetch(`${MyGames.baseUrl}game/user`).then(res => res.json());
                return data;
            },
            async myFavoriteGame() {
                let data = await fetch(`${MyGames.baseUrl}game/favorite`).then(res => res.json());
                return data;
            }
        },
        Post: {
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

    const Events = {
        initialize() {
            $(document)
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
                .on("keyup", function(e) {
                    if($("#game-search-input").is(":focus") && e.key === "Enter") {
                        $("#game-search-button").trigger("click");
                    }
                })
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
                .on("click", ".remove-game-button", async function() {
                    await Fetch.Post.removeGame($(this).parent().parent().attr("data-game-id"));
                    // await Print.myGames();
                    $(this).parent().parent().remove();
                })
                .on("click", ".favorite-game-button", async function() {
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
        for (let game of userGames) {
            // $('.my-games').append(`
            //     <div class="game" style="background-image: url(${game.artwork});">
            //     </div>
            // `)
            $('#my-games').find(".track").append(`
    <!--                <div class="card-container">-->
                        <div class="card" data-game-id="${game.id}" style="background-image: url(${game.artwork});">
                            <div class="buttons-div d-flex justify-content-between">
                                <img class="favorite-game-button clickable" src="Icons/favorite.png">
                                <img class="remove-game-button clickable" src="/Icons/trash.png">
                            </div>
                        </div>
    
    <!--                </div>-->
                 `)
        }
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
let maxIndex = 0;
let maxIndex2 = 0;
window.addEventListener("resize", function () {
    width = carousel.offsetWidth;
});
// next.addEventListener("click", function (e) {
//     e.preventDefault();
//     index = index + 1;
//     prev.classList.remove("hidden");
//     track.style.transform = "translateX(" + index * -70 + "vw)";
//     if (track.offsetWidth - (index+1) * width < index * width / 6) {
//         next.classList.add("hidden");
//     }
// });
// prev.addEventListener("click", function () {
//     index = index - 1;
//     next.classList.remove("hidden");
//     if (index === 0) {
//         prev.classList.add("hidden");
//     }
//     track.style.transform = "translateX(" + index * -70 + "vw)";
// });
// next2.addEventListener("click", function (e) {
//     e.preventDefault();
//     index2 = index2 + 1;
//     prev2.classList.remove("hidden");
//     searchTrack.style.transform = "translateX(" + index2 * -70 + "vw)";
//     if (searchTrack.offsetWidth - (index2+1) * width < index2 * width / 6) {
//         next2.classList.add("hidden");
//     }
// });
// prev2.addEventListener("click", function () {
//     index2 = index2 - 1;
//     next2.classList.remove("hidden");
//     if (index2 === 0) {
//         prev2.classList.add("hidden");
//     }
//     searchTrack.style.transform = "translateX(" + index2 * -70 + "vw)";
// });

const loading = document.querySelector(".load-gif");
$(window).on("load", function () {
    loading.classList.add("none");
});

//media query
let mobileView = window.matchMedia("(max-width: 480px)")

window.addEventListener("resize", function () {
    width = carousel.offsetWidth;
});
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


// carousel 2

window.addEventListener("resize", function () {
    width = carousel.offsetWidth;
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