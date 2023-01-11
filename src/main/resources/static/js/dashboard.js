//================COMRADES FUNCTION==============================
import {Utils} from "./utils.js"

$(function() {

    const csrfToken = $("meta[name='_csrf']").attr("content")

    console.log("Inside comrades.js");
    async function printComCards(comrades) {
        comrades = await comrades;
        comrades = comrades.sort((prev, current) => (new Date(prev.dateComraded)) - new Date(current.dateComraded))
        console.log(comrades);
        $("#card-com").html('');
        for (let comrade of comrades) {
            $(`#card-com`).append(`
                 <div class="su-card" data-comrade-id="${comrade.id}">
                    <div class="su-card-top"> 
                        <div class="su-card-col su-card-col-shrink">
                            <img class="card-img-top" src="https://i.imgur.com/0Z0Z0Z0.jpg" alt="user profile picture">
                            <h4 class="card-title">${comrade.userTwo.username}</h4>
                        </div>
                        <div class="su-card-col">
                            <p class="card-text">${comrade.userTwo.username}'s Bio: ${comrade.userTwo.preferences.bio}</p>
                        </div>
                    </div>
                    <div class="su-card-middle">
                        <div class="su-card-games-list">
                        </div>
                    </div>
                    <div class="su-card-bottom">
                        <button id="remove" class="btn unfriend-link">Remove Comrade</a>
                    </div>
                </div>
            `);
            for (let userTwoGame of comrade.userTwo.preferences.games) {
                $(`#card-com`).children(`[data-comrade-id="${comrade.id}"]`).find(".su-card-games-list").append(`
                    <div class="su-card-game">
                        <img class="su-card-game-image" src="${userTwoGame.artwork}" alt="${userTwoGame.title} icon">
                    </div>
                `);
            }
        }
    }
//======================ARROW BTNS========================


    document.getElementById('arrowsBtnCom').addEventListener('click', async function(e){
        e.preventDefault();
        if (e.target && e.target.classList.contains("arrow-right")) {
            let cardList = document.getElementById('card-com');
            // get the current data-left value of the card list
            let currentLeft = cardList.dataset.left;
            // count the amount of cards in the card list
            let cardCount = cardList.childElementCount;
            let maxClicks = cardCount - 1;
            // if the current data-left value is less than the (maxClicks * 460), decrease the data-left value by 460
            if (currentLeft == -(maxClicks * 35)) {
                //do nothing
            }
            else {
                // set the cardList to have a css left value of 460px less than the current left value
                cardList.style.left = `${parseInt(currentLeft) - 35}vw`;
                // set the data-left value of the card list to the new left value
                cardList.dataset.left = `${parseInt(currentLeft) - 35}`;
            }
        }
        if (e.target && e.target.classList.contains("arrow-left")){
            let cardList = document.getElementById('card-com');
            // get the current data-left value of the card list
            let currentLeft = cardList.dataset.left;
            if (currentLeft == "0") {
                // set the cardList to have a css left value of 460px less than the current left value
                cardList.style.left = `${parseInt(currentLeft) + 35}vw`;
            }
            else {
                // set the cardList to have a css left value of 450px less than the current left value
                cardList.style.left = `${parseInt(currentLeft) + 35}vw`;
                // set the data-left value of the card list to the new left value
                cardList.dataset.left = `${parseInt(currentLeft) + 35}`;
            }
        }
    })






    document.getElementById("card-com").addEventListener('click', async function(e){
        e.preventDefault();
        if (e.target && e.target.classList.contains("unfriend-link")) {
            let unfriend = e.target.parentElement.parentElement.getAttribute("data-comrade-id");
            console.log(unfriend);
            const fetchOptions = {
                method: 'POST',
                headers: {
                    'X-CSRF-TOKEN' : csrfToken
                }
            }
            await fetch(`${Utils.url()}comrades/${unfriend}/delete`, fetchOptions);
            document.querySelector(`[data-comrade-id="${unfriend}"]`).remove();
        }
    })

    async function getComrades(){
        let results = await fetch(`${Utils.url()}comrades/all`);
        let data = await results.json();
        console.log(data);
        return data;
    }

    printComCards(getComrades());

//    ============================RECRUITS FUNCTION===================================
//     const csrfTokenRec = $("meta[name='_csrf']").attr("content")

console.log("Inside recruits.js");
async function printRecCards(recruits) {
    recruits = await recruits;
    recruits = recruits.sort((prev, current) => (new Date(prev.dateRecruited)) - (new Date(current.dateRecruited)))
    console.log(recruits);
    $("#card-rec").html('');
    for (let recruit of recruits) {
        $(`#card-rec`).append(`
                <div class="su-card" data-recruit-id="${recruit.id}">
                    <div class="su-card-top">
                        <div class="su-card-col su-card-col-shrink">
                            <img class="card-img-top" src="https://i.imgur.com/0Z0Z0Z0.jpg" alt="user profile picture">
                            <h4 class="card-title">${recruit.userTwo.username}</h4>
                        </div>
                        <div class="su-card-col">
                            <p class="card-text">${recruit.userTwo.username}'s Bio: ${recruit.userTwo.preferences.bio}</p>
                        </div>
                    </div>
                    <div class="su-card-middle">
                        <div class="su-card-games-list">
                        </div>
                    </div>
                    <div class="su-card-bottom">
                        <button id="accept" class="btn squadup-link">Accept</button>
                        <button id="reject" class="btn squaddown-link">Reject</button>
                    </div>
                </div>
            `);
        for (let userTwoGame of recruit.userTwo.preferences.games) {
            $(`#card-rec`).children(`[data-recruit-id="${recruit.id}"]`).find(".su-card-games-list").append(`
                    <div class="su-card-game">
                        <img class="su-card-game-image" src="${userTwoGame.artwork}" alt="${userTwoGame.title} icon">
                    </div>
                `);
        }
    }
}
//======================ARROW BTNS========================


    document.getElementById('arrowsBtnRec').addEventListener('click', async function(e){
        e.preventDefault();
        if (e.target && e.target.classList.contains("arrow-right")) {
            let cardList = document.getElementById('card-rec');
            // get the current data-left value of the card list
            let currentLeft = cardList.dataset.left;
            // count the amount of cards in the card list
            let cardCount = cardList.childElementCount;
            let maxClicks = cardCount - 1;
            // if the current data-left value is less than the (maxClicks * 460), decrease the data-left value by 460
            if (currentLeft == -(maxClicks * 35)) {
                //do nothing
            }
            else {
                // set the cardList to have a css left value of 460px less than the current left value
                cardList.style.left = `${parseInt(currentLeft) - 35}vw`;
                // set the data-left value of the card list to the new left value
                cardList.dataset.left = `${parseInt(currentLeft) - 35}`;
            }
        }
        if (e.target && e.target.classList.contains("arrow-left")){
            let cardList = document.getElementById('card-rec');
            // get the current data-left value of the card list
            let currentLeft = cardList.dataset.left;
            if (currentLeft == "0") {
                // set the cardList to have a css left value of 460px less than the current left value
                cardList.style.left = `${parseInt(currentLeft) + 35}vw`;
            }
            else {
                // set the cardList to have a css left value of 450px less than the current left value
                cardList.style.left = `${parseInt(currentLeft) + 35}vw`;
                // set the data-left value of the card list to the new left value
                cardList.dataset.left = `${parseInt(currentLeft) + 35}`;
            }
        }
    })


document.getElementById("card-rec").addEventListener('click', async function (e) {
    e.preventDefault();
    if (e.target && e.target.classList.contains("squadup-link")) {
        let accept = e.target.parentElement.parentElement.getAttribute("data-recruit-id");
        console.log(accept);
        const fetchOptions = {
            method: 'POST',
            headers: {
                'X-CSRF-TOKEN' : csrfToken
            }
        }
        let results = await fetch(`${Utils.url()}recruits/${accept}/accept`, fetchOptions);
        let data = await results.json();
        console.log(data);
        e.target.parentElement.parentElement.remove();
    }

    if (e.target && e.target.classList.contains("squaddown-link")) {
        let reject = e.target.parentElement.parentElement.getAttribute("data-recruit-id");
        console.log(reject);
        const fetchOptions = {
            method: 'POST',
            headers: {
                'X-CSRF-TOKEN' : csrfToken
            }
        }
        let results = await fetch(`${Utils.url()}recruits/${reject}/reject`, fetchOptions);
        let data = await results.json();
        console.log(data);
        e.target.parentElement.parentElement.remove();
    }
})

async function getAllRecruits(){
    let results = await fetch(`${Utils.url()}recruits/all`);
    let data = await results.json();
    console.log(data);
    return data;
}

printRecCards(getAllRecruits());



});


window.addEventListener("load", function(e){
    console.log("Inside remove class event listener function")
    document.getElementById("nav-games-link").classList.remove("games");
})