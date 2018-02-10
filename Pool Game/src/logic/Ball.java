package logic;

import java.awt.Color;

public class Ball {
	public double pos[];
	public double vel[];
	public double acc[];
	public double diameter;
	public double scale=3.2;
	public Color color;
	public int number;
	

	public Ball(double posX,double posY,double velX,double velY,double diameter, int number){
		this.pos = new double[2];
		this.vel = new double[2];
		this.acc = new double[2];
		
		this.pos[0]=posX;
		this.pos[1]=posY;
		
		this.vel[0]=velX;
		this.vel[1]=velY;
		
		this.acc[0]=0;
		this.acc[1]=0;
		
		this.diameter=diameter;
		
		this.number=number;
		
		if(number>=1 && number <=7) {
			this.color=Color.BLUE;
		}
		else if(number==8) {
			this.color=Color.BLACK;
		}
		else if(number>=9 && number <=15) {
			this.color=Color.MAGENTA;
		}
		else {
			this.color=Color.RED;
		}
	}
}
