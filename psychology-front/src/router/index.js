import Vue from 'vue'
import VueRouter from 'vue-router'
import store from '@/store'
import authToken from '@/utils/authToken'

Vue.use(VueRouter)

const routes = [
  { path: '/login', name: 'login', component: () => import('../views/LoginView.vue') },
  { path: '/register', name: 'register', component: () => import('../views/RegisterView.vue') },
  {
    path: '/main',
    name: 'main',
    component: () => import('../views/MainView.vue'),
    children: [
      { path: '/consultant-list', name: 'consultantList', component: () => import('../views/ConsultantListView.vue') },
      { path: '/consultant-detail/:id', name: 'consultantDetail', component: () => import('../views/ConsultantDetailView.vue') },
      { path: '/appointment/:id?', name: 'appointment', component: () => import('../views/AppointmentView.vue') },
      { path: '/review/:id', name: 'review', component: () => import('../views/ReviewView.vue') },
      { path: '/evaluation', name: 'evaluation', component: () => import('../views/EvaluationView.vue') },
      { path: '/reports', name: 'reports', component: () => import('../views/ReportListView.vue') },
      { path: '/profile', name: 'profile', component: () => import('../views/ProfileView.vue') }
    ]
  },
  { path: '/', redirect: '/main' }
]

const router = new VueRouter({
  routes
})

// 路由守卫
router.beforeEach((to, _from, next) => {
  const whiteList = ["/login", "/register"]
  // 白名单直接放行
  if (whiteList.includes(to.path)) {
    next()
    return
  }
  // 没有token，跳转登录
  if (!authToken.getToken()) {
    alert("请先登录")
    next("/login")
    return
  }
  // 有token但用户信息未加载，先获取用户信息
  if (store.state.userId === -1) {
    store.dispatch("getuserinfo").then(res => {
      if (res) {
        next()
      } else {
        next('/login')
      }
    }).catch(() => {
      next('/login')
    })
  } else {
    // 已登录且用户信息已存在，直接放行
    next()
  }
})

export default router
