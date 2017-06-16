package koki;

import java.util.regex.*;
class Regu { 
public static void test( String t ) {
Pattern pt = Pattern.compile( "_([^_]+)_" );
Matcher ma = pt.matcher( t );
// nuance entre find() et matches() : matches() est pour la string entiere " de ^ a $ "
boolean resu = ma.find();
// N.B. il existe raccourci pour match pattern entier sans capture
// boolean resu = Pattern.matches( "_([^_]+)_", t ); // remplace les 3 lignes
// encode plus raccourci est la methode matches() de la classe String
System.out.println( "matched " + resu );
if ( resu ) {
 System.out.println( "--> " + ma.group(1) );
 }
// find iteratif :
System.out.println( "find iteratif :");
pt = Pattern.compile( "pipo(\\d)" );
ma = pt.matcher( "... pipo6 pipo9pipo" );
while	( ma.find() ) {
	System.out.println( "pipo --> " + ma.group(1) );
	}
// reset :
System.out.println( "find iteratif again :");
if	( ma.find(0) ) {
	System.out.println( "pipo --> " + ma.group(1) );
	}
while	( ma.find() ) {
	System.out.println( "pipo --> " + ma.group(1) );
	}
	
}
}
