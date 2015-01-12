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
	private String username;
	private MessageArea msgArea;
	
	public ClientEndPoint(String username, MessageArea msgArea) 
			throws URISyntaxException, DeploymentException, IOException 
	{
		this.username = username;
		this.msgArea = msgArea;
		URI uri = new URI("ws://localhost:8080/FSEServer/chatRoomServerEndpoint");
		ContainerProvider.getWebSocketContainer().connectToServer(this, uri);
		sendMsg(username);
	}
	
	public String getUsername() {
		return this.username;
	}
	
	@OnOpen
	public void processOpen(Session session) {
		this.session = session;
	}
	
	@OnMessage
	public void processMsg(String msg) {
		try {
			JsonObject obj = Json.createReader(new StringReader(msg)).readObject();
			String sender = obj.getString("username");
			
			msgArea.addMsg(sender, obj.getString("message"), 
					sender.equals(this.username));
			
			this.msgArea.repaint();
		} catch (Exception e) {
			// Do Nothing!
		}
	}
	
	public void sendMsg(String msg) throws IOException {
		this.session.getBasicRemote().sendText(msg);
	}
	
	@OnClose
	public void processClose() {
		try {
			this.session.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
