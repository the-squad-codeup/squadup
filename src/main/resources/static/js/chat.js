$(function() {
    console.log("Inside chat.js");

    const SquadChat = {
        stompClient: null,
        squadId: $("#squad-title").attr("data-squad-id"),
        topic: null,
        currentSubscription: null,
        messageTextBox: $("#chat-text-input"),
        squadChatMessagesBox: $("#chat-messages-div"),
        initialize() {
            Events.initialize();
            Print.messageHistory();
        },
        scrollToBottom() {
            document.getElementById("chat-messages-div-wrapper").scrollTo(0, document.getElementById("chat-messages-div").scrollHeight);
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
            console.log("Error connecting to stream. Error:");
            console.log(error);
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
            let messageContent = SquadChat.messageTextBox.val();
            SquadChat.topic = `/secured/squad-app/squad-chat/${SquadChat.squadId}`;
            if(messageContent && SquadChat.stompClient) {
                let chatMessage = {
                    content: messageContent,
                    messageType: 'CHAT'
                };
                SquadChat.stompClient.send(`${SquadChat.topic}/send`, {}, JSON.stringify(chatMessage));
            }
            SquadChat.messageTextBox.val("");
        },
        onMessageReceived(payload) {
            let message = JSON.parse(payload.body);
            if(message.messageType === 'JOIN') {
                Print.joinMessage(message);
            } else if(message.messageType === 'LEAVE') {
                Print.leaveMessage(message);
            } else {
                Print.chatMessage(message);
            }
        }
    };

    const Print = {
        joinMessage(message) {
            console.log(`${message.sender.username} has joined the chat!`);
        },
        leaveMessage(message) {
            console.log(`${message.sender.username} left :(`);
        },
        chatMessage(message) {
            SquadChat.squadChatMessagesBox.append(`
                <div class="single-message-wrapper">
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
                </div>
            `);
            SquadChat.scrollToBottom();
        },
        messageHistory() {

        }
    };

    const Events = {
        async initialize() {
            await Socket.connect();
            $(document)
                .on("click", "#chat-send-button", Socket.sendMessage)
                .on("keyup", function(e) {
                    if($("#chat-text-input").is(":focus") && e.key === "Enter") {
                        $("#chat-send-button").trigger("click");
                    }
                })
            ;
            window.onbeforeunload = function() {
                Socket.leaveSquad(SquadChat.squadId);
            }
        }
    }

    SquadChat.initialize();

});