<template>
    <div class="all">
        <span class="crossingName">石头村道口</span>
        <span class="crossingState">当前状态：{{ lingMode }}</span>
        <el-button
            type="primary"
            plain
            size="medium"
            :disabled="operating"
            @click="changeLingState"
            >切换状态</el-button
        >

        <el-divider></el-divider>

        <el-radio-group class="doubleRadio" v-model="lingRadio">
            <el-radio :disabled="lingRadioDisabled" :label="1"
                >开启道口（全部抬起）</el-radio
            >
            <el-radio :disabled="lingRadioDisabled" :label="2"
                >关闭道口（全部放下）</el-radio
            >
        </el-radio-group>
        <el-button
            type="primary"
            plain
            :disabled="lingRadioDisabled"
            :loading="operating"
            size="medium"
            @click="lingOrder"
            >发送指令</el-button
        >

        <img src="../assets/villageMap.png" />

        <div
            class="lan12"
            :style="{ backgroundImage: 'url(' + lan12Url + ')', cursor: pointer12 }"
            @click="clickLan12 = lan12Disabled"
        ></div>

        <el-dialog
            title="发送命令"
            :show-close="!operating"
            :visible.sync="clickLan12"
            width="300px"
        >
            <h2>拦木机1-拦木机2</h2>
            <br />
            <el-radio-group v-model="dialog12Radio">
                <el-radio :disabled="operating" :label="1">抬起</el-radio>
                <el-radio :disabled="operating" :label="2">停止</el-radio>
                <el-radio :disabled="operating" :label="3">放下</el-radio>
            </el-radio-group>
            <div class="dialogButtonBox">
                <el-button
                    type="primary"
                    plain
                    :disabled="operating"
                    :loading="operating"
                    @click="lan12Order"
                    >发送指令</el-button
                >
            </div>
        </el-dialog>

        <div
            class="lan34"
            :style="{ backgroundImage: 'url(' + lan34Url + ')', cursor: pointer12 }"
            @click="clickLan34 = lan34Disabled"
        ></div>

        <el-dialog
            title="发送命令"
            :show-close="!operating"
            :visible.sync="clickLan34"
            width="300px"
        >
            <h2>拦木机3-拦木机4</h2>
            <br />
            <el-radio-group v-model="dialog34Radio">
                <el-radio :disabled="operating" :label="1">抬起</el-radio>
                <el-radio :disabled="operating" :label="2">停止</el-radio>
                <el-radio :disabled="operating" :label="3">放下</el-radio>
            </el-radio-group>
            <div class="dialogButtonBox">
                <el-button
                    type="primary"
                    plain
                    :disabled="operating"
                    :loading="operating"
                    @click="lan34Order"
                    >发送指令</el-button
                >
            </div>
        </el-dialog>

        <div
            class="lan56"
            :style="{ backgroundImage: 'url(' + lan56Url + ')', cursor: pointer12 }"
            @click="clickLan56 = lan56Disabled"
        ></div>

        <el-dialog
            title="发送命令"
            :show-close="!operating"
            :visible.sync="clickLan56"
            width="300px"
        >
            <h2>拦木机5-拦木机6</h2>
            <br />
            <el-radio-group v-model="dialog56Radio">
                <el-radio :disabled="operating" :label="1">抬起</el-radio>
                <el-radio :disabled="operating" :label="2">停止</el-radio>
                <el-radio :disabled="operating" :label="3">放下</el-radio>
            </el-radio-group>
            <div class="dialogButtonBox">
                <el-button
                    type="primary"
                    plain
                    :disabled="operating"
                    :loading="operating"
                    @click="lan56Order"
                    >发送指令</el-button
                >
            </div>
        </el-dialog>

        <div
            class="lan78"
            :style="{ backgroundImage: 'url(' + lan78Url + ')', cursor: pointer12 }"
            @click="clickLan78 = lan78Disabled"
        ></div>

        <el-dialog
            title="发送命令"
            :show-close="!operating"
            :visible.sync="clickLan78"
            width="300px"
        >
            <h2>拦木机7-拦木机8</h2>
            <br />
            <el-radio-group v-model="dialog78Radio">
                <el-radio :disabled="operating" :label="1">抬起</el-radio>
                <el-radio :disabled="operating" :label="2">停止</el-radio>
                <el-radio :disabled="operating" :label="3">放下</el-radio>
            </el-radio-group>
            <div class="dialogButtonBox">
                <el-button
                    type="primary"
                    plain
                    :disabled="operating"
                    :loading="operating"
                    @click="lan78Order"
                    >发送指令</el-button
                >
            </div>
        </el-dialog>
    </div>
</template>

