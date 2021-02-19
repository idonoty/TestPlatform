<template>
  <div class="mod-config">
    <el-form
      :model="dataForm"
      @keyup.enter.native="getDataList()"
      size="mini"
      :inline="true"
    >
      <el-row :gutter="1">
        <el-col :span="4">
          <el-form-item label="测试计划" prop="testplanName">
            <el-input
              v-model="dataForm.testplanName"
              placeholder="测试计划名称"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="4">
          <el-form-item label="项目" prop="projectName">
            <el-select
              v-model="dataForm.projectName"
              clearable
              filterable
              @change="changeProject"
              placeholder="请选择"
            >
              <el-option
                v-for="item in projectList"
                :key="item.projectName"
                :label="item.projectName"
                :value="item.projectName"
              >
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="4">
          <el-form-item label="模块" prop="moduleName">
            <el-select
              v-model="dataForm.moduleName"
              clearable
              filterable
              placeholder="请选择"
            >
              <el-option
                v-for="item in moduleList"
                :key="item.moduleName"
                :label="item.moduleName"
                :value="item.moduleName"
              >
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="4">
          <el-form-item label="结果" prop="result">
            <el-select
              v-model="dataForm.result"
              clearable
              filterable
              placeholder="请选择"
            >
              <el-option
                v-for="item in resultList"
                :key="item"
                :label="item"
                :value="item"
              >
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="4">
          <el-form-item>
            <el-button size="mini" @click="getDataList()">查询</el-button>
            <el-button
              v-if="isAuth('app:testcaseresult:delete')"
              size="mini"
              type="danger"
              @click="deleteHandle()"
              :disabled="dataListSelections.length <= 0"
              >批量删除</el-button
            >
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <el-table
      :data="dataList"
      border
      v-loading="dataListLoading"
      @selection-change="selectionChangeHandle"
      style="width: 100%"
    >
      <el-table-column
        type="selection"
        header-align="center"
        align="center"
        width="40"
      >
      </el-table-column>
      <el-table-column
        prop="testcaseId"
        header-align="center"
        align="center"
        label="用例ID"
      >
      </el-table-column>
      <el-table-column
        prop="testcaseResultId"
        header-align="center"
        align="center"
        label="结果ID"
      >
      </el-table-column>
      <el-table-column
        prop="testplanName"
        header-align="center"
        align="center"
        label="计划名称"
      >
      </el-table-column>
      <el-table-column
        prop="projectName"
        header-align="center"
        align="center"
        label="项目"
      >
      </el-table-column>
      <el-table-column
        prop="moduleName"
        header-align="center"
        align="center"
        label="模块"
      >
      </el-table-column>
      <el-table-column
        prop="uri"
        header-align="center"
        align="center"
        show-overflow-tooltip
        label="路径"
      >
      </el-table-column>
      <el-table-column
        prop="method"
        header-align="center"
        align="center"
        label="方式"
      >
      </el-table-column>
      <el-table-column
        prop="data"
        header-align="center"
        align="center"
        show-overflow-tooltip
        label="数据"
      >
      </el-table-column>
      <el-table-column
        prop="expectedResult"
        header-align="center"
        align="center"
        show-overflow-tooltip
        label="期望结果"
      >
      </el-table-column>
      <el-table-column
        prop="actualResult"
        header-align="center"
        align="center"
        show-overflow-tooltip
        label="实际结果"
      >
      </el-table-column>
      <el-table-column
        prop="description"
        header-align="center"
        align="center"
        show-overflow-tooltip
        label="描述"
      >
      </el-table-column>
      <el-table-column
        prop="result"
        header-align="center"
        align="center"
        label="结果"
      >
        <template slot-scope="scope">
          <el-tag v-if="scope.row.result === '失败'" size="small" type="danger"
            >失败</el-tag
          >
          <el-tag v-else-if="scope.row.result === '成功'" size="small"
            >成功</el-tag
          >
          <el-tag v-else size="small" type="danger">未知状态</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="createTime"
        header-align="center"
        align="center"
        label="执行时间"
      >
      </el-table-column>
      <el-table-column
        fixed="right"
        header-align="center"
        align="center"
        width="50"
        label="操作"
      >
       <template slot-scope="scope">
          <el-button
            type="text"
            size="small"
            @click="deleteHandle(scope.row.testcaseResultId)"
            >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      @size-change="sizeChangeHandle"
      @current-change="currentChangeHandle"
      :current-page="pageIndex"
      :page-sizes="[10, 20, 50, 100]"
      :page-size="pageSize"
      :total="totalPage"
      layout="total, sizes, prev, pager, next, jumper"
    >
    </el-pagination>
  </div>
