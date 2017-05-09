- doc de ref API
	https://docs.oracle.com/javase/8/docs/api/
- tutos Oracle ou "trails"
	http://docs.oracle.com/javase/tutorial/tutorialLearningPaths.html
	- Getting started : comme eclipse mais sans eclipse ! javac HelloWorldApp.java c'est tout
	- Learning the Java language
	- Essential Classes
	- etc... JL a D/L tous les trails dans 1 archive javatutorials.zip

- conventions
	- nom de classe commence par majuscule, methode par minuscule, constante toute en majuscules
	- nom de methode commence par un verbe !!!

- classpath :
	- java prend un nom de classe comme argument (sans le .class)
	- si on lui donne un chemin t.q. Hello/bin/HelloWorld ou Hello.bin.HelloWorld
	  il trouve le fichier mais refuse d'executer la classe !!! (wrong name: HelloWorld)
	- le separateur de chemin est ':' dans le classpath
	- pour specifier un jar, le nommer completement
	- pour specifier tous les .jar de MyDir, // mettre MyDir/*
	- l'option -classpath (aka -cp) override $CLASSPATH
	- l'option -classpath et $CLASSPATH overrident le path par defaut qui est '.'
	  (pas garanti sous windows, ils recommandent -cp .)

- heritage
	- syntaxe : class MountainBike extends Bicycle { }
	- superclass : c'est la classe parent
	- constructeur : comme en C++ mais : tout initialiser dans {} en appelant le constructeur
	  de la classe parente : super(...);
	  N.B. faut pas de type de retour - void MyClass() fait rien sans warning.
	  un constructeur peut appeler un autre de la meme classe (avec une signature differente) : this()
	- interface : c'est une declaration des methodes, comme fait le .h d'une classe en C++
	  avec une syntaxe similaire (les methodes, pas les donnees)
	  en effet la classe proprement dite contient le corps des methodes.
	  Une classe peut implementer plusieurs interfaces (mais heriter d'une seule)
	  syntaxe : class MaClasse implements MonInterface
	- nested class : classe declaree dans une autre !!!
		"Nesting small classes within top-level classes places the code closer to where it is used"
	  inner class : une nested class non static
	- local classe encore plus inner, definie dans un bloc {}
	  "Local Classes Are Similar To Inner Classes"

- instanciantion :
	- statique (i.e. dans un membre) : 2 phases
		- creation membre dans la classe : Myclass myObject; 
		- initialisation dans le constructeur : myObject = new Myclass( args );
	  avant construction, le membre a un type mais c'est un pointeur NULL (sauf si l'erreur a deja
	  ete interceptee au compile-time)
	  a intercepte l'erreur
	- dynamique (i.e; dans une var locale) : new avec le constructeur :
		Myclass myObject = new Myclass( args );
	- destruction : myObject = null; (on detruit la reference, garbage collector fait le reste)

- static : ou class member
	- invocation : Myclass.mystaticvar = Myclass.getTruc( 69 );	// pas de ::
	- constante : static final double PI = 3.141592653589793;

- annotation
	@Schmoldu

- generics
	c'est les templates du C++

