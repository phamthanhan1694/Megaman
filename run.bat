@echo off
setlocal

call "%~dp0build.bat" build-only
if errorlevel 1 exit /b 1

java -cp "%~dp0target\classes" megaman.userinterface.GameFrame
endlocal
