package logic;

import java.awt.Color;

public class Ball {
	public int pos[];
	public int diameter;
	public Color color;
	public int number;
	

	public Ball(int posX, int posY, int diameter, int number){
		this.pos = new int[2];
		
		this.pos[0]=posX;
		this.pos[1]=posY;
	
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
