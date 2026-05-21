<template>
  <div class="meeting-detail" v-if="meeting">
    <div class="detail-card">
      <div class="detail-header">
        <h2>{{ meeting.title }}</h2>
        <span class="status" :class="meeting.status.toLowerCase()">{{ statusText(meeting.status) }}</span>
      </div>

      <div class="detail-meta">
        <div class="meta-item"><strong>地点：</strong>{{ meeting.location || '待定' }}</div>
        <div class="meta-item"><strong>开始时间：</strong>{{ formatTime(meeting.startTime) }}</div>
        <div class="meta-item"><strong>结束时间：</strong>{{ formatTime(meeting.endTime) }}</div>
        <div class="meta-item">
          <strong>名额：</strong>
          {{ meeting.capacity > 0 ? meeting.registeredCount + '/' + meeting.capacity : '不限' }}
        </div>
      </div>

      <div class="detail-desc" v-if="meeting.description">
        <h3>会议详情</h3>
        <p>{{ meeting.description }}</p>
      </div>

      <div class="detail-actions">
        <button v-if="canRegister" @click="handleRegister" class="btn-reg" :disabled="regLoading">
          {{ regLoading ? '处理中...' : '立即报名' }}
        </button>
        <button v-if="isRegistered" @click="handleCancel" class="btn-cancel" :disabled="regLoading">
          {{ regLoading ? '处理中...' : '取消报名' }}
        </button>
      </div>
      <p v-if="regError" class="error">{{ regError }}</p>
    </div>
  </div>
  <div v-else class="loading">加载中...</div>
</template>

<script>
import { getMeetingDetail } from '../api/meeting'
import { registerForMeeting, cancelRegistration, checkRegistration } from '../api/registration'

export default {
  name: 'MeetingDetail',
  data() {
    return { meeting: null, isRegistered: false, regLoading: false, regError: '' }
  },
  computed: {
    canRegister() {
      return this.meeting && this.meeting.status === 'UPCOMING' && !this.isRegistered
    }
  },
  async created() {
    try {
      const [detailRes, checkRes] = await Promise.all([
        getMeetingDetail(this.$route.params.id),
        checkRegistration(this.$route.params.id).catch(() => ({ data: { data: false } }))
      ])
      if (detailRes.data.code === 200) this.meeting = detailRes.data.data
      if (checkRes.data.code === 200) this.isRegistered = checkRes.data.data
    } catch {}
  },
  methods: {
    formatTime(t) { return t ? t.replace('T', ' ').substring(0, 16) : '' },
    statusText(s) {
      const map = { UPCOMING: '即将开始', ONGOING: '进行中', FINISHED: '已结束', CANCELLED: '已取消' }
      return map[s] || s
    },
    async handleRegister() {
      this.regLoading = true; this.regError = ''
      try {
        const res = await registerForMeeting(this.$route.params.id)
        if (res.data.code === 200) {
          this.isRegistered = true
          if (this.meeting) this.meeting.registeredCount++
        } else {
          this.regError = res.data.message
        }
      } catch { this.regError = '操作失败' }
      finally { this.regLoading = false }
    },
    async handleCancel() {
      this.regLoading = true; this.regError = ''
      try {
        const res = await cancelRegistration(this.$route.params.id)
        if (res.data.code === 200) {
          this.isRegistered = false
          if (this.meeting) this.meeting.registeredCount--
        } else { this.regError = res.data.message }
      } catch { this.regError = '操作失败' }
      finally { this.regLoading = false }
    }
  }
}
</script>

<style scoped>
.detail-card { background: #fff; border-radius: 8px; padding: 32px; box-shadow: 0 2px 12px rgba(0,0,0,0.08); }
.detail-header { display: flex; align-items: center; gap: 16px; margin-bottom: 24px; }
.detail-header h2 { font-size: 22px; }
.status { padding: 3px 12px; border-radius: 12px; font-size: 13px; }
.status.upcoming { background: #e3f2fd; color: #1565c0; }
.status.ongoing { background: #e8f5e9; color: #2e7d32; }
.status.finished { background: #f5f5f5; color: #757575; }
.status.cancelled { background: #ffebee; color: #c62828; }
.detail-meta { display: grid; grid-template-columns: 1fr 1fr; gap: 12px; margin-bottom: 20px; }
.meta-item { font-size: 14px; color: #555; }
.detail-desc { margin-bottom: 24px; }
.detail-desc h3 { font-size: 16px; margin-bottom: 8px; }
.detail-desc p { font-size: 14px; color: #555; line-height: 1.8; white-space: pre-wrap; }
.detail-actions { display: flex; gap: 12px; }
.btn-reg { padding: 10px 28px; background: #1a73e8; color: #fff; border: none; border-radius: 4px; font-size: 15px; cursor: pointer; }
.btn-cancel { padding: 10px 28px; background: #fff; color: #e53935; border: 1px solid #e53935; border-radius: 4px; font-size: 15px; cursor: pointer; }
.loading { text-align: center; padding: 60px; color: #999; }
.error { color: #e53935; font-size: 13px; margin-top: 12px; }
</style>
