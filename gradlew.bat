@echo off
setlocal
where gradle >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
  echo Gradle is not installed or not on PATH. Please install Gradle 8.x or run this via a local Gradle installation.
  exit /b 1
)
call gradle %*
