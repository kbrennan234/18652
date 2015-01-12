package com.cmu.fse.kbrennan;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class MessageArea extends JPanel {
	private static final long serialVersionUID = 4537964569040609842L;

	private static final Color USER_COLOR = new Color(146, 251, 83);
	private static final Color OTHER_COLOR = new Color(255, 171, 79);
	private static final SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
	private static final Font usernameFont = new Font("serif", Font.BOLD, 14);
	private static final Font dateFont = new Font("serif", Font.PLAIN, 9);
	private static final Color BKGD_COLOR = new Color(181, 228, 240);
	
	public MessageArea() {
		super();
		
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	}
	
	public void addMsg(String username, String msg, boolean userMsg) {
		if (username == null || msg == null) return;
		
		JPanel msgPanel = new JPanel();
		msgPanel.setBackground(userMsg ? USER_COLOR : OTHER_COLOR);
		msgPanel.setLayout(new GridBagLayout());
		msgPanel.setBorder(BorderFactory.createMatteBorder(
                3, 3, 3, 3, BKGD_COLOR));
		
		JTextArea msgBox = new JTextArea(msg);
		msgBox.setEditable(false);
		msgBox.setLineWrap(true);
		msgBox.setWrapStyleWord(true);
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.EAST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weighty = 0;
		gbc.weightx = 0;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		
		// Add username to message
		JTextArea usernameField = new JTextArea(username + "    ");
		usernameField.setBackground(userMsg ? USER_COLOR : OTHER_COLOR);
		usernameField.setFont(usernameFont);
		usernameField.setEditable(false);
		msgPanel.add(usernameField, gbc);
		
		// Add timestamp to message
		gbc.gridx = 1;
		JTextArea date = new JTextArea(formatter.format(new Date()));
		date.setBackground(userMsg ? USER_COLOR : OTHER_COLOR);
		date.setFont(dateFont);
		date.setEditable(false);
		msgPanel.add(date, gbc);
		
		gbc.gridx = 0;
		gbc.gridwidth = 2;
		gbc.gridy = 1;
		if (userMsg) {
			msgBox.setBackground(USER_COLOR);
			msgPanel.add(msgBox, gbc);
		} else {
			msgBox.setBackground(OTHER_COLOR);
			msgPanel.add(msgBox, gbc);
		}
		
		this.add(msgPanel);
	}
}
