package io;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import logic.Ball;
import logic.Game;
import logic.Game.gameState;
import logic.LoopThread;

public class Server {
	public static void main(String[] args) {
		Server s=new Server(25565);
		s.startServer();
	}
	
	private ServerSocket ts=null;
	public Socket player1;
	public Socket player2;
	private int port;
	
	private int[] screen_size=new int[]{1366,735};
	private double ball_diameter=22;
	private double line_size=300;
	
	public Game game;
	public LoopThread loopThread;
	private OutputThread outputThread;
	
	private InputThread inputThread_p1;
	private InputThread inputThread_p2;
	
	public int x=0;
	public int y=0;
	public double vel_vector_x=0;
	public double vel_vector_y=0;
	
	public Server(int port) {
		this.port=port;
	}
	
	public void startServer() {
		findPlayers();
		sendDimensions();
		
		game=new Game(ball_diameter, screen_size);
		
		loopThread= new LoopThread(this);
		loopThread.setRunning(true);
		
		outputThread= new OutputThread(this);
		outputThread.setRunning(true);
		
		inputThread_p1=new InputThread(player1, this);
		inputThread_p1.setRunning(true);
		
		inputThread_p2=new InputThread(player2, this);
		inputThread_p2.setRunning(true);
		
		loopThread.start();
		inputThread_p1.start();
		inputThread_p2.start();
		outputThread.start();
	}

	public void closeServer(){
		loopThread.setRunning(false);
		outputThread.setRunning(false);
		inputThread_p1.setRunning(false);
		inputThread_p2.setRunning(false);

		try {
			player1.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			player2.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void sendDimensions() {
		try {
			player1.getOutputStream().write((ball_diameter+";"+screen_size[0]+";"+screen_size[1]+";"+line_size+";").getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			player2.getOutputStream().write((ball_diameter+";"+screen_size[0]+";"+screen_size[1]+";"+line_size+";").getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void findPlayers() {
		try {
			ts = new ServerSocket(port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			player1 = ts.accept();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			player1.setTcpNoDelay(true);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Player 1 Connected");
		
		
		try {
			player2 = ts.accept();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			player2.setTcpNoDelay(true);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Player 2 Connected");
		
		try {
			ts.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ts=null;
	}
	
	public void checkChoice(){
		if(game.state == gameState.ChoosingDir) {
			vel_vector_x=x-game.balls.get(0).pos[0];
			vel_vector_y=y-game.balls.get(0).pos[1];
			
			double vector_size=Math.sqrt(Math.pow(vel_vector_x,2)+Math.pow(vel_vector_y,2));
			
			vel_vector_x/=vector_size;
			vel_vector_y/=vector_size;
			game.state = gameState.ChoosingVel;
		}
		else if(game.state == gameState.ChoosingVel) {
			double size= vel_vector_x*(x-game.balls.get(0).pos[0])+vel_vector_y*(y-game.balls.get(0).pos[1]);
			
			if(size > line_size) {
				game.balls.get(0).vel[0]=vel_vector_x*game.max_speed;
				game.balls.get(0).vel[1]=vel_vector_y*game.max_speed;
			}else if(size <= line_size && size > 0){
				game.balls.get(0).vel[0]=vel_vector_x*(size/line_size)*game.max_speed;
				game.balls.get(0).vel[1]=vel_vector_y*(size/line_size)*game.max_speed;
			}
			
			game.hit_ball=false;
			game.hit_player_ball_first=false;
			game.ball_8_in_pocket=false;
			game.ball_0_in_pocket=false;
			game.player_ball_in_pocket=false;
			game.state = gameState.BallsMoving;
		}
		else if(game.state == gameState.MovingQueueBall) {
			boolean valid=true;
			
			for(int i=0; i< game.balls.size(); i++) {
				if(Math.sqrt(Math.pow(game.balls.get(i).pos[0]-x,2) + Math.pow(game.balls.get(i).pos[1]-y,2)) < game.ball_diameter){
					valid=false;
					break;
				}
			}
			
			if( x > game.table_edge[2]-game.ball_diameter/2||
				x < game.table_edge[0]+game.ball_diameter/2||
				y > game.table_edge[3]-game.ball_diameter/2||
				y < game.table_edge[1]+game.ball_diameter/2) {
				valid=false;
			}

			if(valid) {
				game.balls.add(0,new Ball(x,y,0,0,game.ball_diameter,0));
				game.state = gameState.ChoosingDir;
			}
		}
		
		else if(game.state == gameState.MovingQueueBallInit) {
			if(!(x > game.table_edge[2]-game.ball_diameter/2||
					x < game.table_edge[0]+game.ball_diameter/2||
					y > game.table_edge[3]-game.ball_diameter/2||
					y < game.table_edge[1]+game.ball_diameter/2)) {

				if(x < game.table_edge[2]-(game.table_edge[2]-game.table_edge[0])/4) {
					game.balls.add(0,new Ball(game.table_edge[2]-(game.table_edge[2]-game.table_edge[0])/4,y,0,0,game.ball_diameter,0));
					game.state = gameState.ChoosingDir;
				}
				else {
					game.balls.add(0,new Ball(x,y,0,0,game.ball_diameter,0));
					game.state = gameState.ChoosingDir;
				}
			}
		}
	}
}
