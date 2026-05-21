#!/bin/bash
echo "========================================"
echo "  会议报名管理系统 - 启动脚本"
echo "========================================"

# 检查 JDK
if ! command -v java &> /dev/null; then
    echo "[错误] 未检测到 JDK，请安装 JDK 17+"
    exit 1
fi

echo "[信息] JDK 正常"
echo "[信息] 启动后端服务..."
echo "[信息] 访问地址: http://localhost:8080"
echo "[信息] 管理员账号: admin / admin123"
echo "[信息] 按 Ctrl+C 停止"
echo "========================================"

java -jar meeting-api-1.0.0.jar
