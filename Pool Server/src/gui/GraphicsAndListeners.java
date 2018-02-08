package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

import logic.Ball;
import logic.Game;
import logic.Game.gameState;

public class GraphicsAndListeners extends JPanel implements KeyListener, MouseListener, MouseMotionListener, ActionListener{
	
	public Game game;
	public GraphicInterface graphics;
	private int x;
	private int y;
	private LoopThread thread;
	public int pictureNumber; //only used for taking pictures
	public boolean take_pictures;
	private double line_size=300;
	private double vel_vector_x;
	private double vel_vector_y;
	
	
	public GraphicsAndListeners(GraphicInterface g){
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		
		game=new Game();
		this.graphics=g;
		
		pictureNumber=1;
		take_pictures=false;
		
		vel_vector_x=0;
		vel_vector_y=0;
		
		x=0;
    	y=0;
    	
    	thread=new LoopThread(this);
		thread.setRunning(true);
		thread.start();
		
	}
	
	

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		/*g.drawLine((int)space.table_edge[0], (int)space.table_edge[1], (int)space.table_edge[2], (int)space.table_edge[1]);
		g.drawLine((int)space.table_edge[0], (int)space.table_edge[3], (int)space.table_edge[2], (int)space.table_edge[3]);
		g.drawLine((int)space.table_edge[0], (int)space.table_edge[1], (int)space.table_edge[0], (int)space.table_edge[3]);
		g.drawLine((int)space.table_edge[2], (int)space.table_edge[1], (int)space.table_edge[2], (int)space.table_edge[3]);*/
		
		
		for(int i=0;i<game.pockets.size();i++){

			g.setColor(Color.BLACK);

			g.fillOval((int)(game.pockets.get(i).pos[0]-game.pocket_diameter/2),
					(int)(game.pockets.get(i).pos[1]-game.pocket_diameter/2),
					(int)(game.pocket_diameter), 
					(int)(game.pocket_diameter));
		}
		
		for(int i=0;i<game.balls.size();i++){

			g.setColor(game.balls.get(i).color);

			g.fillOval((int)(game.balls.get(i).pos[0]-game.balls.get(i).diameter/2),
					(int)(game.balls.get(i).pos[1]-game.balls.get(i).diameter/2),
					(int)(game.balls.get(i).diameter), 
					(int)(game.balls.get(i).diameter));
		}
		
		for(int i=0;i<game.sides.size();i++){

			g.setColor(Color.BLUE);

			g.drawPolygon(new int[] {  (int)game.sides.get(i).p1[0],
										(int)game.sides.get(i).p2[0],
										(int)game.sides.get(i).p3[0],
										(int)game.sides.get(i).p4[0]}, 
					       new int[] {  (int)game.sides.get(i).p1[1],
										(int)game.sides.get(i).p2[1],
										(int)game.sides.get(i).p3[1],
										(int)game.sides.get(i).p4[1]}, 4);
		}


		if(game.state == gameState.ChoosingDir) {
			g.setColor(Color.RED);
			double vector_x=x-game.balls.get(0).pos[0];
			double vector_y=y-game.balls.get(0).pos[1];

			double vector_size=Math.sqrt(Math.pow(vector_x,2)+Math.pow(vector_y,2));

			vector_x/=vector_size;
			vector_y/=vector_size;

			g.drawLine((int)game.balls.get(0).pos[0],(int)game.balls.get(0).pos[1], (int)(game.balls.get(0).pos[0]+vector_x*1500), (int)(game.balls.get(0).pos[1]+vector_y*1500));
		}
		else if(game.state == gameState.ChoosingVel) {
			g.setColor(Color.RED);
			g.drawLine((int)game.balls.get(0).pos[0],(int)game.balls.get(0).pos[1], (int)(game.balls.get(0).pos[0]+vel_vector_x*line_size), (int)(game.balls.get(0).pos[1]+vel_vector_y*line_size));
			g.setColor(Color.GREEN);
			double size= vel_vector_x*(x-game.balls.get(0).pos[0])+vel_vector_y*(y-game.balls.get(0).pos[1]);
			
			if(size > line_size) {
				g.drawLine((int)game.balls.get(0).pos[0],(int)game.balls.get(0).pos[1], (int)(game.balls.get(0).pos[0]+vel_vector_x*line_size), (int)(game.balls.get(0).pos[1]+vel_vector_y*line_size));
			}
			else if(size <= line_size && size > 0) {
				g.drawLine((int)game.balls.get(0).pos[0],(int)game.balls.get(0).pos[1], (int)(game.balls.get(0).pos[0]+vel_vector_x*size), (int)(game.balls.get(0).pos[1]+vel_vector_y*size));
			}
		}else if(game.state == gameState.MovingQueueBall) {
			g.setColor(Color.RED);
			
			g.fillOval((int)(x-game.ball_diameter/2),
					(int)(y-game.ball_diameter/2),
					(int)(game.ball_diameter), 
					(int)(game.ball_diameter));

		}
		else if(game.state==gameState.MovingQueueBallInit) {
			g.setColor(Color.RED);
			if(x < game.table_edge[2]-(game.table_edge[2]-game.table_edge[0])/4) {
				g.fillOval((int)(game.table_edge[2]-(game.table_edge[2]-game.table_edge[0])/4-game.ball_diameter/2),
						(int)(y-game.ball_diameter/2),
						(int)(game.ball_diameter), 
						(int)(game.ball_diameter));
			}
			else {
			g.fillOval((int)(x-game.ball_diameter/2),
					(int)(y-game.ball_diameter/2),
					(int)(game.ball_diameter), 
					(int)(game.ball_diameter));
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		switch(arg0.getKeyCode()){  
		case KeyEvent.VK_LEFT:
			game=new Game();
			break;
		case KeyEvent.VK_P:
			this.take_pictures=!this.take_pictures;
			
			if(this.take_pictures) {
				System.out.println("Taking Pictures");
			}
			else {
				System.out.println("Stopped Taking Pictures");
			}
			break;
		}
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
	
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	


	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}


	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}


	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		graphics.panel.requestFocusInWindow();
		
