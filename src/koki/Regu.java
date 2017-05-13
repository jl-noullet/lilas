package koki;

import java.util.regex.*;
class Regu { 
public static void test( String t ) {
Pattern pt = Pattern.compile( "_([^_]+)_" );
Matcher ma = pt.matcher( t );
boolean resu = ma.find(); // ou ma.matches() pour pattern entier " de ^ a $ "
// raccourci pour match pattern entier sans capture
// boolean resu = Pattern.matches( "_([^_]+)_", t ); // remplace les 3 lignes
System.out.println( "matched " + resu );
if ( resu ) {
 System.out.println( "--> " + ma.group(1) );
 }
}
}
