<template>
<NavBar />
<router-view> </router-view>
 

</template>
<script> 
import NavBar from '@/components/NavBar.vue'
import "bootstrap/dist/css/bootstrap.min.css"
import "bootstrap/dist/js/bootstrap"
import $ from 'jquery'

export default{
  components:{
    NavBar
  },
  setup(){
    

    // $.ajax({
    //   url:"http://127.0.0.1:3000/user/account/info/",    //请求的地址
    //   type:"get",
    //   beforeSend: function(xhr) {
    //     xhr.setRequestHeader('Authorization', 'Bearer ' + "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIyNDVjNmUzMzQ5MDI0ODZjYTlhOWRlYjI1ZDU0NjVhOSIsInN1YiI6IjEiLCJpc3MiOiJzZyIsImlhdCI6MTcwNjY0MzcwOCwiZXhwIjoxNzA3ODUzMzA4fQ.tfxt8CBi1YEv-jK4yule_6NdC4RoT3CPzBnNI8akUxg");    
    //     //写入的headers参数,如果有多个参数该语句写多个.
    //   },
    //   success(resp){
    //     console.log(resp);
    //   },
    //   error(resp){
    //     console.log(resp);
    //   }
    // });

    // $.ajax({
    //   url:"http://localhost:3000/user/account/register/",
    //   type:'post',
    //   data:{
    //     username:'wb',
    //     password:'phfj',
    //     confirmedPassword:"phfj",
    //   },
    //   success(resp){
    //     console.log(resp);
    //   },
    //   error(resp){
    //     console.log(resp);
    //   }


    // })


  }
}

</script>


<style>
body{
  background-image:url("@/assets/images/backend.png");
  background-size:cover;
}
</style>
