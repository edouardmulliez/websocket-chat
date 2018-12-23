// // websocket authentication
// https://security.stackexchange.com/questions/65959/is-this-a-safe-way-to-identify-users-connecting-to-a-chat-server

function scrollToBottom(id){
    var div = document.getElementById(id);
    div.scrollTop = div.scrollHeight - div.clientHeight;
 }


// small helper function for selecting element by id
let id = id => document.getElementById(id);


var hostname = "localhost";  // location.hostname
var port = 8080;  // location.port

var userName = "doudou";

//Establish the WebSocket connection and set up event handlers
let ws = new WebSocket(
    "ws://" + hostname + ":" + port + "/chat/" + userName + "/");
ws.onmessage = msg => updateChat(msg);
ws.onclose = () => alert("WebSocket connection closed");

// Add event listeners to button and input field
id("message").addEventListener("keypress", function (e) {
    if (e.keyCode === 13) { // Send message if enter is pressed in input field
        sendAndClear(ws, e.target.value);
    }
});

function sendAndClear(ws, message) {
    if (message !== "") {
        ws.send(message);
        id("message").value = "";
    }
}

function updateChat(msg) {
    var data = JSON.parse(msg.data);
    if (data.type === "reply") {
        updateMessages(data.from, data.message);
    } else if (data.type === "users") {
        updateUsers(data.users);
    } else {
        console.log("Incorrect type for msg: " + data.type);
    }
}

function updateUsers(users) {
    id("userlist").innerHTML = users.map(user => "<li>" + user + "</li>").join("");
}

function updateMessages(from, message) { // Update chat-panel and list of connected users
    id("chatwindow").insertAdjacentHTML(
        "beforeend",
        `<strong>${from}</strong> ${message} <br>`);
    
    scrollToBottom("chatwindow");
    // id("chat").insertAdjacentHTML("afterbegin", msg.data + "<br>");

}



