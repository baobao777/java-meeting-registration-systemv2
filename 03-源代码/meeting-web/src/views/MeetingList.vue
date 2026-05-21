<template>
  <div class="meeting-list">
    <div class="page-header">
      <h2>会议列表</h2>
    </div>

    <div v-if="loading" class="loading">加载中...</div>

    <div v-else-if="meetings.length === 0" class="empty">暂无会议</div>

    <div v-else class="meeting-grid">
      <div v-for="m in meetings" :key="m.id" class="meeting-card" @click="$router.push('/meetings/' + m.id)">
        <div class="card-header">
          <span class="status" :class="m.status.toLowerCase()">{{ statusText(m.status) }}</span>
          <span v-if="m.capacity > 0" class="capacity">{{ m.registeredCount }}/{{ m.capacity }}</span>
        </div>
        <h3 class="card-title">{{ m.title }}</h3>
        <div class="card-info">
          <span>📍 {{ m.location || '待定' }}</span>
          <span>🕐 {{ formatTime(m.startTime) }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { getMeetingList } from '../api/meeting'

export default {
  name: 'MeetingList',
  data() {
    return { meetings: [], loading: true }
  },
  async created() {
    try {
      const res = await getMeetingList()
      if (res.data.code === 200) {
        this.meetings = res.data.data
      }
    } finally {
      this.loading = false
    }
  },
  methods: {
    formatTime(t) { return t ? t.replace('T', ' ').substring(0, 16) : '' },
    statusText(s) {
      const map = { UPCOMING: '即将开始', ONGOING: '进行中', FINISHED: '已结束', CANCELLED: '已取消' }
      return map[s] || s
    }
  }
}
</script>

<style scoped>
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
.page-header h2 { font-size: 20px; }
.loading, .empty { text-align: center; color: #999; padding: 60px 0; font-size: 15px; }
.meeting-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(320px, 1fr)); gap: 16px; }
.meeting-card { background: #fff; border-radius: 8px; padding: 20px; cursor: pointer; box-shadow: 0 1px 4px rgba(0,0,0,0.06); transition: box-shadow 0.2s; }
.meeting-card:hover { box-shadow: 0 4px 16px rgba(0,0,0,0.1); }
.card-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 12px; }
.status { padding: 2px 10px; border-radius: 10px; font-size: 12px; }
.status.upcoming { background: #e3f2fd; color: #1565c0; }
.status.ongoing { background: #e8f5e9; color: #2e7d32; }
.status.finished { background: #f5f5f5; color: #757575; }
.status.cancelled { background: #ffebee; color: #c62828; }
.capacity { font-size: 12px; color: #888; }
.card-title { font-size: 16px; margin-bottom: 12px; color: #222; }
.card-info { display: flex; flex-direction: column; gap: 4px; font-size: 13px; color: #888; }
</style>
