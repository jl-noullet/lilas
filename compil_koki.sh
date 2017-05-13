#! /bin/sh
javac -sourcepath ./src -d ./bin -cp ./bin ./src/koki/Main.java
java -cp ./bin koki.Main $1 $2 $3

