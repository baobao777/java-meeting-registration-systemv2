<template>
  <div class="form-page">
    <div class="form-card">
      <h2>用户登录</h2>
      <form @submit.prevent="handleLogin">
        <div class="form-group">
          <label>用户名</label>
          <input v-model="form.username" type="text" required placeholder="请输入用户名" />
        </div>
        <div class="form-group">
          <label>密码</label>
          <input v-model="form.password" type="password" required placeholder="请输入密码" />
        </div>
        <p v-if="error" class="error">{{ error }}</p>
        <button type="submit" class="btn-primary" :disabled="loading">{{ loading ? '登录中...' : '登录' }}</button>
      </form>
      <p class="form-footer">还没有账号？<router-link to="/register">立即注册</router-link></p>
    </div>
  </div>
</template>

<script>
import { login } from '../api/user'

export default {
  name: 'Login',
  data() {
    return { form: { username: '', password: '' }, error: '', loading: false }
  },
  methods: {
    async handleLogin() {
      this.loading = true
      this.error = ''
      try {
        const res = await login(this.form)
        if (res.data.code === 200) {
          localStorage.setItem('user', JSON.stringify(res.data.data))
          this.$emit('login-success')
          this.$router.push('/meetings')
        } else {
          this.error = res.data.message
        }
      } catch {
        this.error = '登录失败，请检查网络'
      } finally {
        this.loading = false
      }
    }
  }
}
</script>

<style scoped>
.form-page { display: flex; justify-content: center; padding-top: 60px; }
.form-card { background: #fff; padding: 40px; border-radius: 8px; box-shadow: 0 2px 12px rgba(0,0,0,0.08); width: 400px; }
.form-card h2 { text-align: center; margin-bottom: 24px; color: #1a73e8; }
.form-group { margin-bottom: 16px; }
.form-group label { display: block; margin-bottom: 6px; font-size: 14px; color: #555; }
.form-group input { width: 100%; padding: 10px 12px; border: 1px solid #d9d9d9; border-radius: 4px; font-size: 14px; }
.form-group input:focus { outline: none; border-color: #1a73e8; }
.btn-primary { width: 100%; padding: 10px; background: #1a73e8; color: #fff; border: none; border-radius: 4px; font-size: 15px; cursor: pointer; }
.btn-primary:disabled { background: #93bdf2; }
.error { color: #e53935; font-size: 13px; margin-bottom: 12px; text-align: center; }
.form-footer { text-align: center; margin-top: 16px; font-size: 14px; color: #888; }
.form-footer a { color: #1a73e8; text-decoration: none; }
</style>
