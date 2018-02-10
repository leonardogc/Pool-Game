package io;

import java.io.IOException;

import io.Client.gameState;

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
	}
	//WaitingForServer, BallsMoving, ChoosingDir, ChoosingVel, MovingQueueBall, MovingQueueBallInit, GameEnded
	
	//turn(0-1);state(0-1-2-3-4-5);ballnumber,x,y.ballnumber,x,y;x,y;
	private void parseData() {
		String data = new String(input_buffer); 
		int turn;
		int state;
		
	}
	
}
