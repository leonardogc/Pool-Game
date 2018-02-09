package io;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
	public static void main(String[] args) throws UnknownHostException, IOException {
		Client c= new Client("94.60.13.255",25565);
		c.startGame();
		
		System.out.println("exiting");
	}

	private Socket server;
	
	private String address; 
	private int port;
	
	private byte[] input_buffer;
	private byte[] output_buffer;
	private int bufferSize = 1024;
	
	private gameState state;
	
	public enum gameState{
		WaitingForServer, BallsMoving, ChoosingDir, ChoosingVel, MovingQueueBall, MovingQueueBallInit, GameEnded
	}
	
	public Client(String address, int port) {
		this.address=address;
		this.port=port;
		
		input_buffer=new byte[bufferSize];
		output_buffer=new byte[bufferSize];
		
		state=gameState.WaitingForServer;
	}
	
	public void startGame() throws UnknownHostException, IOException {
		connectToServer();
		
		InputThread it= new InputThread();
		OutputThread ot= new OutputThread();
		
		it.setRunning(true);
		ot.setRunning(true);
		
		it.start();
		ot.start();
	}

	private void connectToServer() throws UnknownHostException, IOException {
		server=new Socket(InetAddress.getByName(address),port);
		server.setTcpNoDelay(true);
	}
	
	private void send() throws IOException {
		server.getOutputStream().write(output_buffer);
	}
	
	private void receive() throws IOException {
		server.getInputStream().read(input_buffer);
	}
}
