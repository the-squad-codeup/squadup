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
                 <div class="card h-100 row-cols-3" data-comrade-id="${comrade.id}">
                    <img class="card-img-top" src="https://i.imgur.com/0Z0Z0Z0.jpg" alt="user profile picture">
                    <div class="card-body">
                        <h4 class="card-title">${comrade.userTwo.username}</h4>
                        <p class="card-text">${comrade.userTwo.username}'s Bio: ${comrade.userTwo.preferences.bio}</p>
                    </div>
                    <div>
                        <a href="#" class="btn btn-primary unfriend-link">Remove Comrade</a>
                    </div>
                </div>
            `);
            for (let userTwoGame of comrade.userTwo.preferences.games) {
                $(`#card`).children(`[data-comrade-id="${comrade.id}"]`).children(".card-body").append(`
                    <div>
                        <img class="card-img-top" src="${userTwoGame.artwork}" alt="${userTwoGame.title} icon">
                    </div>
                `);
            }
        }
    }



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