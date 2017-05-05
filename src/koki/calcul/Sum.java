package koki.calcul;

public class Sum {

	double X;

	public Sum() { X = 62.1; };
	public Sum( double newX ) { X = newX; };

	public void dump() {
        System.out.println("Dump X = " + X );
	}

	public void addX( double X ) { this.X += X; }
	public void setX( double X ) { this.X = X; }
	public double getX() { return X; }

}
