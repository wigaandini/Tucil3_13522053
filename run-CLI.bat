cd src
javac -d ..\bin *.java
xcopy dictionary.txt ..\bin\ /Y
cd ..
cd bin
java Main