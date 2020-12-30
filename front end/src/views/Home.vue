<template>
  <div class="all">

    <img
      class="controlLing"
      src="../assets/control.png"
      alt="凌志西控制柜"
      @click="controlLing = true"
    />
    <el-dialog :visible.sync="controlLing" width="1350px" top="5vh">
      <img src="../assets/lingMachine.jpg" alt="凌志西道口设备布置图" />
    </el-dialog>

    <img
      class="controlVillage"
      src="../assets/control.png"
      alt="石头村控制柜"
      @click="controlVillage = true"
    />
    <el-dialog :visible.sync="controlVillage" width="1350px" top="5vh">
      <img src="../assets/villageMachine.jpg" alt="石头村道口设备布置图" />
    </el-dialog>

    <img
      class="controlOil"
      src="../assets/control.png"
      alt="石头油库控制柜"
      @click="controlOil = true"
    />
    <el-dialog :visible.sync="controlOil" width="1350px" top="5vh">
      <img src="../assets/oilMachine.jpg" alt="石头油库道口设备布置图" />
    </el-dialog>
    <!-- 修改：从后端获取的道口信息数组中的道口信息 -->
    <div v-for="(crossing, index) in crossings" :key="index">
      <div :class="crossStyle[index]">
        <!-- <el-tag effect="plain"> {{ crossing.crossingName }} </el-tag>
        <br /> -->
        <el-button
          plain
          size="small"
          type="primary"
          :loading="operating[index]"
          @click="handler(mode[index])"
          >{{ crossing.crossingName }}
          <br> ({{crossing.crossingMode}})</el-button
        >
      </div>
    </div>
    <!-- 凌志西的栏目机 -->
    <div
      class="lingUp"
      :style="{ backgroundImage: 'url(' + lingUpUrl + ')' }"
    ></div>
    <div
      class="lingDown"
      :style="{ backgroundImage: 'url(' + lingDownUrl + ')' }"
    ></div>
    <!-- 石头油库的栏目机 -->
    <div
      class="oilUp"
      :style="{ backgroundImage: 'url(' + oilUpUrl + ')' }"
    ></div>
    <div
      class="oilDown"
      :style="{ backgroundImage: 'url(' + oilDownUrl + ')' }"
    ></div>
    <!-- 石头村的栏目机 -->
    <div
      class="villageLeft"
      :style="{ backgroundImage: 'url(' + villageLeftUrl + ')' }"
    ></div>
    <div
      class="villageRight"
      :style="{ backgroundImage: 'url(' + villageRightUrl + ')' }"
    ></div>
    <div
      v-for="(railwayblinker, index1) in railwayblinkers"
      :key="'info2-' + index1"
    >
      <div
        :class="railwayblinkerStyle[index1]"
        :style="{ backgroundColor: railwayblinker.railwayBlinkerStatus }"
      ></div>
    </div>
    <!-- 公路信号灯 -->
    <div v-for="(roadblinker, index2) in roadBlinkers" :key="'info3-' + index2">
      <div
        :class="roadBlinkerStyle[index2]"
        :style="{ backgroundColor: roadblinker.roadBlinkerStatus }"
      ></div>
    </div>
    <!-- 修改：从后端获取的火车信息 -->
    <el-button type="warning" @click="run()">发车</el-button>
    <!-- <el-button type="primary">{{ aim }}</el-button>-->

    <el-button type="danger" @click="reContnect()">尝试重连</el-button> 

    <div class="train" :style="iconstyle" @click="getTrainInfo()"  v-show="isRun">
      <div>
        <div :style="{ color: trainConnect }">WIFI</div>
        <img src="../assets/train.png" alt="火车" />
        <div class="trainNumber">
          {{ trainNumber }}
        </div>
      </div>
    </div>
    <!-- 修改：从后端获取的各个道口的GPS信号信息 -->
    <div class="linkStates">
      <div
        v-for="(crossing, index) in crossings"
        :key="index"
        class="linkState"
      >
        <img
          :style="{ backgroundColor: crossing.crossingGPS }"
          src="../assets/wifi.png"
          alt="wifi"
          @click="WIFIChange(index)"
        />
        <div :style="{ color: crossing.crossingGPS }">
          {{ crossing.crossingName }}
        </div>
      </div>
      <div class="linkState">
        <img
          style=" backgroundColor: green"
          src="../assets/wifi.png"
          alt="wifi"
        />
        <div style=" color: green ">
          数据库
        </div>
      </div>
    </div>

    <div class="alarmBox">
      <span class="alarmBoxTitle">报警消息</span>
      <el-select
        v-model="selectedOption"
        size="mini"
        @change="changeAlarm($event)"
      >
        <el-option
          v-for="item in options"
          :key="item.key"
          :label="item.label"
          :value="item.key"
        >
        </el-option>
      </el-select>

      <!-- <div v-show="showRFIDAlarm">
        <el-divider>RFID</el-divider>
        <ul>
          <li v-for="item in rfidAlarms" :key="item">
            {{ item }}
          </li>
        </ul>
      </div>

      <div v-show="showLingAlarm">
        <el-divider>凌志西</el-divider>
        <ul>
          <li v-for="(iteml, index3) in lingAlarm" :key="'info3-' + index3">
            {{ iteml }}
          </li>
        </ul>
      </div>

      <div v-show="showVillageAlarm">
        <el-divider>石头村</el-divider>
        <ul>
          <li v-for="itemv in villageAlarm" :key="itemv">
            {{ itemv }}
          </li>
        </ul>
      </div> -->

      <div v-show="showOilAlarm">
        <el-divider>石头油库</el-divider>
        <ul>
          <li v-for="itemo in oilAlarm" :key="itemo">
            {{ itemo }}
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";

