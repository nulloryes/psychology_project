import Vuex from 'vuex'
import Vue from 'vue'
import http from '@/utils/request'

Vue.use(Vuex)

const actions = {
  async getadmininfo(context){
    return await http.get("/admin/getAdminInfo")
    .then(res=>{
      if(res.data.code == 1){
        context.commit("GET_ADMININFO",res.data.data)
        return true;
      }else{
        return false;
      }
    })
  }
}

const mutations = {
  GET_ADMININFO(state,value){
    state.adminId = value.adminId
    state.roleId = value.roleId
    state.adminName = value.name
  },
  RESETADMIN(state){
    state.adminId = -1
    state.roleId = -1
    state.adminName = null
  }
}

const state = {
  adminId: -1,
  roleId: -1,
  adminName: null
}

const store = new Vuex.Store({
  actions,
  mutations,
  state
})

export default store;
