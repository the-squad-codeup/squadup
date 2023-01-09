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
            let squadMemberIds = (await Fetch.Get.squadMembers()).map(member => member.id);
            let squadInviteIds = (await Fetch.Get.currentInvitees()).map(invitee => invitee.id);
            let usersToInvite = await Fetch.Get.possibleInvitees();
            for(let user of usersToInvite) {
                if(!(squadMemberIds.includes(user.id) || squadInviteIds.includes(user.id))) {
                    $("#invite-users-select").append(`
                        <option data-user-id="${user.id}">${user.username}</option>
                    `);
                }
            }
        },
        async currentSquadMembers() {
            let squadMembers = await Fetch.Get.squadMembers();
            for(let member of squadMembers) {
                $("#squad-users-div").append(`
                    <h2>${member.username}</h2>
                `);
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
            $(window).ready(function() {
                Print.inviteOptions();
                Print.currentSquadMembers();
            });
            $(document)
                .on("click", "#invite-users-button", SquadChat.inviteUser)
            ;
        }
    }

    SquadChat.initialize();
});