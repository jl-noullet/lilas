#! /bin/sh
javac -verbose -sourcepath ./src -d ./bin -cp ./bin ./src/gloup/Module.java 2> log_gloup
java -cp ./bin gloup.Main
