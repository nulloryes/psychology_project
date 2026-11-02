import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
// 引入 ElementUI 组件库
import ElementUI from 'element-ui'
// 引入 ElementUI 样式
import 'element-ui/lib/theme-chalk/index.css'
import http from './utils/request'
import dayjs from 'dayjs';

// 全局注册 ElementUI
Vue.use(ElementUI)
Vue.prototype.$axios = http;
Vue.config.productionTip = false
Vue.filter('formatTime', (value) => {
  if (!value) return '';
  return dayjs(value).format('YYYY-MM-DD HH:mm:ss');
});

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
