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

// var user = new URLSearchParams(window.location.search.slice(1)).get('user');
var user = prompt("What's your name?", "John Doe");

//Establish the WebSocket connection and set up event handlers
let ws = new WebSocket("ws://" + hostname + ":" + port + "/chat/" + user + "/");
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

    console.log(data);

    if (data.type === "message") {
        updateMessages(data.userName, data.content, data.dt);
    } else if (data.type === "users") {
        updateUsers(data.users);
    } else {
        console.log("Incorrect type for msg: " + data.type);
    }
}

// Update list of connected users
function updateUsers(users) {
    id("userlist").innerHTML = users.map(user => "<li>" + user + "</li>").join("");
}

var previous_date = null;

function updateMessages(userName, content, datetime) { 
    datetime = moment(datetime);
    var new_date = getDate(datetime);
    if (new_date !== previous_date){
        id("chatwindow").insertAdjacentHTML("beforeend",`<br><span>${new_date}</span><br><br>`);
        previous_date = new_date;
    }

    id("chatwindow").insertAdjacentHTML("beforeend",
        `<strong class="user">${userName}</strong> <span>${getTime(datetime)}</span><br>${content}<br>`);
    
    scrollToBottom("chatwindow");
    // id("chat").insertAdjacentHTML("afterbegin", msg.data + "<br>");
}

/**
 * Get time in "hh:mm" format from a moment object.
 * @param {moment object} datetime 
 */
function getTime(datetime){
    return datetime.format("HH:mm");    
}
function getDate(datetime){
    return datetime.locale("fr").format("LL");
}