import storage from '@/utils/storage.js';
import http from '@/utils/http.js';


export default {

  name: "Home",
  
  
  inject: ['reload'],

  data() {

    return {

      aim: "行进中",

      isRun: false,

      gps: 30,

      // 火车信息
      locomotive: {
        locomotiveId: null,
        locomotiveName: null,
        locomotiveGPS: null,
        responseStatus: null,
        nextCrossingId: null,
        trackId: null,
      },
      
      loginTime: null,
      // 道口信息
      crossings: [],

      // 道口div类模式
      crossStyle: ["lingMode", "villageMode", "oilMode"],

      // 不同道口点击方法
      mode: ["clickLingMode", "clickVillageMode", "clickOilMode"],

      // 铁路信号灯
      railwayblinkers: [],

      //铁路信号灯类
      railwayblinkerStyle: [],

      // 公路信号灯
      roadBlinkers: [],

      // 公路信号灯类
      roadBlinkerStyle: [],

      // 栏目机
      balusters: [],

      // 警告信息
      warns: [],

      controlVillage: false,
      controlLing: false,
      controlOil: false,

      // 控制器状态
      operating: [false, false, false],

      // 凌志西栏目机状态
      lingUpUrl: null,
      lingDownUrl: null,
      lingUpState: null,
      lingDownState: null,

      // 石头油库栏目机状态
      oilUpUrl: null,
      oilDownUrl: null,
      oilUpState: null,
      oilDownState: null,

      // 石头村栏目机状态
      villageLeftUrl: null,
      villageRightUrl: null,
      villageUpState: null,
      villageDownState: null,

      trainNumber: null,
      trainConnect: null,

      options: [
        {
          key: "option1",
          label: "全部"
        },
        {
          key: "option2",
          label: "RFID"
        },
        {
          key: "option3",
          label: "凌志西"
        },
        {
          key: "option4",
          label: "石头村"
        },
        {
          key: "option5",
          label: "石头油库"
        }
      ],
      selectedOption: "option5",

      showRFIDAlarm: true,
      showLingAlarm: true,
      showVillageAlarm: true,
      showOilAlarm: false,

      rfidAlarms: [
        "RFID电子标签异常E3E-4异常 2020-12-24 20:12:24",
        "RFID电子标签异常E3C-2异常 2020-12-24 20:12:24",
        "RFID电子标签异常E3D-0异常 2020-12-24 20:12:24",
        "RFID电子标签异常E3A-0异常 2020-12-24 20:12:24",
        "RFID电子标签异常E3A-3异常 2020-12-24 20:12:24"
      ],

      // 各个道口警告信息
      lingAlarm: [],
      villageAlarm: [],
      oilAlarm: [],

      icontranslateX: 0,
      icontranslateY: 0,
      percent: 0
      
    };
  },

  created() {

    var data = new Date();
    this.loginTime =
      data.getFullYear() +
      "/" +
      (data.getMonth() + 1) +
      "/" +
      data.getDate() +
      " " +
      data.getHours() +
      ":" +
      data.getMinutes() +
      ":" +
      data.getSeconds();
    // this.userName = this.$route.query.userName;

    // 创建axios
    this.instance = axios.create({
      baseURL: "http://localhost:8080",
      timeout: 1000
    });





    // 获取道路口信息
    this.getCrossings();

    // 获取火车信息
    // this.getLocomotive();

    // 获取铁路信号灯信息（遮断信号灯）
    this.getRailwayBlinker();

    // 获取公路信号灯
    this.getRoadBlinker();

    // 获取栏目机
    this.getBaluster();

    // 获取警告信息
    this.getWarning();

    this.villageLinkState = "red";


  },

  methods: {

    reContnect() {

      if (this.crossings[2].crossingGPS == "red") {

        this.crossings[2].crossingGPS = "green"
        this.$notify.success({
          title: '已修复',
          duration: 1000
        })
      }

      this.trainConnect = "green";

    },

    // gpsReduce() {
      
    //   this.gps = this.gps - 20;
    //   console.log("gps: " + this.gps)

    // },

        // 弹出火车信息
    getTrainInfo: function() {

      var message = null;
      var response = null;
      
      if (this.locomotive.nextCrossingId == 0) {

        message = "未检测出道口";

      } else if (this.locomotive.nextCrossingId == 1) {

        message = "凌志西道口";

      } else if (this.locomotive.nextCrossingId == 2) {

        message = "石头村道口";

      } else if (this.locomotive.nextCrossingId == 3) {

        message = "石头油库道口";

      }

      if (this.locomotive.responseStatus == 0) {

        response = "通过";

      } else {

        response = "不通过";

      }

      const trainPhoto = require("../assets/trainPhoto.png");

      this.$alert(
        "<img src=" +
          trainPhoto +
          "></br>当 前 列 车 ： " +
          this.locomotive.locomotiveName +
          "</br>前 方 道 口 ： " +
          message +
          "</br>股 道 编 码 ： " +
          this.locomotive.trackId +
          "</br>机车应答状态：" +
          response,
          "列车详情",
        {
          dangerouslyUseHTMLString: true,
          showConfirmButton: false
        }
      );
    },

    logout() {

      this.$confirm("确认退出登录吗?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
          this.$message({
            type: "success",
            message: "退出成功!"
          });
          this.$router.replace("/Login");
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消退出"
          });
        });
    },

    // 道口信息
    getCrossings() {
      this.instance
        .get("/cross.json")
        .then(res => {
          this.crossings = res.data;
          
          for (var i = 0; i < this.crossings.length; i++) {

            if (this.crossings[i].crossingMode == 0) {
              this.crossings[i].crossingMode = "远控自动";
            } else {
              this.crossings[i].crossingMode = "就地手动";
            }
          }
          // console.log(this.crossings);
        })
        .catch(err => {
          console.log(err);
        });
    },

    //火车信息
    // getLocomotive() {
    //   this.instance
    //     .get("/locomotive.json")
    //     .then(res => {
    //       this.locomotive = res.data;
    //       // 火车编码
    //       this.trainNumber = this.locomotive.locomotiveName.substring(
    //         this.locomotive.locomotiveName.length - 4,
    //         this.locomotive.locomotiveName.length
    //       );
    //       //火车GPS信号
    //       if (this.locomotive.locomotiveGPS == "well") {
    //         this.trainConnect = "greenyellow";
    //       } else {
    //         this.trainConnect = "red";
    //       }
    //       // console.log(this.locomotive);
    //     }).catch(err => {
    //       console.log(err);
    //     });
    // },
    
    // 铁路信号灯信息
    getRailwayBlinker() {
      this.instance.get("/railwayblinker.json").then(res => {
          this.railwayblinkers = res.data;
          for (var i = 0; i < this.railwayblinkers.length; i++) {
            if (this.railwayblinkers[i].crossingId == 1) {
              if (this.railwayblinkers[i].railwayBlinkerId == 1) {
                this.railwayblinkerStyle[i] = "lingLeftLight";
                if (this.railwayblinkers[i].railwayBlinkerStatus == 1) {
                  this.railwayblinkers[i].railwayBlinkerStatus = "green";
                } else {
                  this.railwayblinkers[i].railwayBlinkerStatus = "red";
                }
              } else {
                this.railwayblinkerStyle[i] = "lingRightLight";
                if (this.railwayblinkers[i].railwayBlinkerStatus == 1) {
                  this.railwayblinkers[i].railwayBlinkerStatus = "green";
                } else {
                  this.railwayblinkers[i].railwayBlinkerStatus = "red";
                }
              }
            } else if (this.railwayblinkers[i].crossingId == 2) {
              if (this.railwayblinkers[i].railwayBlinkerId == 3) {
                this.railwayblinkerStyle[i] = "villageUpLight";
                if (this.railwayblinkers[i].railwayBlinkerStatus == 1) {
                  this.railwayblinkers[i].railwayBlinkerStatus = "green";
                } else {
                  this.railwayblinkers[i].railwayBlinkerStatus = "red";
                }
              } else {
                this.railwayblinkerStyle[i] = "villageDownLight";
                if (this.railwayblinkers[i].railwayBlinkerStatus == 1) {
                  this.railwayblinkers[i].railwayBlinkerStatus = "green";
                } else {
                  this.railwayblinkers[i].railwayBlinkerStatus = "red";
                }
              }
            } else {
              if (this.railwayblinkers[i].railwayBlinkerId == 5) {
                this.railwayblinkerStyle[i] = "oilLeftLight";
                if (this.railwayblinkers[i].railwayBlinkerStatus == 1) {
                  this.railwayblinkers[i].railwayBlinkerStatus = "green";
                } else {
                  this.railwayblinkers[i].railwayBlinkerStatus = "red";
                }
              } else {
                this.railwayblinkerStyle[i] = "oilRightLight";
                if (this.railwayblinkers[i].railwayBlinkerStatus == 1) {
                  this.railwayblinkers[i].railwayBlinkerStatus = "green";
                } else {
                  this.railwayblinkers[i].railwayBlinkerStatus = "red";
                }
              }
            }
          }
          // console.log(this.railwayblinkers);
        })
        .catch(err => {
          console.log(err);
        });
    },

    // 公路信号灯信息
    getRoadBlinker() {
      this.instance
        .get("/roadblinker.json")
        .then(res => {
          this.roadBlinkers = res.data;
          for (var i = 0; i < this.roadBlinkers.length; i++) {
            if (this.roadBlinkers[i].crossingId == 1) {
              if (this.roadBlinkers[i].roadBlinkerId == 1) {
                this.roadBlinkerStyle[i] = "lingUpLight";
                if (this.roadBlinkers[i].roadBlinkerStatus == 1) {
                  this.roadBlinkers[i].roadBlinkerStatus = "green";
                } else {
                  this.roadBlinkers[i].roadBlinkerStatus = "red";
                }
              } else {
                this.roadBlinkerStyle[i] = "lingDownLight";
                if (this.roadBlinkers[i].roadBlinkerStatus == 1) {
                  this.roadBlinkers[i].roadBlinkerStatus = "green";
                } else {
                  this.roadBlinkers[i].roadBlinkerStatus = "red";
                }
              }
            } else if (this.roadBlinkers[i].crossingId == 2) {
              if (this.roadBlinkers[i].roadBlinkerId == 3) {
                this.roadBlinkerStyle[i] = "villageLeftLight";
                if (this.roadBlinkers[i].roadBlinkerStatus == 1) {
                  this.roadBlinkers[i].roadBlinkerStatus = "green";
                } else {
                  this.roadBlinkers[i].roadBlinkerStatus = "red";
                }
              } else {
                this.roadBlinkerStyle[i] = "villageRightLight";
                if (this.roadBlinkers[i].roadBlinkerStatus == 1) {
                  this.roadBlinkers[i].roadBlinkerStatus = "green";
                } else {
                  this.roadBlinkers[i].roadBlinkerStatus = "red";
                }
              }
            } else {
              if (this.roadBlinkers[i].roadBlinkerId == 5) {
                this.roadBlinkerStyle[i] = "oilUpLight";
                if (this.roadBlinkers[i].roadBlinkerStatus == 1) {
                  this.roadBlinkers[i].roadBlinkerStatus = "green";
                } else {
                  this.roadBlinkers[i].roadBlinkerStatus = "red";
                }
              } else {
                this.roadBlinkerStyle[i] = "oilDownLight";
                if (this.roadBlinkers[i].roadBlinkerStatus == 1) {
                  this.roadBlinkers[i].roadBlinkerStatus = "green";
                } else {
                  this.roadBlinkers[i].roadBlinkerStatus = "red";
                }
              }
            }
          }
        })
        .catch(err => {
          console.log(err);
        });
    },

    // 获取栏目机信息
    getBaluster() {
      this.instance
        .get("/baluster.json")
        .then(res => {
          this.balusters = res.data;
          for (var i = 0; i < this.balusters.length; i++) {
            if (this.balusters[i].crossingId == 1) {
              if (this.balusters[i].balusterId == 1) {
                if (this.balusters[i].balusterStatus == "01") {
                  (this.lingUpUrl = require("../assets/yellowLing.png")),
                    (this.lingUpState = "yellow");
                } else if (this.balusters[i].balusterStatus == "00") {
                  (this.lingUpUrl = null), (this.lingUpState = "green");
                } else {
                  (this.lingUpUrl = require("../assets/redLing.png")),
                    (this.lingUpState = "red");
                }
              } else if (this.balusters[i].balusterId == 5) {
                if (this.balusters[i].balusterStatus == "01") {
                  (this.lingDownUrl = require("../assets/yellowLing.png")),
                    (this.lingDownState = "yellow");
                } else if (this.balusters[i].balusterStatus == "00") {
                  (this.lingDownUrl = null), (this.lingDownState = "green");
                } else {
                  (this.lingDownUrl = require("../assets/redLing.png")),
                    (this.lingDownState = "red");
                }
              }
            } else if (this.balusters[i].crossingId == 2) {
              if (this.balusters[i].balusterId == 9) {
                if (this.balusters[i].balusterStatus == "01") {
                  (this.villageLeftUrl = require("../assets/yellowVillage.png")),
                    (this.villageUpState = "yellow");
                } else if (this.balusters[i].balusterStatus == "00") {
                  (this.villageLeftUrl = null), (this.villageUpState = "green");
                } else {
                  (this.villageLeftUrl = require("../assets/redVillage.png")),
                    (this.villageUpState = "red");
                }
              } else if (this.balusters[i].balusterId == 13) {
                if (this.balusters[i].balusterStatus == "01") {
                  (this.villageRightUrl = require("../assets/yellowVillage.png")),
                    (this.villageDownState = "yellow");
                } else if (this.balusters[i].balusterStatus == "00") {
                  (this.villageRightUrl = null),
                    (this.villageDownState = "green");
                } else {
                  (this.villageRightUrl = require("../assets/redVillage.png")),
                    (this.villageDownState = "red");
                }
              }
            } else if (this.balusters[i].crossingId == 3) {
              if (this.balusters[i].balusterId == 16) {
                if (this.balusters[i].balusterStatus == "01") {
                  (this.oilUpUrl = require("../assets/yellowOil.png")),
                    (this.oilUpState = "yellow");
                } else if (this.balusters[i].balusterStatus == "00") {
                  (this.oilUpUrl = null), (this.oilUpState = "yellow");
                } else {
                  (this.oilUpUrl = require("../assets/redOil.png")),
                    (this.oilUpState = "yellow");
                }
              } else if (this.balusters[i].balusterId == 17) {
                if (this.balusters[i].balusterStatus == "01") {
                  (this.oilDownUrl = require("../assets/yellowOil.png")),
                    (this.oilDownState = "yellow");
                } else if (this.balusters[i].balusterStatus == "00") {
                  (this.oilDownUrl = null), (this.oilDownState = "green");
                } else {
                  (this.oilDownUrl = require("../assets/redOil.png")),
                    (this.oilDownState = "red");
                }
              }
            }
          }
          // console.log(this.balusters);
        })
        .catch(err => {
          console.log(err);
        });
    },

    // 获取警告信息
    getWarning() {
      this.instance
        .get("/warn.json")
        .then(res => {
          this.warns = res.data;
          for (var i = 0, l = 0, v = 0, o = 0; i < this.warns.length; i++) {
            if (this.warns[i].crossingId == 1) {
              this.lingAlarm[l] = this.warns[i].warningContent;
              l++;
            } else if (this.warns[i].crossingId == 2) {
              this.villageAlarm[v] = this.warns[i].warningContent;
              v++;
            } else if (this.warns[i].crossingId == 3) {
              this.oilAlarm[o] = "与道口断开连接" + this.loginTime;
              o++;
            }
          }
          this.$forceUpdate();
          // console.log(this.warns);
          // console.log('凌志西警告信息', this.lingAlarm)
        })
        .catch(err => {
          console.log(err);
        });
    },
    handler(methodsWords) {
      // 关键字
      this[methodsWords]();
    },

    // 跳转到LingHandle.vye
    clickLingMode() {
      if (this.crossings[0].crossingMode == "就地手动") {
        this.$message({
          showClose: true,
          message: "该道口为就地手动模式，无法操作！",
          type: "warning",
          center: true
        });
      } else if (this.crossings[0].crossingMode == "远控自动") {
        let routeData = this.$router.resolve({
          path: "/LingHandle",
          query: {
            lingUpState: this.lingUpState,
            lingDownState: this.lingDownState
          }
        });
        this.crossings[0].crossingMode = "操作中";
        this.operating[0] = true;
        var _this = this;
        window.addEventListener("message", function(e) {
          if (e.data.msg.message == "complete") {
            _this.crossings[0].crossingMode = "远控自动";
            _this.operating[0] = false;
            var lan12State = e.data.msg.lan12State;
            var lan34State = e.data.msg.lan34State;
            var lan56State = e.data.msg.lan56State;
            var lan78State = e.data.msg.lan78State;
            if (lan12State == "red" || lan34State == "red") {
              _this.lingDownState = "red";
              _this.lingDownUrl = require("../assets/redLing.png");
            } else if (lan12State == "yellow" || lan34State == "yellow") {
              _this.lingDownState = "yellow";
              _this.lingDownUrl = require("../assets/yellowLing.png");
            } else {
              _this.lingDownState = "green";
              _this.lingDownUrl = null;
            }

            if (lan56State == "red" || lan78State == "red") {
              _this.lingUpState = "red";
              _this.lingUpUrl = require("../assets/redLing.png");
            } else if (lan56State == "yellow" || lan78State == "yellow") {
              _this.lingUpState = "yellow";
              _this.lingUpUrl = require("../assets/yellowLing.png");
            } else {
              _this.lingUpState = "green";
              _this.lingUpUrl = null;
            }
          }
        });
        window.open(routeData.href, "_blank");
      } else {
        this.$message({
          showClose: true,
          message: "该道口正在操作！",
          type: "warning",
          center: true
        });
      }
    },

    // 跳转到villageHandle.vye
    clickVillageMode() {
      if (this.crossings[1].crossingMode == "就地手动") {
        this.$message({
          showClose: true,
          message: "该道口为就地手动模式，无法操作！",
          type: "warning",
          center: true
        });
      } else if (this.crossings[1].crossingMode == "远控自动") {
        let routeData = this.$router.resolve({
          path: "/VillageHandle",
          query: {
            villageUpState: this.villageUpState,
            villageDownState: this.villageDownState
          }
        });
        this.crossings[1].crossingMode = "操作中";
        this.operating[1] = true;
        var _this = this;
        window.addEventListener("message", function(e) {
          if (e.data.msg.message == "village") {
            _this.crossings[1].crossingMode = "远控自动";
            _this.operating[1] = false;
            var lan12State = e.data.msg.lan12State;
            var lan34State = e.data.msg.lan34State;
            var lan56State = e.data.msg.lan56State;
            var lan78State = e.data.msg.lan78State;
            if (lan12State == "red" || lan34State == "red") {
              _this.villageDownState = "red";
              _this.villageLeftUrl = require("../assets/redVillage.png");
            } else if (lan12State == "yellow" || lan34State == "yellow") {
              _this.villageDownState = "yellow";
              _this.villageLeftUrl = require("../assets/yellowVillage.png");
            } else {
              _this.villageDownState = "green";
              _this.villageLeftUrl = null;
            }

            if (lan56State == "red" || lan78State == "red") {
              _this.villageUpState = "red";
              _this.villageRightUrl = require("../assets/redVillage.png");
            } else if (lan56State == "yellow" || lan78State == "yellow") {
              _this.villageUpState = "yellow";
              _this.villageRightUrl = require("../assets/yellowVillage.png");
            } else {
              _this.villageUpState = "green";
              _this.villageRightUrl = null;
            }
          }
        });
        window.open(routeData.href, "_blank");
      } else {
        this.$message({
          showClose: true,
          message: "该道口正在操作！",
          type: "warning",
          center: true
        });
      }
    },

    // 跳转到OilHandle.vye
    clickOilMode() {
      if (this.crossings[2].crossingMode == "就地手动") {
        this.$message({
          showClose: true,
          message: "该道口为就地手动模式，无法操作！",
          type: "warning",
          center: true
        });
      } else if (this.crossings[2].crossingMode == "远控自动") {
        let routeData = this.$router.resolve({
          path: "/OilHandle",
          query: {
            oilUpState: this.oilUpState,
            oilDownState: this.oilDownState
          }
        });
        this.crossings[2].crossingMode = "操作中";
        this.operating[2] = true;
        var _this = this;
        window.addEventListener("message", function(e) {
          if (e.data.msg.message == "oil") {
            _this.crossings[2].crossingMode = "远控自动";
            _this.operating[2] = false;
            var lan12State = e.data.msg.lan12State;
            var lan34State = e.data.msg.lan34State;
            var lan56State = e.data.msg.lan56State;
            var lan78State = e.data.msg.lan78State;
            if (lan12State == "red" || lan34State == "red") {
              _this.oilDownState = "red";
              _this.oilDownUrl = require("../assets/redLing.png");
            } else if (lan12State == "yellow" || lan34State == "yellow") {
              _this.oilDownState = "yellow";
              _this.oilDownUrl = require("../assets/yellowLing.png");
            } else {
              _this.oilDownState = "green";
              _this.oilDownUrl = null;
            }

            if (lan56State == "red" || lan78State == "red") {
              _this.oilUpState = "red";
              _this.oilUpUrl = require("../assets/redLing.png");
            } else if (lan56State == "yellow" || lan78State == "yellow") {
              _this.oilUpState = "yellow";
              _this.oilUpUrl = require("../assets/yellowLing.png");
            } else {
              _this.oilUpState = "green";
              _this.oilUpUrl = null;
            }
          }
        });
        window.open(routeData.href, "_blank");
      } else {
        this.$message({
          showClose: true,
          message: "该道口正在操作！",
          type: "warning",
          center: true
        });
      }
    },

    changeAlarm(value) {

      switch (value) {
        case "option1":
          this.showRFIDAlarm = true;
          this.showLingAlarm = true;
          this.showVillageAlarm = true;
          this.showOilAlarm = true;
          break;
        case "option2":
          this.showRFIDAlarm = true;
          this.showLingAlarm = false;
          this.showVillageAlarm = false;
          this.showOilAlarm = false;
          break;
        case "option3":
          this.showRFIDAlarm = false;
          this.showLingAlarm = true;
          this.showVillageAlarm = false;
          this.showOilAlarm = false;
          break;
        case "option4":
          this.showRFIDAlarm = false;
          this.showLingAlarm = false;
          this.showVillageAlarm = true;
          this.showOilAlarm = false;
          break;
        default:
          this.showRFIDAlarm = false;
          this.showLingAlarm = false;
          this.showVillageAlarm = false;
          this.showOilAlarm = true;
          break;
      }

    },
    run() {

      var lomoId = 1;

      var _this = this;

      http({

        method: 'post',

        url: '/locomotive/listLoco',

        headers: {

            'Content-Type': 'application/json;charset=utf-8'

        },

        data: {

          locomotiveId: lomoId

        },

        responseType: 'json'

        }).then(function (res) {
            
            var info = res.info;
            var code = res.code;

            if (code == 200) {

                // 创建user对象
                var train = new Object();

                _this.locomotive.locomotiveName = train.locoName = info.locomotiveName;
                _this.locomotive.trackId = train.trackId = info.trackId;
                _this.locomotive.locomotiveGPS = train.gps = info.locomotiveGPS;
                _this.locomotive.responseStatus = info.responseStatus;

                if (_this.locomotive.responseStatus == 0) {

                    _this.$message.success("列车自检正常, 可正常发车！");
                
                }

                // 火车编码
                _this.trainNumber = _this.locomotive.locomotiveName.substring(
                _this.locomotive.locomotiveName.length - 4,
                _this.locomotive.locomotiveName.length
                );

                // 提交train信息
                _this.$store.commit('createTrain', JSON.stringify(train));

                console.table(_this.$store.state.train);

            } else {

                _this.$message.error('train: 错误！');

            }

        }).catch(function (error) {
            
            console.log(error);
            _this.$message.error('系统错误！');

      })

      // 控制v-show
      this.isRun = true;

      this.lingUpUrl = null;
      this.lingDownUrl = null;
      this.villageLeftUrl = null;
      this.villageRightUrl = null;

      // 凌志西
      // this.roadBlinkers[0].roadBlinkerStatus = "green";
      // this.roadBlinkers[1].roadBlinkerStatus = "green";
      this.railwayblinkers[0].railwayBlinkerStatus = "red";
      this.railwayblinkers[1].railwayBlinkerStatus = "red";

      // 石头村
      this.roadBlinkers[2].roadBlinkerStatus = "green";
      this.roadBlinkers[3].roadBlinkerStatus = "green";
      this.railwayblinkers[2].railwayBlinkerStatus = "red";
      this.railwayblinkers[3].railwayBlinkerStatus = "red";

      

      this.timer1 = window.setInterval(() => {

          this.percent++;
          
          this.icontranslateX = this.icontranslateX - 1;
          this.icontranslateY = this.icontranslateY + 0.8;

          // 凌志西1段
          if (this.percent >= 50 && this.percent <= 80) {

              if (this.locomotive.responseStatus == 0) {

                this.trainConnect = "greenyellow";
              
              }

              // 栏目机：黄
              this.lingUpUrl = require("../assets/yellowLing.png");
              this.lingDownUrl =  require("../assets/yellowLing.png"),

              this.roadBlinkers[0].roadBlinkerStatus = "red";
              this.roadBlinkers[1].roadBlinkerStatus = "red";
              this.railwayblinkers[0].railwayBlinkerStatus = "green";
              this.railwayblinkers[1].railwayBlinkerStatus = "green";

              this.locomotive.nextCrossingId = 1;

          }

          
          // 凌志西2段
          if (this.percent > 80 && this.percent <= 180) {

              this.lingUpUrl = require("../assets/redLing.png");
              this.lingDownUrl =  require("../assets/redLing.png"),
              
              this.roadBlinkers[0].roadBlinkerStatus = "red";
              this.roadBlinkers[1].roadBlinkerStatus = "red";

          }

          // 石头村1段
          if (this.percent >= 180 && this.percent < 220) {

              if (this.locomotive.responseStatus == 0) {

                this.trainConnect = "greenyellow";
              
              }

              
              this.villageLeftUrl = require("../assets/yellowVillage.png"),
              this.villageRightUrl = require("../assets/yellowVillage.png"),

              
              this.roadBlinkers[2].roadBlinkerStatus = "red";
              this.roadBlinkers[3].roadBlinkerStatus = "red";

              this.railwayblinkers[2].railwayBlinkerStatus = "green";
              this.railwayblinkers[3].railwayBlinkerStatus = "green";

              
              this.locomotive.nextCrossingId = 2;
            
          }

          // 石头村2段
          if (this.percent >= 220 && this.percent < 325) {


              this.villageLeftUrl = require("../assets/redVillage.png"),
              this.villageRightUrl = require("../assets/redVillage.png"),


              this.roadBlinkers[2].roadBlinkerStatus = "red";
              this.roadBlinkers[3].roadBlinkerStatus = "red";
              this.railwayblinkers[2].railwayBlinkerStatus = "green";
              this.railwayblinkers[3].railwayBlinkerStatus = "green";

          }

          if(this.percent >= 325) {

              clearInterval(this.timer1);
              this.timer2 = window.setInterval(() => {

                  this.percent++;
                  this.icontranslateY = this.icontranslateY + 0.8;

                    if(this.percent >= 420) {
                      
                    clearInterval(this.timer2);
                    this.timer3 = window.setInterval(() => {


                      if (this.percent == 425) {

                          this.$message.error("与" + this.locomotive.locomotiveName + "失去连接");
                          this.showOilAlarm = true;

                      }

                      if(this.percent > 480) {

                        
                          this.locomotive.nextCrossingId = 3;

                          this.lingUpUrl = null;
                          this.lingDownUrl = null;
                          this.villageLeftUrl = null;
                          this.villageRightUrl = null;
                          
                          this.roadBlinkers[0].roadBlinkerStatus = "green";
                          this.roadBlinkers[1].roadBlinkerStatus = "green";
                          this.railwayblinkers[0].railwayBlinkerStatus = "red";
                          this.railwayblinkers[1].railwayBlinkerStatus = "red";

                          this.roadBlinkers[2].roadBlinkerStatus = "green";
                          this.roadBlinkers[3].roadBlinkerStatus = "green";
                          this.railwayblinkers[2].railwayBlinkerStatus = "red";
                          this.railwayblinkers[3].railwayBlinkerStatus = "red";
                          
                          this.railwayblinkers[4].railwayBlinkerStatus = "green";
                          this.railwayblinkers[5].railwayBlinkerStatus = "green";

                      }

                      if (this.percent == 430) {

                        this.locomotive.responseStatus = 1;
                          this.trainConnect = "red";

                          this.crossings[2].crossingGPS = "red";
                          this.$notify.error({
                            title: '信号故障',
                            duration: 0
                          })

                        this.gps = this.gps - 28;

                      }

                        this.percent++;
                        this.icontranslateX = this.icontranslateX - 1;
                        this.icontranslateY = this.icontranslateY + 0.58;

                        if(this.percent >= 800) {

                            window.clearInterval(this.timer3);

                        }
                    }, this.gps)
                    }
              }, this.gps)
          }
      }, this.gps)



    },

    WIFIChange(val) {

      console.log('编号', this.crossings[val]);
      if (this.crossings[val].crossingGPS == "green") {

        this.crossings[val].crossingGPS = "red"
        this.$notify.error({
          title: '信号故障',
          duration: 1000
        })
      } else {
        this.crossings[val].crossingGPS = "green"
        this.$notify.success({
          title: '已修复',
          duration: 1000
        })
      }
    },

    stop() {

      window.clearInterval(this.timer1);
      window.clearInterval(this.timer2);
      window.clearInterval(this.timer3);

    }

  },

  computed: {
    lingStateChange() {
      const { lingUpState, lingDownState } = this;
      return {
        lingUpState,
        lingDownState
      };
    },
    villageStateChange() {
      const { villageUpState, villageDownState } = this;
      return {
        villageUpState,
        villageDownState
      };
    },
    oilStateChange() {
      const { oilUpState, oilDownState } = this;
      return {
        oilUpState,
        oilDownState
      };
    },
    iconstyle:function(){//图标动态样式
      let arr = new Array();
      arr.push('transform:');//注意：先移动后旋转，实现原地旋转；先旋转后移动，位置按照旋转后的新坐标系确定
      arr.push('translateX('+this.icontranslateX+'px) ');
      arr.push('translateY('+this.icontranslateY+'px) ');
      return arr.join("");
      }
  },
  watch: {
    lingStateChange(val) {
      console.log("凌志西：", val);

      if (
        val.lingUpState == "yellow" ||
        val.lingUpState == "red" ||
        val.lingDownState == "yellow" ||
        val.lingDownState == "red"
      ) {
        this.roadBlinkers[0].roadBlinkerStatus = "red";
        this.roadBlinkers[1].roadBlinkerStatus = "red";
        this.railwayblinkers[0].railwayBlinkerStatus = "green";
        this.railwayblinkers[1].railwayBlinkerStatus = "green";
      } else {
        this.roadBlinkers[0].roadBlinkerStatus = "green";
        this.roadBlinkers[1].roadBlinkerStatus = "green";
        this.railwayblinkers[0].railwayBlinkerStatus = "red";
        this.railwayblinkers[1].railwayBlinkerStatus = "red";
      }
    },
    villageStateChange(val) {

      console.log("石头村", val);

      if (
        val.villageUpState == "yellow" ||
        val.villageDownState == "red" ||
        val.villageDownState == "yellow" ||
        val.villageDownState == "red"
      ) {
        this.roadBlinkers[2].roadBlinkerStatus = "red";
        this.roadBlinkers[3].roadBlinkerStatus = "red";
        this.railwayblinkers[2].railwayBlinkerStatus = "green";
        this.railwayblinkers[3].railwayBlinkerStatus = "green";
      } else {
        this.roadBlinkers[2].roadBlinkerStatus = "green";
        this.roadBlinkers[3].roadBlinkerStatus = "green";
        this.railwayblinkers[2].railwayBlinkerStatus = "red";
        this.railwayblinkers[3].railwayBlinkerStatus = "red";
      }
    },
    oilStateChange(val) {
      console.log("石头油库", val);

      if (
        val.oilUpState == "yellow" ||
        val.oilDownState == "red" ||
        val.oilDownState == "yellow" ||
        val.oilDownState == "red"
      ) {
        this.roadBlinkers[4].roadBlinkerStatus = "green";
        this.roadBlinkers[5].roadBlinkerStatus = "green";
        this.railwayblinkers[4].railwayBlinkerStatus = "red";
        this.railwayblinkers[5].railwayBlinkerStatus = "red";
      } else {
        this.roadBlinkers[4].roadBlinkerStatus = "green";
        this.roadBlinkers[5].roadBlinkerStatus = "green";
        this.railwayblinkers[4].railwayBlinkerStatus = "red";
        this.railwayblinkers[5].railwayBlinkerStatus = "red";
      }
    },

    percent(val) {

      console.log(val);

      if (this.percent == 30) {

        this.$message.success('距离凌志西车站还有5km！');

      } else if (this.percent == 180) {

        this.$message.success('距离石头村车站还有5km！');

      } else if (this.percent == 500) {

        this.$message.warning('距离终点：石头油库车站还有5km！');

      }

      if (this.percent > 50 && this.percent < 325) {

        this.aim = "凌志西"
        

      } else if (this.percent >= 325 && this.percent < 420) {

        this.aim = "石头村"

      } else if (this.percent >= 420 && this.percent < 550) {

        this.aim = "石头油库"

      } else if (this.percent > 550) {

        this.aim = "终点"
      
      }

    }
  }
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.all {
    width: 100%;
    background: url("../assets/map.png") no-repeat;
    background-size: 100% auto;
    padding-bottom: 47%;
    height: 0px;
}

