#! /bin/sh
javac -sourcepath ./src -d ./bin -cp ./bin ./src/gloup/Main.java
java -cp ./bin gloup.Main
