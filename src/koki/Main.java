package koki;

import koki.calcul.Sum;

public class Main {

	double X;

	public Main() { X = 1.5; };
	public Main( double newX ) { X = newX; };

	public static void main(String[] args) {
        System.out.println("C'est imposant :" );
        int argc, i;
        argc = args.length;
        System.out.println( argc + " arguments" );
        for ( i = 0; i < argc; ++i ) {
        	System.out.println( "   " + args[i] );
        }
	Main pipo = null;
	pipo = new Main();
	Main pipo2 = new Main( 2.2 );
	System.out.println("pipo = " + pipo.getX() + " pipo2 = " + pipo2.getX() );
	pipo.setX( 3.14 );
	System.out.println("pipo = " + pipo.getX() );
	Sum soma = new Sum();
	soma.addX(6.9);
	soma.dump();
	}

	public void setX( double X ) { this.X = X; }
	public double getX() { return X; }

}
