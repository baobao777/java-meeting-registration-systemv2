@echo off
chcp 65001 >nul
title 会议报名管理系统

echo ========================================
echo   会议报名管理系统 - 启动脚本
echo ========================================
echo.

:: 检查 JDK
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo [错误] 未检测到 JDK，请安装 JDK 17+
    pause
    exit 1
)

echo [信息] JDK 正常
echo.

:: 启动
echo [信息] 启动后端服务...
echo [信息] 访问地址: http://localhost:8080
echo [信息] 管理员账号: admin / admin123
echo [信息] 按 Ctrl+C 停止
echo ========================================
echo.

java -jar meeting-api-1.0.0.jar
pause
