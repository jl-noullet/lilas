#! /bin/sh
javac -verbose -sourcepath ./src -d ./bin -cp ./bin ./src/gloup/Main.java 2> log_gloup
java -cp ./bin gloup.Main
