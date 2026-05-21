<template>
  <div>
    <h2 class="page-title">我的报名</h2>

    <div v-if="loading" class="loading">加载中...</div>
    <div v-else-if="registrations.length === 0" class="empty">暂无报名记录</div>

    <div v-else class="reg-list">
      <div v-for="r in registrations" :key="r.id" class="reg-card">
        <div class="reg-main" @click="$router.push('/meetings/' + r.meetingId)">
          <h3>{{ r.meetingTitle }}</h3>
          <div class="reg-info">
            <span>📍 {{ r.meetingLocation || '待定' }}</span>
            <span>🕐 {{ formatTime(r.meetingStartTime) }}</span>
          </div>
        </div>
        <div class="reg-side">
          <span class="reg-status" :class="r.status.toLowerCase()">{{ r.status === 'REGISTERED' ? '已报名' : '已取消' }}</span>
          <button v-if="r.status === 'REGISTERED'" @click="handleCancel(r.meetingId)" class="btn-sm-cancel">取消</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { getMyRegistrations, cancelRegistration } from '../api/registration'

export default {
  name: 'MyRegistrations',
  data() {
    return { registrations: [], loading: true }
  },
  async created() {
    const res = await getMyRegistrations()
    if (res.data.code === 200) this.registrations = res.data.data
    this.loading = false
  },
  methods: {
    formatTime(t) { return t ? t.replace('T', ' ').substring(0, 16) : '' },
    async handleCancel(meetingId) {
      if (!confirm('确定取消报名？')) return
      const res = await cancelRegistration(meetingId)
      if (res.data.code === 200) {
        const r = this.registrations.find(x => x.meetingId === meetingId)
        if (r) r.status = 'CANCELLED'
      } else {
        alert(res.data.message)
      }
    }
  }
}
</script>

<style scoped>
.page-title { font-size: 20px; margin-bottom: 20px; }
.loading, .empty { text-align: center; color: #999; padding: 60px 0; }
.reg-list { display: flex; flex-direction: column; gap: 12px; }
.reg-card { background: #fff; border-radius: 8px; padding: 20px; display: flex; justify-content: space-between; align-items: center; box-shadow: 0 1px 4px rgba(0,0,0,0.06); }
.reg-main { flex: 1; cursor: pointer; }
.reg-main h3 { font-size: 16px; margin-bottom: 8px; }
.reg-info { display: flex; gap: 16px; font-size: 13px; color: #888; }
.reg-side { display: flex; flex-direction: column; align-items: flex-end; gap: 8px; }
.reg-status { font-size: 13px; padding: 2px 10px; border-radius: 10px; }
.reg-status.registered { background: #e8f5e9; color: #2e7d32; }
.reg-status.cancelled { background: #ffebee; color: #c62828; }
.btn-sm-cancel { padding: 4px 12px; font-size: 12px; background: #fff; color: #e53935; border: 1px solid #e53935; border-radius: 4px; cursor: pointer; }
</style>
