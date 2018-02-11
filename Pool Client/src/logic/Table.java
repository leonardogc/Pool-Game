package logic;

import java.util.Vector;


public class Table {
	
public Vector<Pocket> pockets;
public Vector<TableSide> sides;
public double[] table_edge;

public double ball_diameter;
public double pocket_diameter;

//real ball size 5.7 cm 
//real table size 2.84 m x 1.42 m
//max speed: 8 m/s


public Table(double ball_diameter, int[] screen_size){
	this.ball_diameter=ball_diameter;
	
	double m_to_p=ball_diameter/0.057;
	this.pocket_diameter=2*ball_diameter;
	double table_side_thickness=pocket_diameter/2;
	double middle_hole_opening_size=1.4*pocket_diameter;
	
	pockets=new Vector<Pocket>();
	sides=new Vector<TableSide>();
	
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

	table_edge[0]+=dx;
	table_edge[1]+=dy;
	table_edge[2]+=dx;
	table_edge[3]+=dy;
}
}
