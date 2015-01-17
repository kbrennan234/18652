package cmu.fse.kbrennan.websocket;

import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.json.Json;
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
	private static Set<String> messages = Collections.synchronizedSet(new HashSet<String>());
	private static final int LIMIT = 10;
	private static final SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
	private static final List<String> RESERVED = Arrays.asList("System");
	
	@OnOpen
	public void handleOpen(Session userSession) {
		users.add(userSession);
	}
	
	@OnClose
	public void handleClose(Session userSession) {
		users.remove(userSession);
	}
	
	@OnMessage
	public void handleMessage(String msg, Session userSession) throws IOException {
		String username = (String)userSession.getUserProperties().get("username");
		String timestamp = formatter.format(new Date());
		Iterator<Session> iterator = users.iterator();
		
		if (username != null) {
			// Store incoming message and relay to all users
			String outMsg = new Message(username, msg, timestamp).toString();
			messages.add(outMsg);
			while (messages.size() > LIMIT) messages.remove(0);
			
			while (iterator.hasNext()) {
				iterator.next().getBasicRemote().sendText(outMsg);
			}
		} else {
			// Check username against reserved names
			for (String name : RESERVED) {
				if (name.equalsIgnoreCase(msg)) {
					userSession.getBasicRemote().sendText(jsonMsg("System", "Invalid Username"));
					return;
				}
			}
			
			// Check for available username
			while (iterator.hasNext()) {
				try {
					// Usernames are case-sensitive
					if (iterator.next().getUserProperties().get("username").equals(msg)) {
						userSession.getBasicRemote().sendText(jsonMsg("System", "Invalid Username"));
						return;
					}
				} catch (Exception e) {
					// Do Nothing!
				}
			}
			
			// Add username to session and send acknowledgment
			userSession.getUserProperties().put("username", msg);
			userSession.getBasicRemote().sendText(jsonMsg("System", "you are now connected as " + msg));
			
			// Send saved messages to new user
			Iterator<String> msgIterator = messages.iterator();
			while (msgIterator.hasNext()) {
				userSession.getBasicRemote().sendText(msgIterator.next());
			}
		}
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