<template>
<div class="mod-config">
    <el-form :inline="true" @keyup.enter.native="getDataList()">
        <el-form-item>
            <el-button v-if="isAuth('app:testdata:save')" type="primary" @click="addOrUpdateHandle()">新增</el-button>
        </el-form-item>
    </el-form>
    <el-table :data="dataList" border v-loading="dataListLoading" style="width: 100%;">
        <el-table-column type="index" header-align="center" align="center" label="序号" width="50">
        </el-table-column>
        <el-table-column prop="groupName" header-align="center" align="center" label="分群名称">
        </el-table-column>
        <el-table-column prop="url" header-align="center" align="center" label="url">
        </el-table-column>
        <el-table-column prop="appkey" header-align="center" align="center" label="appkey">
        </el-table-column>
        <el-table-column prop="status" header-align="center" align="center" label="状态">
            <template slot-scope="scope">
                <el-tag v-if="scope.row.status === 10" size="small" type="info">等待执行</el-tag>
                <el-tag v-else-if="scope.row.status === 21" size="small" type="danger">执行中</el-tag>
                <el-tag v-else-if="scope.row.status === 31" size="small">执行完成</el-tag>
                <el-tag v-else-if="scope.row.status === 20" size="small" type="warn">删除</el-tag>
                <el-tag v-else-if="scope.row.status === 32" size="small" type="warn">错误退出</el-tag>
                <el-tag v-else size="small">未知状态</el-tag>
            </template>
        </el-table-column>
    </el-table>
    <el-pagination @size-change="sizeChangeHandle" @current-change="currentChangeHandle" :current-page="pageIndex" :page-sizes="[10, 20, 50, 100]" :page-size="pageSize" :total="totalPage" layout="total, sizes, prev, pager, next, jumper">
    </el-pagination>
    <!-- 弹窗, 新增 / 修改 -->
    <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getDataList"></add-or-update>
</div>
</template>

<script>
import AddOrUpdate from './testdata-add-or-update'
export default {
    data() {
        return {
            dataList: [],
            pageIndex: 1,
            pageSize: 20,
            totalPage: 0,
            dataListLoading: false,
            addOrUpdateVisible: false
        }
    },
    components: {
        AddOrUpdate
    },
    activated() {
        this.getDataList()
    },
    methods: {
        // 获取数据列表
        getDataList() {
            this.pageIndex = 1
            this.refreshDataList()
        },
        refreshDataList() {
            this.dataListLoading = true
            this.$http({
                url: this.$http.adornUrl('/app/testdata/list'),
                method: 'get',
                params: this.$http.adornParams({
                    'page': this.pageIndex,
                    'limit': this.pageSize
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
        // 新增 / 修改
        addOrUpdateHandle(id) {
            this.addOrUpdateVisible = true
            this.$nextTick(() => {
                this.$refs.addOrUpdate.init(id)
            })
        }
    }
}
</script>
