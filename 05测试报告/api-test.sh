#!/bin/bash
# 会议报名管理系统 - 接口测试脚本
# 使用方法: bash api-test.sh
# 需要先启动后端: cd meeting-api && mvn spring-boot:run

BASE_URL="http://localhost:8080/api"
COOKIE_FILE="/tmp/meeting_cookies.txt"
PASS=0
FAIL=0

# 颜色
GREEN='\033[0;32m'
RED='\033[0;31m'
CYAN='\033[0;36m'
NC='\033[0m'

check() {
  local desc="$1"
  local method="$2"
  local url="$3"
  local data="$4"
  local expect_code="${5:-200}"

  echo -e "${CYAN}[$method] $desc${NC}"
  echo "  → $method $url"

  if [ "$method" = "GET" ]; then
    resp=$(curl -s -w "\n%{http_code}" -b "$COOKIE_FILE" -c "$COOKIE_FILE" "$url" 2>/dev/null)
  else
    resp=$(curl -s -w "\n%{http_code}" -b "$COOKIE_FILE" -c "$COOKIE_FILE" \
      -H "Content-Type: application/json" -X "$method" -d "$data" "$url" 2>/dev/null)
  fi

  http_code=$(echo "$resp" | tail -1)
  body=$(echo "$resp" | sed '$d')

  if [ "$http_code" = "$expect_code" ]; then
    echo -e "  ${GREEN}✓ $http_code${NC}"
    PASS=$((PASS+1))
  else
    echo -e "  ${RED}✗ 期望 $expect_code, 实际 $http_code${NC}"
    FAIL=$((FAIL+1))
  fi
  echo "  body: $body"
  echo ""
}

echo "=========================================="
echo "  会议报名管理系统 - API 接口测试"
echo "=========================================="
echo ""

# ============================
# 1. 用户模块
# ============================
echo "─── 1. 用户模块 ────────────────────────"

check "注册新用户" POST "$BASE_URL/user/register" \
  '{"username":"testuser","password":"123456","name":"测试用户","email":"test@test.com","phone":"13800138000"}'

check "重复注册" POST "$BASE_URL/user/register" \
  '{"username":"testuser","password":"123456","name":"测试用户"}'

check "登录" POST "$BASE_URL/user/login" \
  '{"username":"testuser","password":"123456"}'

check "获取当前用户" GET "$BASE_URL/user/info"

check "登录错误密码" POST "$BASE_URL/user/login" \
  '{"username":"testuser","password":"wrong"}'

check "登录不存在的用户" POST "$BASE_URL/user/login" \
  '{"username":"nobody","password":"123"}' 400

# ============================
# 2. 会议模块（未登录）
# ============================
echo "─── 2. 会议模块（未登录访问）───────────"

# 清除 cookie 模拟未登录
rm -f "$COOKIE_FILE"

check "未登录查看会议列表" GET "$BASE_URL/meeting/list"

check "未登录创建会议" POST "$BASE_URL/meeting" \
  '{"title":"test"}' 403

check "未登录删除会议" DELETE "$BASE_URL/meeting/1" '' 403

# ============================
# 3. 报名模块（未登录）
# ============================
echo "─── 3. 报名模块（未登录）───────────────"

check "未登录报名" POST "$BASE_URL/registration" \
  '{"meetingId":1}' 401

check "未登录查看我的报名" GET "$BASE_URL/registration/my" '' 401

# ============================
# 4. 管理员操作
# ============================
echo "─── 4. 管理员操作 ──────────────────────"

check "管理员登录" POST "$BASE_URL/user/login" \
  '{"username":"admin","password":"admin123"}'

check "创建会议" POST "$BASE_URL/meeting" \
  '{"title":"2026年Q2技术研讨会","description":"讨论最新技术趋势","location":"3楼会议室A","startTime":"2026-06-15T09:00:00","endTime":"2026-06-15T12:00:00","capacity":30}'

