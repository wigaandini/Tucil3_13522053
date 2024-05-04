cd src
javac -d ..\bin *.java
xcopy dictionary.txt ..\bin\ /Y
@REM xcopy word_dictionary.txt ..\bin\ /Y
@REM xcopy words_alpha.txt ..\bin\ /Y
cd ..
cd bin
java Main
