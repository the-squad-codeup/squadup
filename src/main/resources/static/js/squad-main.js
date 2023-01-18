import { Utils } from "./utils.js";

$(function() {

    const SquadMain = {
        csrfToken: $("meta[name='_csrf']").attr("content"),
        initialize() {
            Events.initialize();
        },
        async acceptSquadInvite(squadId) {
            return await Fetch.Post.acceptSquadInvite(squadId);
        },
        async deleteSquad(squadId) {
            Fetch.Post.deleteSquad(squadId);
        }
    };

    const Fetch = {
        Get: {
            async currentInvites() {
                return await fetch(`${Utils.url()}invites/recipient`).then(res => res.json());
            },
            async mySquads() {
                return await fetch(`${Utils.url()}user/squads`).then(res => res.json());
            }
        },
        Post: {
            async acceptSquadInvite(squadId) {
                let fetchOptions = {
                    method: 'POST',
                    headers: {
                        'X-CSRF-TOKEN' : SquadMain.csrfToken
                    }
                };
                return await fetch(`${Utils.url()}invites/${squadId}/accept`, fetchOptions).then(res => res.json());
            },
            async deleteSquad(squadId) {
                let fetchOptions = {
                    method: 'POST',
                    headers: {
                        'X-CSRF-TOKEN' : SquadMain.csrfToken
                    }
                };
                return await fetch(`${Utils.url()}squads/${squadId}/delete`, fetchOptions).then(res => res.json());
            }
        }
    };

    const Print = {
        async currentSquads() {
            let squads = await Fetch.Get.mySquads();
            for(let squad of squads) {
                this.singleSquad(squad);
            }
        },
        // singleSquad(squad) {
        //     $("#active-squads-div").append(`
        //         <div data-squad-id="${squad.id}">
        //             <br>
        //             <a href="/squads/${squad.id}/chat">${squad.name}</a>
        //         </div>
        //     `);
        // },
        async currentInvites() {
            let invitesDiv = $("#squad-invites-div");
            let currentInvites = await Fetch.Get.currentInvites();
            for(let invite of currentInvites) {
                invitesDiv.append(`
                    <div data-squad-id="${invite.squad.id}">
                        <h5>${invite.squad.name}</h5>
                        <div class="invite-buttons">
                            <button class="accept-invite-button">Accept</button>
                            <button class="reject-invite-button">Reject</button>
                        </div>
                    </div>
                `);
            }
        }
    };

    const Events = {
        initialize() {
            $(window).ready(function() {
                Print.currentInvites();
                Print.currentSquads();
            });
            $(document)
                .on("click", ".accept-invite-button", async function() {
                    let squadId = $(this).parent().parent().attr("data-squad-id");
                    let acceptedSquad = await SquadMain.acceptSquadInvite(squadId);
                    $(this).parent().parent().remove();
                    Print.singleSquad(acceptedSquad);
                })
                .on("click", ".disband-squad-button", async function() {
                    let squadId = $(this).parent().attr("data-squad-id");
                    await SquadMain.deleteSquad(squadId);
                    $(this).parent().remove();
                })
            ;
        }
    };

    SquadMain.initialize();

});