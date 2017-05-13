package koki;

import java.util.TreeMap;
import java.util.Set;
import java.util.Iterator;

class Mapu {
public static void test() {
TreeMap<String,Integer> mop = new TreeMap<String,Integer>();
mop.put("Magda", 11 );
mop.put("Koumba", new Integer(69) );
int siz = mop.size();
System.out.println( "map de " + siz + " elements" );
System.out.println( mop.toString() );
int i = mop.get("Magda");
System.out.println("Magda -> " + i );
System.out.println("pipo -> " + mop.get("pipo") );
// i = mop.get("pipo");  // null pointer exception
Set clefs = mop.keySet();
System.out.println("keys " + clefs );
Iterator itu = clefs.iterator();
String k, v;
while (itu.hasNext()) {
   // k = itu.next().getClass().getName();
   k = itu.next().toString();  // sais pas pkoi il faut faire cela, c dja String...
   v = mop.get(k).toString();  // la ok, c Integer
   System.out.println("iterated : " + k + " -> " + v );
   }
}
}

