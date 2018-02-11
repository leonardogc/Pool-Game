package gui;

import java.awt.EventQueue;
import java.io.IOException;
import java.net.UnknownHostException;

import io.Client;

public class ClientThread extends Thread{
	
	private String address;
	private ClientApplication cApp;

	public ClientThread(String address, ClientApplication cApp) {
		this.address=address;
		this.cApp=cApp;
		start();
	}
	
	@Override
	public void run() {
		Client c= new Client(address);
		
		try {
			c.startGame();
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		cApp.frame.setVisible(false);
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GraphicInterface window = new GraphicInterface(c);
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
