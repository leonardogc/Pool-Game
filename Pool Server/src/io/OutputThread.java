package io;

import java.io.IOException;

import io.Server;


public class OutputThread extends Thread{
	
	 private boolean running;
	 private double max_fps;
	 private Server s;

	public OutputThread(Server s){
		   running=false;
	       max_fps=60;
	       this.s=s;
	}
	
	   public void setRunning(boolean running){
	       this.running=running;
	   }

	   
	    @Override
	    public void run() {
	        long startTime;
	        long frameDuration;
	        long targetTime=(long)(1000000000/max_fps);
	        long waitTime;
	        long waitTimeMill;
	        int waitTimeNano;
	        long totalTime=0;
	        int frameCounter=0;
	        double averageFps;

	        while(running){
	        	startTime= System.nanoTime();
	        	
	        	sendMessage();

	        	frameDuration=System.nanoTime()-startTime;
	        	waitTime=targetTime-frameDuration;
	        	waitTimeMill = waitTime/1000000;
	            waitTimeNano = (int)(waitTime - waitTimeMill*1000000);

	            try{
	                if(waitTime>0){
	                    this.sleep(waitTimeMill, waitTimeNano);
	                }
	            }catch(Exception e){ e.printStackTrace();}

	            
	            totalTime=totalTime+(System.nanoTime()-startTime);
	            frameCounter++;

	            if(frameCounter==max_fps){
	                averageFps=((double)frameCounter*1000)/((double)totalTime/1000000);
	                frameCounter=0;
	                totalTime=0;
	                ///uncomment to print the average fps
	              //   System.out.println("FPS: "+averageFps);
	            }

	        }

	    }
	    
	    //BallsMoving, ChoosingDir, ChoosingVel, MovingQueueBall, MovingQueueBallInit, GameEnded
		
		//turn(0-1);state(0-1-2-3-4-5);ballnumber,x,y:ballnumber,x,y:;x,y;velx,vely;
	    
	    private void sendMessage() {
	    	String m="";

	    	switch(s.game.state) {
	    	case BallsMoving:
	    		m+="0;";
	    		break;
	    	case ChoosingDir:
	    		m+="1;";
	    		break;
	    	case ChoosingVel:
	    		m+="2;";
	    		break;
	    	case MovingQueueBall:
	    		m+="3;";
	    		break;
	    	case MovingQueueBallInit:
	    		m+="4;";
	    		break;
	    	case GameEnded:
	    		m+="5;";
	    		break;
	    	}
	    	
	    	for(int i=0; i< s.game.balls.size(); i++) {
	    		int number = s.game.balls.get(i).number;
	    		int x= (int)s.game.balls.get(i).pos[0];
	    		int y= (int)s.game.balls.get(i).pos[1];
	    		
	    		m+=number+","+x+","+y+":";
	    	}
	    	
	    	m+=";";
	    	
	    	m+=s.x+","+s.y+";";
	    	m+=s.vel_vector_x+","+s.vel_vector_y+";";
	    	
	    	String m_p1="";
	    	String m_p2="";
	    	
	    	if(s.game.player1_turn) {
	    		m_p1+="1;";
	    		m_p2+="0;";
	    	}
	    	else {
	    		m_p1+="0;";
	    		m_p2+="1;";
	    	}
	    	
	    	m_p1+=m;
	    	m_p2+=m;
	    	
	    	try {
				s.player1.getOutputStream().write(m_p1.getBytes());
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
				
				running=false;
				return;
			}
	    	
	    	try {
				s.player2.getOutputStream().write(m_p2.getBytes());
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
				
				running=false;
				return;
			}
	    }

}
