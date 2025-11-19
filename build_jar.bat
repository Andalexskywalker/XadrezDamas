@echo off
echo Cleaning up previous build...
if exist bin rmdir /s /q bin
if exist XadrezDamas.jar del XadrezDamas.jar

echo Creating bin directory...
mkdir bin

echo Compiling Java sources...
:: We compile MenuJogo.java, and javac will automatically compile all dependencies found in sourcepath
javac -d bin -sourcepath src -encoding UTF-8 src\pds\menu\MenuJogo.java

if %errorlevel% neq 0 (
    echo.
    echo [ERROR] Compilation failed!
    pause
    exit /b %errorlevel%
)

echo Creating Manifest file...
echo Main-Class: pds.menu.MenuJogo> Manifest.txt
:: Ensure there is a newline at the end of the manifest
echo.>> Manifest.txt

echo Packaging JAR file...
jar cfm XadrezDamas.jar Manifest.txt -C bin .

if %errorlevel% neq 0 (
    echo.
    echo [ERROR] JAR packaging failed!
    pause
    exit /b %errorlevel%
)

echo.
echo [SUCCESS] XadrezDamas.jar created successfully!
echo You can run it using: java -jar XadrezDamas.jar
del Manifest.txt
pause
