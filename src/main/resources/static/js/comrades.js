import {Utils} from "./utils"

$(function() {

    const csrfToken = $("meta[name='_csrf']").attr("content")

    console.log("Inside comrades.js");
    async function printUserCards(recruits) {
        recruits = await recruits;
        console.log(recruits);
        $("#card").html('');
        for (let recruit of recruits) {
            $(`#card`).append(`
                <div class=" card h-100 row-cols-2" data-recruit-id="${recruit.id}">
                <img class="card-img-top" src="https://i.imgur.com/0Z0Z0Z0.jpg" alt="user profile picture">
                        <div class="card-body">
                            <h4 class="card-title">${recruit.userTwo.username}</h4>
                            <p class="card-text">${recruit.userTwo.username}'s Bio: ${recruit.userTwo.preferences.bio}</p>
                `);
            for (let userTwoGame of recruit.userTwo.userTwoGames) {
                $(`#card .card[data-recruit-id="${recruit.id}"] .card-body div`).append(`
                <div>
                    <img class="card-img-top" src="${userTwogame.game.image}" alt="${userTwogame.game.name} icon">
                </div>
            `);
            }
        }
    }

    async function getComrades(){
        let results = await fetch(`${Utils.url}recruits/all`);
        let data = await results.json();
        console.log(data);
        return date;
    }

    printUserCards(getComrades());
//                                ^^^ask stephen about this: should it be recruits/{id}/accept^^^

});