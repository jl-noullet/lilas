package koki;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.BufferedReader;
import java.io.IOException;

class Filu {
public static void test() {
Path p1 = Paths.get("/home/jln", "GIT_LILAS");
System.out.println( "get 2 str --> " + p1 );   // implied toString()
Path p2 = p1.resolve("lilas");
System.out.println( "resolve --> " + p2 );
p1 = p2.getFileName();
int cnt = p2.getNameCount();
System.out.println( cnt + " names, file is --> " + p1 );
for ( int i = 0; i < cnt; ++i ) {
   System.out.println( "  " + p2.getName(i) );
   }
p2 = p2.resolve("src/koki/Main.java");
System.out.println( "resolve --> " + p2 + " " + Files.exists(p2) );
/*
int fsiz;
try ( fsiz = Files.size(p2) ) {
    System.out.println( fsiz + " bytes");
    } catch (IOException x) {
			System.err.format("IOException: %s%n", x);
			}
// size : unreported exception IOException; must be caught or declared to be thrown
*/
System.out.println( "parent -> " + p2.getParent() );
System.out.println( "root -> " + p2.getRoot() );
String splut[] = p2.getFileName().toString().split("[.]");
int splutsize = splut.length;
if ( splutsize > 1 )
   System.out.println( "ext ." + splut[splutsize-1] );
//p1 = p2.relativize( Paths.get(".") );
//System.out.println( "relative . --> " + p1 );
p1 = p2.subpath( 1, 3 );
System.out.println( "sub 1 3 --> " + p1 );
String line; int linecnt = 0;
// on essaie d'eviter :
// unreported exception IOException; must be caught or declared to be thrown
if ( Files.exists(p2) ) {
   try (BufferedReader bu = Files.newBufferedReader( p2 ) ) { 
       while( ( line = bu.readLine() ) != null ) {
         if ( ++linecnt > 4 ) break;
         System.out.println( " [" + line + "]" );
         }
       } catch (IOException x) {
			System.err.format("IOException: %s%n", x);
			}
   }
}

}
