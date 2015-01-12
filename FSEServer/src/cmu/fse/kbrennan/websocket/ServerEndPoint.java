package cmu.fse.kbrennan.websocket;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonWriter;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/chatRoomServerEndpoint")
public class ServerEndPoint {
	private static Set<Session> users = Collections.synchronizedSet(new HashSet<Session>());
	
	@OnOpen
	public void handleOpen(Session userSession) {
		users.add(userSession);
	}
	
	@OnClose
	public void handleClose(Session userSession) {
		users.remove(userSession);
	}
	
	@OnMessage
	public void handleMessage(String message, Session userSession) throws IOException {
		String username = (String)userSession.getUserProperties().get("username");
		Iterator<Session> iterator = users.iterator();
		
		if (username != null) {
			while (iterator.hasNext()) {
				iterator.next().getBasicRemote().sendText(jsonMsg(username, message));
			}
		} else {
			// Add username to session
			userSession.getUserProperties().put("username", message);
			userSession.getBasicRemote().sendText(jsonMsg("System", "you are now connected as " + message));
			
			while (iterator.hasNext()) {
				iterator.next().getBasicRemote().sendText(jsonUserMsg());
			}
		}
	}
	
	private String jsonUserMsg() {
		Iterator<String> iterator = getUserNames().iterator();
		JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
		
		while (iterator.hasNext()) {
			jsonArrayBuilder.add((String)iterator.next());
		}
		
		return Json.createObjectBuilder().add("users", jsonArrayBuilder).build().toString();
	}
	
	private Set<String> getUserNames() {
		HashSet<String> names = new HashSet<String>();
		Iterator<Session> iterator = users.iterator();
		
		while (iterator.hasNext()) {
			names.add(iterator.next().getUserProperties().get("username").toString());
		}
		
		return names;
	}
	
	private String jsonMsg(String username, String msg) {
		JsonObject obj = Json.createObjectBuilder()
				.add("username", username)
				.add("message", msg).build();
		StringWriter writer = new StringWriter();
		
		try (JsonWriter jsonWriter = Json.createWriter(writer)) {
			jsonWriter.write(obj);
		}
		
		return writer.toString();
	}
}