.controlLing {
    position: absolute;
    width: 30px;
    left: 800px;
    top: 240px;
    cursor: pointer;
}

.controlVillage {
    position: absolute;
    width: 30px;
    left: 700px;
    top: 490px;
    cursor: pointer;
}

.controlOil {
    position: absolute;
    width: 30px;
    left: 480px;
    top: 620px;
    cursor: pointer;
}

.lingMode {
    position: absolute;
    left: 900px;
    top: 290px;
}

.villageMode {
    position: absolute;
    left: 550px;
    top: 360px;
}

.oilMode {
    position: absolute;
    left: 300px;
    top: 490px;
}

.lingUp{
    position: absolute;
    top: 245px;
    left: 850px;
    width: 50px;
    height: 50px;
}

.lingDown{
    position: absolute;
    top: 314px;
    left: 842px;
    width: 50px;
    height: 50px;
}

.oilUp{
    position: absolute;
    top: 627px;
    left: 380px;
    width: 50px;
    height: 50px;
}

.oilDown{
    position: absolute;
    top: 679px;
    left: 410px;
    width: 50px;
    height: 50px;
}

.villageLeft{
    position: absolute;
    top: 475px;
    left: 625px;
    width: 31px;
    height: 50px;
}

.villageRight{
    position: absolute;
    top: 433px;
    left: 730px;
    width: 31px;
    height: 50px;
}

