package io;

import java.io.IOException;
import java.net.Socket;
import java.util.Vector;

import logic.Ball;

public class InputThread extends Thread {
	private boolean running;
	private Socket player;
	private Server s;
	public byte[] input_buffer;
	private int inputBufferSize = 65536;
	
	public InputThread(Socket player, Server s) {
		running=false;
		this.player=player;
		this.s=s;
		input_buffer=new byte[inputBufferSize];
	}
	
	public void setRunning(boolean running) {
		this.running=running;
	}
	
	@Override
	public void run() {
		while(running) {
				try {
					player.getInputStream().read(input_buffer);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					
					try {
						s.player1.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					try {
						s.player2.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					return;
				}
				
				parseData();
		}
	}
	
	//x,y,check;
	private void parseData() {
		String[] data = new String(input_buffer).split(";"); 
		//System.out.println("Message length: "+new String(input_buffer));
		if(data[0].length() > 0) {
			String[] data2 = data[0].split(",");
			
			s.x=Integer.parseInt(data2[0]);
			s.y=Integer.parseInt(data2[1]);
			if(Integer.parseInt(data2[2]) == 1) {
				s.checkChoice();
			}
		}
	}

}
