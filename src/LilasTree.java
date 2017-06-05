/* invocation type :
   java LilasTree /home/jln/LILASV4/src lilas.Main
*/

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.charset.StandardCharsets;
import java.io.BufferedReader;
import java.io.IOException;

import java.util.TreeMap;
import java.util.Set;
import java.util.Iterator;

public class LilasTree {

String lilas_src;
TreeMap<String,Integer> ijava;
TreeMap<String,Integer> iorg;
TreeMap<String,Integer> iexternal;
TreeMap<String,Integer> ililas;
TreeMap<String,Integer> iinconnu;

// constructeur
public LilasTree( String srcpath ) { 
	lilas_src = srcpath;
	ijava	  = new TreeMap<String,Integer>();
	iorg	  = new TreeMap<String,Integer>();
	iexternal = new TreeMap<String,Integer>();
	ililas	  = new TreeMap<String,Integer>();
	iinconnu  = new TreeMap<String,Integer>();
	}

public void dump() {
	System.out.println( "java et javax : " + ijava.size() + " elements" );
	System.out.println( ijava.toString() );
	}

public void explore( String zeclass ) {
	System.out.println( "Exploring class : " + zeclass );
	// elaborer les pathname du fichier source de cette classe
	String splut[] = zeclass.split("[.]");
	int cnt = splut.length;
	if	( cnt < 1 )
		return;
	String laclass = splut[cnt-1] + ".java";
	Path lepath = Paths.get( lilas_src );
	for	( int i = 0; i < (cnt-1); ++i ) {
		lepath = lepath.resolve( splut[i] );
		}
	lepath = lepath.resolve( laclass );
	System.out.println( "                  " + lepath );   // implied toString()
	if	( !Files.exists( lepath ) ) {
		System.out.println( "not founded !!!" );
		return;
		}
	// lire ce fichier ligne par ligne
	String line; int linecnt = 0;
	try	( BufferedReader bu = Files.newBufferedReader( lepath, StandardCharsets.UTF_8 ) ) {
		while	( ( line = bu.readLine() ) != null ) {
			if ( ++linecnt > 7 ) break;
			System.out.println( " [" + line + "]" );
			}
	} catch (IOException x) {
		System.err.format("IOException: %s%n", x);
		}
	
	} // explore

public static void main(String[] args) {
        int argc = args.length;
	if	( argc < 2 )
		{
		System.out.println("Usage : Lilastree source_path start_class" );
		return;
		}
	LilasTree li = new LilasTree( args[0] );
	li.explore( args[1] );
	li.dump();
	} // main()

} // class

