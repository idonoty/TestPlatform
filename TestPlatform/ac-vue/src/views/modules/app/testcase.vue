<template>
<div class="mod-config">
    <el-form :model="dataForm" @keyup.enter.native="getDataList()" size="mini" :inline="true">
        <el-row :gutter="1" width=700px>
            <el-col :span="7">
                <el-form-item label="项目" prop="projectName">
                    <el-select v-model="dataForm.projectName" clearable filterable @change="changeProject" placeholder="请选择项目">
                        <el-option v-for="item in projectList" :key="item" :label="item" :value="item">
                        </el-option>
                    </el-select>
                </el-form-item>
            </el-col>
            <el-col :span="7">
                <el-form-item label="模块" prop="moduleName">
                    <el-select v-model="dataForm.moduleName" clearable filterable placeholder="请选择项目后再选模块">
                        <el-option v-for="item in moduleList" :key="item" :label="item" :value="item">
                        </el-option>
                    </el-select>
                </el-form-item>
            </el-col>
            <el-col :span="6">
                <el-form-item label="版本" prop="version">
                    <!-- <el-input v-model="dataForm.version" placeholder="请输入版本内容如: V3.1.1"></el-input> -->
                <el-select v-model="dataForm.version" clearable filterable placeholder="请择项目后再选版本">
                    <el-option v-for="item in versionList" :key="item" :label="item" :value="item">
                    </el-option>
                </el-select>
                </el-form-item>
            </el-col>
        </el-row>
    </el-form>
    <el-form>
        <el-form-item>
            <el-col :offset="8" :span="16">
                <div style="float:right;">
                    <el-button size="mini" @click="getDataList()">查询</el-button>
                    <el-button v-if="isAuth('app:testcase:save')" size="mini" type="primary" @click="addOrUpdateHandle()">新增</el-button>
                    <el-button v-if="isAuth('app:testcase:delete')" size="mini" type="danger" @click="deleteHandle()" :disabled="dataListSelections.length <= 0">批量删除</el-button>
                    <el-button  size="mini" type="danger" @click="runTestcaseHandle()" :disabled="dataListSelections.length <= 0 || dataListSelections.length > 50"
                    >批量执行(max50)</el-button>
                    <el-button v-if="isAuth('app:testcase:downloadtemplate')" size="mini" type="primary" @click="downloadTemplateHandle()">下载模板</el-button>
                    <el-button v-if="isAuth('app:testcase:upload')" size="mini" type="primary" @click="uploadHandle()">批量导入</el-button>
                    <el-button v-if="isAuth('app:testcase:downloadtestcases')" size="mini" type="primary" @click="downloadTestcasesHandle()">导出用例</el-button>
                </div>
            </el-col>
        </el-form-item>
    </el-form>
    <el-table :data="dataList" size="mini" border v-loading="dataListLoading" @selection-change="selectionChangeHandle" style="width: 100%;">
        <el-table-column type="selection" header-align="center" align="center" width="50">
        </el-table-column>
        <el-table-column prop="testcaseId" header-align="center" align="center" show-overflow-tooltip label="id">
        </el-table-column>
        <el-table-column prop="testcaseCode" header-align="center" align="center" show-overflow-tooltip label="编号">
        </el-table-column>
        <el-table-column prop="projectName" header-align="center" align="center" show-overflow-tooltip label="项目">
        </el-table-column>
        <el-table-column prop="moduleName" header-align="center" align="center" show-overflow-tooltip label="模块">
        </el-table-column>
        <el-table-column prop="version" header-align="center" align="center" show-overflow-tooltip label="版本">
        </el-table-column>
        <el-table-column prop="uri" header-align="center" align="center" show-overflow-tooltip label="URI">
        </el-table-column>
        <el-table-column prop="method" header-align="center" align="center" show-overflow-tooltip label="方法">
        </el-table-column>
        <el-table-column prop="data" header-align="center" align="center" show-overflow-tooltip label="数据">
        </el-table-column>
        <el-table-column prop="expectedResult" header-align="center" align="center" show-overflow-tooltip label="expected result">
        </el-table-column>
        <el-table-column prop="description" header-align="center" align="center" show-overflow-tooltip label="description">
        </el-table-column>
        <el-table-column fixed="right" header-align="center" align="center" width="150" label="操作">
            <template slot-scope="scope">
                <el-button type="text" size="small" @click="addOrUpdateHandle(scope.row.testcaseId)">修改</el-button>
                <el-button type="text" size="small" @click="deleteHandle(scope.row.testcaseId)">删除</el-button>
                <el-button type="text" size="small" @click="runTestcaseHandle(scope.row.testcaseId)">执行</el-button>
            </template>
        </el-table-column>
    </el-table>
    <el-pagination @size-change="sizeChangeHandle" @current-change="currentChangeHandle" :current-page="pageIndex" :page-sizes="[2, 10, 20, 50, 100]" :page-size="pageSize" :total="totalPage" layout="total, sizes, prev, pager, next, jumper">
    </el-pagination>
    <!-- 弹窗, 新增 / 修改 -->
    <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getDataList"></add-or-update>
    <run-testcase v-if="runTestcaseVisible" ref="runTestcase" @refreshDataList="getDataList"></run-testcase>
    <upload v-if="uploadVisible" ref="upload" @refreshDataList="getDataList"></upload>
