//将后端的的返回响应，通过js进行处理
import $ from 'jquery'

export default({
    //一个用户有以下的信息 state 中的信息主要来自于 mutations，而 mutations 被 actions 中的 context.commit 调用来修改 state。
    state: {
        id: "",
        username:"",
        photo:"",
        token:"",
        is_login:false,
        pulling_info: true,//是否正在拉去信息
    },

    // 如果有需要，可以在这里定义一些获取 state 中数据的计算属性
    getters: {
    },


    //mutations 一般用来修改数据的，先将user的信息复制到state里面
    mutations: {
        updateUser(state,user){
            state.id =user.id;
            state.username=user.username;
            state.photo=user.photo;
            state.is_login=user.is_login;
        },
        updateToken(state,token){
            state.token=token;
        },
        updatePullingInfo(state,pulling_info){
            state.pulling_info = pulling_info;
        }
    },
    

    //一般来说，修改state的函数会写在actions里面，这里开始写login的辅助函数用来实现登陆操作
    actions: {
        //context指的是上下文的数据，包含了 state、getters、commit、dispatch 等
        //通过 context.commit 可以触发 mutations，用于修改 Vuex 中的 state。 通过 context.dispatch 可以调用其他的 actions
        login (context,data){
            $.ajax({
                //通过ajax向后端发送post请求，然后接收到后端的resp，根据resp来对前端页面进行操作
                url:"http://localhost:3000/user/account/token/",
                type:'post',


                //data是用户输入的用户名跟密码以及error函数


                data:{
                  username:data.username,
                  password:data.password,
                },
                //data.error：这是一个函数，用于处理登录过程中可能发生的错误。
                //在登录成功时，success 回调函数会触发 context.commit("updateToken", resp.token)，更新 Vuex 中的 token 数据。
                //在登录失败时，error 回调函数会被调用，处理错误情况，比如显示错误信息给用户。
                success(resp)
                //resp是后端返回的数据，在登陆impl里面 返回了一个error_message 和token
                {
                    if(resp.error_message === "success"){
                    localStorage.setItem("jwt_token",resp.token)
                    context.commit("updateToken",resp.token);
                    
                    data.success(resp);
                }
                    else{
                        data.error(resp);
                    }
                },
                error(resp){
                  data.error(resp);
                }
              });       
        },
        getinfo(context,data){
            
            $.ajax({
                url:"http://localhost:3000/user/account/info/",
                type:"get",
                beforeSend: function(xhr) {
                xhr.setRequestHeader('Authorization', 'Bearer ' + context.state.token)
                 },
                success(resp){
                    if(resp.error_message === "success"){
                        context.commit("updateUser",{
                        ...resp,
                        is_login:true,
                    });
                    data.success(resp);
                }else{
                    data.error(resp);
                }
                
                },
                
                error(resp){
                    data.error(resp);
                }

            })

        },
        logout(context){
            localStorage.removeItem("jwt_token");
            context.commit("logout");
        }
    },



    modules: {
    }
  })
  