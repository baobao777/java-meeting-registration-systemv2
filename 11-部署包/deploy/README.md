# 会议报名管理系统 - 部署包

## 环境要求
- JDK 17+
- MySQL 8.0+

## 部署步骤

### 1. 初始化数据库
```bash
mysql -u root -p --default-character-set=utf8mb4 < init.sql
```

### 2. 修改数据库配置
编辑 `application.yml`，修改 `password` 为你的 MySQL 密码

### 3. 启动系统
Windows 双击 `start.bat`
Linux/Mac 运行 `bash start.sh`

### 4. 访问系统
打开浏览器访问: http://localhost:8080
管理员账号: admin / admin123
