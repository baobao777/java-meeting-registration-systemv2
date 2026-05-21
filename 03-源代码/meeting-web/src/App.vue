<template>
  <div id="app">
    <nav class="navbar">
      <div class="nav-brand">
        <router-link to="/meetings">会议报名管理系统</router-link>
      </div>
      <div class="nav-links">
        <router-link to="/meetings">会议列表</router-link>
        <router-link v-if="user" to="/my-registrations">我的报名</router-link>
        <router-link v-if="user && user.role === 'ADMIN'" to="/admin">管理后台</router-link>
      </div>
      <div class="nav-user">
        <template v-if="user">
          <span class="user-info">{{ user.name }}</span>
          <button @click="handleLogout" class="btn-logout">退出</button>
        </template>
        <template v-else>
          <router-link to="/login" class="btn-login">登录</router-link>
          <router-link to="/register" class="btn-register">注册</router-link>
        </template>
      </div>
    </nav>
    <main class="main-content">
      <router-view @login-success="fetchUser" />
    </main>
  </div>
</template>

<script>
import { logout } from './api/user'

export default {
  name: 'App',
  data() {
    return { user: null }
  },
  created() {
    const saved = localStorage.getItem('user')
    if (saved) this.user = JSON.parse(saved)
  },
  methods: {
    fetchUser() {
      const saved = localStorage.getItem('user')
      if (saved) this.user = JSON.parse(saved)
    },
    async handleLogout() {
      await logout().catch(() => {})
      localStorage.removeItem('user')
      this.user = null
      this.$router.push('/login')
    }
  }
}
</script>

<style>
* { margin: 0; padding: 0; box-sizing: border-box; }
body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif; background: #f0f2f5; color: #333; }
.navbar { display: flex; align-items: center; padding: 0 32px; height: 60px; background: #1a73e8; color: #fff; }
.nav-brand a { color: #fff; text-decoration: none; font-size: 18px; font-weight: 600; margin-right: 40px; }
.nav-links { display: flex; gap: 20px; flex: 1; }
.nav-links a { color: rgba(255,255,255,0.85); text-decoration: none; font-size: 14px; }
.nav-links a:hover, .nav-links a.router-link-exact-active { color: #fff; }
.nav-user { display: flex; align-items: center; gap: 12px; }
.user-info { font-size: 14px; }
.btn-login, .btn-register, .btn-logout { padding: 6px 16px; border-radius: 4px; font-size: 13px; cursor: pointer; text-decoration: none; }
.btn-login { background: rgba(255,255,255,0.2); color: #fff; border: none; }
.btn-register { background: #fff; color: #1a73e8; border: none; }
.btn-logout { background: transparent; color: rgba(255,255,255,0.85); border: 1px solid rgba(255,255,255,0.3); }
.main-content { max-width: 1100px; margin: 24px auto; padding: 0 16px; }
</style>
