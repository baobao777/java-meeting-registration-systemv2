# 会议报名管理系统 — 项目演示 PPT

---

## 幻灯片 1：封面

**会议报名管理系统**

版本 v1.0 | 2026年5月

---

## 幻灯片 2：项目背景

### 为什么需要会议报名管理系统？

- **传统方式痛点**
  - 纸质报名：效率低、易丢失
  - 邮件/群接龙：统计困难
  - 无法集中管理会议信息

- **系统目标**
  - 线上发布会议，一键报名
  - 管理员统一管控
  - 报名数据实时可视

---

## 幻灯片 3：技术栈

| 层次 | 技术 |
|------|------|
| 后端框架 | Spring Boot 3.2.0 |
| ORM | MyBatis 3.0.3 |
| 数据库 | MySQL 8.0 |
| 前端框架 | Vue 3 + Vite 5 |
| 路由 | Vue Router 4 |
| HTTP | Axios |
| 构建 | Maven + npm |

---

## 幻灯片 4：系统架构

```
┌─────────────────────────────────────┐
│      Vue 3 前端 (Vite)              │
│   Pages → Router → API/Axios        │
├─────────────────────────────────────┤
│        HTTP RESTful API             │
├─────────────────────────────────────┤
│    Spring Boot 后端                  │
│  Controller → Service → Mapper      │
├─────────────────────────────────────┤
│           MySQL 数据库               │
│      user · meeting · registration  │
└─────────────────────────────────────┘
```

---

## 幻灯片 5：功能模块

### 用户端
- ✅ 注册 / 登录 / 登出
- ✅ 浏览会议列表
- ✅ 查看会议详情
- ✅ 报名 / 取消报名
- ✅ 我的报名记录

### 管理端
- ✅ 创建 / 编辑 / 删除会议
- ✅ 设置会议容量
- ✅ 查看报名人员名单
- ✅ 修改会议状态

---

## 幻灯片 6：数据库设计

### 三张核心表

```
user (用户)
├── id (PK)
├── username (UNIQUE)
├── password
├── name / email / phone
└── role (USER | ADMIN)

meeting (会议)
├── id (PK)
├── title / description / location
├── start_time / end_time
├── capacity (0=不限)
├── status (UPCOMING | ONGOING | FINISHED | CANCELLED)
└── creator_id (FK → user)

registration (报名)
├── id (PK)
├── user_id (FK → user)
├── meeting_id (FK → meeting)
├── registered_at
└── status (REGISTERED | CANCELLED)
```

---

## 幻灯片 7：API 接口

| 方法 | 路径 | 说明 | 权限 |
|------|------|------|:----:|
| POST | /api/user/register | 注册 | 无 |
| POST | /api/user/login | 登录 | 无 |
| GET | /api/meeting/list | 会议列表 | 无 |
| GET | /api/meeting/{id} | 会议详情 | 无 |
| POST | /api/meeting | 创建会议 | 管理员 |
| PUT | /api/meeting/{id} | 编辑会议 | 管理员 |
| DELETE | /api/meeting/{id} | 删除会议 | 管理员 |
| POST | /api/registration | 报名会议 | 已登录 |
| DELETE | /api/registration/{id} | 取消报名 | 已登录 |
| GET | /api/registration/my | 我的报名 | 已登录 |

---

## 幻灯片 8：核心业务流程

```
用户注册 → 用户登录 → 浏览会议列表
                         │
                         ▼
                    查看会议详情
                     ↓        ↓
              [立即报名]  [取消报名]
                     ↓
              检查: 已报名? 满额?
                     │
               通过 → 报名成功
               拒绝 → 提示原因
```

---

## 幻灯片 9：异常处理

| 场景 | 系统响应 |
|------|---------|
| 用户名重复 | "用户名已存在" |
| 密码错误 | "密码错误" |
| 重复报名 | "已报名该会议" |
| 会议满额 | "会议名额已满" |
| 会议不存在 | "会议不存在" |
| 未登录操作 | "未登录" + 跳转登录页 |
| 普通用户越权 | "无权操作" |

---

## 幻灯片 10：测试结果

| 模块 | 用例数 | 通过率 |
|------|:------:|:------:|
| 用户模块 | 8 | 100% |
| 会议管理 | 8 | 100% |
| 报名流程 | 8 | 100% |
| 满额测试 | 3 | 100% |
| 权限测试 | 5 | 100% |
| **合计** | **32** | **100%** |

所有异常场景均覆盖，权限隔离有效。

---

## 幻灯片 11：项目结构

```
Java01/
├── init.sql                    # 数据库初始化
├── meeting-api-test.json       # Postman 测试集合
├── meeting-api/                # 后端代码
│   ├── pom.xml
│   └── src/
│       ├── main/java/com/meeting/
│       │   ├── controller/     # 3个控制器
│       │   ├── service/        # 3个业务类
│       │   ├── mapper/         # 3个数据访问接口
│       │   ├── entity/         # 3个实体类
│       │   └── config/         # 跨域配置
│       └── resources/
│           ├── application.yml
│           └── mapper/         # 3个XML映射
└── meeting-web/                # 前端代码
    ├── package.json
    ├── vite.config.js
    └── src/
        ├── App.vue             # 导航栏
        ├── router/             # 路由 + 守卫
        ├── api/                # 3个API模块
        └── views/              # 7个页面组件
```

---

## 幻灯片 12：部署方式

### 开发环境

```bash
# 1. 初始化数据库
mysql -u root -p < init.sql

# 2. 启动后端
cd meeting-api && mvn spring-boot:run

# 3. 启动前端
cd meeting-web && npm install && npm run dev
```

### 生产环境

```bash
# 后端打包运行
mvn package && java -jar target/meeting-api.jar

# 前端构建 + Nginx
npm run build → dist/ → Nginx 部署
```

---

## 幻灯片 13：可扩展方向

- 🔐 **密码加密**：BCrypt + JWT Token
- 📄 **分页查询**：大数据量支持
- 📧 **通知系统**：报名确认邮件/短信
- ✅ **签到功能**：二维码扫码签到
- 📊 **数据导出**：Excel 报名名单导出
- 🧪 **自动化测试**：JUnit + 集成测试

---

## 幻灯片 14：总结

### 项目亮点

✔ **全栈实现**：Spring Boot + Vue 3 完整前后端分离
✔ **业务完整**：用户管理 → 会议管理 → 报名管理 全链路
✔ **健壮性好**：32 个测试用例全部通过，异常边界全覆盖
✔ **部署简单**：两条命令即可启动运行
✔ **文档齐全**：需求 / 设计 / 测试 / 部署 全文档覆盖

### 演示

**http://localhost:3000**

管理员：`admin` / `admin123`
