import { createRouter, createWebHistory } from 'vue-router'
import PkIndexView from "@/views/pk/PkIndexView"
import RanklistIndexView from "@/views/ranklist/RanklistIndexView"
import RecordIndexView from "@/views/record/RecordIndexView"
import UserBotIndexView from "@/views/user/bot/UserBotIndexView"
import NotFound from "@/views/error/NotFound"
import UserAccountRegisterView from "@/views/user/account/UserAccountRegisterView"
import UserAccountLoginView from "@/views/user/account/UserAccountLoginView"
import store from'../store/index'




const routes = [
  {
    path: "/",
    name: "home",
    redirect:"/pk/",
    meta:{
      requestAuth: true,
    }//进入这个页面是否要授权？

  },
  {
    path:"/pk/",
    name: "pk_index",
    component: PkIndexView,
    meta:{
      requestAuth: true,
    }
  },
  {
    path:"/record/",
    name: "record_index",
    component: RecordIndexView,
    meta:{
      requestAuth: true,
    }
  },
  {
    path:"/ranklist/",
    name: "ranklist_index",
    component: RanklistIndexView,
    meta:{
      requestAuth: true,
    }
  },
  {
    path:"/user/bot/",
    name: "user_bot_index",
    component: UserBotIndexView,
  },
  {
    path:"/user/account/login/",
    name: "user_account_login",
    component: UserAccountLoginView,
    
  },
  {
    path:"/user/account/register",
    name: "user_account_register",
    component: UserAccountRegisterView,
    
  },
  {
    path:"/404/",
    name: "404",
    component: NotFound,
   
  },
  {
    path: "/:catachAll(.*)",
    redirect :"/404/"


  }

  
 
]

const router = createRouter({
  history: createWebHistory(),
  routes
})
router.beforeEach((to,from,next) =>{//跳转页面做的东西
  if(to.meta.requestAuth && !store.state.user.is_login){
    next({name:"user_account_login"});
  }
  else{
    next(); 
  }

})

export default router
