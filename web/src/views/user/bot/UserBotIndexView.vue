<template>
    <div class="container">
        <div class="row">
            <div class="col-3">
                <div class="card" style="margin-top: 20px;">
                    <div class="card-body">
                        <img :src="$store.state.user.photo" alt="" style="width: 100%;">
                    </div>
                </div>
            </div>
            <div class="col-9">
                <div class="card" style="margin-top: 20px;">
                    <div class="card-header">
                        <span style="font-size: 130%;">我的Bot</span>
                        <!-- Button trigger modal -->                        
                        <button type="button" class="btn btn-dark float-end" data-bs-toggle="modal" data-bs-target="#add-bot-btn">
                            创建Bot
                        </button>

                        <!-- Modal -->
                        <div class="modal fade" id="add-bot-btn" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
                            <div class="modal-dialog modal-xl">
                                <div class="modal-content">
                                <div class="modal-header">
                                    <h1 class="modal-title fs-5">创建Bot</h1>
                                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                </div>
                                <div class="modal-body">
                                    <form>
                                        <div class="mb-3">
                                            <label for="add_bot-title" class="form-label">名称</label>
                                            <input v-model="botadd.title" type="text" class="form-control" id="add_bot-title" placeholder="请输入Bot名称">
                                        </div>
                                        <div class="mb-3">
                                            <label for="add-bot-description" class="form-label">简介</label>
                                            <textarea v-model="botadd.description" class="form-control" id="add-bot-description" rows="3" placeholder="请输入Bot简介"></textarea>
                                        </div>
                                        <div class="mb-3">
                                            <label for="add-bot-code" class="form-label">代码</label>

                                        </div>
                                    </form>
                                </div>
                                <div class="modal-footer">
                                    <div class="error-message">{{ botadd.error_message }}</div>
                                    <button type="button" class="btn btn-dark" @click="add_bot">创建</button>
                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
                                </div>
                                </div>
                            </div>
                        </div>

                    </div>
                    <div class="card-body">
                        <table class="table table-dark table-hover">
                           <thead>
                            <tr>
                                <th>名称</th>
                                <th>创建时间</th>
                                <th>操作</th>
                            </tr>
                           </thead> 
                           <tbody>
                                <tr v-for="bot in bots" :key="bot.id">
                                    <td>{{ bot.title }}</td>
                                    <td>{{ bot.createtime }}</td>
                                    <td>
                                        <button type="button" class="btn btn-outline-success" style="margin-right: 10px;" data-bs-toggle="modal" :data-bs-target="'#update-bot-modal'+bot.id">修改</button>
                                        <button type="button" class="btn btn-outline-danger" @click="remove_bot(bot)">删除</button>

                                        <!-- Modal -->
                                        <div class="modal fade" :id="'update-bot-modal'+bot.id" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
                                            <div class="modal-dialog modal-xl">
                                                <div class="modal-content">
                                                <div class="modal-header">
                                                    <h1 class="modal-title fs-5" >创建Bot</h1>
                                                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                                </div>
                                                <div class="modal-body">
                                                    <form>
                                                        <div class="mb-3">
                                                            <label for="add_bot-title" class="form-label">名称</label>
                                                            <input v-model="bot.title" type="text" class="form-control" id="add_bot-title" placeholder="请输入Bot名称">
                                                        </div>
                                                        <div class="mb-3">
                                                            <label for="add-bot-description" class="form-label">简介</label>
                                                            <textarea v-model="bot.description" class="form-control" id="add-bot-description" rows="3" placeholder="请输入Bot简介"></textarea>
                                                        </div>
                                                        <div class="mb-3">
                                                            <label for="add-bot-code" class="form-label">代码</label>

                                                        </div>
                                                    </form>
                                                </div>
                                                <div class="modal-footer">
                                                    <div class="error-message">{{ bot.error_message }}</div>
                                                    <button type="button" class="btn btn-dark" @click="update_bot(bot)">保存修改</button>
                                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
                                                </div>
                                                </div>
                                            </div>
                                        </div>
                                    </td>
                                </tr>
                           </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>

import { ref,reactive } from 'vue'
import $ from 'jquery'
import { useStore } from 'vuex';
import { Modal } from 'bootstrap/dist/js/bootstrap';


export default{

    setup(){

        const store =useStore();
        let bots = ref([]);

        const botadd = reactive({
            title:"",
            description:"",
            content:"",
            error_message:"",
        });

        const refresh_bots = () =>{
            $.ajax({
                url:"http://127.0.0.1:3000/user/bot/getlist/",
                type:"get",
                beforeSend:function(xhr){
                    xhr.setRequestHeader('Authorization',"Bearer "+store.state.user.token);
                },
                success(resp){
                    bots.value=resp;
                }
            });
        }

        refresh_bots();

        const add_bot = () =>{
            botadd.error_message = "";
            $.ajax({
                url:"http://127.0.0.1:3000/user/bot/add/",
                type:"post",
                data:{
                    title:botadd.title,
                    description:botadd.description,
                    content:botadd.content,
                },
                beforeSend:function(xhr){
                    xhr.setRequestHeader('Authorization',"Bearer "+store.state.user.token);
                },
                success(resp){
                    if(resp.error_message === "success"){
                        botadd.title="";
                        botadd.description="";
                        botadd.content="";
                        Modal.getInstance("#add-bot-btn").hide();
                        refresh_bots();
                    } else {
                        botadd.error_message =resp.error_message;
                    }
                }
            });
        }

        const update_bot = (bot) =>{
            botadd.error_message = "";
            $.ajax({
                url:"http://127.0.0.1:3000/user/bot/update/",
                type:"post",
                data:{
                    bot_id:bot.id,
                    title:bot.title,
                    description:bot.description,
                    content:bot.content,
                },
                beforeSend:function(xhr){
                    xhr.setRequestHeader('Authorization',"Bearer "+store.state.user.token);
                },
                success(resp){
                    if(resp.error_message === "success"){
                        Modal.getInstance('#update-bot-modal'+bot.id).hide();
                        refresh_bots();
                    } else {
                        bot.error_message =resp.error_message;
                    }
                }
            });
        }

        const remove_bot = (bot) => {
            $.ajax({
                url:"http://127.0.0.1:3000/user/bot/remove/",
                type:"post",
                data:{
                    bot_id:bot.id,                    
                },
                beforeSend:function(xhr){
                    xhr.setRequestHeader('Authorization',"Bearer "+store.state.user.token);
                },
                success(resp){
                    if(resp.error_message === "success"){
                        refresh_bots();
                    }
                }
            })
        }

        return{
            bots,
            botadd,
            add_bot,
            update_bot,
            remove_bot
        }
    }
}
</script>

<style scoped>
div.error-message{
    color: red;
}

</style>