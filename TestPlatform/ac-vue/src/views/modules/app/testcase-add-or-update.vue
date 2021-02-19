<template>
<el-dialog :title="!dataForm.id ? '新增' : '修改'" :close-on-click-modal="false" :visible.sync="visible" center width="600px">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px" size="mini" inline-message>
        <el-form-item label="用例编号" prop="testcaseCode">
            <el-input v-model="dataForm.testcaseCode" placeholder="测试用例编号" style='width: 450px'></el-input>
        </el-form-item>
        <el-row :gutter="100">
            <el-col :span="10">
                <el-form-item label="项目" prop="projectName">
                    <el-select v-model="dataForm.projectName" @change="changeProject" placeholder="请选择" style='width: 180px'>
                        <el-option v-for="item in projectList" :key="item" :label="item" :value="item">
                        </el-option>
                    </el-select>
                </el-form-item>
            </el-col>
            <el-col :span="10">
                <el-form-item label="模块" prop="moduleName">
                    <el-select v-model="dataForm.moduleName" placeholder="请选择" style='width: 180px'>
                        <el-option v-for="item in moduleList" :key="item" :label="item" :value="item">
                        </el-option>
                    </el-select>
                </el-form-item>
            </el-col>
        </el-row>
        <el-form-item label="URI" prop="uri">
                    <el-input v-model="dataForm.uri" placeholder="URI" style='width: 400px'></el-input>
                </el-form-item>
        <el-row :gutter="100">
            <el-col :span="10">
                <el-form-item label="版本" prop="version">
                    <el-input v-model="dataForm.version" placeholder="版本" style='width: 180px'></el-input>
                </el-form-item>
            </el-col>
            <el-col :span="10">
                <el-form-item label="请求方式" prop="method">
                    <el-select v-model="dataForm.method" placeholder="请选择" style='width: 180px'>
                        <el-option v-for="item in methodOptions" :key="item" :label="item" :value="item">
                        </el-option>
                    </el-select>
                </el-form-item>
            </el-col>
        </el-row>
        <el-form-item label="请求内容" prop="data">
            <el-input v-model="dataForm.data" type="textarea" placeholder="请求内容" style='width: 450px'></el-input>
        </el-form-item>
        <el-form-item label="期望结果" prop="expectedResult">
            <el-input v-model="dataForm.expectedResult" type="textarea" placeholder="期望结果" style='width: 450px'></el-input>
        </el-form-item>
        <el-form-item label="描述" prop="description">
            <el-input v-model="dataForm.description" type="textarea" placeholder="描述" style='width: 450px'></el-input>
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
            // methodOptions: [{
            //     label: 'POST',
            //     value: 'POST'
            // }, {
            //     label: 'GET',
            //     value: 'GET'
            // }],
            methodOptions: ['POST', 'GET'],
            dataForm: {
                testcaseId: 0,
                testcaseCode: '',
                projectName: '',
                moduleName: '',
                uri: '',
                method: '',
                data: '',
                version: '',
                expectedResult: '',
                description: ''
            },
            dataRule: {
                testcaseCode: [{
                    required: true,
                    message: '不能为空',
                    trigger: 'blur'
                }],
                projectName: [{
                    required: true,
                    message: '不能为空',
                    trigger: 'blur'
                }],
                moduleName: [{
                    required: true,
                    message: '不能为空',
                    trigger: 'blur'
                }],
                uri: [{
                    required: true,
                    message: '不能为空',
                    trigger: 'blur'
                }],
                method: [{
                    required: true,
                    message: '不能为空',
                    trigger: 'blur'
                }],
                expectedResult: [{
                    required: true,
                    message: '不能为空',
                    trigger: 'blur'
                }]
            }
        }
    },
    methods: {
        init(id) {
            this.projectList = [];
            this.moduleList = [];
            this.dataForm.testcaseId = id || 0
            this.visible = true
            this.$http({
                url: this.$http.adornUrl(`/app/testcase/queryprojects`),
                method: 'get',
                params: this.$http.adornParams()
            }).then(({
                data
            }) => {
                if (data && data.code === 0) {
                    console.log(data)
                    console.log(data.list)
                    this.projectList = data.list
                }
            })
            this.$nextTick(() => {
                this.$refs['dataForm'].resetFields()
                if (this.dataForm.testcaseId) {
                    this.$http({
                        url: this.$http.adornUrl(`/app/testcase/info/${this.dataForm.testcaseId}`),
                        method: 'get',
                        params: this.$http.adornParams()
                    }).then(({
                        data
                    }) => {
                        if (data && data.code === 0) {
                            this.dataForm.testcaseCode = data.testcase.testcaseCode
                            this.dataForm.projectName = data.testcase.projectName
                            this.dataForm.moduleName = data.testcase.moduleName
                            this.dataForm.uri = data.testcase.uri
                            this.dataForm.method = data.testcase.method
                            this.dataForm.data = data.testcase.data
                            this.dataForm.version = data.testcase.version
                            this.dataForm.expectedResult = data.testcase.expectedResult
                            this.dataForm.description = data.testcase.description

                            console.log(data)
                            console.log(data.testcase.moduleName)
                            console.log(this.dataForm.moduleName)
                        }
                    })
                }
            })
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
                    console.log(data)
                    this.moduleList = data.list
                }
            })
        },
        // 表单提交
        dataFormSubmit() {
            this.$refs['dataForm'].validate((valid) => {
                if (valid) {
                    this.$http({
                        url: this.$http.adornUrl(`/app/testcase/${!this.dataForm.testcaseId ? 'save' : 'update'}`),
                        method: 'post',
                        data: this.$http.adornData({
                            'testcaseId': this.dataForm.testcaseId || undefined,
                            'testcaseCode': this.dataForm.testcaseCode,
                            'projectName': this.dataForm.projectName,
                            'moduleName': this.dataForm.moduleName,
                            'uri': this.dataForm.uri,
                            'method': this.dataForm.method,
                            'data': this.dataForm.data,
                            'version': this.dataForm.version,
                            'expectedResult': this.dataForm.expectedResult,
                            'description': this.dataForm.description
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
