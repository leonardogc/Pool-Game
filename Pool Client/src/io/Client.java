package io;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
	public static void main(String[] args) throws UnknownHostException, IOException {
		Client c= new Client("94.60.13.255",25565);
		c.startGame();
	}

	public Socket server;
	
	private String address; 
	private int port;
	
	public byte[] output_buffer;
	private int outputBufferSize = 1024;
	
	public gameState state;
	public int turn;  //0 no, 1 yes, -1 unknown
	
	public enum gameState{
		WaitingForServer, BallsMoving, ChoosingDir, ChoosingVel, MovingQueueBall, MovingQueueBallInit, GameEnded
	}
	
	
	public Client(String address, int port) {
		this.address=address;
		this.port=port;
		
		output_buffer=new byte[outputBufferSize];
		
		state=gameState.WaitingForServer;
	}
	
	public void startGame() throws UnknownHostException, IOException {
		connectToServer();
		
		InputThread it= new InputThread(this);
		OutputThread ot= new OutputThread(this);
		
		it.setRunning(true);
		ot.setRunning(true);
		
		it.start();
		ot.start();
	}

	private void connectToServer() throws UnknownHostException, IOException {
		server=new Socket(InetAddress.getByName(address),port);
		server.setTcpNoDelay(true);
	}
}