<script>
export default {
    name: "LingHandle",
    data() {
        return {
            lingMode: "远控自动",
            lingRadioDisabled: true,
            lan12Disabled: false,
            lan34Disabled: false,
            lan56Disabled: false,
            lan78Disabled: false,
            lingRadio: null,
            operating: false,

            clickLan12: false,
            clickLan34: false,
            clickLan56: false,
            clickLan78: false,

            dialog12Radio: null,
            dialog34Radio: null,
            dialog56Radio: null,
            dialog78Radio: null,

            lan12Url: null,
            lan34Url: null,
            lan56Url: null,
            lan78Url: null,

            lan12State: null,
            lan34State: null,
            lan56State: null,
            lan78State: null,

            pointer12: null,
        };
    },

    created: function () {
        var lingUpState = this.$route.query.villageUpState;
        var lingDownState = this.$route.query.villageDownState;
        if (lingUpState == "yellow") {
            this.lan56Url = require("../assets/yellow100.png");
            this.lan78Url = require("../assets/yellow.png");
            this.lan56State = "yellow";
            this.lan78State = "yellow";
        } else if (lingUpState == "red") {
            this.lan56Url = require("../assets/red100.png");
            this.lan78Url = require("../assets/red.png");
            this.lan56State = "red";
            this.lan78State = "red";
        } else {
            this.lan56Url = require("../assets/green100.png");
            this.lan78Url = require("../assets/green.png");
            this.lan56State = "green";
            this.lan78State = "green";
        }

        if (lingDownState == "yellow") {
            this.lan12Url = require("../assets/yellow.png");
            this.lan34Url = require("../assets/yellow.png");
            this.lan12State = "yellow";
            this.lan34State = "yellow";
        } else if (lingDownState == "red") {
            this.lan12Url = require("../assets/red.png");
            this.lan34Url = require("../assets/red.png");
            this.lan12State = "red";
            this.lan34State = "red";
        } else {
            this.lan12Url = require("../assets/green.png");
            this.lan34Url = require("../assets/green.png");
            this.lan12State = "green";
            this.lan34State = "green";
        }
    },

    mounted() {
        window.addEventListener("beforeunload", (e) =>
            this.beforeunloadHandler(e)
        );
    },

    methods: {
        beforeunloadHandler(e) {
            var msg = {
                message: "village",
                lan12State: this.lan12State,
                lan34State: this.lan34State,
                lan56State: this.lan56State,
                lan78State: this.lan78State,
            };
            window.opener.postMessage(
                {
                    msg
                },
                "*"
            );
        },

        changeLingState() {
            if (this.lingMode == "远控自动") {
                this.lingMode = "远控手动";
                this.lingRadioDisabled = false;
                this.lan12Disabled = true;
                this.lan34Disabled = true;
                this.lan56Disabled = true;
                this.lan78Disabled = true;
                this.pointer12 = "pointer";
            } else {
                this.lingMode = "远控自动";
                this.lingRadioDisabled = true;
                this.lingRadioDisabled = true;
                this.lan12Disabled = false;
                this.lan34Disabled = false;
                this.lan56Disabled = false;
                this.lan78Disabled = false;
                this.pointer12 = "pointer";
            }
        },

        lingOrder() {
            var _this = this;
            if (this.lingRadio == "1") {
                this.lingRadioDisabled = true;
                this.operating = true;
                this.lan12Url = require("../assets/yellow.png");
                this.lan34Url = require("../assets/yellow.png");
                this.lan56Url = require("../assets/yellow100.png");
                this.lan78Url = require("../assets/yellow.png");
                setTimeout(() => {
                    _this.operating = false;
                    _this.lingRadioDisabled = false;
                    _this.lan12Url = require("../assets/green.png");
                    _this.lan34Url = require("../assets/green.png");
                    _this.lan56Url = require("../assets/green100.png");
                    _this.lan78Url = require("../assets/green.png");
                    this.lan12State = "green";
                    this.lan34State = "green";
                    this.lan56State = "green";
                    this.lan78State = "green";
                }, 3000);
            } else if (this.lingRadio == "2") {
                this.lingRadioDisabled = true;
                this.operating = true;
                this.lan12Url = require("../assets/yellow.png");
                this.lan34Url = require("../assets/yellow.png");
                this.lan56Url = require("../assets/yellow100.png");
                this.lan78Url = require("../assets/yellow.png");
                setTimeout(() => {
                    _this.operating = false;
                    _this.lingRadioDisabled = false;
                    this.lan12Url = require("../assets/red.png");
                    this.lan34Url = require("../assets/red.png");
                    this.lan56Url = require("../assets/red100.png");
                    this.lan78Url = require("../assets/red.png");
                    this.lan12State = "red";
                    this.lan34State = "red";
                    this.lan56State = "red";
                    this.lan78State = "red";
                }, 3000);
            }
        },

        lan12Order() {
            var _this = this;
            if (this.dialog12Radio == "1") {
                this.lingRadioDisabled = true;
                this.operating = true;
                this.lan12Url = require("../assets/yellow.png");
                setTimeout(() => {
                    _this.operating = false;
                    _this.lingRadioDisabled = false;
                    _this.lan12Url = require("../assets/green.png");
                    _this.lan12State = "green";
                }, 3000);
            } else if (this.dialog12Radio == "2") {
                this.lingRadioDisabled = true;
                this.operating = true;
                this.lan12Url = require("../assets/yellow.png");
                setTimeout(() => {
                    _this.operating = false;
                    _this.lingRadioDisabled = false;
                    _this.lan12State = "yellow";
                }, 1000);
            } else if (this.dialog12Radio == "3") {
                this.lingRadioDisabled = true;
                this.operating = true;
                this.lan12Url = require("../assets/yellow.png");
                setTimeout(() => {
                    _this.operating = false;
                    _this.lingRadioDisabled = false;
                    this.lan12Url = require("../assets/red.png");
                    _this.lan12State = "red";
                }, 3000);
            }
        },

        lan34Order() {
            var _this = this;
            if (this.dialog34Radio == "1") {
                this.lingRadioDisabled = true;
                this.operating = true;
                this.lan34Url = require("../assets/yellow.png");
                setTimeout(() => {
                    _this.operating = false;
                    _this.lingRadioDisabled = false;
                    _this.lan34Url = require("../assets/green.png");
                    _this.lan34State = "green";
                }, 3000);
            } else if (this.dialog34Radio == "2") {
                this.lingRadioDisabled = true;
                this.operating = true;
                this.lan34Url = require("../assets/yellow.png");
                setTimeout(() => {
                    _this.operating = false;
                    _this.lingRadioDisabled = false;
                    _this.lan34State = "yellow";
                }, 1000);
            } else if (this.dialog34Radio == "3") {
                this.lingRadioDisabled = true;
                this.operating = true;
                this.lan34Url = require("../assets/yellow.png");
                setTimeout(() => {
                    _this.operating = false;
                    _this.lingRadioDisabled = false;
                    this.lan34Url = require("../assets/red.png");
                    _this.lan34State = "red";
                }, 3000);
            }
        },

        lan56Order() {
            var _this = this;
            if (this.dialog56Radio == "1") {
                this.lingRadioDisabled = true;
                this.operating = true;
                this.lan56Url = require("../assets/yellow100.png");
                setTimeout(() => {
                    _this.operating = false;
                    _this.lingRadioDisabled = false;
                    _this.lan56Url = require("../assets/green100.png");
                    _this.lan56State = "green";
                }, 3000);
            } else if (this.dialog56Radio == "2") {
                this.lingRadioDisabled = true;
                this.operating = true;
                this.lan56Url = require("../assets/yellow100.png");
                setTimeout(() => {
                    _this.operating = false;
                    _this.lingRadioDisabled = false;
                    _this.lan56State = "yellow";
                }, 1000);
            } else if (this.dialog56Radio == "3") {
                this.lingRadioDisabled = true;
                this.operating = true;
                this.lan56Url = require("../assets/yellow100.png");
                setTimeout(() => {
                    _this.operating = false;
                    _this.lingRadioDisabled = false;
                    this.lan56Url = require("../assets/red100.png");
                    _this.lan56State = "red";
                }, 3000);
            }
        },

        lan78Order() {
            var _this = this;
            if (this.dialog78Radio == "1") {
                this.lingRadioDisabled = true;
                this.operating = true;
                this.lan78Url = require("../assets/yellow.png");
                setTimeout(() => {
                    _this.operating = false;
                    _this.lingRadioDisabled = false;
                    _this.lan78Url = require("../assets/green.png");
                    _this.lan78State = "green";
                }, 3000);
            } else if (this.dialog78Radio == "2") {
                this.lingRadioDisabled = true;
                this.operating = true;
                this.lan78Url = require("../assets/yellow.png");
                setTimeout(() => {
                    _this.operating = false;
                    _this.lingRadioDisabled = false;
                    _this.lan78State = "yellow";
                }, 1000);
            } else if (this.dialog78Radio == "3") {
                this.lingRadioDisabled = true;
                this.operating = true;
                this.lan78Url = require("../assets/yellow.png");
                setTimeout(() => {
                    _this.operating = false;
                    _this.lingRadioDisabled = false;
                    this.lan78Url = require("../assets/red.png");
                    _this.lan78State = "red";
                }, 3000);
            }
        },
    },

    beforeDestroy: function () {
        window.removeEventListener("beforeunload", (e) =>
            this.beforeunloadHandler(e)
        );
    },
};
</script>

<style  scoped>
.all {
    margin: 0 auto;
    width: 1043px;
    padding-top: 10px;
}

.crossingName {
    color: blue;
    font-size: 30px;
    margin-right: 400px;
    margin-left: 3em;
}

.crossingState {
    font-size: 20px;
    margin-right: 1em;
}

.doubleRadio {
    margin-left: 280px;
}

img {
    margin-top: 20px;
}

.lan12 {
    position: absolute;
    left: 521px;
    top: 603px;
    width: 50px;
    height: 102px;
    /* cursor: pointer; */
}

.lan34 {
    position: absolute;
    left: 701px;
    top: 486px;
    width: 50px;
    height: 102px;
    /* cursor: pointer; */
}

.lan56 {
    position: absolute;
    left: 748px;
    top: 560px;
    width: 100px;
    height: 100px;
    /* cursor: pointer; */
}

.lan78 {
    position: absolute;
    left: 899px;
    top: 446px;
    width: 50px;
    height: 102px;
    /* cursor: pointer; */
}

.dialogButtonBox {
    margin-top: 20px;
    text-align: center;
}
</style>