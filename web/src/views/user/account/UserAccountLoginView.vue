<template>
    <ContentField v-if="!$store.state.user.pulling_info">
       <div class="row justify-content-md-center">
        <div class="col_3">
            <form @submit.prevent="login"> 
                <div class="mb-3">
  <label for="username" class="form-label">用户名</label>
  <input v-model = "username" type="text" class="form-control" id="username" placeholder="请输入用户名">
</div>
<div class="mb-3">
  <label for="password" class="form-label">密码</label>
  <input v-model = "password" type="password" class="form-control" id="password" placeholder="请输入密码">
</div>
<div class ="error-message">{{ error_message }}</div>
<button type="submit" class="btn btn-primary">提交</button>

            </form>


        </div>
       </div>
    </ContentField>
    </template>
    
    <script>
    import ContentField from "@/components/ContentField.vue"
    import { useStore } from "vuex";//useStore 的作用是在组件中获取 Vuex Store 的实例，使得组件可以访问和修改 Vuex 中的状态（state）、触发 mutations、调用 actions 等
    import { ref } from "vue"; //ref 是后端返回的数据
    import router from "@/router";

    export default{
        components:{
            ContentField
        },
        //这里进行登陆页面的代码编写
        setup(){
            const store = useStore();
            let username = ref('');
            let password = ref('');
            let error_message = ref('');//利用ref取得所输入的值
            

            const jwt_token =localStorage.getItem("jwt_token");
            if(jwt_token){
                store.commit("updateToken",jwt_token);
                store.dispatch("getinfo",{
                    success(){
                        router.push({name:"home"});
                        store.commit("updatePullingInfo",false);

                    },
                    error(){
                        store.commit("updatePullingInfo",false);

                    }
                })
            }
            else{
                store.commit("updatePullingInfo",false);
            }

            const login = () => {
                error_message.value = "";
                store.dispatch("login",{  //注意 dispatch是链接.js与登陆vue页面，这里的login 是user.js编写的login，在这里面需要传2个值用户名跟密码，用。value将用户输入到ref,然后网页的值传过去
                    username: username.value,
                    password: password.value,
                    success(){
                        //如果登陆成功会跳到pk页面，用router实现   在router中我们自己将pk页面定义为home
                        store.dispatch("getinfo",{
                            success(){
                                router.push({ name: "home"});
                                console.log(store.state.user);
                            }
                        })
                        


                    },
                    error(){
                        //登陆失败弹出提示
                       error_message.value="用户名或者密码错误";

                    }

                })

            }
            return{
                username,
                password,
                error_message,
                login,
                
            }
        }
    }
    </script>
   
    
    
    <style scoped>

    button{
        width :100%;
    }
    div.error-message{
        color:red;

    }
    </style>