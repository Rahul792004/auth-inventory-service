@echo off
echo Starting Inventory Management System...
echo.

echo Checking if MySQL is running...
tasklist /FI "IMAGENAME eq mysqld.exe" 2>NUL | find /I /N "mysqld.exe">NUL
if "%ERRORLEVEL%"=="0" (
    echo MySQL is running ✓
) else (
    echo MySQL is NOT running ✗
    echo Please start MySQL service first
    echo.
    echo To start MySQL:
    echo 1. Open Services (services.msc)
    echo 2. Find MySQL80 (or MySQL57)
    echo 3. Right-click → Start
    echo.
    pause
    exit /b 1
)

echo.
echo Attempting to start application...
echo If you see "Access denied" error, the MySQL password is wrong.
echo Edit src\main\resources\application.properties and change the password.
echo.

cd /d "%~dp0"
mvn spring-boot:run

pause