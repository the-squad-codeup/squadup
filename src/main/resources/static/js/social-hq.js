import {Utils} from "./utils.js"
$(function () {
    console.log("Inside social-hq.js");
    let backgroundUrl = `${window.location.protocol}//${window.location.host}/background-image`;
    $(".squad-modal").css("background", `url('${backgroundUrl}') no-repeat center center`);
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////// Recruits Section ///////////////////////////////////////////////////
    const csrfToken = $("meta[name='_csrf']").attr("content");

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

    async function getSquad(squadId) {
        return await fetch(`${Utils.url()}squads/${squadId}/info`).then(res => res.json());
    }

    async function getSquadOwner(squadId) {
        return await fetch(`${Utils.url()}squads/${squadId}/owner`).then(res => res.json());
    }

    async function getComrades(){
        return await fetch(`${Utils.url()}comrades/all`).then(res => res.json());
    }

    async function getCurrentUser() {
        return await fetch(`${Utils.url()}user/get`).then(res => res.json());
    }

    async function getUser(userId) {
        return await fetch(`${Utils.url()}user/${userId}/info`).then(res => res.json());
    }

    async function getCurrentInvitees(squadId) {
        return await fetch(`${Utils.url()}invites/${squadId}/current`).then(res => res.json());
    }

    async function getPossibleInvitees(squadId) {
        return await fetch(`${Utils.url()}invites/${squadId}/possible`).then(res => res.json());
    }

    async function sendInvite(squadId, userId) {
        const fetchOptions = {
            method: 'POST',
            headers: {
                'X-CSRF-TOKEN' : csrfToken
            }
        }
        return await fetch(`${Utils.url()}squads/${squadId}/invite/${userId}`, fetchOptions).then(res => res.json());
    }

    async function createSquad() {
        let invitees = [];
        console.log($(".add-modal-squad-invitee-wrapper"));
        for(let $invitee of $(".add-modal-squad-invitee-wrapper")) {
            console.log($invitee)
            invitees.push($invitee.attributes[1].value);
        };
        let squadToCreate = {
            name: $(".add-modal-squad-name-input").val(),
            inviteeIds: invitees,
            squadPictureId: $(".add-modal-squad-img").attr("data-squad-img-id")
        };
        let fetchOptions = {
            method: 'POST',
            headers: {
                'Content-Type' : 'application/json',
                'X-CSRF-TOKEN' : csrfToken
            },
            body: JSON.stringify(squadToCreate)
        };
        console.log(fetchOptions);
        let addedSquad = await fetch(`${Utils.url()}squads/create/new`, fetchOptions).then(res => res.json());
        console.log(addedSquad);
        printNewSquad(addedSquad);
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
        console.log(squads);
        $("#squads-content").empty();
        for(let squad of squads){
            $("#squads-content").append(`
                <div data-squad-id="${squad.id}" class="solo-squad">
                    <h5>${squad.name}</h5>
                    <img class="solo-squad-img rgb" src="${squad.squadPicture.url}">
                </div>
            `)
        }
        $("#squads-content").append(`
            <div id="add-squad-wrapper" class="solo-squad">
                <h5>Add Squad</h5>
                <img class="solo-squad-img rgb" src="https://cdn.filestackcontent.com/YmC6UtutQsiTT2tYduKI">
            </div>
        `);
    }

    async function printNewSquad(squad) {
        $("#squads-content").prepend(`
            <div data-squad-id="${squad.id}" class="solo-squad">
                <h5>${squad.name}</h5>
                <img class="solo-squad-img rgb" src="${squad.squadPicture.url}">
            </div>
        `);
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
            <div class="add-modal-title rgb">
                Create A Squad
            </div>
            <div class="add-modal-top">
                <div class="add-modal-squad-name-label">
                    Squad Name:
                </div>
                <input class="add-modal-squad-name-input" type="text">
                <div class="add-modal-squad-img-wrapper">
                    <img class="add-modal-squad-img" src="https://cdn.filestackcontent.com/Humw6OOXTemRtPob8kJB">
                </div>
            </div>
            <div class="add-modal-squad-invites-wrapper">
                <div class="add-modal-squad-comrades-wrapper user-wrapper">
                    <div class="add-modal-squad-comrades-title invite-title">
                        Invite Comrades:
                    </div>
                    <div class="add-modal-squad-comrades-mask invite-mask">
                        <div class="add-modal-squad-comrades invite-container">
                            
                        </div>
                    </div>
                </div>
                <div class="add-modal-squad-invitees-wrapper user-wrapper">
                    <div class="add-modal-squad-invitees-title invite-title">
                        Current Invites
                    </div>
                    <div class="add-modal-squad-invitees-mask invite-mask">
                        <div class="add-modal-squad-invitees invite-container">
                            <div class="add-empty-invitees" style="display: flex; width: 100%; justify-content: center;">
                                Choose A Comrade To Invite
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="add-modal-squad-create-btn-wrapper">
                <button class="add-modal-squad-create-btn">Create Squad</button>
            </div>
        `);
        for(let comrade of comrades) {
            $(".add-modal-squad-comrades").append(`
                <div class="add-modal-squad-comrade-wrapper single-user-wrapper" data-user-id="${comrade.userTwo.id}">
                    <div class="add-modal-squad-comrade-username">
                        ${comrade.userTwo.username}
                    </div>
                    <div class="add-modal-squad-comrade-img-wrapper">
                        <img class="add-modal-squad-comrade-img modal-user-img" src="${comrade.userTwo.profilePicture.url}">
                    </div>
                </div>
            `);
        }
    }

    async function printSquadModalOwner(squadId) {
        let squad = await getSquad(squadId);
        console.log(squad);
        let squadMemberIds = squad.members.map(member => member.id);
        let squadInvitees = await getCurrentInvitees(squadId);
        let squadInviteIds = squadInvitees.map(invitee => invitee.id);
        let usersToInvite = await getPossibleInvitees(squadId);
        let trimmedUsersToInvite = [];
        for(let user of usersToInvite) {
            if(!(squadMemberIds.includes(user.id) || squadInviteIds.includes(user.id))) {
                trimmedUsersToInvite.push(user);
            }
        }
        for(let member of squad.members) {
            $(".squad-modal").empty().append(`
            <div hidden id="modal-squad-info" data-squad-id="${squad.id}"></div>
            <div class="modal-top">
                <div class="modal-squad-img-wrapper">
                    <img class="modal-squad-img squad-image" src="${squad.squadPicture.url}">
                </div>
                <div class="modal-title rgb">
                    ${squad.name}
                </div>
                <div class="modal-squad-chat-btn-wrapper">
                    <button class="modal-squad-chat-btn">Chat</button>
                </div>
            </div>
            <div class="modal-squad-members-wrapper user-wrapper">
                <div class="modal-squad-members-title invite-title">
                    Members:
                </div>
                <div class="modal-squad-members-mask invite-mask">
                    <div class="modal-squad-members invite-container">
                        
                    </div>
                </div>
            </div>
            <div class="modal-squad-invites-wrapper">
                <div class="modal-squad-comrades-wrapper user-wrapper">
                    <div class="modal-squad-comrades-title invite-title">
                        Invite Comrades:
                    </div>
                    <div class="modal-squad-comrades-mask invite-mask">
                        <div class="modal-squad-comrades invite-container">
                            
                        </div>
                    </div>
                </div>
                <div class="modal-squad-invitees-wrapper user-wrapper">
                    <div class="modal-squad-invitees-title invite-title">
                        Current Invites
                    </div>
                    <div class="modal-squad-invitees-mask invite-mask">
                        <div class="modal-squad-invitees invite-container">
                            <div class="empty-invitees" style="display: flex; width: 100%; justify-content: center;">
                                Choose A Comrade To Invite
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-squad-btn-wrapper">
                <button class="modal-squad-disband-btn">Disband Squad</button>
            </div>
        `);
            $(".modal-squad-members").append(`
                <div class="modal-squad-member-wrapper single-user-wrapper" data-user-id="${member.id}">
                    <div class="modal-squad-comrade-username">
                        ${member.username}
                    </div>
                    <div class="modal-squad-comrade-img-wrapper">
                        <img class="modal-squad-comrade-img modal-user-img" src="${member.profilePicture.url}">
                    </div>
                </div>
            `);
        }
        for(let user of trimmedUsersToInvite) {
            $(".modal-squad-comrades").append(`
                <div class="modal-squad-comrade-wrapper single-user-wrapper" data-user-id="${user.id}">
                    <div class="modal-squad-comrade-username">
                        ${user.username}
                    </div>
                    <div class="modal-squad-comrade-img-wrapper">
                        <img class="modal-squad-comrade-img modal-user-img" src="${user.profilePicture.url}">
                    </div>
                </div>
            `);
        }
        for(let invitee of squadInvitees) {
            if($(".modal-squad-invitees").find(".empty-invitees").length){
                $(".modal-squad-invitees").empty();
            }
            $(".modal-squad-invitees").append(`
                <div class="modal-squad-invitee-wrapper single-user-wrapper" data-user-id="${invitee.id}">
                    <div class="modal-squad-invitee-username">
                        ${invitee.username}
                    </div>
                    <div class="modal-squad-invitee-img-wrapper">
                        <img class="modal-squad-invitee-img modal-user-img" src="${invitee.profilePicture.url}">
                    </div>
                </div>
            `);
        }
    }

    async function printSquadModal(squadId) {
        let squad = await getSquad(squadId);
        console.log(squad);

        $(".squad-modal").empty().append(`
            <div hidden id="modal-squad-info" data-squad-id="${squad.id}"></div>
            <div class="modal-top">
                <div class="modal-squad-img-wrapper">
                    <img class="modal-squad-img" src="${squad.squadPicture.url}">
                </div>
                <div class="modal-title rgb">
                    ${squad.name}
                </div>
                <div class="modal-squad-chat-btn-wrapper">
                    <button class="modal-squad-chat-btn">Chat</button>
                </div>
            </div>
            <div class="modal-squad-members-wrapper user-wrapper">
                <div class="modal-squad-members-title invite-title">
                    Members:
                </div>
                <div class="modal-squad-members-mask invite-mask">
                    <div class="modal-squad-members invite-container">
                        
                    </div>
                </div>
            </div>
        `);
        for(let member of squad.members) {
            $(".modal-squad-members").append(`
                <div class="modal-squad-member-wrapper single-user-wrapper" data-user-id="${member.id}">
                    <div class="modal-squad-comrade-username">
                        ${member.username}
                    </div>
                    <div class="modal-squad-comrade-img-wrapper">
                        <img class="modal-squad-comrade-img modal-user-img" src="${member.profilePicture.url}">
                    </div>
                </div>
            `);
        }
    }


    async function printAddModalInvitee(userId) {
        let user = await getUser(userId);
        if($(".add-modal-squad-invitees").find(".add-empty-invitees").length) {
            $(".add-modal-squad-invitees").empty()
        }
        $(".add-modal-squad-invitees").prepend(`
            <div class="add-modal-squad-invitee-wrapper single-user-wrapper" data-user-id="${user.id}">
                    <div class="add-modal-squad-invitee-username">
                        ${user.username}
                    </div>
                    <div class="add-modal-squad-invitee-img-wrapper">
                        <img class="add-modal-squad-invitee-img modal-user-img" src="${user.profilePicture.url}">
                    </div>
                </div>
        `);
    }

    async function printModalInvitee(userId) {
        let user = await getUser(userId);
        if($(".modal-squad-invitees").find(".empty-invitees").length) {
            $(".modal-squad-invitees").empty()
        }
        $(".modal-squad-invitees").prepend(`
            <div class="modal-squad-invitee-wrapper single-user-wrapper" data-user-id="${user.id}">
                    <div class="modal-squad-invitee-username">
                        ${user.username}
                    </div>
                    <div class="modal-squad-invitee-img-wrapper">
                        <img class="modal-squad-invitee-img modal-user-img" src="${user.profilePicture.url}">
                    </div>
                </div>
        `);
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
            if($(this).parent("#add-squad-wrapper").length) {
                printAddSquadModal();
            } else {
                let user = await getCurrentUser();
                let owner = await getSquadOwner($(this).parent().attr("data-squad-id"));
                if(user.id === owner.id) {
                    printSquadModalOwner($(this).parent().attr("data-squad-id"));
                } else {
                    printSquadModal($(this).parent().attr("data-squad-id"))
                }
            }
            showModal();
        })
        .on("click", ".squad-overlay", hideModal)
        .on("click", ".add-modal-squad-comrade-img", function() {
            printAddModalInvitee($(this).parent().parent().attr("data-user-id"));
            $(this).parent().parent().remove();
        })
        .on("click", ".modal-squad-comrade-img", async function() {
            await sendInvite($("#modal-squad-info").attr("data-squad-id"), $(this).parent().parent().attr("data-user-id"))
            printModalInvitee($(this).parent().parent().attr("data-user-id"));
            $(this).parent().parent().remove();
        })
        .on("click", ".add-modal-squad-create-btn", async function() {
            createSquad();
            hideModal();
        })
        .on("click", ".modal-squad-chat-btn", function() {

        })
    ;
});