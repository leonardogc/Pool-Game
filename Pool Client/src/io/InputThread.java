package io;

import java.io.IOException;
import java.util.Vector;

import io.Client.gameState;
import logic.Ball;

public class InputThread extends Thread {
	private Client c;
	public byte[] input_buffer;
	private int inputBufferSize = 65536;
	
	public InputThread(Client c) {
		this.c=c;
		input_buffer=new byte[inputBufferSize];
	}
	
	@Override
	public void run() {
		while(!c.stop) {
				try {
					c.server.getInputStream().read(input_buffer);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					c.closeGame();
        			return;
				}
				
				parseData();
		}
		
	}
	//BallsMoving, ChoosingDir, ChoosingVel, MovingQueueBall, MovingQueueBallInit, GameEnded
	
	//turn(0-1);state(0-1-2-3-4-5);ballnumber,x,y:ballnumber,x,y:;x,y;velx,vely;
	private void parseData() {
		String[] data = new String(input_buffer).split(";"); 
		//System.out.println("Message: "+new String(input_buffer));
		if(data[0].length() > 0) {
			c.turn=Integer.parseInt(data[0]);
			//System.out.println("Turn: "+c.turn);
		}
		
		if(data[2].length() > 0) {
			String[] balls = data[2].split(":");
			Vector <Ball> balls_vector =new Vector<Ball>();
			
			for(int i=0; i < balls.length; i++) {
				String[] ball = balls[i].split(",");
				int number=Integer.parseInt(ball[0]);
				int posX=Integer.parseInt(ball[1]);
				int posY=Integer.parseInt(ball[2]);
				
				//System.out.println("x:"+posX+" y:"+posY+" number:"+number);
				
				if(number==0) {
					balls_vector.add(0,new Ball(posX,posY,(int)c.table.ball_diameter,number));
				}
				else {
					balls_vector.add(new Ball(posX,posY,(int)c.table.ball_diameter,number));
				}
			}
			
			c.balls=balls_vector;
		}
		
		if(data[3].length() > 0 && c.turn==0) {
			String[] coords=data[3].split(",");
			c.x=Integer.parseInt(coords[0]);
			c.y=Integer.parseInt(coords[1]);

			//System.out.println("x:"+c.x+" y:"+c.y);
		}
		
		if(data[4].length() > 0) {
			String[] coords=data[4].split(",");
			c.vel_vector_x=Double.parseDouble(coords[0]);
			c.vel_vector_y=Double.parseDouble(coords[1]);
	
			//System.out.println("VelX:"+c.vel_vector_x+" VelY:"+c.vel_vector_y);
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