</div>
</template>

<script>
import AddOrUpdate from './testcase-add-or-update'
import RunTestcase from './testplan-add-testcase'
import upload from './testcase-upload'
export default {
    data() {
        return {
            dataForm: {
                projectName: '',
                moduleName: '',
                version: ''
            },
            dataList: [],
            projectList: '',
            moduleList: '',
            versionList:'',
            pageIndex: 1,
            pageSize: 20,
            totalPage: 0,
            dataListLoading: false,
            dataListSelections: [],
            addOrUpdateVisible: false,
            runTestcaseVisible: false,
            uploadVisible: false
        }
    },
    components: {
        AddOrUpdate,
        RunTestcase,
        upload
    },
    activated() {
        this.getDataList()
        this.getProjectlist()
    },
    methods: {
        getProjectlist() {
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
                    this.moduleList = data.list
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
                    this.versionList = data.list
                }
            })
        },
        // 获取数据列表
        getDataList() {
            this.pageIndex = 1
            this.refreshDataList()
        },
        refreshDataList() {
            this.dataListLoading = true
            this.$http({
                url: this.$http.adornUrl('/app/testcase/list'),
                method: 'get',
                params: this.$http.adornParams({
                    'page': this.pageIndex,
                    'limit': this.pageSize,
                    'projectName': this.dataForm.projectName,
                    'moduleName': this.dataForm.moduleName,
                    'version': this.dataForm.version
                })
            }).then(({
                data
            }) => {
                if (data && data.code === 0) {
                    this.dataList = data.page.list
                    this.totalPage = data.page.totalCount
                } else {
                    this.dataList = []
                    this.totalPage = 0
                }
                this.dataListLoading = false
            })
        },
        // 每页数
        sizeChangeHandle(val) {
            this.pageSize = val
            this.pageIndex = 1
            this.refreshDataList()
        },
        // 当前页
        currentChangeHandle(val) {
            this.pageIndex = val
            this.refreshDataList()
        },
        // 多选
        selectionChangeHandle(val) {
            this.dataListSelections = val
        },
        // 新增 / 修改
        addOrUpdateHandle(id) {
            this.addOrUpdateVisible = true
            this.$nextTick(() => {
                this.$refs.addOrUpdate.init(id)
            })
        },
        uploadHandle() {
            this.uploadVisible = true
            this.$nextTick(() => {
                this.$refs.upload.init()
            })
        },
        downloadTemplateHandle() {
            this.$http({
                    url: this.$http.adornUrl('/app/testcase/downloadtemplate'),
                    method: 'get',
                    responseType: 'arraybuffer'
                }).then(
                    function (response) {
                        this.fileDownload(response.data, "测试用例模板.xlsx");
                    }.bind(this)
                )
                .catch(
                    function (error) {
                        this.$message.error("网络请求出错");
                    }.bind(this)
                );
        },
        downloadTestcasesHandle() {
            this.$http({
                    url: this.$http.adornUrl('/app/testcase/downloadtestcases'),
                    method: 'get',
                    params: this.$http.adornParams({
                        'projectName': this.dataForm.projectName,
                        'moduleName': this.dataForm.moduleName,
                        'version': this.dataForm.version
                    }),
                    responseType: 'arraybuffer'
                }).then(
                    function (response) {
                        this.fileDownload(response.data, "测试用例.xlsx");
                    }.bind(this)
                )
                .catch(
                    function (error) {
                        this.$message.error("网络请求出错");
                    }.bind(this)
                );
        },
        fileDownload(data, fileName) {
            let blob = new Blob([data], {
                type: "application/octet-stream"
            });
            let filename = fileName || new Date().getTime() + ".xlsx";
            if (typeof window.navigator.msSaveBlob !== "undefined") {
                window.navigator.msSaveBlob(blob, filename);
            } else {
                var blobURL = window.URL.createObjectURL(blob);
                var tempLink = document.createElement("a");
                tempLink.style.display = "none";
                tempLink.href = blobURL;
                tempLink.setAttribute("download", filename);
                if (typeof tempLink.download === "undefined") {
                    tempLink.setAttribute("target", "_blank");
                }
                document.body.appendChild(tempLink);
                tempLink.click();
                document.body.removeChild(tempLink);
                window.URL.revokeObjectURL(blobURL);
            }
        },
        // 删除
        deleteHandle(id) {
            var ids = id ? [id] : this.dataListSelections.map(item => {
                return item.testcaseId
            })
            this.$confirm(`确定对[id=${ids.join(',')}]进行[${id ? '删除' : '批量删除'}]操作?`, '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                this.$http({
                    url: this.$http.adornUrl('/app/testcase/delete'),
                    method: 'post',
                    data: this.$http.adornData(ids, false)
                }).then(({
                    data
                }) => {
                    if (data && data.code === 0) {
                        this.$message({
                            message: '操作成功',
                            type: 'success',
                            duration: 1500,
                            onClose: () => {
                                this.getDataList()
                            }
                        })
                    } else {
                        this.$message.error(data.msg)
                    }
                })
            })
        },
        runTestcaseHandle(id) {
            this.runTestcaseVisible = true
            var ids = id ? [id] : this.dataListSelections.map(item => {
                return item.testcaseId
            })
            this.$nextTick(() => {
                this.$refs.runTestcase.init(ids)
            })
        }
    }
}
</script>
