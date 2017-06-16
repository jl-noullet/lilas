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

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class LilasTree {

String lilas_src;
TreeMap<String,Integer> ijava;
TreeMap<String,Integer> iorg;
TreeMap<String,Integer> iexternal;
TreeMap<String,Integer> ililas;
TreeMap<String,Integer> iinconnu;
TreeMap<String,Integer> ililenum;

// constructeur
public LilasTree( String srcpath ) { 
	lilas_src = srcpath;
	ijava	  = new TreeMap<String,Integer>();
	iorg	  = new TreeMap<String,Integer>();
	iexternal = new TreeMap<String,Integer>();
	ililas	  = new TreeMap<String,Integer>();
	iinconnu  = new TreeMap<String,Integer>();
	ililenum  = new TreeMap<String,Integer>();
	}

public void dump() {
	System.out.println( "java et javax : " + ijava.size() + " elements" );

	System.out.println( "org : " + iorg.size() + " elements" );

	System.out.println( "external : " + iexternal.size() + " elements" );

	System.out.println( "lilas : " + ililas.size() + " elements" );
	mapdump( ililas );

	System.out.println( "ililenum : " + ililenum.size() + " elements" );
	mapdump( ililenum );

	System.out.println( "inconnu : " + iinconnu.size() + " elements" );
	mapdump( iinconnu );
	}

private static void indent( int depth ) {
	for	( int i = 0; i < depth; ++ i)
		System.out.print("| ");
	}

private static void mapdump( TreeMap<String,Integer> mama ) {
	Set clefs = mama.keySet();
	// System.out.println("keys " + clefs );
	Iterator itu = clefs.iterator();
	String k, v;
	while	(itu.hasNext()) {
		k = itu.next().toString();  // sais pas pkoi il faut faire cela, c dja String...
		v = mama.get(k).toString();  // la ok, c Integer
		System.out.println("  " + k + " -> " + v );
		}
	}

public void explore( String zeclass, int depth ) {
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
	// affichage
	indent( depth );
	if	( !Files.exists( lepath ) ) {
		System.out.println( "NOT FOUND " + zeclass + " (" + lepath +")" );
		return; 
		}
	else	{
		System.out.println( zeclass );
		}
	// lire ce fichier ligne par ligne
	String line; int linecnt = 0;
	Pattern papa; Matcher mama;
	String targetClass;
	try	( BufferedReader bu = Files.newBufferedReader( lepath, StandardCharsets.UTF_8 ) ) {
		while	( ( line = bu.readLine() ) != null ) {
			// d'abord traiter les lignes import
			papa = Pattern.compile( "^\\s*import\\s+([0-9A-Za-z_.*]+)" );
			mama = papa.matcher( line );
			if	( mama.find() ) {
				targetClass = mama.group(1);
				splut = targetClass.split("[.]");
				if	( splut.length > 0 ) {
					if	( splut[0].startsWith("java") ) {
						if	( ijava.get( targetClass ) == null )
							ijava.put( targetClass, 1 );
						else	ijava.put( targetClass, ijava.get( targetClass ) + 1 );
						}
					else if	( splut[0].equals("org") ) {
						if	( iorg.get( targetClass ) == null )
							iorg.put( targetClass, 1 );
						else	iorg.put( targetClass, iorg.get( targetClass ) + 1 );
						}
					else if	( splut[0].equals("external") ) {
						if	( iexternal.get( targetClass ) == null )
							iexternal.put( targetClass, 1 );
						else	iexternal.put( targetClass, iexternal.get( targetClass ) + 1 );
						}
					else if	( splut[0].equals("lilas") ) {
						// ici on doit detecter les enums eventuelles
						if	( splut.length > 2 ) {
							if	( splut[splut.length-2].matches("^[A-Z].*") ) {
								// avant dernier nom est 1 class, donc dernier est enum
								if	( ililenum.get( targetClass ) == null )
									ililenum.put( targetClass, 1 );
								else	ililenum.put( targetClass, ililenum.get( targetClass ) + 1 );
								// on doit retirer le dernier nom pour avoir la classe
								System.out.println("ENUM " + targetClass);
								Pattern papa2; Matcher mama2;
								papa2 = Pattern.compile( "^(.+)[.][0-9A-Za-z_]+$" );
								mama2 = papa2.matcher( targetClass );
								if	( mama2.find() ) {
									targetClass = mama2.group(1);
									}
								}
							} 
						if	( ililas.get( targetClass ) == null ) {
							ililas.put( targetClass, 1 );
							explore( targetClass, depth+1 );		// recursion ici !
							}
						else	ililas.put( targetClass, ililas.get( targetClass ) + 1 );
						}
					else    {
						if	( iinconnu.get( targetClass ) == null )
							iinconnu.put( targetClass, 1 );
						else	iinconnu.put( targetClass, iinconnu.get( targetClass ) + 1 );
						}
					}
				}
			++linecnt;
			}
	} catch (IOException x) {
		System.err.format("IOException: %s%n", x);
		}
	indent( depth ); System.out.println( "vu " + linecnt + " lignes" );
	} // explore

public static void main(String[] args) {
        int argc = args.length;
	if	( argc < 2 )
		{
		System.out.println("Usage : Lilastree source_path start_class" );
		return;
		}
	LilasTree li = new LilasTree( args[0] );
	li.ililas.put( args[1], 1 );	// marquer cette classe pour eviter bouclage infini...
	li.explore( args[1], 0 );
	li.dump();
	} // main()

} // class

