import axios from 'axios';
import router from '@/router';
import authToken from './authToken';

const http = axios.create({
  baseURL: '/api',
  timeout: 80000,
});

// 添加请求拦截器
http.interceptors.request.use(function (config) {
  if(authToken.getToken()){
    config.headers["psychology_token"] = authToken.getToken()
  }
    return config;
  }, function (error) {
    return Promise.reject(error);
  });

// 添加响应拦截器
http.interceptors.response.use(function (response) {
    if(response.data.code==-10){
      alert(response.data.msg)
      //跳转到登录界面
      router.replace('/login'); 
      return Promise.reject(response.data);
    }
    return response;
  }, function (error) {
    return Promise.reject(error);
  });

export default http;