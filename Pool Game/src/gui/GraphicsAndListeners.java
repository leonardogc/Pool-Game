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

import logic.Particle;
import logic.Space;
import logic.Space.gameState;

public class GraphicsAndListeners extends JPanel implements KeyListener, MouseListener, MouseMotionListener, ActionListener{
	
	public Space space;
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
		
		space=new Space();
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
		
		
		for(int i=0;i<space.pockets.size();i++){

			g.setColor(Color.BLACK);

			g.fillOval((int)(space.pockets.get(i).pos[0]-space.pocket_diameter/2),
					(int)(space.pockets.get(i).pos[1]-space.pocket_diameter/2),
					(int)(space.pocket_diameter), 
					(int)(space.pocket_diameter));
		}
		
		for(int i=0;i<space.particles.size();i++){

			g.setColor(space.particles.get(i).color);

			g.fillOval((int)(space.particles.get(i).pos[0]-space.particles.get(i).diameter/2),
					(int)(space.particles.get(i).pos[1]-space.particles.get(i).diameter/2),
					(int)(space.particles.get(i).diameter), 
					(int)(space.particles.get(i).diameter));
		}
		
		for(int i=0;i<space.sides.size();i++){

			g.setColor(Color.BLUE);

			g.drawPolygon(new int[] {  (int)space.sides.get(i).p1[0],
										(int)space.sides.get(i).p2[0],
										(int)space.sides.get(i).p3[0],
										(int)space.sides.get(i).p4[0]}, 
					       new int[] {  (int)space.sides.get(i).p1[1],
										(int)space.sides.get(i).p2[1],
										(int)space.sides.get(i).p3[1],
										(int)space.sides.get(i).p4[1]}, 4);
		}


		if(space.state == gameState.ChoosingDir) {
			g.setColor(Color.RED);
			double vector_x=x-space.particles.get(0).pos[0];
			double vector_y=y-space.particles.get(0).pos[1];

			double vector_size=Math.sqrt(Math.pow(vector_x,2)+Math.pow(vector_y,2));

			vector_x/=vector_size;
			vector_y/=vector_size;

			g.drawLine((int)space.particles.get(0).pos[0],(int)space.particles.get(0).pos[1], (int)(space.particles.get(0).pos[0]+vector_x*1500), (int)(space.particles.get(0).pos[1]+vector_y*1500));
		}
		else if(space.state == gameState.ChoosingVel) {
			g.setColor(Color.RED);
			g.drawLine((int)space.particles.get(0).pos[0],(int)space.particles.get(0).pos[1], (int)(space.particles.get(0).pos[0]+vel_vector_x*line_size), (int)(space.particles.get(0).pos[1]+vel_vector_y*line_size));
			g.setColor(Color.GREEN);
			double size= vel_vector_x*(x-space.particles.get(0).pos[0])+vel_vector_y*(y-space.particles.get(0).pos[1]);
			
			if(size > line_size) {
				g.drawLine((int)space.particles.get(0).pos[0],(int)space.particles.get(0).pos[1], (int)(space.particles.get(0).pos[0]+vel_vector_x*line_size), (int)(space.particles.get(0).pos[1]+vel_vector_y*line_size));
			}
			else if(size <= line_size && size > 0) {
				g.drawLine((int)space.particles.get(0).pos[0],(int)space.particles.get(0).pos[1], (int)(space.particles.get(0).pos[0]+vel_vector_x*size), (int)(space.particles.get(0).pos[1]+vel_vector_y*size));
			}
		}else if(space.state == gameState.MovingQueueBall) {
			g.setColor(Color.RED);
			
			g.fillOval((int)(x-space.ball_diameter/2),
					(int)(y-space.ball_diameter/2),
					(int)(space.ball_diameter), 
					(int)(space.ball_diameter));

		}
		else if(space.state==gameState.MovingQueueBallInit) {
			g.setColor(Color.RED);
			if(x < space.table_edge[2]-(space.table_edge[2]-space.table_edge[0])/4) {
				g.fillOval((int)(space.table_edge[2]-(space.table_edge[2]-space.table_edge[0])/4-space.ball_diameter/2),
						(int)(y-space.ball_diameter/2),
						(int)(space.ball_diameter), 
						(int)(space.ball_diameter));
			}
			else {
			g.fillOval((int)(x-space.ball_diameter/2),
					(int)(y-space.ball_diameter/2),
					(int)(space.ball_diameter), 
					(int)(space.ball_diameter));
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		switch(arg0.getKeyCode()){  
		case KeyEvent.VK_LEFT:
			space=new Space();
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
		
		if(space.state == gameState.ChoosingDir) {
			vel_vector_x=x-space.particles.get(0).pos[0];
			vel_vector_y=y-space.particles.get(0).pos[1];
			
			double vector_size=Math.sqrt(Math.pow(vel_vector_x,2)+Math.pow(vel_vector_y,2));
			
			vel_vector_x/=vector_size;
			vel_vector_y/=vector_size;
			space.state = gameState.ChoosingVel;
		}
		else if(space.state == gameState.ChoosingVel) {
			double size= vel_vector_x*(x-space.particles.get(0).pos[0])+vel_vector_y*(y-space.particles.get(0).pos[1]);
			
			if(size > line_size) {
				space.particles.get(0).vel[0]=vel_vector_x*space.max_speed;
				space.particles.get(0).vel[1]=vel_vector_y*space.max_speed;
			}else if(size <= line_size && size > 0){
				space.particles.get(0).vel[0]=vel_vector_x*(size/line_size)*space.max_speed;
				space.particles.get(0).vel[1]=vel_vector_y*(size/line_size)*space.max_speed;
			}
			
			space.hit_ball=false;
			space.hit_player_ball_first=false;
			space.ball_8_in_pocket=false;
			space.ball_0_in_pocket=false;
			space.player_ball_in_pocket=false;
			space.state = gameState.BallsMoving;
		}
		else if(space.state == gameState.MovingQueueBall) {
			boolean valid=true;
			
			for(int i=0; i< space.particles.size(); i++) {
				if(Math.sqrt(Math.pow(space.particles.get(i).pos[0]-x,2) + Math.pow(space.particles.get(i).pos[1]-y,2)) < space.ball_diameter){
					valid=false;
					break;
				}
			}
			
			if( x > space.table_edge[2]-space.ball_diameter/2||
				x < space.table_edge[0]+space.ball_diameter/2||
				y > space.table_edge[3]-space.ball_diameter/2||
				y < space.table_edge[1]+space.ball_diameter/2) {
				valid=false;
			}

			if(valid) {
				space.particles.add(0,new Particle(x,y,0,0,space.ball_diameter,0));
				space.state = gameState.ChoosingDir;
			}
		}
		
		else if(space.state == gameState.MovingQueueBallInit) {
			if(!(x > space.table_edge[2]-space.ball_diameter/2||
					x < space.table_edge[0]+space.ball_diameter/2||
					y > space.table_edge[3]-space.ball_diameter/2||
					y < space.table_edge[1]+space.ball_diameter/2)) {

				if(x < space.table_edge[2]-(space.table_edge[2]-space.table_edge[0])/4) {
					space.particles.add(0,new Particle(space.table_edge[2]-(space.table_edge[2]-space.table_edge[0])/4,y,0,0,space.ball_diameter,0));
					space.state = gameState.ChoosingDir;
				}
				else {
					space.particles.add(0,new Particle(x,y,0,0,space.ball_diameter,0));
					space.state = gameState.ChoosingDir;
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
		if(space.state!=gameState.BallsMoving) {
        	x=e.getX();
        	y=e.getY();
        }
	}

}
