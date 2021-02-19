<template>
<el-dialog :title="!dataForm.id ? '新增' : '修改'" v-loading="dataListLoading" :close-on-click-modal="false" :visible.sync="visible" width="500px" inline-message="true" status-icon="true">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
        <el-form-item label="分组名" prop="groupName">
            <el-input style="width:190px;" v-model="dataForm.groupName" placeholder=""></el-input>
        </el-form-item>
        <el-form-item label="用户文件" prop="profileName">
            <el-upload class="upload-demo" action="" :auto-upload="false" :on-change="changeProfileFile" multiple :limit="1" :file-list="profileFileList">
                <el-button size="small" type="primary">上传</el-button>
            </el-upload>
        </el-form-item>
        <el-form-item label="事件文件" prop="eventName">
            <el-upload class="upload-demo" action="" :auto-upload="false" :on-change="changeEventFile" multiple :limit="1" :file-list="eventFileList">
                <el-button size="small" type="primary">上传</el-button>
            </el-upload>
        </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="cancel()">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </span>
</el-dialog>
</template>

<script>
export default {
    data() {
        return {
            visible: false,
            dataListLoading: false,
            fd: new FormData(),
            profileFileList: [],
            eventFileList: [],
            dataForm: {
                profileLogId: 0,
                groupName: 'a',
                profileName: '',
                eventName: '',
                profileFile: null,
                eventFile: null
            },
            dataRule: {
                groupName: [{
                    required: true,
                    message: '不能为空',
                    trigger: 'blur'
                }]
            }
        }
    },
    methods: {
        init(id) {
            this.dataForm.profileLogId = id || 0
            this.visible = true
            this.$nextTick(() => {
                this.$refs['dataForm'].resetFields()
                if (this.dataForm.profileLogId) {
                    this.$http({
                        url: this.$http.adornUrl(`/app/profilelog/info/${this.dataForm.profileLogId}`),
                        method: 'get',
                        params: this.$http.adornParams()
                    }).then(({
                        data
                    }) => {
                        if (data && data.code === 0) {
                            this.dataForm.groupName = data.profielog.groupName
                            this.dataForm.profileName = data.profielog.profileName
                            this.dataForm.eventName = data.profielog.eventName
                        }
                    })
                }
            })
        },
        // 表单提交
        dataFormSubmit() {
            this.$refs['dataForm'].validate((valid) => {
                if (valid) {
                    this.fd.append('groupName', this.dataForm.groupName)
                    this.fd.append('profileName', this.dataForm.profileName)
                    this.fd.append('eventName', this.dataForm.eventName)

                    this.dataListLoading = true

                    this.$http({
                        url: this.$http.adornUrl(`/app/profilelog/${!this.dataForm.profileLogId ? 'save' : 'update'}`),
                        method: 'post',
                        data: this.fd
                    }).then(({
                        data
                    }) => {
                        if (data && data.code === 0) {
                            this.$message({
                                message: '操作成功',
                                type: 'success',
                                duration: 1000,
                                onClose: () => {
                                    this.visible = false
                                    this.$emit('refreshDataList')
                                }
                            })
                            this.dataForm.groupName = ''
                            this.dataForm.profileName = ''
                            this.dataForm.eventName = ''
                            this.dataForm.profileFile = null
                            this.dataForm.eventFile = null
                            this.fd = new FormData()
                            this.profileFileList = []
                            this.eventFileList = []
                            this.dataListLoading = false
                        } else {
                            this.dataListLoading = false
                            this.$message.error(data.msg)
                        }
                    })
                }
            })
        },
        changeProfileFile(file) {
            this.fd.set('profileFile', file.raw)
            this.dataForm.profileName = file.name
        },
        changeEventFile(file) {
            this.fd.set('eventFile', file.raw)
            this.dataForm.eventName = file.name
        },
        cancel() {
            this.visible = false
            this.profileFileList = []
            this.eventFileList = []
            this.dataForm.groupName = ''
            this.dataForm.profileName = ''
            this.dataForm.eventName = ''
            this.dataForm.profileFile = null
            this.dataForm.eventFile = null
            this.fd = new FormData()
        }
    }
}
</script>
