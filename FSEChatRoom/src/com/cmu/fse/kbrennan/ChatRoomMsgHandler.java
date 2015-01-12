package com.cmu.fse.kbrennan;

import javax.websocket.MessageHandler;

public class ChatRoomMsgHandler implements MessageHandler.Whole<String> {
	private MessageArea msgArea;
	
	public ChatRoomMsgHandler(MessageArea msgArea) {
		this.msgArea = msgArea;
	}
	
	@Override
	public void onMessage(String arg0) {
		msgArea.addMsg(arg0,"",true);
	}
}