</template>

<script>
export default {
  data() {
    return {
      dataForm: {
        projectName: "",
        moduleName: "",
        testplanName: "",
        result: "",
      },
      dataList: [],
      projectList: [],
      moduleList: [],
      resultList: ["成功", "失败"],
      pageIndex: 1,
      pageSize: 10,
      totalPage: 0,
      dataListLoading: false,
      dataListSelections: [],
      addOrUpdateVisible: false,
    };
  },
  activated() {
    this.getDataList();
    this.getProjectlist();
  },
  methods: {
    getProjectlist() {
      this.$http({
        url: this.$http.adornUrl(`/app/pm/queryprojects`),
        method: "get",
        params: this.$http.adornParams(),
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.projectList = data.list;
        }
      });
    },
    changeProject() {
      this.$http({
        url: this.$http.adornUrl(`/app/pm/querymodules`),
        method: "get",
        params: {
          projectName: `${this.dataForm.projectName}`,
        },
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.moduleList = data.list;
        }
      });
    },
    // 获取数据列表
    getDataList() {
      this.pageIndex = 1;
      this.refreshDataList();
    },
    refreshDataList() {
      this.dataListLoading = true;
      this.$http({
        url: this.$http.adornUrl("/app/testcaseresult/list"),
        method: "get",
        params: this.$http.adornParams({
          page: this.pageIndex,
          limit: this.pageSize,
          projectName: this.dataForm.projectName,
          moduleName: this.dataForm.moduleName,
          testplanName: this.dataForm.testplanName,
          result: this.dataForm.result,
        }),
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.dataList = data.page.list;
          this.totalPage = data.page.totalCount;
        } else {
          this.dataList = [];
          this.totalPage = 0;
        }
        this.dataListLoading = false;
      });
    },
    // 每页数
    sizeChangeHandle(val) {
      this.pageSize = val;
      this.pageIndex = 1;
      this.refreshDataList();
    },
    // 当前页
    currentChangeHandle(val) {
      this.pageIndex = val;
      this.refreshDataList();
    },
    // 多选
    selectionChangeHandle(val) {
      this.dataListSelections = val;
    },
    // 新增 / 修改
    addOrUpdateHandle(id) {
      this.addOrUpdateVisible = true;
      this.$nextTick(() => {
        this.$refs.addOrUpdate.init(id);
      });
    },
    // 删除
    deleteHandle(id) {
      var ids = id
        ? [id]
        : this.dataListSelections.map((item) => {
            return item.testcaseResultId;
          });
      this.$confirm(
        `确定对[id=${ids.join(",")}]进行[${id ? "删除" : "批量删除"}]操作?`,
        "提示",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      ).then(() => {
        this.$http({
          url: this.$http.adornUrl("/app/testcaseresult/delete"),
          method: "post",
          data: this.$http.adornData(ids, false),
        }).then(({ data }) => {
          if (data && data.code === 0) {
            this.$message({
              message: "操作成功",
              type: "success",
              duration: 1500,
              onClose: () => {
                this.getDataList();
              },
            });
          } else {
            this.$message.error(data.msg);
          }
        });
      });
    },
  },
};
</script>
