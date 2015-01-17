package com.cmu.fse.kbrennan;

import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.URISyntaxException;
import javax.swing.JFrame;
import javax.websocket.DeploymentException;

public class Client {
	private static final Dimension WINDOW_SIZE = new Dimension(400, 500);

	public static JFrame buildGui(ClientEndPoint endpoint, MessageArea msgArea) {
		JFrame gui = new JFrame();
		
		gui.setMinimumSize(WINDOW_SIZE);
		gui.setResizable(false);
		gui.setTitle("FSE Chat Room");
		
		gui.addWindowListener(new java.awt.event.WindowAdapter() {
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
		
		LoginScreen loginScreen = new LoginScreen(endpoint);
		loginScreen.addPropertyChangeListener("connected", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent arg0) {
				gui.remove(loginScreen);
				
				gui.add(new MessageScreen(endpoint, msgArea));
				gui.getContentPane().repaint();
				gui.pack();
			}
		});
		gui.add(loginScreen);
		
		return gui;
	}
	
	public static void main(String[] args)
			throws URISyntaxException, DeploymentException, IOException 
	{	
		MessageArea msgArea = new MessageArea();
		ClientEndPoint endpoint = new ClientEndPoint(msgArea);
		
		JFrame gui = buildGui(endpoint, msgArea);
		gui.pack();
		gui.setVisible(true);
	}
}
