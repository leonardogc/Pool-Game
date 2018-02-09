package io;

public class InputThread extends Thread {
	private boolean running;
	
	public InputThread() {
		running=false;
	}
	
	public void setRunning(boolean running) {
		this.running=running;
	}
	
	@Override
	public void run() {
		while(running) {
			System.out.println("Input");
			try {
				this.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
