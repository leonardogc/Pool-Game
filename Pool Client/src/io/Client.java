package io;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

	private Socket server;
	
	private String address; 
	private int port;
	
	public Client(String address, int port) {
		this.address=address;
		this.port=port;
	}
	
	public void startGame() throws UnknownHostException, IOException {
		connectToServer();
	}

	private void connectToServer() throws UnknownHostException, IOException {
		server=new Socket(InetAddress.getByName(address),port);
	}
}
