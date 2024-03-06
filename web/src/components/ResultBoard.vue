<template>
    <div class="result-board" :style="{ backgroundImage: `url(${matchgroundImage})` }">
    
        <div class="result-board-text" v-if="$store.state.pk.loser === 'all'" >
            平局
        </div>
        <div class="result-board-text" v-else-if="$store.state.pk.loser === 'A' &&  $store.state.pk.a_id == $store.state.user.id " >
            Lose
        </div>
        <div class="result-board-text" v-else-if="$store.state.pk.loser === 'B' &&  $store.state.pk.b_id == $store.state.user.id " >
            Lose
        </div>
        <div class="result-board-text" v-else >
            Win
        </div>
        <div class="result-board-btn">
            <button @click ='restart'  type="button" class="btn btn-warning btn-lg">
                再来!</button>

        </div>
    </div>


</template>

<script>
import store from '@/store';
import { useStore } from 'vuex';

export default {
    name: 'MyComponent',
    data() {
      return {
        matchgroundImage: 'https://th.bing.com/th/id/OIP.73LZpfTZ2LBzM3GcLkF_UwHaEs?rs=1&pid=ImgDetMain',
      };
    },
    setup(){
        const store =useStore();
        const restart =()=>{
            store.commit('updateStatus','matching');
            store.commit('updateLoser','none');
            store.commit("updateOpponent",{
                username: "钩子哥",
                photo:`https://th.bing.com/th/id/OIP.Euix6FlJRCEFAuPuCXmUXAHaE7?rs=1&pid=ImgDetMain`,

            })

        }
        return {
            restart
        };

    }

    
}



</script>





<style scoped>

div.result-board{
    /* background-color: red; */
    /* background-size: cover; */
    background-size: contain;
    height: 30vh;
    width: 24.5vw;
    background-color: rgba(255, 255, 255, 0.5);
    position: absolute;
    top:20%;
    left:37%;
}
div.result-board-text{
    
    text-align: center;
    color:white;
    font-size: 50px;
    font-weight: 600;
    font-style: italic;
    padding-top:5vh;

}
div.result-board-btn{
    padding-top: 7vh;
    text-align: center;

}
</style>