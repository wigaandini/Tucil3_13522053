cd src
javac -d ..\bin *.java
@REM xcopy dictionary.txt ..\bin\ /Y
@REM xcopy word_dictionary.txt ..\bin\ /Y
xcopy words_alpha.txt ..\bin\ /Y
cd ..
cd bin
java Main