check "创建会议（不限容量）" POST "$BASE_URL/meeting" \
  '{"title":"全员大会","description":"公司全员季度大会","location":"1楼大礼堂","startTime":"2026-07-01T14:00:00","endTime":"2026-07-01T17:00:00","capacity":0}'

check "获取所有会议" GET "$BASE_URL/meeting/list"

check "获取会议详情" GET "$BASE_URL/meeting/1"

check "编辑会议" PUT "$BASE_URL/meeting/1" \
  '{"title":"2026年Q2技术研讨会（更新）","description":"更新后的描述","location":"3楼会议室B","startTime":"2026-06-15T09:00:00","endTime":"2026-06-15T12:00:00","capacity":50,"status":"UPCOMING"}'

check "获取不存在的会议" GET "$BASE_URL/meeting/999" '' 404

check "编辑不存在的会议" PUT "$BASE_URL/meeting/999" \
  '{"title":"不存在"}' 404

check "删除不存在的会议" DELETE "$BASE_URL/meeting/999" '' 404

# ============================
# 5. 报名流程
# ============================
echo "─── 5. 报名流程 ────────────────────────"

# 先切回普通用户
check "普通用户登录" POST "$BASE_URL/user/login" \
  '{"username":"testuser","password":"123456"}'

check "检查报名状态（未报名）" GET "$BASE_URL/registration/check/1"

check "报名会议" POST "$BASE_URL/registration" '{"meetingId":1}'

check "重复报名" POST "$BASE_URL/registration" '{"meetingId":1}' 400

check "检查报名状态（已报名）" GET "$BASE_URL/registration/check/1"

check "查看我的报名" GET "$BASE_URL/registration/my"

check "取消报名" DELETE "$BASE_URL/registration/1"

check "取消已取消的报名" DELETE "$BASE_URL/registration/1" 400

# ============================
# 6. 报名满额测试
# ============================
echo "─── 6. 容量限制测试 ───────────────────"

# 管理员创建名额为1的会议
check "管理员登录" POST "$BASE_URL/user/login" \
  '{"username":"admin","password":"admin123"}'

check "创建限额会议" POST "$BASE_URL/meeting" \
  '{"title":"限量报名测试","description":"仅限1人","location":"测试","startTime":"2026-08-01T10:00:00","endTime":"2026-08-01T11:00:00","capacity":1}'

# testuser 报名（应该成功）
check "testuser报名限额会议" POST "$BASE_URL/user/login" \
  '{"username":"testuser","password":"123456"}'
check "testuser报名" POST "$BASE_URL/registration" '{"meetingId":3}'

# 注册第二个用户
check "注册第二个用户" POST "$BASE_URL/user/register" \
  '{"username":"testuser2","password":"123456","name":"测试用户2"}'
check "testuser2报名限额会议（应满额）" POST "$BASE_URL/user/login" \
  '{"username":"testuser2","password":"123456"}'
check "testuser2报名" POST "$BASE_URL/registration" '{"meetingId":3}' 400

# ============================
# 7. 管理员查看报名
# ============================
echo "─── 7. 管理员查看报名 ─────────────────"

check "管理员查看报名列表" POST "$BASE_URL/user/login" \
  '{"username":"admin","password":"admin123"}'
check "查看会议1报名列表" GET "$BASE_URL/registration/meeting/1"

check "非管理员查看报名列表" POST "$BASE_URL/user/login" \
  '{"username":"testuser","password":"123456"}'
check "testuser查看报名列表（应403）" GET "$BASE_URL/registration/meeting/1" '' 403

# ============================
# 8. 权限测试
# ============================
echo "─── 8. 权限测试 ────────────────────────"

check "普通用户创建会议" POST "$BASE_URL/meeting" \
  '{"title":"普通用户创建","startTime":"2026-09-01T10:00:00","endTime":"2026-09-01T11:00:00"}' 403

check "普通用户删除会议" DELETE "$BASE_URL/meeting/1" '' 403

echo "=========================================="
echo -e "  测试完成: ${GREEN}通过 $PASS${NC}, ${RED}失败 $FAIL${NC}"
echo "=========================================="
