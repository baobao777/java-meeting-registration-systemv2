import axios from 'axios'

export function login(data) {
  return axios.post('/api/user/login', data)
}

export function register(data) {
  return axios.post('/api/user/register', data)
}

export function logout() {
  return axios.post('/api/user/logout')
}

export function getUserInfo() {
  return axios.get('/api/user/info')
}
