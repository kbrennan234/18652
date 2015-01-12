var webSocket = new WebSocket("ws://localhost:8080/FSEServer/serverendpoint");
var msgArea = document.getElementById("msgArea");

webSocket.onopen = function processOpen(message) {
	msgArea.value += "Server Connected\n";
}
webSocket.onclose = function processClose(message) {
	webSocket.send("Client Disconnected");
	msgArea.value += "Server Disconnected\n";
}
	
webSocket.onerror = function processError(message) {
	msgArea.value += "error...\n"
}

webSocket.onmessage = function processMsg(message) {
	var data = JSON.parse(message.data);
	if (data.message != null) {
		msgArea.value += data.username + ": ";
		msgArea.value += data.message + "\n";
	}
}

function sendMsg() {
	if (textMessage.value != "close") {
		webSocket.send(textMessage.value);
		msgArea.value += textMessage.value + "\n";
	} else {
		webSocket.close();
	}	
	
	textMessage.value = "";	
}