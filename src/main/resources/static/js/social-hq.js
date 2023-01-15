import {Utils} from "./utils.js"
$(function () {
    console.log("Inside social-hq.js");
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////// Recruits Section ///////////////////////////////////////////////////
    const csrfToken = $("meta[name='_csrf']").attr("content")

    console.log("Inside recruits.js");
    async function printUserCards(recruits) {
        recruits = await recruits;
        recruits = recruits.sort((prev, current) => (new Date(prev.dateRecruited)) - (new Date(current.dateRecruited)))
        console.log(recruits);
        $("#card").html('');
        for (let recruit of recruits) {
            $(`#card`).append(`
                <div class="su-card rgb" data-recruit-id="${recruit.id}">
                    <div class="su-card-top">
                        <div class="su-card-col su-card-col-shrink">
                            <img class="card-img-top" src="${recruit.userTwo.profilePicture.url}" alt="user profile picture">
                            <h4 class="card-title">${recruit.userTwo.username}</h4>
                        </div>
                        <div class="su-card-col">
                            <p class="card-text">${recruit.userTwo.preferences.bio}</p>
                        </div>
                    </div>
                    <div class="su-card-middle">
                        <div class="su-card-games-list">
                        </div>
                    </div>
                    <div class="su-card-bottom">
                        <a href="#" id="accept" class="btn btn-outline-success accept-link">Accept</a>
                        <a href="#" id="reject" class="btn btn-outline-danger reject-link">Reject</a>
                    </div>
                </div>
            `);
            for (let userTwoGame of recruit.userTwo.preferences.games) {
                $(`#card`).children(`[data-recruit-id="${recruit.id}"]`).find(".su-card-games-list").append(`
                    <div class="su-card-game">
                        <img class="su-card-game-image" src="${userTwoGame.artwork}" alt="${userTwoGame.title} icon">
                    </div>
                `);
            }
        }
    }


    $(document).on('click', ".arrow", async function(e){
        e.preventDefault();
        if (e.target && e.target.classList.contains("arrow-right")) {
            let cardList = document.getElementById('card');
            // get the current data-left value of the card list
            let currentLeft = cardList.dataset.left;
            // count the amount of cards in the card list
            let cardCount = cardList.childElementCount;
            let maxClicks = cardCount - 1;
            // if the current data-left value is less than the (maxClicks * 450), decrease the data-left value by 450
            if (currentLeft == -(maxClicks * 450)) {
                //do nothing
            }
            else {
                // set the cardList to have a css left value of 450px less than the current left value
                cardList.style.left = `${parseInt(currentLeft) - 450}px`;
                // set the data-left value of the card list to the new left value
                cardList.dataset.left = `${parseInt(currentLeft) - 450}`;
            }
        }
        if (e.target && e.target.classList.contains("arrow-left")){
            let cardList = document.getElementById('card');
            // get the current data-left value of the card list
            let currentLeft = cardList.dataset.left;
            if (currentLeft == "0") {
                // set the cardList to have a css left value of 450px less than the current left value
                // cardList.style.left = `${parseInt(currentLeft) + 450}px`;
            }
            else {
                // set the cardList to have a css left value of 450px less than the current left value
                cardList.style.left = `${parseInt(currentLeft) + 450}px`;
                // set the data-left value of the card list to the new left value
                cardList.dataset.left = `${parseInt(currentLeft) + 450}`;
            }
        }
    })



    document.getElementById("card").addEventListener('click', async function (e) {
        e.preventDefault();
        if (e.target && e.target.classList.contains("accept-link")) {
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

        if (e.target && e.target.classList.contains("reject-link")) {
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

    printUserCards(getAllRecruits());




    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////// Squads and Comrades Section /////////////////////////////////////////////
    async function getSquads(){
        return await fetch(`${Utils.url()}user/squads`).then(res => res.json());
    }

    async function getComrades(){
        return await fetch(`${Utils.url()}comrades/all`).then(res => res.json())
    }

    function showModal() {
        $(".squad-modal").removeClass("hidden");
        $(".squad-overlay").removeClass("hidden");
    }

    function hideModal() {
        $(".squad-modal").addClass("hidden");
        $(".squad-overlay").addClass("hidden");
    }

    async function printSquads(){
        let squads = await getSquads();
        console.log(squads)
        $("#squads-content").empty().append(`
            <div id="add-squad-wrapper" class="solo-squad">
                <h5>Add Squad</h5>
                <img class="solo-squad-img rgb" src="https://cdn.filestackcontent.com/Humw6OOXTemRtPob8kJB">
            </div>
        `);
        for(let squad of squads){
            $("#squads-content").append(`
                <div data-squad-id="${squad.id}" class="solo-squad">
                    <h5>${squad.name}</h5>
                    <img class="solo-squad-img rgb" src="${squad.squadPicture.url}">
                </div>
            `)
        }
    }


    async function printComrades(){
        let comrades = await getComrades();
        console.log(comrades)
        for(let comrade of comrades){
            $("#comrades-content").append(`
                <div data-comrade-id="${comrade.id}" class="solo-com">
                    <h5>${comrade.userTwo.username}</h5>
                    <img class="solo-com-img rgb" src="${comrade.userTwo.profilePicture.url}">
                </div>
            `);
        }
    }

    async function printAddSquadModal() {
        let comrades = await getComrades();
        $(".squad-modal").empty().append(`
            <button class="modal-exit-btn">X</button>
            <div class="modal-squad-name-wrapper">
                <div class="modal-squad-name-label">
                    Squad Name:
                </div>
                <input class="modal-squad-name-input" type="text">
            </div>
            <div class="modal-squad-picture-wrapper">
                <img class="modal-squad-picture" src="https://cdn.filestackcontent.com/YmC6UtutQsiTT2tYduKI">
            </div>
            <div class="modal-squad-invites-wrapper">
                <div class="modal-squad-invitees-mask">
                    <div class="modal-squad-invitees">
                        
                    </div>
                </div>
                <div class="modal-squad-comrades-mask">
                    <div class="modal-squad-comrades">
                    
                    </div>
                </div>
            </div>
            <div class="modal-squad-create-btn-wrapper">
                <button class="modal-squad-create-btn">Create Squad</button>
            </div>
        `);
        for(let comrade of comrades) {
            $(".modal-squad-comrades").append(`
                <div class="modal-squad-comrade-wrapper" data-user-id="${comrade.userTwo.id}">
                    <div class="modal-squad-comrade-username">
                        ${comrade.userTwo.username}
                    </div>
                    <div class="modal-squad-comrade-img-wrapper">
                        <img class="modal-squad-comrade-img" src="${comrade.userTwo.profilePicture.url}">
                    </div>
                </div>
            `);
        }
    }


    printSquads();
    printComrades();

    $(document)
        .on("click", ".solo-com-img", function () {
            window.location.href=`${Utils.url()}profile/${$(this).parent().attr("data-comrade-id")}/comrade`;
        })
        .on("click", ".card-img-top", function() {
            window.location.href=`${Utils.url()}profile/${$(this).parent().parent().parent().attr("data-recruit-id")}/recruit`;
        })
        .on("click", ".solo-squad-img", async function() {
            showModal();
            if($(this).parent(`[id="add-squad-wrapper"]`)) {
                printAddSquadModal();
            } else {
                printSquadModal();
            }
        })
    ;
});