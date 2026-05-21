<template>
  <div class="form-page">
    <div class="form-card">
      <h2>用户注册</h2>
      <form @submit.prevent="handleRegister">
        <div class="form-group">
          <label>用户名</label>
          <input v-model="form.username" type="text" required placeholder="用于登录" />
        </div>
        <div class="form-group">
          <label>密码</label>
          <input v-model="form.password" type="password" required placeholder="至少6位" minlength="6" />
        </div>
        <div class="form-group">
          <label>姓名</label>
          <input v-model="form.name" type="text" required placeholder="真实姓名" />
        </div>
        <div class="form-group">
          <label>邮箱</label>
          <input v-model="form.email" type="email" placeholder="选填" />
        </div>
        <div class="form-group">
          <label>电话</label>
          <input v-model="form.phone" type="text" placeholder="选填" />
        </div>
        <p v-if="error" class="error">{{ error }}</p>
        <button type="submit" class="btn-primary" :disabled="loading">{{ loading ? '注册中...' : '注册' }}</button>
      </form>
      <p class="form-footer">已有账号？<router-link to="/login">立即登录</router-link></p>
    </div>
  </div>
</template>

<script>
import { register } from '../api/user'

export default {
  name: 'Register',
  data() {
    return { form: { username: '', password: '', name: '', email: '', phone: '' }, error: '', loading: false }
  },
  methods: {
    async handleRegister() {
      this.loading = true
      this.error = ''
      try {
        const res = await register(this.form)
        if (res.data.code === 200) {
          alert('注册成功，请登录')
          this.$router.push('/login')
        } else {
          this.error = res.data.message
        }
      } catch {
        this.error = '注册失败，请检查网络'
      } finally {
        this.loading = false
      }
    }
  }
}
</script>

<style scoped>
.form-page { display: flex; justify-content: center; padding-top: 40px; }
.form-card { background: #fff; padding: 40px; border-radius: 8px; box-shadow: 0 2px 12px rgba(0,0,0,0.08); width: 400px; }
.form-card h2 { text-align: center; margin-bottom: 24px; color: #1a73e8; }
.form-group { margin-bottom: 14px; }
.form-group label { display: block; margin-bottom: 6px; font-size: 14px; color: #555; }
.form-group input { width: 100%; padding: 10px 12px; border: 1px solid #d9d9d9; border-radius: 4px; font-size: 14px; }
.form-group input:focus { outline: none; border-color: #1a73e8; }
.btn-primary { width: 100%; padding: 10px; background: #1a73e8; color: #fff; border: none; border-radius: 4px; font-size: 15px; cursor: pointer; }
.btn-primary:disabled { background: #93bdf2; }
.error { color: #e53935; font-size: 13px; margin-bottom: 12px; text-align: center; }
.form-footer { text-align: center; margin-top: 16px; font-size: 14px; color: #888; }
.form-footer a { color: #1a73e8; text-decoration: none; }
</style>
