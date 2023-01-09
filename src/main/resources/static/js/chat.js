$(function() {
    console.log("Inside chat.js");


    ///////////////////////////// COPIED FROM CHAT-TEST/////////////////////////

    // const Chat = {
    //     stompClient: null,
    //     roomId: $("#room-id").text(),
    //     topic: null,
    //     currentSubscription: null,
    //     messageTextBox: $("#message-text")
    // }
    //
    // const Socket = {
    //     connect() {
    //         let socket = new SockJS("/secured/sock");
    //         Chat.stompClient = Stomp.over(socket);
    //         Chat.stompClient.connect({'X-CSRF-TOKEN': $("meta[name='_csrf']").attr("content")}, this.onConnected, this.onError);
    //     },
    //     onConnected() {
    //         console.log("inside onConnected");
    //         Socket.enterRoom(Chat.roomId);
    //     },
    //     onError(error) {
    //         console.log("Error connecting to stream. Error:");
    //         console.log(error);
    //     },
    //     enterRoom(roomId) {
    //         console.log("Inside enterRoom");
    //         Chat.topic = `/secured/app/chat/${roomId}`;
    //         Chat.currentSubscription = Chat.stompClient.subscribe(`/secured/room/${roomId}`, this.onMessageReceived);
    //         Chat.stompClient.send(`${Chat.topic}/add-user`, {}, JSON.stringify({messageType: 'JOIN'}));
    //     },
    //     sendMessage(event) {
    //         console.log("Inside sendMessage");
    //         let messageContent = Chat.messageTextBox.val();
    //         console.log(messageContent);
    //         Chat.topic = `/secured/app/chat/${Chat.roomId}`;
    //         console.log(Chat.stompClient);
    //         if(messageContent && Chat.stompClient) {
    //             let chatMessage = {
    //                 text: messageContent,
    //                 messageType: 'CHAT'
    //             };
    //             console.log(chatMessage);
    //             Chat.stompClient.send(`${Chat.topic}/send`, {}, JSON.stringify(chatMessage));
    //         }
    //         Chat.messageTextBox.val("");
    //     },
    //     onMessageReceived(payload) {
    //         console.log("Inside onMessageReceived!");
    //         let message = JSON.parse(payload.body);
    //         console.log("Message received:");
    //         console.log(message);
    //         if(message.messageType === 'JOIN') {
    //             Print.joinMessage(message);
    //         } else if (message.messageType === 'LEAVE') {
    //             Print.leaveMessage(message);
    //         } else {
    //             Print.chatMessage(message);
    //         }
    //     }
    // }
    //
    // const Print = {
    //     joinMessage(message) {
    //         console.log(`${message.sender.username} has joined the chat!`);
    //     },
    //     chatMessage(message) {
    //         console.log(`${message.sender.username}: ${message.text}`);
    //     },
    //     leaveMessage(message) {
    //         console.log(`${message.sender.username} left :(`);
    //     }
    // }
    //
    // const Events = {
    //     async initialize() {
    //         await Socket.connect();
    //         $(document)
    //             .on("click", "#send-button", Socket.sendMessage)
    //         ;
    //     }
    // }
    //
    // $(document).ready(function() {
    //     Events.initialize();
    // })
});