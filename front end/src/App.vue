<template>
  <div id="app">
    <!-- <el-container style="height: auto; width: 100%; border: 10px solid black"> -->
      <!-- <router-link to="/address"></router-link>          -->
        <!-- <router-link>会被渲染为一个<a>标签, to是一个prop, 指定需要跳转的路径
        tag：改变渲染结果; replace: 不会留下Hsitory;
        active-class: 高亮显示当前页面对应的菜单      -->

    <router-view name="nav" v-if="isRouterAlive" class="header1"></router-view>   
    <!-- router.js对应的path项的components中若有name就渲染, 没有name则不渲染 -->
    <router-view v-if="isRouterAlive"/>     <!-- 默认挂载router.js中default对应的路径 -->
    <!-- <router-view name="footer"></router-view> -->
    

    <!-- 路由视图, 挂载所有路由组件; 根据当前路由动态渲染不同的页面组件; 路由切换时, 切换的是<router-view>挂载的组件 -->
    <!-- </el-container> -->
  </div>
</template>

<script>

import storage from './utils/storage'
import http from './utils/http'

export default {

  name: 'app',
  
  components: {

  },

  provide () {
  
    return {

      reload: this.reload
    
    }
  
  },

  data() {

    return {

      isRouterAlive: true,

    }

  },

  methods: {

    reload () {
      
      this.isRouterAlive = false;
      
      this.$nextTick(function () {
        
        this.isRouterAlive = true;
      
      })
    
    }
  },

  // created () {
    
  //   var _this = this;

  //   console.log("halo---------------");
    
  //   if (storage.get("typeList") == null) {

  //     http({

  //     // 假设后台需要的是表单数据这里你就可以更改
  //     headers: {

  //     "Content-Type": "application/json;charset=UTF-8"
      
  //     },

  //     method: 'post',
  //     url: 'http://localhost:8080/listTypes',

  //     data: {

  //       message: "typeList"

  //     },

  //     responseType: 'json'

  //     }).then(function (res) {

  //         console.log(res);

  //         console.log("halo---------------1");

  //         var code = res.code;
  //         var info = res.info;

  //         if (res.code == 200) {

  //           storage.set("typeList", JSON.stringify(info));

  //           _this.$store.commit('setTypeList', storage.get('typeList'));

  //           console.log(JSON.parse(storage.get("typeList")));
          
  //         } else {
              
  //             console.log("halo---------------2");
  //             _this.$message.error(info);

  //         }

  //     }).catch(function (err) {

  //         console.log("halo---------------3");
  //         _this.$message.error("系统错误！");
    
  //   });

  //   }

  //   // console.log(JSON.parse(storage.get("typeList")));
  //   // console.log(JSON.parse(this.$store.getters.getTypeList)[0].typeName);

    
  // }

}
</script>

<!-- App.vue中加scoped后，会出现白边 -->
<style lang="less">
*{
  padding: 0;
  margin: 0;

}

.header1{
  position: relative;
  z-index: 1;
}
// body {
//   background-color: #1e1f23;
//   padding-top: 8rem;
// }

// body.is-loading:after {
//   visibility: hidden;
//   opacity: 0;
// }

// body:after {
//   -moz-pointer-events: none;
//   -webkit-pointer-events: none;
//   -ms-pointer-events: none;
//   pointer-events: none;
//   -moz-transition: opacity 1.5s ease-in-out, visibility 1.5s;
//   -webkit-transition: opacity 1.5s ease-in-out, visibility 1.5s;
//   -ms-transition: opacity 1.5s ease-in-out, visibility 1.5s;
//   transition: opacity 1.5s ease-in-out, visibility 1.5s;
//   content: '';
//   background-image: url(./assets/bg.jpg);
//   background-attachment: fixed;
//   background-position: center;
//   background-repeat: no-repeat;
//   background-size: cover;
//   position: fixed;
//   display: block;
//   top: 0;
//   left: 0;
//   height: 100%;
//   width: 100%;
//   z-index: -1;
//   visibility: visible;
//   opacity: 1;
// }

// @media screen and (max-width: 980px) {

//   body:after {
//     background-attachment: scroll;
//     background-position: top;
//     background-size: 100%;
//   }

// }

</style>
