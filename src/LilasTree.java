/* invocation type :
   java LilasTree /home/jln/LILASV4/src lilas.Main
   java LilasTree ~/DEV/SFO/DEZIPPED/LILASV4/src lilas.Main
*/

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.charset.StandardCharsets;
import java.io.BufferedReader;
import java.io.IOException;

import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Set;
import java.util.Iterator;
import java.util.ArrayList;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class LilasTree {

class LilasNode {
	String classname;
	int rank;
	TreeSet<Integer> referes;
	TreeSet<Integer> referants;
	// constructeur
	LilasNode( String name ) {
		classname = name;
		rank = -1;
		referes = new TreeSet<Integer>();
		referants = new TreeSet<Integer>();
		}
	// dump d'un set de LilasNodes indexes
	private void setdump( TreeSet<Integer> s ) {
		Iterator<Integer> itu = s.iterator();
		int v;
		while	(itu.hasNext()) {
			v = itu.next();
			LilasNode n = noeuds.get(v);
			System.out.println("      " + n.classname + " [" + v + "]" );
			}
		}
	// dump du LilasNode en detail
	private void dump( int detail ) {
		if	( detail == 0 )
			System.out.println("  " + classname + " R" + rank +
					   " [^" + referants.size() + "_" + referes.size() + "]" );
		else	{
			System.out.println("  " + classname + " R" + rank );
			System.out.println("    refered by " + referants.size() + " classes :" );
			setdump( referants );
			System.out.println("    refering  " + referes.size() + " classes :" );
			setdump( referes );
			}
		} 	
	}

String lilas_src;
ArrayList<LilasNode>    noeuds;
TreeMap<String,Integer> ijava;
TreeMap<String,Integer> iorg;
TreeMap<String,Integer> iexternal;
TreeMap<String,Integer> ililas;
TreeMap<String,Integer> iinconnu;
TreeMap<String,Integer> ililenum;

// constructeur
public LilasTree( String srcpath ) { 
	lilas_src = srcpath;
	noeuds	  = new ArrayList<LilasNode>();
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

	System.out.println( "ililenum : " + ililenum.size() + " elements" );
	mapdump( ililenum );

	System.out.println( "inconnu : " + iinconnu.size() + " elements" );
	mapdump( iinconnu );

	System.out.println( "lilas : " + ililas.size() + " elements" );
	mapdump2( ililas, 1 );
	}

private static void indent( int depth ) {
	for	( int i = 0; i < depth; ++ i)
		System.out.print("| ");
	}

// dump de map assez generique
private static void mapdump( TreeMap<String,Integer> mama ) {
	Set<String> clefs = mama.keySet();
	// System.out.println("keys " + clefs );
	Iterator<String> itu = clefs.iterator();
	String k, v;
	while	(itu.hasNext()) {
		k = itu.next();
		v = mama.get(k).toString();  // la ok, c Integer
		System.out.println("  " + k + " -> " + v );
		}
	}

// dump de map specifique pour LilasNodes crees en phase 2
private void mapdump2( TreeMap<String,Integer> mama, int detail ) {
	Set<String> clefs = mama.keySet();
	// System.out.println("keys " + clefs );
	Iterator<String> itu = clefs.iterator();
	String k; int v;
	while	(itu.hasNext()) {
		k = itu.next();
		v = mama.get(k);
		if	( v < 0 ) {
			System.out.println("  " + k + " -> " + v + " -> ERR" );
			continue;
			}
		LilasNode n = noeuds.get(v);
		if	( !k.equals( n.classname ) )
			System.out.println("  " + k + " -> " + v + " -> " + n.classname + " -> ERR AAAARGH bad dico" );
		n.dump( detail );
		}
	}

public int explore( String zeClass, int depth ) {
	// elaborer les pathname du fichier source de cette classe
	String splut[] = zeClass.split("[.]");
	int cnt = splut.length;
	if	( cnt < 1 )
		return -3;
	String laclass = splut[cnt-1] + ".java";
	Path lepath = Paths.get( lilas_src );
	for	( int i = 0; i < (cnt-1); ++i ) {
		lepath = lepath.resolve( splut[i] );
		}
	lepath = lepath.resolve( laclass );
	// affichage
	indent( depth );
	if	( !Files.exists( lepath ) ) {
		System.out.println( "NOT FOUND " + zeClass + " (" + lepath +")" );
		return -2; 
		}
	else	{
		System.out.println( zeClass );
		}
	// creer le noeud
	LilasNode lenoeud = new LilasNode( zeClass );
	int zeIndex = noeuds.size();
	noeuds.add( lenoeud );
	// lire ce fichier ligne par ligne
	String line; int linecnt = 0; int resu, id;
	Pattern papa, pali; Matcher mama;
	papa = Pattern.compile( "^\\s*import\\s+([0-9A-Za-z_.*]+)" );
	pali = Pattern.compile( "[^\"t/](lilas[.][0-9A-Za-z_.]+)" );	// on vise filtrer classes prefixees par : '"', '\t' et "//"
	String targetClass; int targetIndex;
	try	( BufferedReader bu = Files.newBufferedReader( lepath, StandardCharsets.UTF_8 ) ) {
		while	( ( line = bu.readLine() ) != null ) {
			// 							d'abord traiter les lignes import
			mama = papa.matcher( line );
			if	( mama.find() ) {
				// System.out.println( "== " + ililas.size() );
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
								Pattern papa2; Matcher mama2;
								papa2 = Pattern.compile( "^(.+)[.][0-9A-Za-z_]+$" );
								mama2 = papa2.matcher( targetClass );
								if	( mama2.find() ) {
									targetClass = mama2.group(1);
									}
								}
							}
						if	( splut[splut.length-1].equals("*") ) {
							indent( depth+1 ); System.out.println("ETOILE " + targetClass );
							}
						else	{	
							if	( ililas.get( targetClass ) == null ) {
								// il faut deja marquer avant de recurser, sinon peut boucler !
								ililas.put( targetClass, -1 );
								targetIndex = explore( targetClass, depth+1 );	// recursion ici !
 								if	( targetIndex < 0 ) {
									indent( depth+1 );
									System.out.println("ERR line " + linecnt + " in " + zeClass );
									}
								// marquage definitif
								ililas.put( targetClass, targetIndex );
								}
							else	{	// si on l'a deja traite il faut qd meme l'index
								targetIndex = ililas.get( targetClass );
								}
							// dependances
							if	( targetIndex >= 0 ) {
								noeuds.get(targetIndex).referants.add(zeIndex);
								noeuds.get(zeIndex).referes.add(targetIndex);
								}
							}
						}
					else    {
						if	( iinconnu.get( targetClass ) == null )
							iinconnu.put( targetClass, 1 );
						else	iinconnu.put( targetClass, iinconnu.get( targetClass ) + 1 );
						}
					}
				}
			else	{	// puis si ce n'est pas import on essaie de trouver lilas.quelquechose.Majuscule (fully qualified)
				mama = pali.matcher( line );
				while	( mama.find() ) {
					// System.out.println("pali-->" + mama.group(0) );
					splut = mama.group(1).split("[.]");
					targetClass = splut[0];
					for	( int i = 1; i < splut.length; ++i ) {
						targetClass = targetClass + "." + splut[i];
						if	( splut[i].matches("^[A-Z].*") ) {
							if	( ililas.get( targetClass ) == null ) {
								// il faut deja marquer avant de recurser, sinon peut boucler !
								ililas.put( targetClass, -1 );
 								targetIndex = explore( targetClass, depth+1 );	// recursion ici !
 								if	( targetIndex < 0 ) {
									indent( depth+1 );
									System.out.println("ERR line " + linecnt + " in " + zeClass );
									}
								// marquage definitif
								ililas.put( targetClass, targetIndex );
								}
							else	{	// si on l'a deja traite il faut qd meme l'index
								targetIndex = ililas.get( targetClass );
								}
							// dependances
							if	( targetIndex >= 0 ) {
								noeuds.get(targetIndex).referants.add(zeIndex);
								noeuds.get(zeIndex).referes.add(targetIndex);
								}
							break;
							}
						}
					}
				}
			++linecnt;
			}
	} catch (IOException x) {
		System.err.format("IOException: %s%n", x);
		}
	indent( depth ); System.out.println( "vu " + linecnt + " lignes" );
	return zeIndex;
	} // explore

public static void main(String[] args) {
        int argc = args.length;
	if	( argc < 2 )
		{
		System.out.println("Usage : Lilastree source_path start_class" );
		return;
		}
	LilasTree li = new LilasTree( args[0] );
	li.ililas.put( args[1], 0 );	// marquer cette classe pour eviter bouclage infini...
	li.explore( args[1], 0 );
	li.dump();
	} // main()

} // class

