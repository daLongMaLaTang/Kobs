import { createStore } from 'vuex'
import ModuleUser from "./user" //将写好的user.js传到全局变量 index.js里面，这里起名为ModuleUser

export default createStore({
  state: {
  },
  getters: {
  },
  mutations: {
  },
  actions: {
  },
  //这个是用来穿模块的
  modules: {
    user:ModuleUser
  }
})