.lingLeftLight{
    position: absolute;
    top: 342px;
    left: 806px;
    width: 14px;
    height: 14px;
    border-radius: 7px;
}

.lingRightLight{
    position: absolute;
    top: 251px;
    left: 920px;
    width: 14px;
    height: 14px;
    border-radius: 7px;
}

.lingUpLight{
    position: absolute;
    top: 227px;
    left: 865px;
    width: 14px;
    height: 14px;
    border-radius: 7px;
}

.lingDownLight{
    position: absolute;
    top: 356px;
    left: 858px;
    width: 14px;
    height: 14px;
    border-radius: 7px;
}

.villageLeftLight{
    position: absolute;
    top: 504px;
    left: 612px;
    width: 14px;
    height: 14px;
    border-radius: 7px;
}

.villageRightLight{
    position: absolute;
    top: 439px;
    left: 760px;
    width: 14px;
    height: 14px;
    border-radius: 7px;
}

.villageUpLight{
    position: absolute;
    top: 402px;
    left: 732px;
    width: 14px;
    height: 14px;
    border-radius: 7px;
}

.villageDownLight{
    position: absolute;
    top: 497px;
    left: 686px;
    width: 14px;
    height: 14px;
    border-radius: 7px;
}

.oilLeftLight{
    position: absolute;
    top: 697px;
    left: 360px;
    width: 14px;
    height: 14px;
    border-radius: 7px;
}

.oilRightLight{
    position: absolute;
    top: 642px;
    left: 459px;
    width: 14px;
    height: 14px;
    border-radius: 7px;
}

.oilUpLight{
    position: absolute;
    top: 627px;
    left: 388px;
    width: 14px;
    height: 14px;
    border-radius: 7px;
}

.oilDownLight{
    position: absolute;
    top: 718px;
    left: 440px;
    width: 14px;
    height: 14px;
    border-radius: 7px;
}

.train {
    position: absolute;
    width: 40px;
    left: 1000px;
    top: 129px;
    background-color: skyblue;
    cursor: pointer;
}

.trainNumber {
    color: whitesmoke;
}

.linkStates {
    position: absolute;
    left: 0;
    bottom: 0;
}

.linkState {
    float: left;
    font-size: 13px;
    text-align: center;
    margin-left: 10px;
    margin-bottom: 10px;
}

.alarmBox {
    width: 400px;
    padding: 10px 10px 10px 10px;
    background-color: whitesmoke;
}

.alarmBoxTitle {
    color: red;
    margin-right: 7em;
}
</style>