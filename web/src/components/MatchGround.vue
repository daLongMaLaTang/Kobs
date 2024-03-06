<template>
    <div class="matchground" :style="{ backgroundImage: `url(${matchgroundImage})` }">
      <div class="row">
        <div class="col-6">
          <div class="user-photo">
            <img :src="$store.state.user.photo" alt="">
          </div>
          <div class="user-username">
            {{ $store.state.user.username }}
          </div>
        </div>
        <div class="col-6">
            <div class="user-photo">
            <img :src="$store.state.pk.opponent_photo" alt="">
          </div>
          <div class="user-username">
            {{ $store.state.pk.opponent_username }}
          </div>
        </div>

        <div class="col-12" style = 'text-align:center; padding-top: 10vh;'>
            <button @click="click_match_btn" type="button" class="btn btn-info" btn-lg>{{ match_btn_info}}</button>
        </div>
      </div>
    </div>
  </template>
  
  <script>
import { ref } from 'vue'
import { useStore } from 'vuex';
  export default {
    name: 'MyComponent',
    data() {
      return {
        matchgroundImage: 'https://bpic.588ku.com/back_our/20210311/bg/a96f25de6c2c5.png',
      };
    },
    setup(){
        let match_btn_info =ref("开始匹配");
        const store = useStore();

        const click_match_btn =()=>{
            if(match_btn_info.value ==='开始匹配'){
                match_btn_info.value = "取消";
                store.state.pk.socket.send(JSON.stringify({
                    event:"start-matching",
                    // bot_id:select_bot.value,
                }));

            }
            else{
                match_btn_info.value = '开始匹配';
                store.state.pk.socket.send(JSON.stringify({
                    event:"stop-matching",
                }));
                    
            }

        }
        return{
            match_btn_info,
            click_match_btn,
        }

    }
  }
  </script>
  
  <style scoped>
  /* scoped 属性表示这个样式只在当前组件中生效 */
  .matchground {
    width: 60vw;
    height: 70vh;
    margin: 40px auto;
    background-size: cover;
    background-position: center;
  }
  div.user-photo {
    text-align: center;
    padding-top: 10vh;
  }
  div.user-photo > img {
    border-radius: 50%;
    width: 25vh;
    height: 25vh;
  }
  div.user-username {
    text-align: center;
    font-size: 24px;
    font-weight: 600;
    color: red;
    padding-top: 2vh;
  }
  
  </style>