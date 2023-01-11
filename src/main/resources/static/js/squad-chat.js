import { Utils } from "./utils.js";

$(function() {

    const SquadChat = {
        csrfToken: $("meta[name='_csrf']").attr("content"),
        squadId: $("#squad-title").attr("data-squad-id"),
        stompClient: null,
        topic: null,
        currentSubscription: null,
        messageInputBox: $("#chat-text-input"),
        messageOutputBox: $("#chat-messages-div"),
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
        },
        recentMessage(message) {
            let lastMessage = SquadChat.messageOutputBox.children().last();
            if(lastMessage[0].previousElementSibling != null && lastMessage[0].attributes[1].nodeValue === lastMessage[0].previousElementSibling.attributes[1].nodeValue) {
                let prevMessageTime = Utils.dateStringToJSDate(lastMessage.prev().find(".single-message-timestamp").text().trim()).getTime();
                let thisMessageTime = Utils.dateStringToJSDate(message.timestamp).getTime();
                return thisMessageTime - prevMessageTime < 60000;
            }
            return false;
        }
    };

    const Socket = {
        connect() {
            let socket = new SockJS("/secured/squad-sock");
            SquadChat.stompClient = Stomp.over(socket);
            SquadChat.stompClient.connect({'X-CSRF-TOKEN': $("meta[name='_csrf']").attr("content")}, this.onConnected, this.onError);
        },
        onConnected() {
            Socket.enterSquad(SquadChat.squadId);
        },
        onError(error) {
        },
        enterSquad(squadId) {
            SquadChat.topic = `/secured/squad-app/squad-chat/${squadId}`;
            SquadChat.currentSubscription = SquadChat.stompClient.subscribe(`/secured/squad-room/${squadId}`, this.onMessageReceived);
            SquadChat.stompClient.send(`${SquadChat.topic}/add-user`, {}, JSON.stringify({messageType: 'JOIN'}));
        },
        leaveSquad(squadId) {
            SquadChat.topic = `/secured/squad-app/squad-chat/${squadId}`;
            SquadChat.stompClient.send(`${SquadChat.topic}add-user`, {}, JSON.stringify({messageType: 'LEAVE'}));
            SquadChat.currentSubscription = SquadChat.stompClient.unsubscribe();
        },
        sendMessage() {
            let messageContent = SquadChat.messageInputBox.val();
            SquadChat.topic = `/secured/squad-app/squad-chat/${SquadChat.squadId}`;
            if(messageContent && SquadChat.stompClient) {
                let chatMessage = {
                    content: messageContent,
                    messageType: 'CHAT'
                };
                SquadChat.stompClient.send(`${SquadChat.topic}/send`, {}, JSON.stringify(chatMessage));
            }
            SquadChat.messageInputBox.val("");
        },
        async onMessageReceived(payload) {
            let message = JSON.parse(payload.body);
            if(message.messageType === 'JOIN') {
                Print.joinMessage(message);
            } else if(message.messageType === 'LEAVE') {
                Print.leaveMessage(message);
            } else {
                await Print.singleMessage(message);
                SquadChat.scrollToBottom();
            }
        }
    };

    const Print = {
        joinMessage(message) {
        },
        leaveMessage(message) {
        },
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
            SquadChat.messageOutputBox.append(`
                <div class="single-message-wrapper" data-user-id="${message.sender.id}">
                    <div class="message-sender-img-wrapper">
                        <img class="message-sender-img soft-hidden" src="${message.sender.profilePicture.url}">
                    </div>
                    <div class="single-message-content-wrapper">
                        <div class="single-message-top-wrapper hidden">
                            <div class="single-message-username">
                                ${message.sender.username}
                            </div>
                            <div class="single-message-timestamp">
                                ${message.timestamp}
                            </div>
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
            if(!SquadChat.recentMessage(message)) {
                let children = SquadChat.messageOutputBox.children();
                children.last().find(".message-sender-img").removeClass("soft-hidden");
                children.last().find(".single-message-top-wrapper").removeClass("hidden");
            }
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
            await Socket.connect();
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
                .on("click", "#chat-send-button", Socket.sendMessage)
                .on("keyup", function(e) {
                    if($("#chat-text-input").is(":focus") && e.key === "Enter") {
                        $("#chat-send-button").trigger("click");
                    }
                })
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