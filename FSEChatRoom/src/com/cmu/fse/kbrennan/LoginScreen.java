package com.cmu.fse.kbrennan;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class LoginScreen extends JPanel {
	private static final long serialVersionUID = 5536679200420437586L;
	
	private static final Color BKGD_COLOR = new Color(181, 228, 240);
	private static final Color CENTER_COLOR = new Color(102, 255, 51);
	
	private JTextField loginField;
	private JButton submitBtn;
	
	public LoginScreen(ClientEndPoint endpoint) {
		this.setBackground(BKGD_COLOR);
		this.setLayout(new GridBagLayout());
		
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new GridBagLayout());
		centerPanel.setBackground(CENTER_COLOR);
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.insets = new Insets(10, 25, 3, 25);
		gbc.anchor = GridBagConstraints.CENTER;
		
		JLabel userMsg = new JLabel("Enter your Username:");
		userMsg.setHorizontalAlignment(SwingConstants.CENTER);	
		centerPanel.add(userMsg, gbc);
		
		gbc.gridy = 1;
		gbc.insets = new Insets(3, 25, 3, 25);
		loginField = new JTextField();
		loginField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				submitUsername(endpoint);
			}
		});
		centerPanel.add(loginField, gbc);
		
		gbc.gridy = 2;
		gbc.insets = new Insets(3, 25, 10, 25);
		submitBtn = new JButton("Submit");
		submitBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				submitUsername(endpoint);
			}
		});
		centerPanel.add(submitBtn, gbc);
		
		GridBagConstraints mainGbc = new GridBagConstraints();
		mainGbc.fill = GridBagConstraints.NONE;
		mainGbc.anchor = GridBagConstraints.CENTER;
		mainGbc.weightx = 0.75;
		this.add(centerPanel, mainGbc);
	}
	
	private void submitUsername(ClientEndPoint endpoint) {
		loginField.setEnabled(false);
		submitBtn.setEnabled(false);
		
		String username = loginField.getText();
		if (username.isEmpty() || username.length() > 30) {
			reset();
			return;
		}
		
		try {
			endpoint.sendMsg(username);
		} catch (Exception e) {
			reset();
			return;
		}
		
		// Wait for response
		for (;;) {
			int response = endpoint.isConnected();
			if (response > 0) {
				break;
			} else if (response < 0) {
				reset();
				return;
			}
			
			try {
				Thread.sleep(250);
			} catch (Exception e) {
				// Do Nothing!
			}
		}
		
		this.firePropertyChange("connected", false, true);
	}
	
	private void reset() {
		loginField.setText("");
		loginField.setEnabled(true);
		submitBtn.setEnabled(true);
	}
}
