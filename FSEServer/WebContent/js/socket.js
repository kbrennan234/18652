var webSocket = new WebSocket("ws://localhost:8080/FSEServer/chatRoomServerEndpoint");
var username = null;

webSocket.onmessage = function processMsg(message) {
	var data = JSON.parse(message.data);
	
	var msg_username = data.username;
	var msg = data.message;
	
	if (msg_username != null && msg != null) {
		if (username == null && msg_username.toLowerCase() == "system") {
			if (msg.substring(0,3) == "you") {
				username = msg.substring(25, msg.length);
				enterChatRoom();
			} else {
				document.getElementById('user').removeAttribute("readonly");
				document.getElementById('login').removeAttribute("disabled");
			}
		} else if (username != null) {
			var date = data.timestamp;
			if (msg_username.toLowerCase() == "System".toLowerCase()) {
				addConnectMsg(date, msg);
			} else if (msg_username == username) {
				addMsg("Me", date, msg, "user-msg");
			} else {
				addMsg(msg_username, date, msg, "other-msg");
			}
		}
	}
}

function sendMessage() {
	if (username == null) {
		var msg_username = document.getElementById("user");
		
		if (msg_username.value != "Username" && msg_username.value != "") {
			webSocket.send(msg_username.value);
			
			msg_username.setAttribute("readonly","readonly");
			document.getElementById("login").setAttribute("disabled", "disabled");
		}
	} else {
		var user_text = document.getElementById("user_text");
	
		if (user_text.value != "") {
			webSocket.send(user_text.value);
			user_text.value = "";
		}
	}
}

document.getElementById('user').addEventListener('keypress', function (e) {
    var key = e.which || e.keyCode;
    if (key == 13) { // 13 is enter
		sendMessage()
    }
});

document.getElementById("user_text").addEventListener('keypress', function (e) {
    var key = e.which || e.keyCode;
    if (key == 13) { // 13 is enter
		sendMessage()
    }
});


function addConnectMsg(date, msg) {
	var connectMsg = document.createElement("p");
	connectMsg.setAttribute("class", "user-connect");
	
	if (msg.substring(0,9) == "connected") {
		var message = document.createTextNode(
				msg.substring(9,msg.length) + " connected at " + date + "\n");
		connectMsg.appendChild(message);
		document.getElementById("message_pane").appendChild(connectMsg);
	} else if (msg.substring(0,12) == "disconnected") {
		var message = document.createTextNode(
				msg.substring(12,msg.length) + " disconnected at " + date + "\n");
		connectMsg.appendChild(message);
		document.getElementById("message_pane").appendChild(connectMsg);
	}
	
}

function addMsg(username, date, msg, msg_class) {
	var nameField = document.createElement("span");
	nameField.appendChild(document.createTextNode(username));
	nameField.setAttribute("class", "msg-sender");
	var dateField = document.createElement("span");
	dateField.appendChild(document.createTextNode(date));
	dateField.setAttribute("class", "timestamp");
	var msgField = document.createElement("span");
	msgField.appendChild(document.createTextNode(msg));
	msgField.setAttribute("class", "message");
	
	var node11 = document.createElement("td");
	node11.appendChild(nameField);
	var node12 = document.createElement("td");
	node12.appendChild(dateField);
	var node21 = document.createElement("td");
	node21.appendChild(msgField);
	
	var row1 = document.createElement("tr");
	row1.appendChild(node11);
	row1.appendChild(node12);
	var row2 = document.createElement("tr");
	row2.appendChild(node21);
	
	var table = document.createElement("table");
	table.appendChild(row1);
	table.appendChild(row2);
	table.setAttribute("class", msg_class);
	
	document.getElementById("message_pane").appendChild(table);
}

function enterChatRoom() {
	document.getElementById("login_screen").remove();
	document.getElementById("wrapper").style.opacity = 1.0;
	document.getElementById("user_text").removeAttribute("readonly");
	document.getElementById("user_send").removeAttribute("disabled");
}