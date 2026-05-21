import axios from 'axios'

export function registerForMeeting(meetingId) {
  return axios.post('/api/registration', { meetingId })
}

export function cancelRegistration(meetingId) {
  return axios.delete(`/api/registration/${meetingId}`)
}

export function getMyRegistrations() {
  return axios.get('/api/registration/my')
}

export function getMeetingRegistrations(meetingId) {
  return axios.get(`/api/registration/meeting/${meetingId}`)
}

export function checkRegistration(meetingId) {
  return axios.get(`/api/registration/check/${meetingId}`)
}