- package
	"A package is a namespace for organizing classes and interfaces in a logical manner"
	"At first, packages appear to be hierarchical, but they are not"
	- un package peut contenir une classe heritant d'un autre package
	- chaque fichier du package DOIT porter le nom de l'UNIQUE classe publique qu'il contient
	  (mais il peut contenir des classes non-public t.q. par defaut package private).
	- chaque fichier DOIT commencer par :
		package fr.sourcecode.all_lower_case_my_package;
	- reference a un membre d'un package :
		- simple nom si on est dans le meme package
		- fully qualified : fr.sourcecode.all_lower_case_my_package.Myclass
		- simple nom si le membre a ete importe :
			import fr.sourcecode.my_package.Myclass
		- simple nom si le package entier a ete importe : (sauf ambiguite, alors use fully qualified)
			import fr.sourcecode.my_package.*
		- importation des nested classes publiques de Myclass :
			import fr.sourcecode.my_package.Myclass.*  (n'importe pas Myclass)
		- import static : importe static constants and methods
			import static java.lang.Math.*;
		  permet de faire
			double r = cos(PI * theta);
		  au lieu de :
			double r = Math.cos(Math.PI * theta);
		  N.B. le package java.lang est implicitement importe, y compris la classe Math,
		  mais ici on a besoin de membres statiques qui ne sont pas importes avec la classe 
	- filesystem
		- la classe Myclass doit etre dans
			<classpath>/fr/sourcecode/my_package/Myclass.class
		  i.e. finalement les noms de package refletent bien une hierarchie mais c'est une hierarchie
		  de fichiers, pas de packages (sourcecode n'est PAS un package)
		- curieusement le trail Oracle conseille d'invoquer le compilateur depuis le repertoire commun
		  a plusieurs package, par exemple fr/sourcecode
		- javac prend 4 arguments essentiels pour faire la compilation recursive de tout un arbre :
			-d : destination dir - il doit exister, ensuite javac est capable d'y recreer l'arbre des packages
			     pour y mettre les classes
			-cp : classpath pour les dependances - doit inclure le destination dir plus d'autres si necessaire
			-sourcepath : racine de l'arbre des packages, pour chercher les sources
			le pathname unix complet du .java de la classe principale (sourcecpath n'est pas pris en compte)
		  il ne dit pas ce qu'il fait, sauf avec -verbose, alors c'est trop (rediriger avec 2>)
	- javap
		- espionne le contenu des classes !!!

- types primitifs :
	byte : s8
	short : s16
	char : u16 for Unicode 16
	int : s32 (has unsigned methods)
	long : s64 (has unsigned methods)
	float, double : IEEE 754
	boolean : true, false
- Array : taille fixe
	int myArray[] = new int[10];
	int herArray[] = { 1, 3, 5, 7, 11 };
	longueur : a.length()
	iteration : exemple sur un array de String : for ( String elem: myArray ) { }
- String : const (immutable) sequence of Unicode 16
	"une methode qui doit modifier la string retourne une nouvelle string"
	concatenation : +
	char[] to String : s = String(charArray)
	String to char[] : s.toCharArray() ou s.getChars(sBegin, sEnd, charArray, dBegin )
	String to char : s.charAt(i)
	byte[] to String : String( byteArray )		(with platform local charset)
	String to bytes : s.getBytes ou s.getBytes(sBegin, sEnd, byteArray, dBegin )
	longueur : s.length, s.isEmpty()
	regex : s.matches(String regex)
 	split : s.split(String regex)
 	substring : s.substring( iBegin ) ou s.substring( iBegin, iEnd )
	trim whitespace : s.trim()
	conversion to String : s = valueOf( truc ) convertit truc en texte comme il peut (dump)
	conversion from String : i = Integer.parseInt(mystring); d = Double.parseDouble( mystring );
	N.B. Integer et Double sont les wrapper classes des types primitifs int et double
	
- File I/O
	- la classe Path
		Path p1 = Paths.get("/home/joe/foo");		// Paths avec un 's' !! sert a creer un path ?!?
		methodes d'epluchage : toString(), getFileName(), getName(0), getNameCount(), subpath(0,2), getParent(), getRoot()
		assemblage : Path p1 = Paths.get("/home/joe/foo"); p2 = p1.resolve("bar")); // /home/joe/foo/bar
		operation enverse : relativize()
	  N.B. Paths est une classe qui ne contient que des methodes statiques, Path sert a creer des objets.
		teste existence d'un fichier : Files.exists( myPath )
		rend la taille d'un fichier : Files.size( myPath )
	  La classe Files a des methodes statiques pour copier, mouvoir, effacer des fichiers, ainsi
	  que pour scruter les repertoires : The FileVisitor Interface : To walk a file tree
	- Options d'ouverture :
		WRITE, CREATE_NEW, CREATE
		APPEND with WRITE or CREATE
		TRUNCATE_EXISTING with WRITE
	- small file at once :
		byte[] fileArray;
		fileArray = Files.readAllBytes( myPath );
		String[] stringArray;
		stringArray = readAllLines( myPath, StandardCharsets.UTF_8 )
	  (assure ouverture et fermeture du fichier)
	  il y a aussi un truc similaire pour l'ecriture.
	- lecture bufferisee a la stdio :
		try (BufferedReader reader = Files.newBufferedReader( myPath, StandardCharsets.UTF_8) {
			String line = null;
			while ( (line = reader.readLine() ) != null ) {
				// do someting with the line, like :
				System.out.println(line);
			}
		} catch (IOException x) {
			System.err.format("IOException: %s%n", x);
			}
- Regex (Perl compatible :-)
	Pattern myPat = Pattern.compile( myString, Pattern.CASE_INSENSITIVE ); ou sans flag
	Pattern myPat = Pattern.compile( myString );
  N.B. il y a des embedded flags qui font la meme chose, e.g; (?i) pour case insensitive
  	Matcher myMatch = Pattern.matcher( myStringToMatch );
  Et alors :
	if ( myPat.find() ) { do something }
  par exemple :
	myCapturedString = myPat.group( i ) rend la capture d'index i
  le matchage entier a l'indice 0. ( myPat.group() est equivalent a myPat.group(0) )
	myPat.groupCount() rend le nombre de captures ( group(0) non inclus )
		     
- Vector :
	par defaut, capacity increment par un facteur 2 (sinon par addition de capacityIncrement)

- Maps : plusieurs types : Hashtable, HashMap, TreeMap (uses the natural ordering of its keys)

-------------------------------------------------------------------------------------------------------
- tutos Eclipse
	- avec HelloWorld : public static void main(String[] args)
		- creation projet depuis File->New
		- creation classe avec ?
		- si auto-compile, il faut faire "run as" pour compiler
		- sinon faire build
	- avec HelloWorld GUI SWT
		- SWT est supporte par eclipse, pas par (Oracle)
		- le tuto nous amene à importer un "fragment" i.e. copier un jar en local
		- "run as" marche mais si on essaie de lancer depuis le shell, java reclame 
		  a propos du manifest du jar de la lib SWT
- autres
	https://www.tutorialspoint.com/compile_java8_online.php		// marche que avec Chrome
---------------------------------------------------------------------------------------------------
Exemple de compilation recursive
on prepare
src/koki/Main.java
	package koki;
	import koki.calcul.Sum;
src/koki/calcul/Sum.java
	package koki.calcul;

mkdir bin;
javac -sourcepath ./src -d ./bin -cp ./bin src/koki/Main.java
java -cp ./bin koki.Main

--> et ça marche !!!

Autre experience : on oblige le compilateur a compiler seulement 1 classe a la fois
rm -r ./bin/*
javac -sourcepath ./src -d ./bin src/koki/calcul/Sum.java
mv src/koki/calcul/Sum.java pipo.j
javac -sourcepath ./src -d ./bin -cp ./bin src/koki/Main.java
java -cp ./bin koki.Main
mv pipo.j src/koki/calcul/Sum.java

---------------------------------------------------------------------------------------------------
Exemples d'execution
on fait une classe HelloWorld dans le fichier /home_pers/noullet/DEV/JAVA/Hello/bin/HelloWorld.class

SUCCESS :
cd /home_pers/noullet/DEV/JAVA/Hello/bin/; java HelloWorld
export classpath=; export CLASSPATH=;
echo $classpath; echo $CLASSPATH;
cd;  java -classpath /home_pers/noullet/DEV/JAVA/Hello/bin HelloWorld
cd; export CLASSPATH=/home_pers/noullet/DEV/JAVA/Hello/bin; java HelloWorld
java -classpath /home_pers/noullet/DEV/JAVA/Hello/bin:/home_pers/noullet/DEV/JAVA/Hello HelloWorld


FAIL :
cd /home_pers/noullet/DEV/JAVA/Hello/; java bin.HelloWorld
cd /home_pers/noullet/DEV/JAVA/Hello/; java bin/HelloWorld
cd /home_pers/noullet/DEV/JAVA; java Hello.bin.HelloWorld
cd; export CLASSPATH=/home_pers/noullet/DEV/JAVA/Hello; java bin.HelloWorld
cd; export CLASSPATH=/home_pers/noullet/DEV/JAVA/Hello; java bin/HelloWorld
--- cependant dans les 5 cas il trouve bien le fichier mais le nom de la classe ne lui plait pas (wrong name: HelloWorld)
export CLASSPATH=/home_pers/noullet/DEV/JAVA/Hello; cd /home_pers/noullet/DEV/JAVA/Hello/bin/; java HelloWorld
export CLASSPATH=; cd /home_pers/noullet/DEV/JAVA/Hello/bin/; java -classpath /home_pers/noullet/DEV/JAVA/Hello HelloWorld
--- dans ces 2 cas il ne trouve pas le fichier, i.e. classpath par defaut est . mais cela est oublie si on met $CLASSPATH ou -classpath

vu dans man java :
-classpath classpath, -cp classpath
             Specifies a list of directories, JAR files, and ZIP archives to search for class  files.  Sepa‐
             rate  class path entries with colons (:). Specifying -classpath or -cp overrides any setting of
             the CLASSPATH environment variable.
             If -classpath and -cp are not used and CLASSPATH is not set, then the user class path  consists
             of the current directory (.).
             As  a  special  convenience,  a class path element that contains a base name of * is considered
             equivalent to specifying a list of all the files in the directory with the  extension  .jar  or
             .JAR. A Java program cannot tell the difference between the two invocations.
             For  example,  if directory mydir contains a.jar and b.JAR, then the class path element mydir/*
             is expanded to a A.jar:b.JAR, except that the order of jar files is unspecified. All jar  files
             in the specified directory, even hidden ones, are included in the list. A class path entry con‐
             sisting simply of * expands to a list of all the jar files in the current directory. The CLASS‐
             PATH  environment  variable, where defined, will be similarly expanded. Any class path wildcard
             expansion occurs before the Java VM is started. No Java program will ever see wild  cards  that
             are   not   expanded  except  by  querying  the  environment.  For  example,  by  calling  Sys‐
             tem.getenv("CLASSPATH").

