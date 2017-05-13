package koki;
import java.util.ArrayList;
class Vectu {
public static void test() {
ArrayList<Double> dvec = new ArrayList();
dvec.add( 1.1 ); dvec.add( 2.2 );
int siz = dvec.size();
for ( int i = 0; i < siz; ++i ) {
  System.out.println( i + "--> " + dvec.get(i) );
  }
}
// test saut de ligne // // fin
}
