package com.cmu.fse.kbrennan;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.URISyntaxException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.websocket.DeploymentException;

public class Client extends JFrame {
	private static final long serialVersionUID = 7730571604124283764L;
	private static final int WIDTH = 400;
	private static final int HEIGHT = 500;
	private static final Color BKGD_COLOR = new Color(181, 228, 240);
	
	private ClientEndPoint endpoint;
	private MessageArea msgArea;
	private JScrollPane scrollPane;
	
	public Client() {
		this.setMinimumSize(new Dimension(WIDTH, HEIGHT));
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.setLayout(new GridBagLayout());
		this.getContentPane().setBackground(BKGD_COLOR);
		this.setTitle("FSE Chat Room");
		
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				try {
					if (endpoint != null) endpoint.processClose();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				System.exit(0);
			}
		});
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.weightx = 1;
		gbc.weighty = 1;
		
		// Add message area
		msgArea = new MessageArea();
		scrollPane = new JScrollPane(msgArea);
		scrollPane.setBackground(BKGD_COLOR);
		this.add(scrollPane, gbc);
		
		// Add user message field and send button
		gbc.gridy = 1;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.SOUTH;
		this.add(buildUserArea(), gbc);
		
		this.setVisible(true);
	}
	
	private JPanel buildUserArea() {
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
							//msgArea.addMsg(endpoint.getUsername(), msg, true);
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
					Rectangle visibleRect = scrollPane.getVisibleRect();
				    visibleRect.y = scrollPane.getHeight() - visibleRect.height;
				    scrollPane.scrollRectToVisible(visibleRect);
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

	public static void main(String[] args)
			throws URISyntaxException, DeploymentException, IOException 
	{	
		Client gui = new Client();
		
		// Input dialog for username
		for (;;) {
			String username = (String)JOptionPane.showInputDialog(
					gui, "Please enter your username!","Login", 
					JOptionPane.INFORMATION_MESSAGE);
		
			if (username == null) {
				if (JOptionPane.showConfirmDialog(gui, "Press Yes to Quit or No to Return!", "Quit?",
						JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == 0) System.exit(0);
			} else if (!username.isEmpty()) {
				try {
					gui.endpoint = new ClientEndPoint(username, gui.msgArea);
					break;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
