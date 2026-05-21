<template>
  <div>
    <div class="page-header">
      <h2>管理后台</h2>
      <router-link to="/admin/meeting/new" class="btn-create">+ 创建会议</router-link>
    </div>

    <div v-if="loading" class="loading">加载中...</div>
    <div v-else-if="meetings.length === 0" class="empty">暂无会议</div>

    <div v-else class="admin-table-wrapper">
      <table class="admin-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>标题</th>
            <th>时间</th>
            <th>状态</th>
            <th>报名/容量</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="m in meetings" :key="m.id">
            <td>{{ m.id }}</td>
            <td class="title-cell">{{ m.title }}</td>
            <td>{{ formatTime(m.startTime) }}</td>
            <td><span class="status" :class="m.status.toLowerCase()">{{ statusText(m.status) }}</span></td>
            <td>{{ m.registeredCount }}{{ m.capacity > 0 ? '/' + m.capacity : '' }}</td>
            <td class="actions">
              <router-link :to="'/admin/meeting/' + m.id + '/edit'" class="btn-edit">编辑</router-link>
              <button @click="handleViewRegistrations(m)" class="btn-view">报名列表</button>
              <button @click="handleDelete(m.id)" class="btn-del">删除</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- registration detail modal -->
    <div v-if="showModal" class="modal-overlay" @click.self="showModal = false">
      <div class="modal">
        <div class="modal-header">
          <h3>报名列表 - {{ modalMeeting?.title }}</h3>
          <button @click="showModal = false" class="modal-close">&times;</button>
        </div>
        <div class="modal-body">
          <table class="admin-table" v-if="registrations.length > 0">
            <thead>
              <tr><th>姓名</th><th>电话</th><th>报名时间</th><th>状态</th></tr>
            </thead>
            <tbody>
              <tr v-for="r in registrations" :key="r.id">
                <td>{{ r.userName }}</td>
                <td>{{ r.userPhone || '-' }}</td>
                <td>{{ formatTime(r.registeredAt) }}</td>
                <td><span class="status" :class="r.status.toLowerCase()">{{ r.status === 'REGISTERED' ? '已报名' : '已取消' }}</span></td>
              </tr>
            </tbody>
          </table>
          <div v-else class="empty">暂无报名记录</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { getMeetingList, deleteMeeting } from '../api/meeting'
import { getMeetingRegistrations } from '../api/registration'

export default {
  name: 'AdminDashboard',
  data() {
    return { meetings: [], loading: true, showModal: false, modalMeeting: null, registrations: [] }
  },
  async created() {
    const res = await getMeetingList()
    if (res.data.code === 200) this.meetings = res.data.data
    this.loading = false
  },
  methods: {
    formatTime(t) { return t ? t.replace('T', ' ').substring(0, 16) : '' },
    statusText(s) {
      const map = { UPCOMING: '即将开始', ONGOING: '进行中', FINISHED: '已结束', CANCELLED: '已取消' }
      return map[s] || s
    },
    async handleDelete(id) {
      if (!confirm('确定删除该会议？')) return
      const res = await deleteMeeting(id)
      if (res.data.code === 200) {
        this.meetings = this.meetings.filter(m => m.id !== id)
      } else {
        alert(res.data.message)
      }
    },
    async handleViewRegistrations(meeting) {
      this.modalMeeting = meeting
      const res = await getMeetingRegistrations(meeting.id)
      if (res.data.code === 200) {
        this.registrations = res.data.data
        this.showModal = true
      }
    }
  }
}
</script>

<style scoped>
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
.page-header h2 { font-size: 20px; }
.btn-create { padding: 8px 20px; background: #1a73e8; color: #fff; text-decoration: none; border-radius: 4px; font-size: 14px; }
.loading, .empty { text-align: center; color: #999; padding: 40px 0; }
.admin-table-wrapper { background: #fff; border-radius: 8px; overflow: hidden; box-shadow: 0 1px 4px rgba(0,0,0,0.06); }
.admin-table { width: 100%; border-collapse: collapse; }
.admin-table th, .admin-table td { padding: 12px 16px; text-align: left; font-size: 14px; border-bottom: 1px solid #f0f0f0; }
.admin-table th { background: #fafafa; font-weight: 600; color: #555; }
.title-cell { max-width: 200px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.status { padding: 2px 10px; border-radius: 10px; font-size: 12px; }
.status.upcoming { background: #e3f2fd; color: #1565c0; }
.status.ongoing { background: #e8f5e9; color: #2e7d32; }
.status.finished { background: #f5f5f5; color: #757575; }
.status.cancelled { background: #ffebee; color: #c62828; }
.actions { display: flex; gap: 8px; }
.btn-edit { padding: 4px 12px; font-size: 12px; background: #e3f2fd; color: #1565c0; text-decoration: none; border-radius: 4px; }
.btn-view { padding: 4px 12px; font-size: 12px; background: #f3e5f5; color: #6a1b9a; border: none; border-radius: 4px; cursor: pointer; }
.btn-del { padding: 4px 12px; font-size: 12px; background: #ffebee; color: #c62828; border: none; border-radius: 4px; cursor: pointer; }
.modal-overlay { position: fixed; inset: 0; background: rgba(0,0,0,0.4); display: flex; align-items: center; justify-content: center; z-index: 100; }
.modal { background: #fff; border-radius: 8px; width: 600px; max-height: 80vh; overflow: auto; }
.modal-header { display: flex; justify-content: space-between; align-items: center; padding: 16px 24px; border-bottom: 1px solid #f0f0f0; }
.modal-header h3 { font-size: 16px; }
.modal-close { background: none; border: none; font-size: 24px; cursor: pointer; color: #888; }
.modal-body { padding: 16px 24px; }
</style>
