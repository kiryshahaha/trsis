@echo off
:: ============================================================
::  Скрипт запуска лабораторной работы №2 — Книжный магазин
::  REST/JSON сервис на Spring Boot
:: ============================================================

set MAVEN_HOME=C:\tools\maven\apache-maven-3.9.16
set MAVEN_BIN=%MAVEN_HOME%\bin\mvn.cmd

:: ─── Ищем Java 17 в нескольких возможных местах ─────────────
set JAVA17=

:: 1. Стандартная установка JDK 17 (Oracle / Adoptium MSI)
if exist "C:\Program Files\Java\jdk-17\bin\java.exe" set JAVA17=C:\Program Files\Java\jdk-17

:: 2. Portable zip распакован в C:\tools\jdk17
if not defined JAVA17 (
    for /d %%D in ("C:\tools\jdk17\jdk-17*") do set JAVA17=%%D
)

:: 3. winget Adoptium установил в Program Files
if not defined JAVA17 (
    for /d %%D in ("C:\Program Files\Eclipse Adoptium\jdk-17*") do set JAVA17=%%D
)

:: 4. Другие варианты в Program Files\Java
if not defined JAVA17 (
    for /d %%D in ("C:\Program Files\Java\jdk-17*") do set JAVA17=%%D
)

if not defined JAVA17 (
    echo.
    echo [ОШИБКА] Java 17 не найдена!
    echo.
    echo Скачайте portable JDK 17 по ссылке:
    echo https://github.com/adoptium/temurin17-binaries/releases/download/jdk-17.0.11+9/OpenJDK17U-jdk_x64_windows_hotspot_17.0.11_9.zip
    echo.
    echo Распакуйте архив в папку C:\tools\jdk17\
    echo Должно получиться: C:\tools\jdk17\jdk-17.0.11+9\
    echo.
    pause
    exit /b 1
)

set JAVA_HOME=%JAVA17%
set PATH=%JAVA_HOME%\bin;%MAVEN_HOME%\bin;%PATH%

echo ============================================
echo  Книжный магазин — REST API (Лаб. раб. №2)
echo ============================================
echo.
echo  Java:  %JAVA_HOME%
echo  Maven: %MAVEN_HOME%
echo.

echo [Проверка Java]
java -version
if errorlevel 1 (
    echo ОШИБКА: не удалось запустить java.exe
    pause
    exit /b 1
)

echo.
echo [Сборка и запуск]
echo Первый запуск займёт несколько минут (скачиваются зависимости Maven)
echo После запуска откройте браузер: http://localhost:8080/swagger-ui/index.html
echo.

cd /d "%~dp0"
call "%MAVEN_BIN%" spring-boot:run

pause
