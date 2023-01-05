//================COMRADES FUNCTION==============================

import {Utils} from "./utils.js"

$(function() {

    const csrfToken = $("meta[name='_csrf']").attr("content")

    console.log("Inside comrades.js");

    async function printComCards(comrades) {

        comrades = await comrades;
        console.log(comrades);
        $("#card-com").html('');
        for (let comrade of comrades) {
            console.log("printing individual comrade: ");
            console.log(comrade);
            $(`#card-com`).append(`
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
                $(`#card-com`).children(`[data-comrade-id="${comrade.id}"]`).children(".card-body").append(`
                    <div>
                        <img class="card-img-top" src="${userTwoGame.artwork}" alt="${userTwoGame.title} icon">
                    </div>
                `);
            }
        }
    }


    document.getElementById("card-com").addEventListener('click', async function (e) {
        e.preventDefault();
        if (e.target && e.target.classList.contains("unfriend-link")) {
            let unfriend = e.target.parentElement.parentElement.getAttribute("data-comrade-id");
            console.log(unfriend);
            const fetchOptions = {
                method: 'POST',
                headers: {
                    'X-CSRF-TOKEN': csrfToken
                }
            }
            await fetch(`${Utils.url()}comrades/${unfriend}/delete`, fetchOptions);
            document.querySelector(`[data-comrade-id="${unfriend}"]`).remove();
        }


    })

    async function getComrades() {
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
        console.log(recruits);
        $("#card-rec").html('');
        for (let recruit of recruits) {
            $(`#card-rec`).append(`
                <div class="card h-100 row-cols-3" data-recruit-id="${recruit.id}">
                    <img class="card-img-top" src="https://i.imgur.com/0Z0Z0Z0.jpg" alt="user profile picture">
                    <div class="card-body">
                        <h4 class="card-title">${recruit.userTwo.username}</h4>
                        <p class="card-text">${recruit.userTwo.username}'s Bio: ${recruit.userTwo.preferences.bio}</p>
                    </div>
                    <div>
                        <a href="#"  class="btn btn-primary squadup-link">Accept</a>
                        <a href="#" class="btn btn-primary squaddown-link">Reject</a>
                    </div>
                </div>
            `);
            for (let userTwoGame of recruit.userTwo.preferences.games) {
                $(`#card-rec`).children(`[data-recruit-id="${recruit.id}"]`).children(".card-body").append(`
                    <div>
                        <img class="card-img-top" src="${userTwoGame.artwork}" alt="${userTwoGame.title} icon">
                    </div>
                `);
            }
        }
    }



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
