package logic;

import java.util.Collections;
import java.util.Random;
import java.util.Vector;


public class Game {
	
public Vector<Ball> balls;
public Vector<Pocket> pockets;
public Vector<TableSide> sides;
public Random rand;
public double[] table_edge;

public double ball_side_coefficientOfRestitution=0.75; //0.6-0.9
public double ball_ball_coefficientOfRestitution=0.95; //0.92-0.98
public double ball_cloth_coefficientOfFriction=0.01;//0.005-0.015

public double ball_diameter;
public double m_to_p;
public double max_speed;
public double pocket_diameter;
public double table_side_thickness;
public double middle_hole_opening_size;

public int[] screen_size;

public boolean balls_stopped;
public gameState state;

public boolean hit_ball;
public boolean hit_player_ball_first;
public boolean ball_8_in_pocket;
public boolean ball_0_in_pocket;
public boolean player_ball_in_pocket;

public int balls_left_group_1;
public int balls_left_group_2;

public group_of_balls player1;
public boolean player1_turn;

//real ball size 5.7 cm 
//real table size 2.84 m x 1.42 m
//max speed: 8 m/s

public enum gameState{
	BallsMoving, ChoosingDir, ChoosingVel, MovingQueueBall, MovingQueueBallInit, GameEnded
}

public enum group_of_balls{
	Group_1, Group_2, Not_picked
}


public Game(double ball_diameter, int[] screen_size){
	this.screen_size=screen_size;
	this.ball_diameter=ball_diameter;
	
	this.m_to_p=ball_diameter/0.057;
	this.max_speed=8*m_to_p;
	this.pocket_diameter=2*ball_diameter;
	this.table_side_thickness=pocket_diameter/2;
	this.middle_hole_opening_size=1.4*pocket_diameter;
	
	rand=new Random();
	balls=new Vector<Ball>();
	pockets=new Vector<Pocket>();
	sides=new Vector<TableSide>();
	
	balls_stopped=true;
	state = gameState.MovingQueueBallInit;
	player1_turn=true;
	player1=group_of_balls.Not_picked;
	
	hit_ball=false;
	hit_player_ball_first=false;
	ball_8_in_pocket=false;
	ball_0_in_pocket=false;
	player_ball_in_pocket=false;
	
	balls_left_group_1=7;
	balls_left_group_2=7;
	
	table_edge=new double[]{0,0,2.84*m_to_p,1.42*m_to_p};
	
	double dx=(screen_size[0]-table_edge[2])/2;
	double dy=(screen_size[1]-table_edge[3])/2;
	
	pockets.add(new Pocket(table_edge[0]-table_side_thickness+pocket_diameter/3+dx,table_edge[1]-table_side_thickness+pocket_diameter/3+dy));
	pockets.add(new Pocket(table_edge[2]/2+dx,table_edge[1]-table_side_thickness+dy));
	pockets.add(new Pocket(table_edge[2]+table_side_thickness-pocket_diameter/3+dx,table_edge[1]-table_side_thickness+pocket_diameter/3+dy));
	pockets.add(new Pocket(table_edge[2]+table_side_thickness-pocket_diameter/3+dx,table_edge[3]+table_side_thickness-pocket_diameter/3+dy));
	pockets.add(new Pocket(table_edge[2]/2+dx,table_edge[3]+table_side_thickness+dy));
	pockets.add(new Pocket(table_edge[0]-table_side_thickness+pocket_diameter/3+dx,table_edge[3]+table_side_thickness-pocket_diameter/3+dy));

	sides.add(new TableSide(table_edge[0]-table_side_thickness-(pocket_diameter/2)*Math.cos(Math.PI/4)+dx,
							table_edge[1]-table_side_thickness+(pocket_diameter/2)*Math.cos(Math.PI/4)+dy,
							table_edge[0]+dx, 
							table_edge[1]+pocket_diameter*Math.cos(Math.PI/4)+dy,
							table_edge[0]+dx,
							table_edge[3]-pocket_diameter*Math.cos(Math.PI/4)+dy,
							table_edge[0]-table_side_thickness-(pocket_diameter/2)*Math.cos(Math.PI/4)+dx, 
							table_edge[3]+table_side_thickness-(pocket_diameter/2)*Math.cos(Math.PI/4)+dy));
	
	sides.add(new TableSide(table_edge[0]-table_side_thickness+(pocket_diameter/2)*Math.cos(Math.PI/4)+dx,
							table_edge[1]-table_side_thickness-(pocket_diameter/2)*Math.cos(Math.PI/4)+dy,
							table_edge[2]/2-Math.sqrt(Math.pow(pocket_diameter/2, 2)-Math.pow(Math.cos(Math.PI/4)*pocket_diameter/2, 2))+dx,
							table_edge[1]-table_side_thickness-(pocket_diameter/2)*Math.cos(Math.PI/4)+dy,
							table_edge[2]/2-middle_hole_opening_size/2+dx,
							table_edge[1]+dy,
							table_edge[0]+pocket_diameter*Math.cos(Math.PI/4)+dx, 
							table_edge[1]+dy));
	
	sides.add(new TableSide(table_edge[2]/2+Math.sqrt(Math.pow(pocket_diameter/2, 2)-Math.pow(Math.cos(Math.PI/4)*pocket_diameter/2, 2))+dx,
							table_edge[1]-table_side_thickness-(pocket_diameter/2)*Math.cos(Math.PI/4)+dy,
							table_edge[2]+table_side_thickness-(pocket_diameter/2)*Math.cos(Math.PI/4)+dx,
							table_edge[1]-table_side_thickness-(pocket_diameter/2)*Math.cos(Math.PI/4)+dy,
							table_edge[2]-pocket_diameter*Math.cos(Math.PI/4)+dx,
							table_edge[1]+dy,
							table_edge[2]/2+middle_hole_opening_size/2+dx,
							table_edge[1]+dy));
	
	sides.add(new TableSide(table_edge[2]+dx,
							table_edge[1]+pocket_diameter*Math.cos(Math.PI/4)+dy,
							table_edge[2]+table_side_thickness+(pocket_diameter/2)*Math.cos(Math.PI/4)+dx, 
							table_edge[1]-table_side_thickness+(pocket_diameter/2)*Math.cos(Math.PI/4)+dy,
							table_edge[2]+table_side_thickness+(pocket_diameter/2)*Math.cos(Math.PI/4)+dx,
							table_edge[3]+table_side_thickness-(pocket_diameter/2)*Math.cos(Math.PI/4)+dy,
							table_edge[2]+dx, 
							table_edge[3]-pocket_diameter*Math.cos(Math.PI/4)+dy));
	
	sides.add(new TableSide(table_edge[2]/2+middle_hole_opening_size/2+dx,
							table_edge[3]+dy,
							table_edge[2]-pocket_diameter*Math.cos(Math.PI/4)+dx,
							table_edge[3]+dy,
							table_edge[2]+table_side_thickness-(pocket_diameter/2)*Math.cos(Math.PI/4)+dx,
							table_edge[3]+table_side_thickness+(pocket_diameter/2)*Math.cos(Math.PI/4)+dy,
							table_edge[2]/2+Math.sqrt(Math.pow(pocket_diameter/2, 2)-Math.pow(Math.cos(Math.PI/4)*pocket_diameter/2, 2))+dx,
							table_edge[3]+table_side_thickness+(pocket_diameter/2)*Math.cos(Math.PI/4)+dy));
	
	sides.add(new TableSide(table_edge[0]+pocket_diameter*Math.cos(Math.PI/4)+dx,
							table_edge[3]+dy,
							table_edge[2]/2-middle_hole_opening_size/2+dx,
							table_edge[3]+dy,
							table_edge[2]/2-Math.sqrt(Math.pow(pocket_diameter/2, 2)-Math.pow(Math.cos(Math.PI/4)*pocket_diameter/2, 2))+dx,
							table_edge[3]+table_side_thickness+(pocket_diameter/2)*Math.cos(Math.PI/4)+dy,
							table_edge[0]-table_side_thickness+(pocket_diameter/2)*Math.cos(Math.PI/4)+dx,
							table_edge[3]+table_side_thickness+(pocket_diameter/2)*Math.cos(Math.PI/4)+dy));
	
	Vector<Integer>options=new 	Vector<Integer>();
	
	options.add(1);
	options.add(2);
	options.add(3);
	options.add(4);
	options.add(5);
	options.add(6);
	options.add(7);
	options.add(9);
	options.add(10);
	options.add(11);
	options.add(12);
	options.add(13);
	options.add(14);
	options.add(15);
	
	int selection;
	
	selection=rand.nextInt(options.size());
	balls.add(new Ball(table_edge[2]/4+dx,table_edge[3]/2+dy,0,0,ball_diameter,options.get(selection)));
	options.remove(selection);
	
	selection=rand.nextInt(options.size());
	balls.add(new Ball(table_edge[2]/4-ball_diameter*Math.cos(Math.PI/6)+dx,table_edge[3]/2-ball_diameter*Math.sin(Math.PI/6)+dy,0,0,ball_diameter,options.get(selection)));
	options.remove(selection);
	selection=rand.nextInt(options.size());
	balls.add(new Ball(table_edge[2]/4-ball_diameter*Math.cos(Math.PI/6)+dx,table_edge[3]/2+ball_diameter*Math.sin(Math.PI/6)+dy,0,0,ball_diameter,options.get(selection)));
	options.remove(selection);
	
	selection=rand.nextInt(options.size());
	balls.add(new Ball(table_edge[2]/4-2*ball_diameter*Math.cos(Math.PI/6)+dx,table_edge[3]/2-2*ball_diameter*Math.sin(Math.PI/6)+dy,0,0,ball_diameter,options.get(selection)));
	options.remove(selection);
	balls.add(new Ball(table_edge[2]/4-2*ball_diameter*Math.cos(Math.PI/6)+dx,table_edge[3]/2+dy,0,0,ball_diameter,8));
	selection=rand.nextInt(options.size());
	balls.add(new Ball(table_edge[2]/4-2*ball_diameter*Math.cos(Math.PI/6)+dx,table_edge[3]/2+2*ball_diameter*Math.sin(Math.PI/6)+dy,0,0,ball_diameter,options.get(selection)));
	options.remove(selection);
	
	selection=rand.nextInt(options.size());
	balls.add(new Ball(table_edge[2]/4-3*ball_diameter*Math.cos(Math.PI/6)+dx,table_edge[3]/2-3*ball_diameter*Math.sin(Math.PI/6)+dy,0,0,ball_diameter,options.get(selection)));
	options.remove(selection);
	selection=rand.nextInt(options.size());
	balls.add(new Ball(table_edge[2]/4-3*ball_diameter*Math.cos(Math.PI/6)+dx,table_edge[3]/2-ball_diameter*Math.sin(Math.PI/6)+dy,0,0,ball_diameter,options.get(selection)));
	options.remove(selection);
	selection=rand.nextInt(options.size());
	balls.add(new Ball(table_edge[2]/4-3*ball_diameter*Math.cos(Math.PI/6)+dx,table_edge[3]/2+ball_diameter*Math.sin(Math.PI/6)+dy,0,0,ball_diameter,options.get(selection)));
	options.remove(selection);
	selection=rand.nextInt(options.size());
	balls.add(new Ball(table_edge[2]/4-3*ball_diameter*Math.cos(Math.PI/6)+dx,table_edge[3]/2+3*ball_diameter*Math.sin(Math.PI/6)+dy,0,0,ball_diameter,options.get(selection)));
	options.remove(selection);
	
	selection=rand.nextInt(options.size());
	balls.add(new Ball(table_edge[2]/4-4*ball_diameter*Math.cos(Math.PI/6)+dx,table_edge[3]/2-4*ball_diameter*Math.sin(Math.PI/6)+dy,0,0,ball_diameter,options.get(selection)));
	options.remove(selection);
	selection=rand.nextInt(options.size());
	balls.add(new Ball(table_edge[2]/4-4*ball_diameter*Math.cos(Math.PI/6)+dx,table_edge[3]/2-2*ball_diameter*Math.sin(Math.PI/6)+dy,0,0,ball_diameter,options.get(selection)));
	options.remove(selection);
	selection=rand.nextInt(options.size());
	balls.add(new Ball(table_edge[2]/4-4*ball_diameter*Math.cos(Math.PI/6)+dx,table_edge[3]/2+dy,0,0,ball_diameter,options.get(selection)));
	options.remove(selection);
	selection=rand.nextInt(options.size());
	balls.add(new Ball(table_edge[2]/4-4*ball_diameter*Math.cos(Math.PI/6)+dx,table_edge[3]/2+2*ball_diameter*Math.sin(Math.PI/6)+dy,0,0,ball_diameter,options.get(selection)));
	options.remove(selection);
	selection=rand.nextInt(options.size());
	balls.add(new Ball(table_edge[2]/4-4*ball_diameter*Math.cos(Math.PI/6)+dx,table_edge[3]/2+4*ball_diameter*Math.sin(Math.PI/6)+dy,0,0,ball_diameter,options.get(selection)));
	options.remove(selection);
	
	Collections.shuffle(balls);
	
	table_edge[0]+=dx;
	table_edge[1]+=dy;
	table_edge[2]+=dx;
	table_edge[3]+=dy;
}

public void update(double t){
	updateAcc_Vel_Pos(t);
	update_ball_collisions();
	update_table_collisions();
	update_hole_collisions();
	update_game_state();
}

public void update_game_state() {
	if(balls_stopped && state == gameState.BallsMoving) {
		
		if(ball_8_in_pocket) {
			state=gameState.GameEnded;
			return;
		}
		
		if(ball_0_in_pocket) {
			player1_turn=!player1_turn;
			System.out.println("Player: "+ (player1_turn? 1 : 2));
			state=gameState.MovingQueueBall;
			return;
		}
		
		if(!hit_player_ball_first) {
			for(int i =0 ; i< balls.size(); i++) {
				if(balls.get(i).number==0) {
					balls.remove(i);
					break;
				}
			}
			
			player1_turn=!player1_turn;
			System.out.println("Player: "+ (player1_turn? 1 : 2));
			state=gameState.MovingQueueBall;
			return;
		}
		
		if(!player_ball_in_pocket) {
			player1_turn=!player1_turn;
			System.out.println("Player: "+ (player1_turn? 1 : 2));
			state=gameState.ChoosingDir;
			return;
		}
		
		state=gameState.ChoosingDir;
	}
}

public void update_hole_collisions() {
	for(int i=0;i< balls.size();i++){
		for(int i2=0; i2 < pockets.size(); i2++) {
			if(Math.sqrt(Math.pow(balls.get(i).pos[0]-pockets.get(i2).pos[0],2) + Math.pow(balls.get(i).pos[1]-pockets.get(i2).pos[1],2)) < pocket_diameter/2) {
				
				if(player1 == group_of_balls.Not_picked) {
					if(player1_turn) {
						if(balls.get(i).number>=1 && balls.get(i).number<=7) {
							player1=group_of_balls.Group_1;
							player_ball_in_pocket=true;
						}else if(balls.get(i).number>=9 && balls.get(i).number<=15) {
							player1=group_of_balls.Group_2;
							player_ball_in_pocket=true;
						}
					}
					else {
						if(balls.get(i).number>=1 && balls.get(i).number<=7) {
							player1=group_of_balls.Group_2;
							player_ball_in_pocket=true;
						}else if(balls.get(i).number>=9 && balls.get(i).number<=15) {
							player1=group_of_balls.Group_1;
							player_ball_in_pocket=true;
						}
					}
				}
				else {
					if(player1_turn) {
						if(player1 == group_of_balls.Group_1) {
							if(balls.get(i).number>=1 && balls.get(i).number<=7) {
								player_ball_in_pocket=true;
							}
						}else if(player1 == group_of_balls.Group_2) {
							if(balls.get(i).number>=9 && balls.get(i).number<=15) {
								player_ball_in_pocket=true;
							}
						}
					}
					else {
						if(player1 == group_of_balls.Group_1) {
							if(balls.get(i).number>=9 && balls.get(i).number<=15) {
								player_ball_in_pocket=true;
							}
						}else if(player1 == group_of_balls.Group_2) {
							if(balls.get(i).number>=1 && balls.get(i).number<=7) {
								player_ball_in_pocket=true;
							}
						}
					}
				}

				if(balls.get(i).number == 0) {
					ball_0_in_pocket=true;
				}
				else if(balls.get(i).number == 8) {
					ball_8_in_pocket=true;
				}
				
				if(balls.get(i).number>=1 && balls.get(i).number<=7) {
					balls_left_group_1--;
				}else if(balls.get(i).number>=9 && balls.get(i).number<=15) {
					balls_left_group_2--;
				}
				
				balls.remove(i);
				i--;
				break;
			}
		}
	}
}

public void update_table_collisions() {
	for(int i=0;i< balls.size();i++){
		for(int i2=0; i2 < sides.size(); i2++) {
			check_table_collision(balls.get(i), sides.get(i2));
		}
	}
}

public void check_table_collision(Ball b, TableSide s) {
	if(check_point_table_collision(s.p1, s.p4, s.p2, b)) {
		return;
	}
	if(check_point_table_collision(s.p2, s.p1, s.p3, b)) {
		return;
	}
	if(check_point_table_collision(s.p3, s.p2, s.p4, b)) {
		return;
	}
	if(check_point_table_collision(s.p4, s.p3, s.p1, b)) {
		return;
	}
	
	if(check_line_table_collision(s.p4, s.p1, b)) {
		return;
	}
	if(check_line_table_collision(s.p1, s.p2, b)) {
		return;
	}
	if(check_line_table_collision(s.p2, s.p3, b)) {
		return;
	}
	if(check_line_table_collision(s.p3, s.p4, b)) {
		return;
	}
}

public boolean check_line_table_collision(double[] p4,double[] p1, Ball b) {
	double vector_x=0;
	double vector_y=0;
	
	double vector_x2=0;
	double vector_y2=0;
	
	double vector_t=0;
	
	double eVel=0;
	
	double x=0;
	double y=0;
	double k=0;
	
	double distance;
	
	vector_x=p1[0]-p4[0];
	vector_y=p1[1]-p4[1];

	vector_x2=vector_y;
	vector_y2=-vector_x;

	vector_t=Math.sqrt(Math.pow(vector_x2,2) + Math.pow(vector_y2,2));

	vector_x2/=vector_t;
	vector_y2/=vector_t;

	eVel=b.vel[0]*vector_x2+b.vel[1]*vector_y2;

	if(eVel < 0) {
		k=(vector_y2*(b.pos[0]-p4[0])+vector_x2*(p4[1]-b.pos[1]))/(vector_y2*(p1[0]-p4[0])-vector_x2*(p1[1]-p4[1]));

		x=p4[0]+k*(p1[0]-p4[0]);
		y=p4[1]+k*(p1[1]-p4[1]);

		if(Math.sqrt(Math.pow(b.pos[0]-x,2) + Math.pow(b.pos[1]-y,2)) < ball_diameter/2) {
			distance=Math.sqrt(Math.pow(p1[0]-p4[0],2) + Math.pow(p1[1]-p4[1],2));
			if(Math.sqrt(Math.pow(p1[0]-x,2) + Math.pow(p1[1]-y,2)) <= distance && Math.sqrt(Math.pow(p4[0]-x,2) + Math.pow(p4[1]-y,2)) <= distance) {
				eVel*=ball_side_coefficientOfRestitution;
				b.vel[0]+=-2*eVel*vector_x2;
				b.vel[1]+=-2*eVel*vector_y2;
				return true;
			}
		}
	}
	
	return false;
}


public boolean check_point_table_collision(double[] p1,double[] p4,double[] p2, Ball b) {
	double ex=0;
	double ey=0;
	double et=0;
	
	double eVel=0;
	
	double vector_x=0;
	double vector_y=0;
	
	double vector_x2=0;
	double vector_y2=0;
	
	double vector_x3=0;
	double vector_y3=0;
	
	double angle1=0;
	double angle2=0;
	double angle3=0;
	
	if(Math.sqrt(Math.pow(p1[0]-b.pos[0],2) + Math.pow(p1[1]-b.pos[1],2)) < ball_diameter/2) {
		ex=p1[0]-b.pos[0];
		ey=p1[1]-b.pos[1];

		et=Math.sqrt(Math.pow(ex,2) + Math.pow(ey,2));

		ex/=et;
		ey/=et;

		eVel=b.vel[0]*ex+b.vel[1]*ey;

		vector_x=p1[0]-p4[0];
		vector_y=p1[1]-p4[1];

		vector_x2=-vector_y;
		vector_y2=vector_x;

		angle1=Math.acos((ex*eVel*vector_x2+ey*eVel*vector_y2)/(Math.sqrt(Math.pow(ex*eVel,2) + Math.pow(ey*eVel,2))*Math.sqrt(Math.pow(vector_x2,2) + Math.pow(vector_y2,2))));

		vector_x=p1[0]-p2[0];
		vector_y=p1[1]-p2[1];

		vector_x3=vector_y;
		vector_y3=-vector_x;

		angle2=Math.acos((ex*eVel*vector_x3+ey*eVel*vector_y3)/(Math.sqrt(Math.pow(ex*eVel,2) + Math.pow(ey*eVel,2))*Math.sqrt(Math.pow(vector_x3,2) + Math.pow(vector_y3,2))));

		angle3=Math.acos((vector_x2*vector_x3+vector_y2*vector_y3)/(Math.sqrt(Math.pow(vector_x2,2) + Math.pow(vector_y2,2))*Math.sqrt(Math.pow(vector_x3,2) + Math.pow(vector_y3,2))));

		if(Math.abs(angle3-angle1-angle2)< 0.2) {
			eVel*=ball_side_coefficientOfRestitution;
			b.vel[0]+=-2*eVel*ex;
			b.vel[1]+=-2*eVel*ey;
			return true;
		}
	}
	
	return false;
}


public void update_ball_collisions(){
	double dx;
	double dy;
	double r;
	double x;
	double y;
	double ex[]=new double[2];
	double v1_ex;
	double v2_ex;
	double v1_ex_after=0;
	double v2_ex_after=0;
	double targetDistance;
	double proportion1=0.5;
	double proportion2=0.5;
	double extra = 0.01;
	int ball_number=-1;


	for(int i =0;i< balls.size();i++){
		for(int i2=i+1;i2<balls.size();i2++){	

			dx=balls.get(i).pos[0]-balls.get(i2).pos[0];
			dy=balls.get(i).pos[1]-balls.get(i2).pos[1];
			r=Math.sqrt(dx*dx+dy*dy);
			targetDistance=(balls.get(i).diameter+balls.get(i2).diameter)/2;

			if(r<targetDistance){

				ex[0]=dx/r;
				ex[1]=dy/r;

				v1_ex=balls.get(i).vel[0]*ex[0]+balls.get(i).vel[1]*ex[1];

				v2_ex=balls.get(i2).vel[0]*ex[0]+balls.get(i2).vel[1]*ex[1];
				
				
				v2_ex_after=v1_ex;
				v1_ex_after=v2_ex;
				
				v1_ex_after*=ball_ball_coefficientOfRestitution;
				v2_ex_after*=ball_ball_coefficientOfRestitution;
				
				if(Math.abs(v1_ex_after)==Math.abs(v2_ex_after)) {
					proportion1=0.5;
					proportion2=0.5;
				}
				else if(Math.abs(v1_ex_after) > Math.abs(v2_ex_after)) {
					proportion1=1;
					proportion2=0;
				}
				else if(Math.abs(v1_ex_after) < Math.abs(v2_ex_after)) {
					proportion1=0;
					proportion2=1;
				}

				balls.get(i).vel[0]=balls.get(i).vel[0]+((v1_ex_after - v1_ex)*ex[0]);
				balls.get(i).vel[1]=balls.get(i).vel[1]+((v1_ex_after - v1_ex)*ex[1]);

				balls.get(i2).vel[0]=balls.get(i2).vel[0]+((v2_ex_after - v2_ex)*ex[0]);
				balls.get(i2).vel[1]=balls.get(i2).vel[1]+((v2_ex_after - v2_ex)*ex[1]);


				x=((targetDistance + extra)-r)*ex[0];
				y=((targetDistance + extra)-r)*ex[1];


				balls.get(i).pos[0]=balls.get(i).pos[0]+proportion1*x;
				balls.get(i2).pos[0]=balls.get(i2).pos[0]-proportion2*x;

				balls.get(i).pos[1]=balls.get(i).pos[1]+proportion1*y;
				balls.get(i2).pos[1]=balls.get(i2).pos[1]-proportion2*y;
				
				if(!hit_player_ball_first && !hit_ball) {
					if(balls.get(i).number==0 || balls.get(i2).number==0) {
						hit_ball=true;
						
						if(balls.get(i).number==0) {
							ball_number=balls.get(i2).number;
						}else if(balls.get(i2).number==0) {
							ball_number=balls.get(i).number;
						}

						if(player1 == group_of_balls.Group_1) {
							if(player1_turn) {
								if(balls_left_group_1 == 0) {
									if(ball_number == 8) {
										hit_player_ball_first=true;
									}
								}
								else {
									if(ball_number>=1 && ball_number<=7) {
										hit_player_ball_first=true;
									}
								}
							}
							else {
								if(balls_left_group_2 == 0) {
									if(ball_number == 8) {
										hit_player_ball_first=true;
									}
								}
								else {
									if(ball_number>=9 && ball_number<=15) {
										hit_player_ball_first=true;
									}
								}
							}
						}
						else if(player1 == group_of_balls.Group_2) {
							if(player1_turn) {
								if(balls_left_group_2 == 0) {
									if(ball_number == 8) {
										hit_player_ball_first=true;
									}
								}
								else {
									if(ball_number>=9 && ball_number<=15) {
										hit_player_ball_first=true;
									}
								}
							}
							else {
								if(balls_left_group_1 == 0) {
									if(ball_number == 8) {
										hit_player_ball_first=true;
									}
								}
								else {
									if(ball_number>=1 && ball_number<=7) {
										hit_player_ball_first=true;
									}
								}
							}
						}
						else if(player1 == group_of_balls.Not_picked) {
							if(ball_number != 8) {
								hit_player_ball_first=true;
							}
						}
					}
				}
				
				i=-1;
				break;
			}
		}		
	}
}


public void updateAcc_Vel_Pos(double t){
	double vectorSize;
	int counter=0;
	
	for(int i =0;i<balls.size();i++){
		vectorSize=Math.sqrt(Math.pow(balls.get(i).vel[0],2)+Math.pow(balls.get(i).vel[1],2));
		if(vectorSize>0){
		balls.get(i).acc[0]=(-balls.get(i).vel[0]/vectorSize)*9.8*m_to_p*ball_cloth_coefficientOfFriction;
		balls.get(i).acc[1]=(-balls.get(i).vel[1]/vectorSize)*9.8*m_to_p*ball_cloth_coefficientOfFriction;
		}
		else{
			balls.get(i).acc[0]=0;
			balls.get(i).acc[1]=0;
		}

		balls.get(i).vel[0]=balls.get(i).vel[0]+balls.get(i).acc[0]*t;
		balls.get(i).vel[1]=balls.get(i).vel[1]+balls.get(i).acc[1]*t;

		if(Math.abs(balls.get(i).vel[0])<0.2 && Math.abs(balls.get(i).vel[1])<0.2) {
			balls.get(i).vel[0]=0;
			balls.get(i).vel[1]=0;
			counter++;
		}

		balls.get(i).pos[0]=balls.get(i).pos[0]+balls.get(i).vel[0]*t;
		balls.get(i).pos[1]=balls.get(i).pos[1]+balls.get(i).vel[1]*t;

	}

	if(counter == balls.size()) {
		balls_stopped=true;
	}
	else {
		balls_stopped=false;
	}
}
}
