package io;

import java.io.IOException;
import java.util.Vector;

import io.Client.gameState;
import logic.Ball;

public class InputThread extends Thread {
	private boolean running;
	private Client c;
	public byte[] input_buffer;
	private int inputBufferSize = 1024;
	
	public InputThread(Client c) {
		running=false;
		this.c=c;
		input_buffer=new byte[inputBufferSize];
	}
	
	public void setRunning(boolean running) {
		this.running=running;
	}
	
	@Override
	public void run() {
		while(running) {
			if((c.state == gameState.BallsMoving) || (c.state == gameState.WaitingForServer)||
					(c.turn==0 && (c.state == gameState.ChoosingDir ||
								   c.state == gameState.ChoosingVel ||
								   c.state == gameState.MovingQueueBall ||
								   c.state == gameState.MovingQueueBallInit))) {
				try {
					c.server.getInputStream().read(input_buffer);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				parseData();
			}
		}
		
		//input_buffer="1;0;20;1,0,-2:0,2,2:;30,31;".getBytes();
		//parseData();
	}
	//BallsMoving, ChoosingDir, ChoosingVel, MovingQueueBall, MovingQueueBallInit, GameEnded
	
	//turn(0-1);state(0-1-2-3-4-5);ball_size;ballnumber,x,y:ballnumber,x,y:;x,y;
	private void parseData() {
		String[] data = new String(input_buffer).split(";"); 
		if(data[0].length() > 0) {
			c.turn=Integer.parseInt(data[0]);
			//System.out.println(""+c.turn);
		}
		
		if(data[2].length() > 0) {
			c.ball_diameter=Integer.parseInt(data[2]);
			//System.out.println(""+c.ball_diameter);
		}
		
		if(data[3].length() > 0) {
			String[] balls = data[3].split(":");
			Vector <Ball> balls_vector =new Vector<Ball>();
			
			for(int i=0; i < balls.length; i++) {
				String[] ball = balls[i].split(",");
				int number=Integer.parseInt(ball[0]);
				int posX=Integer.parseInt(ball[1]);
				int posY=Integer.parseInt(ball[2]);
				
				//System.out.println("x:"+posX+" y:"+posY+" number:"+number);
				
				if(number==0) {
					balls_vector.add(0,new Ball(posX,posY,c.ball_diameter,number));
				}
				else {
					balls_vector.add(new Ball(posX,posY,c.ball_diameter,number));
				}
			}
		}
		
		if(data[4].length() > 0) {
			String[] coords=data[4].split(",");
			int x=Integer.parseInt(coords[0]);
			int y=Integer.parseInt(coords[1]);
			//System.out.println("x:"+x+" y:"+y);
		}
		
		if(data[1].length() > 0) {
			switch(data[1]) {
			case "0":
				c.state=gameState.BallsMoving;
				break;
			case "1":
				c.state=gameState.ChoosingDir;
				break;
			case "2":
				c.state=gameState.ChoosingVel;
				break;
			case "3":
				c.state=gameState.MovingQueueBall;
				break;
			case "4":
				c.state=gameState.MovingQueueBallInit;
				break;
			case "5":
				c.state=gameState.GameEnded;
				break;
			}
			
			//System.out.println(""+c.state);
		}
	}

}
