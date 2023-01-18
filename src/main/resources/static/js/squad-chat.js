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
        scrollToBottom() {
            document.getElementById("chat-messages-div-wrapper").scrollTo(0, document.getElementById("chat-messages-div").scrollHeight);
        },
        recentMessage(message) {
            let lastMessage = SquadChat.messageOutputBox.children().last();
            if(lastMessage[0].previousElementSibling.attributes.length > 2) {
                if (lastMessage[0].previousElementSibling != null && lastMessage[0].attributes[2].nodeValue === lastMessage[0].previousElementSibling.attributes[2].nodeValue) {
                    let prevMessageTime = Utils.dateStringToJSDate(lastMessage.prev().find(".single-message-timestamp").attr("data-timestamp").trim()).getTime();
                    let thisMessageTime = Utils.dateStringToJSDate(message.timestamp).getTime();
                    return thisMessageTime - prevMessageTime < 60000;
                }
            }
            return false;
        },
        editMessage(messageDiv) {
            let messageId = messageDiv.attr("data-message-id");
            let content = messageDiv.find(".single-message-content").text().trim();
            Socket.editMessage(messageId, content);
        },
        deleteMessage(messageDiv) {
            let messageId = messageDiv.attr("data-message-id");
            Socket.deleteMessage(messageId);
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
        editMessage(messageId, messageContent) {
            SquadChat.topic = `/secured/squad-app/squad-chat/${SquadChat.squadId}`;
            if(messageContent && SquadChat.stompClient) {
                let editMessage = {
                    id: messageId,
                    content: messageContent,
                    messageType: 'EDIT'
                };
                SquadChat.stompClient.send(`${SquadChat.topic}/edit`, {}, JSON.stringify(editMessage));
            }
        },
        deleteMessage(messageId) {
            SquadChat.topic = `/secured/squad-app/squad-chat/${SquadChat.squadId}`;
            if(messageId && SquadChat.stompClient) {
                let deleteMessage = {
                    id: messageId,
                    messageType: 'DELETE'
                };
                SquadChat.stompClient.send(`${SquadChat.topic}/delete`, {}, JSON.stringify(deleteMessage));
            }
        },
        async onMessageReceived(payload) {
            let message = JSON.parse(payload.body);
            if(message.messageType === 'JOIN') {
                // await SquadChat.updateSquadMembers(message);
            } else if(message.messageType === 'LEAVE') {
                await Print.leaveMessage(message);
            } else if(message.messageType === 'EDIT') {
                await Print.editMessage(message);
            } else if(message.messageType === 'DELETE') {
                await Print.deleteMessage(message);
            } else {
                await Print.singleMessage(message);
                SquadChat.scrollToBottom();
            }
        }
    };

    const Print = {
        async joinMessage(message) {
            let squadMemberIds = (await Fetch.Get.squadMembers()).map(member => member.id);
            if(!squadMemberIds.includes(message.sender.id)) {
                message.content = `${message.sender.username} has joined the chat! Say hello :)`;
                message.sender.username = "";
                this.singleMessage(message);
            }
        },
        leaveMessage(message) {
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
                    <div class="single-message-wrapper" data-message-id="${message.id}" data-user-id="${message.sender.id}">
                        <div class="message-sender-img-wrapper">
                            <img class="message-sender-img soft-hidden" src="${message.sender.profilePicture.url}">
                        </div>
                        <div class="single-message-content-wrapper">
                            <div class="single-message-top-wrapper hidden">
                                <div class="single-message-username">
                                    ${message.sender.username}
                                </div>
                                <div class="single-message-timestamp" data-timestamp="${message.timestamp}">
                                    ${Utils.dateStringToJSDate(message.timestamp).toString().slice(0, 24)}
                                </div>
                            </div>
                            <div class="single-message-content" contenteditable="false">
                                ${message.content}
                            </div>
                        </div>
                        <div class="message-edit-button-wrapper clickable hidden">
                            <div class="message-edit-button"><i class="bi bi-send"></i></div>
                        </div>
                        <div class="message-options hidden">
                            <img class="message-button edit-message-button clickable" src="/Icons/edit.png" alt="">
                            <img class="message-button delete-message-button clickable" src="/Icons/trash.png" alt="">
                        </div>
                    </div>
                `);
                if(!SquadChat.recentMessage(message)) {
                    let children = SquadChat.messageOutputBox.children();
                    children.last().find(".message-sender-img").removeClass("soft-hidden");
                    children.last().find(".single-message-top-wrapper").removeClass("hidden");
                }
            if(message.edited) {
                SquadChat.messageOutputBox.find(`[data-message-id="${message.id}"]`).find(".single-message-content").append(`
                    <span class="was-edited">(edited)</span>
                `);
            }
        },
        async editMessage(message) {
            $("#chat-messages-div").find(`[data-message-id="${message.id}"]`).find(".single-message-content span").remove();
            $("#chat-messages-div").find(`[data-message-id="${message.id}"]`).find(".single-message-content").text(message.content);
            SquadChat.messageOutputBox.find(`[data-message-id="${message.id}"]`).find(".single-message-content").append(`
                    <span class="was-edited">(edited)</span>
                `)
            ;
        },
        async deleteMessage(message) {
            SquadChat.messageOutputBox.find(`[data-message-id="${message.id}"]`).remove();
        },
        async squadPicture() {
            let squadPicture = await Fetch.Get.squadPicture();
            $('.squad-image').attr('src', `${squadPicture.url}`);
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
            async editMessage(messageId, content) {
                const message = {
                    id: messageId,
                    content: content
                }
                const fetchOptions = {
                    method: 'POST',
                    headers: {
                        'X-CSRF-TOKEN' : SquadChat.csrfToken
                    },
                    body: JSON.stringify(message)
                }
                return await fetch(`${Utils.url()}messages/${SquadChat.squadId}/edit/${messageId}`, fetchOptions).then(res => res.json());
            }
        }
    }

    const Events = {
        async initialize() {
            await Socket.connect();
            await $(window)
                .ready(async function() {
                    await Print.currentSquadMembers();
                    await Print.messageHistory();
                    await Print.squadPicture();
                    await Print.squadUserDetails();
                    if($("#user-details-div").attr("data-is-owner") === "false") {
                        $(".squad-image").removeClass("clickable");
                    }
                })
            ;
            $(document)
                .on("click", "#chat-send-button", Socket.sendMessage)
                .on("click", ".message-edit-button", async function() {
                    $(this).parent().parent().find(".single-message-content").attr("contenteditable", "false");
                    await SquadChat.editMessage($(this).parent().parent());
                    $(this).parent().addClass("hidden");
                })
                .on("keydown", function(e) {
                    if($(".single-message-content").is(":focus") && e.key === "Enter") {
                        e.preventDefault();
                    }
                })
                .on("keyup", function(e) {
                    if($("#chat-text-input").is(":focus") && e.key === "Enter") {
                        $("#chat-send-button").trigger("click");
                    }
                    if($(".single-message-content div").is(":focus") && e.key === "Enter") {
                        $(":focus").parent().parent().find(".message-edit-button").trigger("click");
                    }
                })
                .on("mouseenter", ".single-message-wrapper", function() {
                    if(
                        $("#user-details-div").attr("data-user-id") === $(this).attr("data-user-id") &&
                        $(this).find(".message-edit-button-wrapper").hasClass("hidden")
                    ) {
                        $(this).find(".message-options").removeClass("hidden");
                    }
                })
                .on("mouseleave", ".single-message-wrapper", function () {
                    if($("#user-details-div").attr("data-user-id") === $(this).attr("data-user-id")) {
                        $(this).find(".message-options").addClass("hidden");
                    }
                })
                .on("mouseenter", ".squad-image", function() {
                    if($("#user-details-div").attr("data-is-owner") === "true"){
                        $(this).addClass("darken");
                        $("#upload-squad-picture").removeClass("hidden");
                    }
                })
                .on("mouseleave", ".squad-image", function() {
                    if($("#user-details-div").attr("data-is-owner") === "true") {
                        $(this).removeClass("darken");
                        $("#upload-squad-picture").addClass("hidden");
                    }
                })
                .on("click", ".edit-message-button", function() {
                    $(this).parent().parent().find(".message-edit-button-wrapper").removeClass("hidden");
                    $(this).parent().addClass("hidden");
                    $(this).parent().parent().find(".single-message-content span").remove();
                    $(this).parent().parent().find(".single-message-content").attr("contenteditable", "true").focus();
                })
                .on("click", ".delete-message-button", async function() {
                    await SquadChat.deleteMessage($(this).parent().parent());
                })
            ;
        }
    };

    SquadChat.initialize();
});