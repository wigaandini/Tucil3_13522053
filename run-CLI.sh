cd "$(dirname "$0")"
javac -d ../bin src/*.java
cp src/dictionary.txt ../bin/
cd ../bin
java -cp . Main
