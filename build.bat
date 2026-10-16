@echo off
title ServiceHub Builder
echo ==========================================================
echo Building ServiceHub Project...
echo ==========================================================
echo.

cd /d "%~dp0"

set MVN_CMD=mvn
where mvn >nul 2>nul
if %errorlevel% neq 0 (
    if exist "C:\Users\H6jyo\mvn-bin\apache-maven-3.9.6\bin\mvn.cmd" (
        set MVN_CMD="C:\Users\H6jyo\mvn-bin\apache-maven-3.9.6\bin\mvn.cmd"
    ) else (
        echo [ERROR] Maven not found in PATH or C:\Users\H6jyo\mvn-bin\apache-maven-3.9.6\bin\mvn.cmd
        echo Please ensure Maven is installed.
        pause
        exit /b 1
    )
)

echo [INFO] Copying frontend static assets to backend...
xcopy /s /y /i "frontend\*" "backend\src\main\resources\static\" >nul

echo [INFO] Running Maven Package...
cd backend
%MVN_CMD% package -DskipTests
if %errorlevel% neq 0 (
    echo.
    echo [ERROR] Maven build failed!
    cd ..
    pause
    exit /b %errorlevel%
)

cd ..
echo.
echo ==========================================================
echo [SUCCESS] Build completed successfully!
echo Executable JAR created at: backend\target\servicehub-backend-1.0.0.jar
echo ==========================================================
pause
