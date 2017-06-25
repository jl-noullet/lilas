package koki;

import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;

class Mapuh {
public static void test() {
HashMap<String,Integer> mop = new HashMap<String,Integer>();
mop.put("Magda", 11 );
mop.put("Koumba", new Integer(69) );
int siz = mop.size();
System.out.println( "hashmap de " + siz + " elements" );
System.out.println( mop.toString() );
int i = mop.get("Magda");
System.out.println("Magda -> " + i );
System.out.println("pipo -> " + mop.get("pipo") );
// i = mop.get("pipo");  // null pointer exception
Set<String> clefs = mop.keySet();
System.out.println("keys " + clefs );
Iterator<String> itu = clefs.iterator();
String k, v;
while (itu.hasNext()) {
   // k = itu.next().getClass().getName();
   k = itu.next();
   v = mop.get(k).toString();  // la ok, c Integer
   System.out.println("iterated : " + k + " -> " + v );
   }
}
}

