@echo off
title ServiceHub Application Runner
echo ==========================================================
echo Starting ServiceHub (Spring Boot + Web Frontend)...
echo ==========================================================
echo.

cd /d "%~dp0"

if not exist "backend\target\servicehub-backend-1.0.0.jar" (
    echo [INFO] JAR file not found. Building project first...
    call build.bat
    if not exist "backend\target\servicehub-backend-1.0.0.jar" (
        echo [ERROR] Build failed. Please check the logs.
        pause
        exit /b 1
    )
)

echo [INFO] Launching ServiceHub backend on port 8080...
echo [INFO] Application will be available at: http://localhost:8080/
echo.

start "" "http://localhost:8080/"
java -jar "backend\target\servicehub-backend-1.0.0.jar"

pause
