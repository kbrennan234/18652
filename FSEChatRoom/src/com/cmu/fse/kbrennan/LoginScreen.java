package com.cmu.fse.kbrennan;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class LoginScreen extends JPanel {
	private static final long serialVersionUID = 5536679200420437586L;
	
	private static final Color BKGD_COLOR = new Color(181, 228, 240);
	
	private JTextField loginField;
	private JButton submitBtn;
	
	public LoginScreen(ClientEndPoint endpoint) {
		this.setBackground(BKGD_COLOR);
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JLabel userMsg = new JLabel("Enter your Username");
		this.add(userMsg);
		
		loginField = new JTextField();
		loginField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				submitUsername(endpoint);
			}
		});
		this.add(loginField);
		
		submitBtn = new JButton("Submit");
		submitBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				submitUsername(endpoint);
			}
		});
		this.add(submitBtn);
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
