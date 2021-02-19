<template>
  <el-dialog :title="!dataForm.id ? '修改' : '修改'" :close-on-click-modal="false" :visible.sync="visible" width="626px">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="120px"
      inline-message="true" status-icon="true">
      <el-form-item label="计划名称" prop="testplanName">
        <el-input v-model="dataForm.testplanName" placeholder="计划名称"></el-input>
      </el-form-item>
      <el-form-item label="请求地址" prop="endpoint" style="position: relative">
        <el-input v-model="dataForm.endpoint" placeholder="url末尾不能有斜杠: /"></el-input>
        <el-select v-model="dataForm.endpoint" filterable allow-create placeholder="url末尾不能有斜杠: /" style="width: 47px; position: absolute; right: 0px">
          <el-option v-for="item in dataForm.endpointList" :key="item" :label="item.url1" :value="item.url1"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="appkey" prop="cookie">
        <el-input v-model="dataForm.cookie" placeholder="方舟模块填appkey , openapi填appkey;token"></el-input>
      </el-form-item>
      <el-form-item label="执行状态" prop="status" >
        <el-select v-model="dataForm.status" placeholder="请选择" style="width: 466px">
          <el-option v-for="item in dataForm.statusList" :key="item.value" :label="item.label" :value="item.value">
            <span style="float: left">{{ item.label }}</span>
            <span style="float: right; color: #8492a6; font-size: 13px">{{ item.value }}</span>
          </el-option>
        </el-select>
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
        projectList: [],
        moduleList: [],
        versionList: [],
        dataForm: {
          testplanId: 0,
          testplanName: '',
          projectName: '',
          moduleName: '',
          version: '',
          endpoint: '',
          cookie: '',
          status: 10,
          createTime: this.getNowDay() + " " + this.getNowDate(),
          endpointList: [{
              url1: "https://arkpaastest.analysys.cn:4005"
            },
            {
              url1: "http://117.50.5.132:4005"
            },
            {
              url1: "http://192.168.220.130:4005"
            },
            {
              url1: "http://192.168.220.111:4005"
            },
            {
              url1: "http://192.168.8.111:4005"
            },
          ],
          statusList: [{
            value: '10',
            label: '等待执行'
          }, {
            value: '20',
            label: '删除'
          }, {
            value: '21',
            label: '执行中'
          }, {
            value: '31',
            label: '执行完成'
          }, {
            value: '32',
            label: '错误退出'
          }],
          // onlyCheckCode: 1
        },
        dataRule: {
          testplanName: [{
            required: true,
            message: '测试计划名称不能为空',
            trigger: 'blur'
          }],
          status: [{
            required: true,
            message: '状态不能为空',
            trigger: 'blur'
          }],
          endpoint: [{
            required: true,
            message: 'endpoint不能为空',
            trigger: 'blur'
          }],
          cookie: [{
            required: true,
            message: 'appkey不能为空',
            trigger: 'blur'
          }]
        }
      }
    },
    methods: {
      init(id) {
        this.projectList = [];
        this.moduleList = [];
        this.versionList = [];
        this.dataForm.testplanId = id || 0
        this.visible = true
        this.$http({
          url: this.$http.adornUrl(`/app/testcase/queryprojects`),
          method: 'get',
          params: this.$http.adornParams()
        }).then(({
          data
        }) => {
          if (data && data.code === 0) {
            this.projectList = data.list
          }
        })

        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.testplanId) {
            this.$http({
              url: this.$http.adornUrl(`/app/testplan/info/${this.dataForm.testplanId}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({
              data
            }) => {
              if (data && data.code === 0) {
                this.dataForm.testplanId = data.testplan.testplanId
                this.dataForm.testplanName = data.testplan.testplanName
                this.dataForm.projectName = data.testplan.projectName
                this.dataForm.moduleName = data.testplan.moduleName
                this.dataForm.version = data.testplan.version
                this.dataForm.status = data.testplan.status
                this.dataForm.endpoint = data.testplan.endpoint
                this.dataForm.cookie = data.testplan.cookie
                // this.dataForm.onlyCheckCode = data.testplan.onlyCheckCode
              }
            })
          }
        })
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
      changeProject() {
        this.$http({
            url: this.$http.adornUrl(`/app/testcase/querymodules`),
            method: 'get',
            params: {
              'projectName': `${this.dataForm.projectName}`
            }
          }).then(({
            data
          }) => {
            if (data && data.code === 0) {
              this.moduleList = data.list;
            }
          }),
          this.$http({
            url: this.$http.adornUrl(`/app/testcase/queryversions`),
            method: 'get',
            params: {
              'projectName': `${this.dataForm.projectName}`
            }
          }).then(({
            data
          }) => {
            if (data && data.code === 0) {
              this.versionList = data.list;
            }
          })

      },
      // 表单提交
      dataFormSubmit() {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            let obj = {}
            var modulenames = [].concat(this.dataForm.moduleName);
            var dataList = []
            //注释for循环，将多个模块统一放到同一个集合，仅提交一条测试计划

            obj.moduleName = modulenames[0];
            console.log(obj.moduleName)
            for (var i = 1; i < modulenames.length; i++) {
              obj.moduleName = obj.moduleName + "," + modulenames[i];
            }
            obj.testplanId = this.dataForm.testplanId
            obj.testplanName = this.dataForm.testplanName
            obj.projectName = this.dataForm.projectName
            //obj.moduleName = modulenames
            obj.version = this.dataForm.version
            obj.status = this.dataForm.status
            obj.endpoint = this.dataForm.endpoint
            obj.cookie = this.dataForm.cookie
            obj.createTime = this.dataForm.createTime
            // obj.onlyCheckCode = this.dataForm.onlyCheckCode
            dataList.push(obj)
            // }
            console.log(dataList)
            this.$http({
              url: this.$http.adornUrl(`/app/testplan/update`),
              method: 'post',
              data: this.$http.adornData({
                'dataList': dataList

              })
            }).then(({
              data
            }) => {
              if (data && data.code === 0) {
                this.$message({
                  message: '操作成功',
                  type: 'success',
                  duration: 1500,
                  onClose: () => {
                    this.visible = false
                    this.$emit('refreshDataList')
                  }
                })
              } else {
                this.$message.error(data.msg)
              }
            })
          }
        })
      }
    }
  }
</script>
