package io;

import java.awt.EventQueue;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Vector;

import gui.GraphicInterface;
import logic.Ball;
import logic.Table;

public class Client {
	public static void main(String[] args) throws UnknownHostException, IOException {
		Client c= new Client("94.60.13.255",25565);
		c.startGame();
		
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

	public Socket server;
	
	private String address; 
	private int port;
	
	public gameState state;
	public int turn;  //0 no, 1 yes, -1 unknown
	
	public Table table;
	public Vector<Ball> balls;
	
	public int x=0;
	public int y=0;
	public double vel_vector_x=0;
	public double vel_vector_y=0;
	public double line_size;
	
	public enum gameState{
		WaitingForServer, BallsMoving, ChoosingDir, ChoosingVel, MovingQueueBall, MovingQueueBallInit, GameEnded
	}
	
	
	public Client(String address, int port) {
		this.address=address;
		this.port=port;
		
		state=gameState.WaitingForServer;
	}
	
	public void startGame() throws UnknownHostException, IOException {
		connectToServer();
		
		InputThread it= new InputThread(this);
		
		it.setRunning(true);
		
		initialize();
		
		it.start();
	}

	private void connectToServer() throws UnknownHostException, IOException {
		server=new Socket(InetAddress.getByName(address),port);
		server.setTcpNoDelay(true);
	}
	
	private void initialize() throws IOException {
		byte[] buffer=new byte[1024]; 
		String[] s;
		
		double ball_diameter;
		int screen_size_x;
		int screen_size_y;

		
		server.getInputStream().read(buffer);
		
		s=new String(buffer).split(";");
		
		
		ball_diameter=Double.parseDouble(s[0]);
		
		System.out.println("Ball Diameter:"+ ball_diameter);
		
		screen_size_x=Integer.parseInt(s[1]);
		
		System.out.println("size_x:"+ screen_size_x);

		screen_size_y=Integer.parseInt(s[2]);
		
		System.out.println("size_y:"+ screen_size_y);

		this.line_size=Double.parseDouble(s[3]);
		
		System.out.println("line:"+ this.line_size+"\n\n");

		table = new Table(ball_diameter,new int[] {screen_size_x,screen_size_y});
		balls=new Vector<Ball>();
	}
}
