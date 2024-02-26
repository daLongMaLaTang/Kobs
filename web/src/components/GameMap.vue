<template>
    <div ref ="parent" class="gamemap">
        <canvas ref ="canvas" tabindex='0'> </canvas>
    </div>

</template>

<script>
import{ GameMap }from "@/assets/scripts/GameMap";
import{ ref,onMounted }from 'vue' //onMounted 表示初始完（挂载完）后要进行的动作
export default{
    setup() {
        let parent = ref(null);
        let canvas =ref(null);

        onMounted(() => {
            new GameMap(canvas.value.getContext('2d'),parent.value)
        });

        return {
            parent,
            canvas
        }

    }
}

</script>

<style scoped>
div.gamemap{
    width: 100%;
    height: 100%;
    display: flex;
    justify-content: center ;
    align-items: center;
}

</style>