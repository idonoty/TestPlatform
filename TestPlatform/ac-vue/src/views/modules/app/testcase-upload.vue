<template>
<el-dialog :visible.sync="dialogVisible">
    <el-upload class="upload-demo" action="" :before-upload="uploadFile" multiple :limit="1" :on-exceed="handleExceed" :file-list="fileList">
        <el-button size="small" type="primary">点击上传</el-button>
        <div slot="tip" class="el-upload__tip">只能上传xlsx文件，且不超过500kb</div>
    </el-upload>
</el-dialog>
</template>

<script>
export default {
    data() {
        return {
            dialogVisible: false,
            fileList: []
        };
    },
    methods: {
        init() {
            this.dialogVisible = true;
        },
        handleExceed(files, fileList) {
            this.$message.warning(`当前限制选择 1 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
        },
        uploadFile(file) {
            let fd = new FormData()
            fd.append('file', file)
            console.log(fd)
            this.$http({
                url: this.$http.adornUrl(`/app/testcase/upload`),
                method: 'post',
                data: fd
            }).then(({
                data
            }) => {
                if (data && data.code === 0) {
                    this.$message({
                        message: '上传操作成功！',
                        type: 'success',
                        duration: 1000,
                        onClose: () => {
                            this.dialogVisible = false
                            this.$emit('refreshDataList')
                        }
                    });
                } else {
                    this.$message.error(data.msg);
                }
                this.fileList = []
            })
        }
    }
}
</script>
