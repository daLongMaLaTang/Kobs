<template>
   <ContentField>
    我的Bot
   </ContentField>
    </template>
    
    <script>
    import ContentField from "@/components/ContentField.vue"
    import $ from 'jquery'
    import { useStore } from "vuex";

    export default{
        components:{
            ContentField
        },
        setup(){
            const store =useStore();
            // $.ajax({
            //     url:"http://localhost:3000/user/bot/add/",
            //     type: 'post',
            //     data:{
            //         title : "Bot的标题",
            //         description:"Bot的描述",
            //         content:"Bot的代码",
            //     },
            //     beforeSend: function(xhr) {
            //     xhr.setRequestHeader('Authorization', 'Bearer ' + store.state.user.token)
            //      },
            //     success(resp){
            //         console.log(resp);
            //      },
            //     error(resp){
            //         console.log(resp);
            //      }


            // })
            

            // $.ajax({
            //     url:"http://localhost:3000/user/bot/remove/",
            //     type: 'post',
            //     data:{
            //         bot_id: 9,
            //     },
            //     beforeSend: function(xhr) {
            //     xhr.setRequestHeader('Authorization', 'Bearer ' + store.state.user.token)
            //      },
            //     success(resp){
            //         console.log(resp);
            //      },
            //     error(resp){
            //         console.log(resp);
            //      }


            // }) 


            // $.ajax({
            //     url:"http://localhost:3000/user/bot/update/",
            //     type: 'post',
            //     data:{
            //         bot_id: 9,
            //         title : "Bot的标题1",
            //         description:"Bot的描述1",
            //         content:"Bot的代码1"
            //     },
            //     beforeSend: function(xhr) {
            //     xhr.setRequestHeader('Authorization', 'Bearer ' + store.state.user.token)
            //      },
            //     success(resp){
            //         console.log(resp);
            //      },
            //     error(resp){
            //         console.log(resp);
            //      }


            // }) 
            $.ajax({
                url:"http://localhost:3000/user/bot/getlist/",
                type: 'get',
                
                beforeSend: function(xhr) {
                xhr.setRequestHeader('Authorization', 'Bearer ' + store.state.user.token)
                 },
                success(resp){
                    console.log(resp);
                 },
                error(resp){
                    console.log(resp);
                 }


            }) 
        }
    }
    </script>
    <style scoped>
    </style>