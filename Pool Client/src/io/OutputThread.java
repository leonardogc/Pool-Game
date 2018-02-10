package io;

import java.io.IOException;

import io.Client.gameState;

public class OutputThread extends Thread {
	private boolean running;
	private Client c;

	public OutputThread(Client c) {
		running=false;
		this.c=c;
	}

	public void setRunning(boolean running) {
		this.running=running;
	}

	@Override
	public void run() {
		while(running) {
			if(c.turn==1 && (c.state == gameState.ChoosingDir ||
							 c.state == gameState.ChoosingVel ||
							 c.state == gameState.MovingQueueBall ||
							 c.state == gameState.MovingQueueBallInit)){
				
				try {
					c.server.getOutputStream().write(c.output_buffer);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
