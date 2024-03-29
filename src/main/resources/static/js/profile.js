import { Utils } from "./utils.js";
$(async function (){
    const $backendDiv = $("#profile-backend-info");
    const currentUserId = $backendDiv.attr("data-user-id");
    const isMyProfile = $backendDiv.attr("data-is-current-user") === "true";
    const isComrade = $backendDiv.attr("data-is-comrade") === "true";
    const isRecruit = $backendDiv.attr("data-is-recruit") === "true";
    const currentUser = await fetch(`${Utils.url()}user/${currentUserId}/info`).then(res => res.json());

    async function getUserGames() {
        // let userGames = await fetch(`${Utils.url()}game/user`).then(res => res.json());
        let userGames = currentUser.preferences.games;
        setMaxIndex(userGames);
        if(mobileView.matches){
            if(userGames.length > 3) {
                next.classList.remove("hidden");
            }
        } else {
            if(userGames.length > 6) {
                next.classList.remove("hidden");
            }
        }
        for(let game of userGames){
            // $('.my-games').append(`
            //     <div class="game" style="background-image: url(${game.artwork});">
            //     </div>
            // `)
            $('.track').prepend(`
<!--                <div class="card-container">-->
                    <div class="card rgb" style="background-image: url(${game.artwork});"></div>
<!--                </div>-->
             `)
        }
    }
    getUserGames()


    async function getUserFavorite() {
        // let favoriteGame = await fetch(`${Utils.url()}game/favorite`).then(res => res.json());
        let favoriteGame = currentUser.preferences.favoriteGame;
        if (favoriteGame.id !== null) {
            $('#page-wrapper').append(`
                <div class="favorite-game rgb" style="background-image: url(${favoriteGame.artwork});">
                    <img src="/Icons/favorite.png" alt="" id="favorite-icon" class="rgbImage">
                </div>
            `)
        }else{
            $('#page-wrapper').append(`
                <div class="favorite-game">
                <img src="/Icons/favDefault.jpg" alt="">
                    <img src="/Icons/favorite.png" alt="" id="favorite-icon">
                </div>
            `)
        }
    }
    getUserFavorite()

    async function getUserInfo(){
        // let userInfo = await fetch(`${Utils.url()}user/get`).then(res => res.json());
        let userInfo = currentUser;
        // Platforms
        $('.my-platforms').append(`
          My Games:
          `)
        for(let platform of userInfo.preferences.platforms ){
            if(platform.type === "PC"){
                $('.my-platforms').append(`
                    <img class="icon rgb" src="data:image/png;base64,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" alt="">
                 `)
            }else if(platform.type === "Playstation"){
                $('.my-platforms').append(`
                    <img class="icon rgb" src="https://upload.wikimedia.org/wikipedia/commons/thumb/9/91/PlayStation_App_Icon.jpg/800px-PlayStation_App_Icon.jpg" alt="">
                `)
            }else if(platform.type === "Xbox"){
                $('.my-platforms').append(`
                    <img class="icon rgb" src="data:image/png;base64,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" alt="">
                `)
            }else if(platform.type === "Nintendo"){
                $('.my-platforms').append(`
                    <img class="icon rgb" src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTemkurGVqCn_VtSMJPM8KLn7HWbRGlJ904lA&usqp=CAU" alt="">
                `)
            }
        }

        // Bio
        $('.bio').append(`
            <div>${userInfo.preferences.bio}</div>
        `)

        // Username
        $('.info-container').append(`
            <div class="username">
                ${userInfo.username}
            </div>
        `);

        // Gamertag
        if(isMyProfile || isComrade) {
            $('.info-container').append(`
                <div class="gamertag">
                       ${userInfo.preferences.gamertag}
                </div>
            `);
        }

        // Genres
        // $('.genres').append(`
        //                 <h3>GENRES</h3>
        //                 <div></div>
        // `)
        for(let genre of userInfo.preferences.genres){
            $('#genres-wrapper').append(`
                 <p>${genre.name} </p>
            `)
        }

        // location
        $('.preferences').append(`
                    <div class="location">${userInfo.preferences.location.timezone}</div>
        `)

        // Languages
        for(let language of userInfo.preferences.languages){
            $('.preferences').append(`
            <div>${language.language}</div>
        `)
        }
        // Profanity
        if(userInfo.preferences.matureLanguage === true){
            $('.preferences').append(`
            <div class="mature-language"> | Profanity: On | </div>
        `)
        }else{
            $('.preferences').append(`
            <div class="mature-language"> | Profanity: Off | </div>
            `)
        }

        // Game rating
        $('.preferences').append(`
            <div>${userInfo.preferences.rating.rating}</div>
        `)

        $('.profile-image').css('background-image', `url("${userInfo.profilePicture.url}")`)



    }
    getUserInfo()

})









/*<!--        Stroke inducing carousel -->*/
const prev = document.querySelector(".prev");
const next = document.querySelector(".next");
const carousel = document.querySelector(".my-games");
const track = document.querySelector(".track");
let width = carousel.offsetWidth;
let index = 0;

let maxIndex = 0;
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

// IS AUTHENTICATED //
function editPermissions(){
    if(document.getElementById("profile-backend-info").getAttribute("data-is-current-user") === "true"){
        $('#profile-image-container').append(`             
            <img id="upload-profile-picture" class="hidden" src="/Icons/edit.png">
        `)
        $(document)
            .on("mouseenter", "#profile-image-container", function() {
                $(".profile-image").addClass("darken");
                $("#upload-profile-picture").removeClass("hidden");
            })
            .on("mouseleave", "#profile-image-container", function() {
                $(".profile-image").removeClass("darken");
                $("#upload-profile-picture").addClass("hidden");
            })
    }
}
editPermissions()

$(document)
.on("click","#addGameCard",()=>{
    window.location.href =`${Utils.url()}games`;
})

function setMaxIndex(userGames) {
    if(mobileView.matches) {
        maxIndex = Math.ceil(userGames.length / 3);
        if(userGames.length + 1 > 3) {
            next.classList.remove("hidden");
        }
    } else {
        if(userGames.length + 1 > 6) {
            next.classList.remove("hidden");
        }
        maxIndex = Math.ceil(userGames.length / 6);
    }
}