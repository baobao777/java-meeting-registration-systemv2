import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  { path: '/', redirect: '/login' },
  { path: '/login', name: 'Login', component: () => import('../views/Login.vue') },
  { path: '/register', name: 'Register', component: () => import('../views/Register.vue') },
  { path: '/meetings', name: 'MeetingList', component: () => import('../views/MeetingList.vue'), meta: { requiresAuth: true } },
  { path: '/meetings/:id', name: 'MeetingDetail', component: () => import('../views/MeetingDetail.vue'), meta: { requiresAuth: true } },
  { path: '/my-registrations', name: 'MyRegistrations', component: () => import('../views/MyRegistrations.vue'), meta: { requiresAuth: true } },
  { path: '/admin', name: 'AdminDashboard', component: () => import('../views/AdminDashboard.vue'), meta: { requiresAuth: true, requiresAdmin: true } },
  { path: '/admin/meeting/new', name: 'AdminMeetingCreate', component: () => import('../views/AdminMeetingForm.vue'), meta: { requiresAuth: true, requiresAdmin: true } },
  { path: '/admin/meeting/:id/edit', name: 'AdminMeetingEdit', component: () => import('../views/AdminMeetingForm.vue'), meta: { requiresAuth: true, requiresAdmin: true } },
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫：未登录跳转登录页
router.beforeEach((to, from, next) => {
  const userStr = localStorage.getItem('user')
  const user = userStr ? JSON.parse(userStr) : null

  if (to.meta.requiresAuth && !user) {
    next('/login')
  } else if (to.meta.requiresAdmin && user?.role !== 'ADMIN') {
    next('/meetings')
  } else {
    next()
  }
})

export default router
