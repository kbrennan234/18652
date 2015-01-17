package com.cmu.fse.kbrennan;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class MessageScreen extends JPanel {
	private static final long serialVersionUID = -5019737017129033757L;

	private static final Color BKGD_COLOR = new Color(181, 228, 240);

	public MessageScreen(ClientEndPoint endpoint, MessageArea msgArea) {
		this.setBackground(BKGD_COLOR);
		this.setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.weightx = 1;
		gbc.weighty = 1;
		
		// Add message area
		JScrollPane scrollPane = new JScrollPane(msgArea,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
	            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBackground(BKGD_COLOR);
		this.add(scrollPane, gbc);
		
		// Add user message field and send button
		gbc.gridy = 1;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.SOUTH;
		this.add(buildUserArea(endpoint), gbc);
	}
	
	private JPanel buildUserArea(ClientEndPoint endpoint) {
		JPanel userArea = new JPanel();
		userArea.setBorder(BorderFactory.createMatteBorder(
                3, 5, 3, 3, BKGD_COLOR));
		userArea.setLayout(new BorderLayout());
		
		JTextArea userMsg = new JTextArea();
		userMsg.setLineWrap(true);
		userMsg.setWrapStyleWord(true);
		userMsg.setMargin(new Insets(3,3,3,3));
		userMsg.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
					String msg = userMsg.getText();
					if (msg != null && !msg.equalsIgnoreCase("")) {
						try {
							endpoint.sendMsg(msg);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					userMsg.setText("");
				}
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
					userMsg.setText("");
				}
			}

			@Override
			public void keyTyped(KeyEvent arg0) {}
		});
		
		JButton userSend = new JButton("Send");
		userSend.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String msg = userMsg.getText();
				if (msg != null && !msg.equalsIgnoreCase("")) {
					try {
						endpoint.sendMsg(msg);
					} catch (Exception e) {
						e.printStackTrace();
					}
					userMsg.setText("");
				}
			}
		});

		userArea.add(userMsg, BorderLayout.CENTER);
		userArea.add(userSend, BorderLayout.EAST);
		return userArea;
	}
}
