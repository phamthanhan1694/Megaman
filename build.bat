@echo off
setlocal
pushd "%~dp0"
if errorlevel 1 exit /b 1

set "OUT_DIR=target\classes"
set "SOURCE_LIST=target\sources.txt"

if not exist "%OUT_DIR%" mkdir "%OUT_DIR%"

xcopy /E /I /Y src\main\resources "%OUT_DIR%" > nul
if errorlevel 2 goto failure
(for /r src\main\java %%f in (*.java) do echo %%f) > "%SOURCE_LIST%"

javac --release 17 -encoding UTF-8 -d "%OUT_DIR%" @"%SOURCE_LIST%"
if errorlevel 1 (
    echo Compilation failed.
    goto failure
)

jar --create --file target\megaman-1.0.0.jar --main-class megaman.userinterface.GameFrame -C "%OUT_DIR%" .
if errorlevel 1 goto failure

if /I "%~1"=="build-only" (
    echo Compilation successful.
    popd
    exit /b 0
)

java -cp "%OUT_DIR%" megaman.userinterface.GameFrame
set "GAME_EXIT_CODE=%ERRORLEVEL%"
popd
exit /b %GAME_EXIT_CODE%

:failure
popd
exit /b 1