		if(game.state == gameState.ChoosingDir) {
			vel_vector_x=x-game.balls.get(0).pos[0];
			vel_vector_y=y-game.balls.get(0).pos[1];
			
			double vector_size=Math.sqrt(Math.pow(vel_vector_x,2)+Math.pow(vel_vector_y,2));
			
			vel_vector_x/=vector_size;
			vel_vector_y/=vector_size;
			game.state = gameState.ChoosingVel;
		}
		else if(game.state == gameState.ChoosingVel) {
			double size= vel_vector_x*(x-game.balls.get(0).pos[0])+vel_vector_y*(y-game.balls.get(0).pos[1]);
			
			if(size > line_size) {
				game.balls.get(0).vel[0]=vel_vector_x*game.max_speed;
				game.balls.get(0).vel[1]=vel_vector_y*game.max_speed;
			}else if(size <= line_size && size > 0){
				game.balls.get(0).vel[0]=vel_vector_x*(size/line_size)*game.max_speed;
				game.balls.get(0).vel[1]=vel_vector_y*(size/line_size)*game.max_speed;
			}
			
			game.hit_ball=false;
			game.hit_player_ball_first=false;
			game.ball_8_in_pocket=false;
			game.ball_0_in_pocket=false;
			game.player_ball_in_pocket=false;
			game.state = gameState.BallsMoving;
		}
		else if(game.state == gameState.MovingQueueBall) {
			boolean valid=true;
			
			for(int i=0; i< game.balls.size(); i++) {
				if(Math.sqrt(Math.pow(game.balls.get(i).pos[0]-x,2) + Math.pow(game.balls.get(i).pos[1]-y,2)) < game.ball_diameter){
					valid=false;
					break;
				}
			}
			
			if( x > game.table_edge[2]-game.ball_diameter/2||
				x < game.table_edge[0]+game.ball_diameter/2||
				y > game.table_edge[3]-game.ball_diameter/2||
				y < game.table_edge[1]+game.ball_diameter/2) {
				valid=false;
			}

			if(valid) {
				game.balls.add(0,new Ball(x,y,0,0,game.ball_diameter,0));
				game.state = gameState.ChoosingDir;
			}
		}
		
		else if(game.state == gameState.MovingQueueBallInit) {
			if(!(x > game.table_edge[2]-game.ball_diameter/2||
					x < game.table_edge[0]+game.ball_diameter/2||
					y > game.table_edge[3]-game.ball_diameter/2||
					y < game.table_edge[1]+game.ball_diameter/2)) {

				if(x < game.table_edge[2]-(game.table_edge[2]-game.table_edge[0])/4) {
					game.balls.add(0,new Ball(game.table_edge[2]-(game.table_edge[2]-game.table_edge[0])/4,y,0,0,game.ball_diameter,0));
					game.state = gameState.ChoosingDir;
				}
				else {
					game.balls.add(0,new Ball(x,y,0,0,game.ball_diameter,0));
					game.state = gameState.ChoosingDir;
				}
			}
		}
	}


	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
	}
	
	public void takePicture() {
	    BufferedImage img = new BufferedImage(graphics.panel.getWidth(), graphics.panel.getHeight(), BufferedImage.TYPE_INT_RGB);
	    graphics.panel.print(img.getGraphics()); // or: panel.printAll(...);
	    try {
	        ImageIO.write(img, "png", new File("images/"+pictureNumber+".png"));
	        pictureNumber++;
	    }
	    catch (IOException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }
	}


	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}



	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
        	x=e.getX();
        	y=e.getY();
	}

}
