<template>

    <PlayGround v-if="$store.state.pk.status === 'playing'" />
    <MatchGround v-if ="$store.state.pk.status === 'matching'" />


</template>

<script>
import PlayGround from "@/components/PlayGround.vue"
import { onMounted,onUnmounted } from "vue";
import { useStore } from "vuex";
import MatchGround from "../../components/MatchGround.vue";


export default{
    components:{
        PlayGround,
        MatchGround,
    },
    setup(){
        const store =useStore();
        const socketUrl = `ws://127.0.0.1:3000/websocket/${store.state.user.token}/`;

        let socket = null;
        onMounted(() =>{
            store.commit("updateOpponent",{
                username: "钩子哥",
                photo:`https://th.bing.com/th/id/OIP.Euix6FlJRCEFAuPuCXmUXAHaE7?rs=1&pid=ImgDetMain`,

            })
            socket = new WebSocket(socketUrl);
            socket.onopen = ()=>{//相当于后端的onOpen
                console.log("connected!");
                store.commit("updateSocket",socket);
            }
            
            socket.onmessage= msg=>{ //传数据为msg 这个函数是用来接受后端返回的信息
                const data =JSON.parse(msg.data);
                console.log(data)
                if(data.event ==='start-matching'){//匹配成功
                    store.commit("updateOpponent",{
                        username:data.opponent_username,
                        photo:data.opponent_photo,
                    })
                    setTimeout(()=>{
                        store.commit('updateStatus','playing');
                    },2000);
                    store.commit("updateGamemap",data.gamemap)
                    
                    

                }
                console.log(data);
            }
            socket.onclose = ()=>{
                
                // store.commit("updateStatus",'playing');
                console.log("disconnected!");
            }

            onUnmounted(() => {
                socket.close();
                store.commit('updateStatus','matching');
            })

        })

    }


}
</script>



<style scoped>
</style>