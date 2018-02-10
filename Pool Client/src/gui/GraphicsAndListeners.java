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

import io.Client;
import io.Client.gameState;
import logic.Ball;

public class GraphicsAndListeners extends JPanel implements KeyListener, MouseListener, MouseMotionListener, ActionListener{
	
	public Client c;
	public GraphicInterface graphics;
	private LoopThread thread;	
	
	public GraphicsAndListeners(GraphicInterface graphics,Client c){
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		
		this.c = c;
		this.graphics=graphics;
		
    	
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
		
		
		for(int i=0;i<c.table.pockets.size();i++){

			g.setColor(Color.BLACK);

			g.fillOval((int)(c.table.pockets.get(i).pos[0]-c.table.pocket_diameter/2),
					(int)(c.table.pockets.get(i).pos[1]-c.table.pocket_diameter/2),
					(int)(c.table.pocket_diameter), 
					(int)(c.table.pocket_diameter));
		}
		
		for(int i=0;i<c.balls.size();i++){

			g.setColor(c.balls.get(i).color);

			g.fillOval((int)(c.balls.get(i).pos[0]-c.balls.get(i).diameter/2),
					(int)(c.balls.get(i).pos[1]-c.balls.get(i).diameter/2),
					(int)(c.balls.get(i).diameter), 
					(int)(c.balls.get(i).diameter));
		}
		
		for(int i=0;i<c.table.sides.size();i++){

			g.setColor(Color.BLUE);

			g.drawPolygon(new int[] {  (int)c.table.sides.get(i).p1[0],
										(int)c.table.sides.get(i).p2[0],
										(int)c.table.sides.get(i).p3[0],
										(int)c.table.sides.get(i).p4[0]}, 
					       new int[] {  (int)c.table.sides.get(i).p1[1],
										(int)c.table.sides.get(i).p2[1],
										(int)c.table.sides.get(i).p3[1],
										(int)c.table.sides.get(i).p4[1]}, 4);
		}

			if(c.state == gameState.ChoosingDir) {
				g.setColor(Color.RED);
				double vector_x=c.x-c.balls.get(0).pos[0];
				double vector_y=c.y-c.balls.get(0).pos[1];

				double vector_size=Math.sqrt(Math.pow(vector_x,2)+Math.pow(vector_y,2));

				vector_x/=vector_size;
				vector_y/=vector_size;

				g.drawLine((int)c.balls.get(0).pos[0],(int)c.balls.get(0).pos[1], (int)(c.balls.get(0).pos[0]+vector_x*1500), (int)(c.balls.get(0).pos[1]+vector_y*1500));
			}
			else if(c.state == gameState.ChoosingVel) {
				g.setColor(Color.RED);
				g.drawLine((int)c.balls.get(0).pos[0],(int)c.balls.get(0).pos[1], (int)(c.balls.get(0).pos[0]+c.vel_vector_x*c.line_size), (int)(c.balls.get(0).pos[1]+c.vel_vector_y*c.line_size));
				g.setColor(Color.GREEN);
				double size= c.vel_vector_x*(c.x-c.balls.get(0).pos[0])+c.vel_vector_y*(c.y-c.balls.get(0).pos[1]);

				if(size > c.line_size) {
					g.drawLine((int)c.balls.get(0).pos[0],(int)c.balls.get(0).pos[1], (int)(c.balls.get(0).pos[0]+c.vel_vector_x*c.line_size), (int)(c.balls.get(0).pos[1]+c.vel_vector_y*c.line_size));
				}
				else if(size <= c.line_size && size > 0) {
					g.drawLine((int)c.balls.get(0).pos[0],(int)c.balls.get(0).pos[1], (int)(c.balls.get(0).pos[0]+c.vel_vector_x*size), (int)(c.balls.get(0).pos[1]+c.vel_vector_y*size));
				}
			}else if(c.state == gameState.MovingQueueBall) {
				g.setColor(Color.RED);

				g.fillOval((int)(c.x-c.table.ball_diameter/2),
						(int)(c.y-c.table.ball_diameter/2),
						(int)(c.table.ball_diameter), 
						(int)(c.table.ball_diameter));

			}
			else if(c.state==gameState.MovingQueueBallInit) {
				g.setColor(Color.RED);
				if(c.x < c.table.table_edge[2]-(c.table.table_edge[2]-c.table.table_edge[0])/4) {
					g.fillOval((int)(c.table.table_edge[2]-(c.table.table_edge[2]-c.table.table_edge[0])/4-c.table.ball_diameter/2),
							(int)(c.y-c.table.ball_diameter/2),
							(int)(c.table.ball_diameter), 
							(int)(c.table.ball_diameter));
				}
				else {
					g.fillOval((int)(c.x-c.table.ball_diameter/2),
							(int)(c.y-c.table.ball_diameter/2),
							(int)(c.table.ball_diameter), 
							(int)(c.table.ball_diameter));
				}
			}
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		
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
		if(c.turn==1) {
			if(c.state == gameState.ChoosingDir) {
					try {
						c.server.getOutputStream().write((c.x+","+c.y+",1;").getBytes());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
			else if(c.state == gameState.ChoosingVel) {
				try {
					c.server.getOutputStream().write((c.x+","+c.y+",1;").getBytes());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(c.state == gameState.MovingQueueBall) {
				try {
					c.server.getOutputStream().write((c.x+","+c.y+",1;").getBytes());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			else if(c.state == gameState.MovingQueueBallInit) {
				try {
					c.server.getOutputStream().write((c.x+","+c.y+",1;").getBytes());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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
	

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}



	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		if(c.turn==1 && (c.state==gameState.ChoosingDir || 
						 c.state==gameState.ChoosingVel ||
						 c.state==gameState.MovingQueueBall ||
						 c.state==gameState.MovingQueueBallInit)) {
			
			c.x=e.getX();
			c.y=e.getY();
        	
        	try {
				c.server.getOutputStream().write((c.x+","+c.y+",0;").getBytes());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
        	
	}

}
