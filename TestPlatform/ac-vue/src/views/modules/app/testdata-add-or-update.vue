<template>
<el-dialog :title="!dataForm.id ? '新增' : '修改'" :close-on-click-modal="false" :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px" inline-message="true" status-icon="true">
        <el-form-item label="url" prop="url">
            <el-input v-model="dataForm.url" placeholder="url"></el-input>
        </el-form-item>
        <el-form-item label="appkey" prop="appkey">
            <el-input v-model="dataForm.appkey" placeholder="appkey"></el-input>
        </el-form-item>
        <el-form-item label="分群名称" prop="groupName">
            <el-select v-model="dataForm.groupName" clearable placeholder="请选择">
                <el-option v-for="item in groupList" :key="item" :label="item" :value="item">
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
            groupList: [],
            dataForm: {
                testdataId: 0,
                url: 'https://arkpaastest.analysys.cn:4089/up',
                appkey: '',
                groupName: ''
            },
            dataRule: {
                url: [{
                    required: true,
                    message: 'url不能为空',
                    trigger: 'blur'
                }],
                appkey: [{
                    required: true,
                    message: 'appkey不能为空',
                    trigger: 'blur'
                }],
                groupName: [{
                    required: true,
                    message: '分群名称不能为空',
                    trigger: 'blur'
                }]
            }
        }
    },
    methods: {
        init(id) {
            this.dataForm.recordId = id || 0
            this.visible = true

            this.$http({
                url: this.$http.adornUrl(`/app/testdata/grouplist`),
                method: 'get'
            }).then(({
                data
            }) => {
                if (data && data.code === 0) {
                    this.groupList = data.list
                }
            })

            this.$nextTick(() => {
                this.$refs['dataForm'].resetFields()
                if (this.dataForm.recordId) {
                    this.$http({
                        url: this.$http.adornUrl(`/app/testdata/info/${this.dataForm.testdataId}`),
                        method: 'get',
                        params: this.$http.adornParams()
                    }).then(({
                        data
                    }) => {
                        if (data && data.code === 0) {
                            this.dataForm.url = data.testdata.url
                            this.dataForm.appkey = data.testdata.appkey
                            this.dataForm.groupName = data.testdata.groupName
                        }
                    })
                }
            })
        },
        // 表单提交
        dataFormSubmit() {
            this.$refs['dataForm'].validate((valid) => {
                if (valid) {
                    const loadingObj = this.$loading({
                        lock: true,
                        text: '提交中...',
                        spinner: 'el-icon-loading',
                        background: 'rgba(0, 0, 0, 0.7)'
                    });
                    this.$http({
                        url: this.$http.adornUrl(`/app/testdata/${!this.dataForm.recordId ? 'save' : 'update'}`),
                        method: 'post',
                        data: this.$http.adornData({
                            'testdataId': this.dataForm.testdataId || undefined,
                            'url': this.dataForm.url,
                            'appkey': this.dataForm.appkey,
                            'groupName': this.dataForm.groupName
                        })
                    }).then(res => {
                        let data = res.data
                        if (data && data.code === 0) {
                            loadingObj.close()
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
                            loadingObj.close()
                            this.$message.error(data.msg)
                        }
                    })
                }
            })
        }
    }
}
</script>
