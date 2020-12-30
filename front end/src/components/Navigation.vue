<template>
  <!-- <el-header style="height: auto; width: 100%; border: 10px solid red"> -->
    <header class="head-section">
      <el-button type="text" style="margin-left: 10px" @click="getUserInfo()">用户信息</el-button>
        <el-button type="text" @click="logout()">退出登录</el-button>
  </header>
  <!-- </el-header> -->
</template>

<script>
import storage from "../utils/storage.js"
import http from "../utils/http.js"
export default {
  
  inject: ['reload'],

  name: 'navigation',

  data() {

    return {

      userName: null,
      userAccount: null,
      lastLoginTime: null,
      loginTime: null,
      cnt: 0
      
    }

  },

  created() {

    var user = JSON.parse(this.$store.getters.getUser);
    this.userAccount = user.userAccount;
    this.loginTime = user.loginTime;

    
    // console.log("0: " + this.cnt++);

    this.listInfo();


    // console.log("2: " + this.cnt++);

    // // var userInfo = JSON.parse(storage.get('userInfo'));
    
    // console.log("3: " + this.cnt++);

    // // console.table(userInfo);

    
    // var userInfo = JSON.parse(storage.get('userInfo'));

  },


  methods: {

      listInfo: function() {

        var _this = this;

         http({

            method: 'post',

            url: '/user/listOneUser',

            headers: {

                'Content-Type': 'application/json;charset=utf-8'

            },

            data: {

                userAccount: _this.userAccount
            },

            responseType: 'json'

            }).then(function (res) {
                
                var info = res.info;
                var code = res.code;

                if (code == 200) {
                  
                  console.log("----------")
                  console.log("ssdsd")
                  console.log(info);
                  console.log("----------")

                  // 创建userInfo
                  var userInfo = new Object();
                  userInfo.userId = info.userId;
                  userInfo.userAccount = info.userAccount;

                  _this.userName = userInfo.userName = info.userName;
                  _this.lastLoginTime = userInfo.lastLoginTime = info.lastLoginTime;

                  // 提交userInfo
                  storage.set('userInfo', JSON.stringify(userInfo));
                  _this.$store.commit('setUserInfo', storage.get('userInfo'));

                } else {

                    _this.$message.error('global error！');

                }

            }).catch(function (error) {
                
                console.log(error);
                _this.$message.error('系统错误！');

            })

      },

      getUserInfo() {

            var date2 = new Date();
            
            // 时间差的毫秒数
            var date3 = date2.getTime() - new Date(this.loginTime).getTime();
            
            // 计算出相差天数
            var days = Math.floor(date3 / (24 * 3600 * 1000));

            // 计算出小时数
            // 计算天数后剩余的毫秒数
            var leave1 = date3 % (24 * 3600 * 1000);
            var hours = Math.floor(leave1 / (3600 * 1000));
            
            // 计算相差分钟数
            // 计算小时数后剩余的毫秒数
            var leave2 = leave1 % (3600 * 1000); 
            var minutes = Math.floor(leave2 / (60 * 1000));
            
            // 计算相差秒数
            // 计算分钟数后剩余的毫秒数
            var leave3 = leave2 % (60 * 1000); 
            var seconds = Math.round(leave3 / 1000);

            this.$alert(
                "用户账号：" + this.userAccount + "<br>" + 
                "用户名：" +
                    this.userName +
                    "</br>登录时间：" +
                    this.loginTime +
                    "</br>登录时长：" +
                    days +
                    "天 " +
                    hours +
                    "小时 " +
                    minutes +
                    " 分钟" +
                    seconds +
                    " 秒",
                "用户信息",
                {
                    dangerouslyUseHTMLString: true,
                    confirmButtonText: "确定",
                }
            );
        },

      exit() {

        var _this = this;

        _this.message = 'logout'

        http({

          method: 'post',

          url: '/user/logout',

          headers: {

            'Content-Type': 'application/json;charset=utf-8'

          },

          data: {

            message: _this.message,
            userAccount: _this.userAccount,
            lastLoginTime: _this.loginTime

          },

          responseType: 'json'

          }).then(function (response) {

            var info = response.info;
          var code = response.code;

          console.log(response);

          if (code == 200) {

            storage.clear();
            
            _this.$router.replace('/Login');
            
            _this.$message.success('退出成功!');
            
            _this.reload();  // 刷新界面


          } else if (code == 400) {
            
            _this.$message.error('遭遇异常！')
        
          } else {
          
            _this.$message.error('未知错误...')
          
          }
        
        }).catch(function (error) {

            console.log(error);
        
            _this.$message.error('系统错误！')
      
        });

    },

    logout() {

        var _this = this;

        this.$confirm("确认退出登录吗?", "提示", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning",
        }).then(() => {
        
            _this.exit();

        }).catch(() => {
            
            // this.$message({
            //         type: "info",
            //         message: "已取消退出",
            // });
        });

    }

  },

}

</script>

<style lang="less" scoped>

</style>
