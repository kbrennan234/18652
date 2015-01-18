package com.cmu.fse.kbrennan;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import javax.json.Json;
import javax.json.JsonObject;
import javax.websocket.ClientEndpoint;
import javax.websocket.ContainerProvider;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.DeploymentException;

@ClientEndpoint
public class ClientEndPoint {
	private Session session;
	private MessageArea msgArea;
	private int connected = 0;
	private String username;
	
	public ClientEndPoint(MessageArea msgArea) 
			throws URISyntaxException, DeploymentException, IOException 
	{
		this.msgArea = msgArea;
		URI uri = new URI("ws://localhost:8080/FSEServer/chatRoomServerEndpoint");
		ContainerProvider.getWebSocketContainer().connectToServer(this, uri);
	}
	
	@OnOpen
	public void processOpen(Session session) {
		this.session = session;
	}
	
	@OnMessage
	public void processMsg(String jsonMsg) {
		try {
			JsonObject obj = Json.createReader(new StringReader(jsonMsg)).readObject();
			String sender = obj.getString("username");
			String msg = obj.getString("message");
			
			if (sender.equalsIgnoreCase("System")) {
				if (connected == 0) {
					if (!msg.startsWith("you")) {
						connected = -1;
						return;
					} else {
						this.username = msg.substring(25, msg.length());
						connected = 1;
					}
				}
			} else {
				msgArea.addMsg(sender, msg, obj.getString("timestamp"),
						sender.equals(this.username));
			}
			
			this.msgArea.repaint();
			this.msgArea.validate();
		} catch (Exception e) {
			// Do Nothing!
		}
	}
	
	@OnClose
	public void processClose() {
		try {
			this.session.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendMsg(String msg) throws IOException {
		this.session.getBasicRemote().sendText(msg);
	}
	
	public int isConnected() {
		return connected;
	}
}
