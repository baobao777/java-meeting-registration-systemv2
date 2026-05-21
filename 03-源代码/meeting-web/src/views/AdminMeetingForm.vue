<template>
  <div class="form-page">
    <div class="form-card">
      <h2>{{ isEdit ? '编辑会议' : '创建会议' }}</h2>
      <form @submit.prevent="handleSubmit">
        <div class="form-group">
          <label>会议标题 *</label>
          <input v-model="form.title" type="text" required placeholder="请输入会议标题" />
        </div>
        <div class="form-group">
          <label>会议描述</label>
          <textarea v-model="form.description" rows="4" placeholder="会议详细内容..."></textarea>
        </div>
        <div class="form-group">
          <label>地点</label>
          <input v-model="form.location" type="text" placeholder="会议室/线上链接" />
        </div>
        <div class="form-row">
          <div class="form-group">
            <label>开始时间 *</label>
            <input v-model="form.startTime" type="datetime-local" required />
          </div>
          <div class="form-group">
            <label>结束时间 *</label>
            <input v-model="form.endTime" type="datetime-local" required />
          </div>
        </div>
        <div class="form-group">
          <label>容量（0=不限）</label>
          <input v-model="form.capacity" type="number" min="0" />
        </div>
        <div class="form-group" v-if="isEdit">
          <label>状态</label>
          <select v-model="form.status">
            <option value="UPCOMING">即将开始</option>
            <option value="ONGOING">进行中</option>
            <option value="FINISHED">已结束</option>
            <option value="CANCELLED">已取消</option>
          </select>
        </div>
        <p v-if="error" class="error">{{ error }}</p>
        <div class="form-actions">
          <button type="submit" class="btn-primary" :disabled="loading">{{ loading ? '提交中...' : '保存' }}</button>
          <button type="button" @click="$router.push('/admin')" class="btn-cancel">取消</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import { getMeetingDetail, createMeeting, updateMeeting } from '../api/meeting'

export default {
  name: 'AdminMeetingForm',
  data() {
    return {
      isEdit: false,
      form: { title: '', description: '', location: '', startTime: '', endTime: '', capacity: 0, status: 'UPCOMING' },
      error: '',
      loading: false
    }
  },
  async created() {
    if (this.$route.params.id) {
      this.isEdit = true
      const res = await getMeetingDetail(this.$route.params.id)
      if (res.data.code === 200) {
        const m = res.data.data
        this.form = {
          title: m.title,
          description: m.description || '',
          location: m.location || '',
          startTime: m.startTime ? m.startTime.substring(0, 16) : '',
          endTime: m.endTime ? m.endTime.substring(0, 16) : '',
          capacity: m.capacity,
          status: m.status
        }
      }
    }
  },
  methods: {
    async handleSubmit() {
      this.loading = true; this.error = ''
      try {
        const payload = {
          ...this.form,
          startTime: this.form.startTime + ':00',
          endTime: this.form.endTime + ':00',
          capacity: Number(this.form.capacity)
        }
        let res
        if (this.isEdit) {
          res = await updateMeeting(this.$route.params.id, payload)
        } else {
          res = await createMeeting(payload)
        }
        if (res.data.code === 200) {
          this.$router.push('/admin')
        } else {
          this.error = res.data.message
        }
      } catch {
        this.error = '提交失败'
      } finally {
        this.loading = false
      }
    }
  }
}
</script>

<style scoped>
.form-page { display: flex; justify-content: center; }
.form-card { background: #fff; padding: 32px; border-radius: 8px; box-shadow: 0 2px 12px rgba(0,0,0,0.08); width: 560px; }
.form-card h2 { margin-bottom: 24px; color: #1a73e8; }
.form-group { margin-bottom: 16px; }
.form-group label { display: block; margin-bottom: 6px; font-size: 14px; color: #555; }
.form-group input, .form-group textarea, .form-group select { width: 100%; padding: 10px 12px; border: 1px solid #d9d9d9; border-radius: 4px; font-size: 14px; font-family: inherit; }
.form-group input:focus, .form-group textarea:focus, .form-group select:focus { outline: none; border-color: #1a73e8; }
.form-row { display: flex; gap: 16px; }
.form-row .form-group { flex: 1; }
.form-actions { display: flex; gap: 12px; margin-top: 20px; }
.btn-primary { flex: 1; padding: 10px; background: #1a73e8; color: #fff; border: none; border-radius: 4px; font-size: 15px; cursor: pointer; }
.btn-primary:disabled { background: #93bdf2; }
.btn-cancel { padding: 10px 24px; background: #fff; color: #555; border: 1px solid #d9d9d9; border-radius: 4px; font-size: 14px; cursor: pointer; }
.error { color: #e53935; font-size: 13px; margin-bottom: 12px; text-align: center; }
</style>
