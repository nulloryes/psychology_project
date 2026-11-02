import Vuex from 'vuex'
import Vue from 'vue'
import http from '@/utils/request'

Vue.use(Vuex)

const USER_KEY = 'psychology_user'

// 从 localStorage 恢复用户信息，避免刷新后 userId=-1
const saved = JSON.parse(localStorage.getItem(USER_KEY) || 'null')
const initialState = saved || { userId: -1, userName: null }

const actions = {
  setuser(context, value) {
    context.commit("SETUSER", value)
  },
  async getuserinfo(context) {
    return await http.get("/user/getUserInfo")
      .then(res => {
        if (res.data.code == 1) {
          context.commit("GETUSERINFO", res.data.data)
          return true
        }
        return false
      })
  }
}

const mutations = {
  SETUSER(state, value) {
    state.userId = value.userId
    state.userName = value.name
    localStorage.setItem(USER_KEY, JSON.stringify({ userId: value.userId, userName: value.name }))
  },
  GETUSERINFO(state, value) {
    state.userId = value.userId
    state.userName = value.name
    localStorage.setItem(USER_KEY, JSON.stringify({ userId: value.userId, userName: value.name }))
  },
  RESETUSER(state) {
    state.userId = -1
    state.userName = null
    localStorage.removeItem(USER_KEY)
  }
}

const state = {
  userId: initialState.userId,
  userName: initialState.userName,
}

const store = new Vuex.Store({
  actions,
  mutations,
  state
})

export default store
