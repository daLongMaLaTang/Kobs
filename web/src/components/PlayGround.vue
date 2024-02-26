<template>
<div class="playground">
    <GameMap />
</div>
</template>

<script>
import GameMap from '@/components/GameMap.vue'

export default{
    components: {
    GameMap,
    }
}
</script>

<style scoped>  
div.playground { /* 百分之60宽 70%高 */
    width: 60vw;
    height:70vh;
    
    margin:40px auto;
    

}

</style> 