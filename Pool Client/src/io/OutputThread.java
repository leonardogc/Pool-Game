package io;

public class OutputThread extends Thread {
	private boolean running;

	public OutputThread() {
		running=false;
	}

	public void setRunning(boolean running) {
		this.running=running;
	}

	@Override
	public void run() {
		while(running) {
			System.out.println("Output");
			try {
				this.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
