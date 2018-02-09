package io;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	private Socket player1;
	private Socket player2;
	private int port;
	
	public Server(int port) {
		this.port=port;
	}
	
	public void startServer() throws IOException {
		findPlayers();
		
		player1.close();
		player2.close();
	}
	
	private void findPlayers() throws IOException {
		ServerSocket ts = new ServerSocket(port);
		
		player1 = ts.accept();
		System.out.println("Player 1 Connected");
		
		
		player2 = ts.accept();
		System.out.println("Player 2 Connected");
		
		ts.close();
	}
}
