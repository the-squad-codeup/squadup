import { Utils } from "./utils.js";

$(function() {
    console.log("Inside squad-chat.js");

    const SquadChat = {
        csrfToken: $("meta[name='_csrf']").attr("content"),
        squadId: $("#squad-title").attr("data-squad-id"),
        messageBoxDiv: $("#chat-messages-div"),
        initialize() {
            Events.initialize();
        },
        async inviteUser() {
            let userId = $("#invite-users-select").find(":selected").attr("data-user-id");
            let invitedUser = await Fetch.Post.sendInvite(userId);
            Print.removeUserFromInviteList(invitedUser);
        },
        scrollToBottom() {
            document.getElementById("chat-messages-div-wrapper").scrollTo(0, document.getElementById("chat-messages-div").scrollHeight);
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
                    <div class="squad-member-wrapper">
                        <img class="squad-member-img" src="${member.profilePicture.url}">
                        <div class="squad-member-name">${member.username}</div>
                    </div>
                `);
            }
        },
        removeUserFromInviteList(user) {
            $("#invite-users-select").children(`[data-user-id="${user.id}"]`).remove();
        },
        async messageHistory() {
            let messages = await Fetch.Get.squadMessages();
            for(let message of messages) {
                if(message.content != null){
                    this.singleMessage(message);
                }
            }
            SquadChat.scrollToBottom();
        },
        singleMessage(message) {
            SquadChat.messageBoxDiv.append(`
                <div class="single-message-wrapper" data-user-id="${message.sender.id}">
                    <div class="message-sender-img-wrapper">
                        <img class="message-sender-img" src="${message.sender.profilePicture.url}">
                    </div>
                    <div class="single-message-content-wrapper">
                        <div class="single-message-timestamp">
                            ${message.timestamp}
                        </div>
                        <div class="single-message-content">
                            ${message.content}
                        </div>
                    </div>
                    <div class="message-options hidden">
                        <img class="message-button edit-message-button" src="/Icons/edit.png" alt="">
                        <img class="message-button delete-message-button" src="/Icons/trash.png" alt="">
                    </div>
                </div>
            `);
        },
        async squadPicture() {
            let squadPicture = await Fetch.Get.squadPicture();
            $('.squad-image').css('background-image', `url("${squadPicture.url}")`);
        },
        async squadUserDetails() {
            let squadOwner = await Fetch.Get.squadOwner();
            let currentUser = await Fetch.Get.currentUser();
            let isOwner = squadOwner.id === currentUser.id
            $("body").prepend(`
                <div hidden id="user-details-div" data-user-id="${currentUser.id}" data-is-owner="${isOwner}">
            `);
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
            },
            async squadMessages() {
                return await fetch(`${Utils.url()}squads/${SquadChat.squadId}/messages`).then(res => res.json());
            },
            async squadPicture() {
                return await fetch(`${Utils.url()}squads/${SquadChat.squadId}/picture`).then(res => res.json());
            },
            async squadOwner() {
                return await fetch(`${Utils.url()}squads/${SquadChat.squadId}/owner`).then(res => res.json());
            },
            async currentUser() {
                return await fetch(`${Utils.url()}user/get`).then(res => res.json());
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
        async initialize() {
            await $(window)
                .ready(async function() {
                    Print.inviteOptions();
                    Print.currentSquadMembers();
                    Print.messageHistory();
                    Print.squadPicture();
                    await Print.squadUserDetails();
                })
            ;
            $(document)
                .on("click", "#invite-users-button", SquadChat.inviteUser)
                .on("mouseenter", ".single-message-wrapper", function() {
                    if($("#user-details-div").attr("data-user-id") === $(this).attr("data-user-id")) {
                        $(this).find(".message-options").removeClass("hidden");
                    }
                })
                .on("mouseleave", ".single-message-wrapper", function () {
                    if($("#user-details-div").attr("data-user-id") === $(this).attr("data-user-id")) {
                        $(this).find(".message-options").addClass("hidden");
                    }
                })
                .on("mouseenter", ".squad-image", function() {
                    $(this).addClass("darken");
                    $("#upload-squad-picture").removeClass("hidden");
                })
                .on("mouseleave", ".squad-image", function() {
                    $(this).removeClass("darken");
                    $("#upload-squad-picture").addClass("hidden");
                })
                .on("click", ".edit-message-button", function() {

                })
                .on("click", ".delete-message-button", function() {

                })
            ;
        }
    };

    SquadChat.initialize();
});