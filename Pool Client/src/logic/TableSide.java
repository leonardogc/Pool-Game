package logic;

public class TableSide {
	public double p1[]; 
	public double p2[]; 
	public double p3[]; 
	public double p4[]; 

	public TableSide(double x1, double y1,double x2, double y2,double x3, double y3,double x4, double y4) {
		p1= new double[2];
		p2= new double[2];
		p3= new double[2];
		p4= new double[2];
		
		p1[0]=x1;
		p1[1]=y1;
		
		p2[0]=x2;
		p2[1]=y2;
		
		p3[0]=x3;
		p3[1]=y3;
		
		p4[0]=x4;
		p4[1]=y4;
	}
}
