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
                 <div class="card h-100 row-cols-3" data-recruit-id="${recruit.id}">
                    <img class="card-img-top" src="https://i.imgur.com/0Z0Z0Z0.jpg" alt="user profile picture">
                    <div class="card-body">
                        <h4 class="card-title">${recruit.userTwo.username}</h4>
                        <p class="card-text">${recruit.userTwo.username}'s Bio: ${recruit.userTwo.preferences.bio}</p>
                    </div>
                    <div>
                        <a href="#" className="btn btn-primary unfriend-link">Remove Recruit</a>
                    </div>
                </div>
            `);
            for (let userTwoGame of recruit.userTwo.preferences.games) {
                $(`#card`).children(`[data-recruit-id="${recruit.id}"]`).children(".card-body").append(`
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
            let unfriend = e.target.parentElement.parentElement.getAttribute("data-recruit-id");
            console.log(unfriend);
            const fetchOptions = {
                method: 'POST',
                headers: {
                    'X-CSRF-TOKEN' : csrfToken
                }
            }
            let results = await fetch(`${Utils.url()}comrades/${unfriend}/delete`, fetchOptions);
            let data = await results.json();
            console.log(data);
            e.target.parentElement.parentElement.remove();
        }



    })

    async function getComrades(){
        let results = await fetch(`${Utils.url}/comrades/{id}/delete`);
        let data = await results.json();
        console.log(data);
        return date;
    }

    printUserCards(getComrades());

});