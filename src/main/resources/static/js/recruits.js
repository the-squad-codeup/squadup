import {Utils} from "./utils.js"

$(function() {

    const csrfToken = $("meta[name='_csrf']").attr("content")

    console.log("Inside recruits.js");
    async function printUserCards(recruits) {
        recruits = await recruits;
        console.log(recruits);
        $("#card").html('');
        for (let recruit of recruits) {
            $(`#card`).append(`
                <div class="card h-100 row-cols-1" data-recruit-id="${recruit.id}">
                    <img class="card-img-top" src="https://i.imgur.com/0Z0Z0Z0.jpg" alt="user profile picture">
                        <div class="card-body">
                            <h4 class="card-title">${recruit.userTwo.username}</h4>
                            <p class="card-text">${recruit.userTwo.username}'s Bio: ${recruit.userTwo.preferences.bio}</p>
                            `);
            for (let userTwoGame of recruit.userTwo.userTwoGames) {
                $(`#card .card[data-recruit-id="${recruit.id}"] .card-body div`).append(`
                    <img class="card-img-top" src="${userTwogame.game.image}" alt="${userTwogame.game.name} icon">
                            <div>
                                <a href="#"  class="btn btn-primary squadup-link">Accept</a>
                                <a href="#" class="btn btn-primary squaddown-link">Reject</a>
                            </div>
            `);
            }
        }
    }



    document.getElementById("card").addEventListener('click', async function (e) {
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
        let results = await fetch("http://localhost:8080/recruits/all");
        let data = await results.json();
        console.log(data);
        return data;
    }

    printUserCards(getAllRecruits());

});

