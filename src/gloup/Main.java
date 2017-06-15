package gloup;

import gloup.Module;


public class Main {
	
	public static void arret(String msg) {
		System.out.println("arret : "+msg);
	}
	
	public static void main (String[] args)  {
		Module momo = new Module();
		momo.dump();
		arret("yen a mahr");
	} 
	
}