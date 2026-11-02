import Vue from 'vue'
import VueRouter from 'vue-router'
import store from '@/store'
import authToken from '@/utils/authToken'

Vue.use(VueRouter)

const routes = [
  {path: '/login',name: 'login',component: () => import('../views/LoginView.vue')},
  {path: '/register',name: 'register',component: () => import('../views/RegisterView.vue')},
  {
    path: '/main',
    name: 'main',
    component: () => import('../views/MainView.vue'),
    children: [
      {path: 'dashboard',name: 'dashboard',component: () => import('../views/DashboardView.vue')},
      {path: 'user-management',name: 'userManagement',component: () => import('../views/UserManagementView.vue')},
      {path: 'consultant-management',name: 'consultantManagement',component: () => import('../views/ConsultantManagementView.vue')},
      {path: 'consultation-list',name: 'consultationList',component: () => import('../views/ConsultationListView.vue')},
      {path: 'question-bank',name: 'questionBank',component: () => import('../views/QuestionBankView.vue')},
      {path: 'menu-management',name: 'menuManagement',component: () => import('../views/MenuManagementView.vue')},
      {path: 'permission-config',name: 'permissionConfig',component: () => import('../views/PermissionConfigView.vue')},
      {path: 'log-query',name: 'logQuery',component: () => import('../views/LogQueryView.vue')},
      {path: 'appointment-setting',name: 'appointmentSetting',component: () => import('../views/AppointmentSettingView.vue')},
      {path: 'profile',name: 'profile',component: () => import('../views/ProfileView.vue')},
      {path: 'consultation-diagnosis',name: 'consultationDiagnosis',component: () => import('../views/ConsultationDiagnosisView.vue')},
      {path: 'ai-management',name: 'aiManagement',component: () => import('../views/AImanagementView.vue')}
    ]
  },
  {path: '/', redirect: '/main'}
]

const router = new VueRouter({
  routes
})

//路由守卫
router.beforeEach((to,from,next)=>{
  let whileName = ["/login","/register"]
  //是白名单的放行
  if(whileName.indexOf(to.path)!=-1){
    next()
  }else{
    if(authToken.getToken()){
      if(store.state.adminId==-1 || store.state.roleId==-1){
        store.dispatch("getadmininfo").then(res=>{
          if(res) {
            next()
          } else {
            next('/login')
          }
        }).catch(()=>{
          next('/login')
        })
      }else{
        next()
      }
    }else{
      next("/login")
    }
  }
})

export default router
