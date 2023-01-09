import { Utils } from "./utils.js";

$(function() {
    console.log("Inside squad-chat.js");

    const SquadChat = {
        csrfToken: $("meta[name='_csrf']").attr("content"),
        squadId: $("#squad-title").attr("data-squad-id"),
        initialize() {
            Events.initialize();
        },
        async inviteUser() {
            let userId = $("#invite-users-select").find(":selected").attr("data-user-id");
            let invitedUser = await Fetch.Post.sendInvite(userId);
            Print.removeUserFromInviteList(invitedUser);
        }
    };

    const Print = {
        async inviteOptions() {
            console.log("inside print inviteOptions");
            let squadMembers = await Fetch.Get.squadMembers();
            let squadInvites = await Fetch.Get.currentInvitees();
            let usersToInvite = await Fetch.Get.possibleInvitees();
            console.log("usersToInvite:");
            console.log(usersToInvite);
            for(let user of usersToInvite) {
                if(!squadMembers.includes(user) || !squadInvites.includes(user)) {
                    $("#invite-users-select").append(`
                        <option data-user-id="${user.id}">${user.username}</option>
                    `);
                }
            }
        },
        removeUserFromInviteList(user) {
            $("#invite-users-select").children(`[data-user-id="${user.id}"]`).remove();
        }
    };

    const Fetch = {
        Get: {
            async squadMembers() {
                return await fetch(`${Utils.url()}squads/${SquadChat.squadId}/members`).then(res => res.json());
            },
            async possibleInvitees() {
                return await fetch(`${Utils.url()}invites/${SquadChat.squadId}/possible`).then(res => res.json());
            },
            async currentInvitees() {
                return await fetch(`${Utils.url()}invites/${SquadChat.squadId}/current`).then(res => res.json());
            }
        },
        Post: {
            async sendInvite(userId) {
                const fetchOptions = {
                    method: 'POST',
                    headers: {
                        'X-CSRF-TOKEN' : SquadChat.csrfToken
                    }
                }
                return await fetch(`${Utils.url()}squads/${SquadChat.squadId}/invite/${userId}`, fetchOptions).then(res => res.json());
            }
        }
    }

    const Events = {
        initialize() {
            $(window).ready(Print.inviteOptions);
            $(document)
                .on("click", "#invite-users-button", SquadChat.inviteUser)
            ;
        }
    }

    SquadChat.initialize();
});