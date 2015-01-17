package cmu.fse.kbrennan.websocket;

import java.io.StringWriter;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonWriter;

public class Message {
	private String username;
	private String msg;
	private String timestamp;
	
	public Message(String username, String msg, String timestamp) {
		this.username = username;
		this.msg = msg;
		this.timestamp = timestamp;
	}
	
	@Override
	public String toString() {
		JsonObject obj = Json.createObjectBuilder()
				.add("username", username)
				.add("message", msg)
				.add("timestamp", timestamp).build();
		StringWriter writer = new StringWriter();
		
		try (JsonWriter jsonWriter = Json.createWriter(writer)) {
			jsonWriter.write(obj);
		}
		
		return writer.toString();
	}
}