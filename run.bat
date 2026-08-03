@echo off
REM Smart Hostel Complaint Management System - Run Script for Windows

echo ==========================================
echo Smart Hostel Complaint Management System
echo Starting Application...
echo ==========================================
echo.

REM Check if Maven is installed
where mvn >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo ❌ Maven is not installed or not in PATH
    echo Please install Maven and add it to your PATH
    pause
    exit /b 1
)

echo ✅ Maven is installed
echo.

REM Check if project is built
if not exist "target" (
    echo 🔧 Project not built. Building now...
    call mvn clean compile
    echo ✅ Project built
)

REM Run the application
echo 🚀 Starting application...
echo.
call mvn exec:java -Dexec.mainClass="com.hostel.complaint.view.MainApplication"

pause
