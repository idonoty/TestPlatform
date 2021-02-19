<template>
  <el-dialog :title="!dataForm.id ? '新增' : '修改'" :close-on-click-modal="false" :visible.sync="visible" width="600px">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px"
      inline-message="true" status-icon="true">
      <el-form-item label="url" prop="url" style="position: relative">
        <el-input v-model="dataForm.url" placeholder="url"></el-input>
        <el-select v-model="dataForm.url" filterable allow-create placeholder="url" style="width: 47px; position: absolute; right: 0px">
          <el-option v-for="item in dataForm.urlList" :key="item" :label="item.url1" :value="item.url1"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="appkey" prop="appkey">
        <el-input v-model="dataForm.appkey" placeholder="请输入appkey"></el-input>
      </el-form-item>
      <el-form-item label="线程数" prop="loopNum">
        <el-input v-model="dataForm.loopNum" placeholder="loopNum"></el-input>
      </el-form-item>
      <el-form-item label="用户数" prop="userNum">
        <el-input v-model="dataForm.userNum" placeholder="userNum"></el-input>
      </el-form-item>
      <el-form-item label="事件下限" prop="eventMin">
        <el-input v-model="dataForm.eventMin" placeholder="eventMin"></el-input>
      </el-form-item>
      <el-form-item label="事件上限" prop="eventMax">
        <el-input v-model="dataForm.eventMax" placeholder="eventMax"></el-input>
      </el-form-item>
      <el-form-item label="起始时间" prop="timeMin">
        <el-input v-model="dataForm.timeMin" placeholder="timeMin"></el-input>
      </el-form-item>
      <el-form-item label="结束时间" prop="timeMax">
        <el-input v-model="dataForm.timeMax" placeholder="timeMax"></el-input>
      </el-form-item>
      <el-form-item label="上报alias" prop="alias">
        <el-input v-model="dataForm.alias" placeholder="填写false则不产生alias绑定"></el-input>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </span>
  </el-dialog>
</template>

<script>
  export default {
    data() {
      return {
        visible: false,
        groupList: [],
        dataForm: {
          url: "https://arkpaastest.analysys.cn:4089",
          appkey: "",
          loopNum: "3",
          userNum: "10",
          eventMin: "10",
          eventMax: "20",
          timeMin: this.getNowDay() + " 00:00:00",
          timeMax: this.getNowDay() + " " + this.getNowDate(),
          alias: "",
          urlList: [{
              url1: "https://arkpaastest.analysys.cn:4089"
            },
            {
              url1: "http://117.50.5.132:8089"
            },
            {
              url1: "http://192.168.220.130:8089"
            },
            {
              url1: "http://192.168.220.111:8089"
            },
            {
              url1: "http://192.168.8.111:8089"
            },
          ],
        },
        dataRule: {
          url: [{
            required: true,
            message: "url不能为空",
            trigger: "blur",
          }, ],
          appkey: [{
            required: true,
            message: "appkey不能为空",
            trigger: "blur",
          }, ],
          loopNum: [{
            required: true,
            message: "线程数不能为空",
            trigger: "blur",
          }, ],
          userNum: [{
            required: true,
            message: "用户数不能为空",
            trigger: "blur",
          }, ],
          eventMin: [{
            required: true,
            message: "事件下限不能为空",
            trigger: "blur",
          }, ],
          eventMax: [{
            required: true,
            message: "事件上限不能为空",
            trigger: "blur",
          }, ],
          timeMin: [{
            required: true,
            message: "起始时间不能为空",
            trigger: "blur",
          }, ],
          timeMax: [{
            required: true,
            message: "结束时间不能为空",
            trigger: "blur",
          }, ],
        },
      };
    },
    methods: {
      init(id) {
        this.dataForm.recordId = id || 0;
        this.visible = true;

        this.$http({
          url: this.$http.adornUrl(`/app/testdata/grouplist`),
          method: "get",
        }).then(({
          data
        }) => {
          if (data && data.code === 0) {
            this.groupList = data.list;
          }
        });

        this.$nextTick(() => {
          this.$refs["dataForm"].resetFields();
          if (this.dataForm.recordId) {
            this.$http({
              url: this.$http.adornUrl(
                `/app/testdata/info/${this.dataForm.testdataId}`
              ),
              method: "get",
              params: this.$http.adornParams(),
            }).then(({
              data
            }) => {
              if (data && data.code === 0) {
                this.dataForm.url = data.testdata.url;
                this.dataForm.appkey = data.testdata.appkey;
                this.dataForm.groupName = data.testdata.groupName;
              }
            });
          }
        });
      },
      getNowDay() {
        var date = new Date();
        var seperator1 = "-";
        var seperator2 = ":";
        var month = date.getMonth() + 1;
        var strDate = date.getDate();
        if (month >= 1 && month <= 9) {
          month = "0" + month;
        }
        if (strDate >= 0 && strDate <= 9) {
          strDate = "0" + strDate;
        }
        var currentdate =
          date.getFullYear() + seperator1 + month + seperator1 + strDate;
        return currentdate;
      },
      getNowDate() {
        var date = new Date();
        var seperator1 = "-";
        var seperator2 = ":";
        var strHour = date.getHours();
        var strMinute = date.getMinutes();
        var strSecond = date.getSeconds();
        if (strHour >= 0 && strHour <= 9) {
          strHour = "0" + strHour;
        }
        if (strMinute >= 0 && strMinute <= 9) {
          strMinute = "0" + strMinute;
        }
        if (strSecond >= 0 && strSecond <= 9) {
          strSecond = "0" + strSecond;
        }
        var currentdate =
          strHour + seperator2 + strMinute + seperator2 + strSecond;
        return currentdate;
      },
      // 表单提交
      dataFormSubmit() {
        this.$refs["dataForm"].validate((valid) => {
          if (valid) {
            const loadingObj = this.$loading({
              lock: false,
              text: "提交中...",
              spinner: "el-icon-loading",
              background: "rgba(0, 0, 0, 0.7)",
            });
            this.$http({
              url: this.$http.adornUrl(`/app/testdata/updata`),
              method: "post",
              data: this.$http.adornData({
                url: this.dataForm.url,
                appkey: this.dataForm.appkey,
                groupName: this.dataForm.groupName,
                loopNum: this.dataForm.loopNum,
                userNum: this.dataForm.userNum,
                eventMin: this.dataForm.eventMin,
                eventMax: this.dataForm.eventMax,
                timeMin: this.dataForm.timeMin,
                timeMax: this.dataForm.timeMax,
                alias: this.dataForm.alias,
              }),
            }).then(({
              data
            }) => {
              if (data && data.code === 0) {
                loadingObj.close();

                this.$message({
                  message: "操作成功",
                  type: "success",
                  duration: 1500,
                  onClose: () => {
                    this.visible = false;
                    this.$emit("refreshDataList");
                  },
                });
              } else {
                loadingObj.close();
                this.$message.error(data.msg);
              }
            });
          }
        });
      },
    },
  };
</script>
