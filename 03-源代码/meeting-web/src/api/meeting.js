import axios from 'axios'

export function getMeetingList() {
  return axios.get('/api/meeting/list')
}

export function getMeetingDetail(id) {
  return axios.get(`/api/meeting/${id}`)
}

export function createMeeting(data) {
  return axios.post('/api/meeting', data)
}

export function updateMeeting(id, data) {
  return axios.put(`/api/meeting/${id}`, data)
}

export function deleteMeeting(id) {
  return axios.delete(`/api/meeting/${id}`)
}
