package com.cmu.fse.kbrennan;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

public class MessageArea extends JPanel {
	private static final long serialVersionUID = 4537964569040609842L;

	private static final Color USER_COLOR = new Color(146, 251, 83);
	private static final Color OTHER_COLOR = new Color(255, 171, 79);
	private static final SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
	private static final Font usernameFont = new Font("serif", Font.BOLD, 14);
	private static final Font dateFont = new Font("serif", Font.PLAIN, 10);
	private static final Color BKGD_COLOR = new Color(181, 228, 240);
	private static final int MSG_SPACING = 3;
	private static int num_panels = 0;
	private GridBagConstraints gbc_scroll;
	
	public MessageArea() {
		super();
		
		this.setLayout(new GridBagLayout());
		this.setBackground(BKGD_COLOR);
		
		this.gbc_scroll = new GridBagConstraints();
		this.gbc_scroll.fill = GridBagConstraints.HORIZONTAL;
		this.gbc_scroll.gridx = 0;
		this.gbc_scroll.anchor = GridBagConstraints.NORTH;
		this.gbc_scroll.weightx = 1.0;
	}
	
	public void addMsg(String username, String msg, boolean userMsg) {
		if (username == null || msg == null) return;
		
		Color msgColor = userMsg ? USER_COLOR : OTHER_COLOR;
		
		JPanel msgPanel = new JPanel();
		msgPanel.setBackground(msgColor);
		msgPanel.setLayout(new GridBagLayout());
		msgPanel.setBorder(BorderFactory.createMatteBorder(
                MSG_SPACING, MSG_SPACING, MSG_SPACING, MSG_SPACING, BKGD_COLOR));
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.weightx = 0.5;
		gbc.weighty = 0;
		
		// Add username to message
		JLabel usernameField = new JLabel(username + "    ");
		usernameField.setBackground(msgColor);
		usernameField.setFont(usernameFont);
		usernameField.setBorder(BorderFactory.createMatteBorder(
                MSG_SPACING, MSG_SPACING, 0, 0, msgColor));
		msgPanel.add(usernameField, gbc);
		
		// Add timestamp to message
		gbc.gridx = 1;
		JLabel date = new JLabel(formatter.format(new Date()));
		date.setBackground(msgColor);
		date.setHorizontalAlignment(SwingConstants.RIGHT);
		date.setFont(dateFont);
		date.setBorder(BorderFactory.createMatteBorder(
                MSG_SPACING, 0, 0, MSG_SPACING, msgColor));
		msgPanel.add(date, gbc);
		
		gbc.gridx = 0;
		gbc.gridwidth = 2;
		gbc.gridy = 1;
		gbc.weightx = 1;
		JTextArea msgBox = new JTextArea(msg);
		msgBox.setEditable(false);
		msgBox.setLineWrap(true);
		msgBox.setWrapStyleWord(true);
		msgBox.setBackground(msgColor);
		msgBox.setBorder(BorderFactory.createMatteBorder(
                0, MSG_SPACING, MSG_SPACING, MSG_SPACING, msgColor));
		msgPanel.add(msgBox, gbc);
		
		gbc_scroll.gridy = num_panels++;
		
		this.add(msgPanel, gbc_scroll);
	}
}
