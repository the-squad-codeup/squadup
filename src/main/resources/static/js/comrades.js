import {Utils} from "./utils.js"

$(function() {

    const csrfToken = $("meta[name='_csrf']").attr("content")

    console.log("Inside comrades.js");
    async function printUserCards(comrades) {
        comrades = await comrades;
        comrades = comrades.sort((prev, current) => (new Date(prev.dateComraded)) - new Date(current.dateComraded))
        console.log(comrades);
        $("#card").html('');
        for (let comrade of comrades) {
            $(`#card`).append(`
                 <div class="su-card" data-comrade-id="${comrade.id}">
                    <div class="su-card-top"> 
                        <div class="su-card-col su-card-col-shrink">
                            <img class="card-img-top" src="${comrade.userTwo.profilePicture.url}" alt="user profile picture">
                            <h4 class="card-title">${comrade.userTwo.username}</h4>
                        </div>
                        <div class="su-card-col">
                            <p class="card-text">${comrade.userTwo.preferences.bio}</p>
                        </div>
                    </div>
                    <div class="su-card-middle">
                        <div class="su-card-games-list">
                        </div>
                    </div>
                    <div class="su-card-bottom">
                        <a href="#" id="remove" class="btn btn-primary unfriend-link">Remove Comrade</a>
                    </div>
                </div>
            `);
            for (let userTwoGame of comrade.userTwo.preferences.games) {
                $(`#card`).children(`[data-comrade-id="${comrade.id}"]`).find(".su-card-games-list").append(`
                    <div class="su-card-game">
                        <img class="su-card-game-image" src="${userTwoGame.artwork}" alt="${userTwoGame.title} icon">
                    </div>
                `);
            }
        }
    }


    document.getElementById('arrowsRect').addEventListener('click', async function(e){
        e.preventDefault();
        if (e.target && e.target.classList.contains("arrow-right")) {
            let cardList = document.getElementById('card');
            // get the current data-left value of the card list
            let currentLeft = cardList.dataset.left;
            // count the amount of cards in the card list
            let cardCount = cardList.childElementCount;
            let maxClicks = cardCount - 2;
            // if the current data-left value is less than the (maxClicks * 460), decrease the data-left value by 460
            if (currentLeft == -(maxClicks * 460)) {
                //do nothing
            }
            else {
                // set the cardList to have a css left value of 460px less than the current left value
                cardList.style.left = `${parseInt(currentLeft) - 460}px`;
                // set the data-left value of the card list to the new left value
                cardList.dataset.left = `${parseInt(currentLeft) - 460}`;
            }
        }
        if (e.target && e.target.classList.contains("arrow-left")){
            let cardList = document.getElementById('card');
            // get the current data-left value of the card list
            let currentLeft = cardList.dataset.left;
            if (currentLeft == "0") {
                //do nothing
            }
            else {
                // set the cardList to have a css left value of 460px less than the current left value
                cardList.style.left = `${parseInt(currentLeft) + 460}px`;
                // set the data-left value of the card list to the new left value
                cardList.dataset.left = `${parseInt(currentLeft) + 460}`;
            }
        }
    })



    document.getElementById("card").addEventListener('click', async function(e){
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

    printUserCards(getComrades());

});