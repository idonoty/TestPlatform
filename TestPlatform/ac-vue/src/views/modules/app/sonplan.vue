<template>
  <div class="mod-config">
    <el-form :inline="true" :model="dataForm" @keyup.enter.native="getFlteredData()" size="mini">
      <el-form-item label="计划名称">
        <el-input v-model="dataForm.testplan_name" placeholder="计划名称" clearable></el-input>
      </el-form-item>
      <el-form-item label="项目">
        <el-input v-model="dataForm.project_name" placeholder="项目" clearable></el-input>
      </el-form-item>
      <el-form-item label="模块">
        <el-input v-model="dataForm.module_name" placeholder="模块" clearable></el-input>
      </el-form-item>
      <el-form-item label="版本">
        <el-input v-model="dataForm.version" placeholder="版本" clearable></el-input>
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="dataForm.status" placeholder="请选择">
          <el-option v-for="item in selectOption" :key="item.value" :label="item.label" :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item>
        <el-button @click="getFlteredData()">查询</el-button>
        <el-button v-if="isAuth('app:testplan:save')" type="primary" @click="addOrUpdateHandle()">新增</el-button>
        <el-button v-if="isAuth('app:testplan:delete')" type="danger" @click="deleteHandle()" :disabled="dataListSelections.length <= 0">批量删除</el-button>
      </el-form-item>
    </el-form>

    <el-table :data="dataList" border v-loading="dataListLoading" @selection-change="selectionChangeHandle"  style="width: 100%">
      <el-table-column type="selection" header-align="center" align="center" width="50"></el-table-column>
<!--      <el-table-column prop="testplanName" header-align="center" align="center" show-overflow-tooltip label="计划名称">
      </el-table-column> -->
      <el-table-column header-align="center" align="center" label="计划名称">
        <template slot-scope="scope">
          <el-button style="font-size: 14px;color: #606266;" type="text" size="small" @click="putalert()"  >{{scope.row.testplanName}}</el-button>
        </template>
      </el-table-column>
      <el-table-column prop="projectName" header-align="center" align="center" show-overflow-tooltip label="项目"></el-table-column>
      <el-table-column prop="moduleName" header-align="center" align="center" show-overflow-tooltip label="模块"></el-table-column>
      <el-table-column prop="version" header-align="center" align="center" show-overflow-tooltip sortable label="用例版本"></el-table-column>
      <el-table-column prop="endpoint" header-align="center" align="center" show-overflow-tooltip label="url"></el-table-column>
      <el-table-column prop="testcaseId" header-align="center" align="center" show-overflow-tooltip label="用例id"></el-table-column>
      <el-table-column prop="cookie" header-align="center" align="center" show-overflow-tooltip label="cookie"></el-table-column>
      <el-table-column fixed="right" header-align="center" align="center" width="150" label="操作">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="addOrUpdateHandle(scope.row.testplanId)">修改</el-button>
          <el-button type="text" size="small" @click="deleteHandle(scope.row.testplanId)">删除</el-button>
        </template>
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
      <!-- <el-table-column prop="onlyCheckCode" header-align="center" align="center" show-overflow-tooltip label="只检查返回码">
            <template slot-scope="scope">
                <el-tag v-if="scope.row.onlyCheckCode" size="small" type="info">是</el-tag>
                <el-tag v-else size="small" type="info">否</el-tag>
            </template>
        </el-table-column> -->
      <!-- <el-table-column fixed="right" header-align="center" align="center" width="150" label="操作">
            <template slot-scope="scope">
                <el-button type="text" size="small" @click="addOrUpdateHandle(scope.row.testplanId)">修改</el-button>
                <el-button type="text" size="small" @click="deleteHandle(scope.row.testplanId)">删除</el-button>
            </template>
        </el-table-column> -->
    </el-table>
    <el-pagination @size-change="sizeChangeHandle" @current-change="currentChangeHandle" :current-page="pageIndex"
      :page-sizes="[10, 20, 50, 100]" :page-size="pageSize" :total="totalPage" layout="total, sizes, prev, pager, next, jumper">
    </el-pagination>
    <!-- 弹窗, 新增 / 修改 -->
    <add v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getDataList"></add>
  </div>
</template>

<script>
  import SonPlan from "./sonplan";
  import AddOrUpdate from "./testplan-add";
  export default {
    data() {
      return {
        dataForm: {
          testplan_name: "",
          project_name: "",
          module_name: "",
          version: "",
          status: "",
          testcaseId: "",
        },
        dataList: [],
        pageIndex: 1,
        pageSize: 20,
        totalPage: 0,
        dataListLoading: false,
        dataListSelections: [],
        addOrUpdateVisible: false,
        selectOption: [{
            value: 10,
            label: "等待执行",
          },
          {
            value: 21,
            label: "执行中",
          },
          {
            value: 31,
            label: "执行完成",
          },
        ],
      };
    },
    components: {
      AddOrUpdate,
    },
    activated() {
      this.getDataList();
    },
    methods: {
      putalert() {
        alert("警告");
      },
      // 获取数据列表
      getDataList() {
        this.pageIndex = 1;
        this.refreshDataList();
      },
      refreshDataList() {
        this.dataListLoading = true;
        this.$http({
          url: this.$http.adornUrl("/app/testplan/sonlist"),
          method: "get",
          params: this.$http.adornParams({
            page: this.pageIndex,
            limit: this.pageSize,
          }),
        }).then(({
          data
        }) => {
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
      //根据dataForm是否是真值,来动态添加配置对象
      addOptionsPorp() {
        var options = {
          page: this.pageIndex,
          limit: this.pageSize
        };
        if (this.dataForm.testplan_name) {
          options["testplan_name"] = this.dataForm.testplan_name;
        }
        if (this.dataForm.project_name) {
          options["project_name"] = this.dataForm.project_name;
        }
        if (this.dataForm.module_name) {
          options["module_name"] = this.dataForm.module_name;
        }
        if (this.dataForm.version) {
          options["version"] = this.dataForm.version;
        }
        if (this.dataForm.status) {
          options["status"] = this.dataForm.status;
        }
        if (this.dataForm.testcaseId) {
          options["status"] = this.dataForm.testcaseId;
        }
        return options;
      },
      // 根据参数查询
      getFlteredData() {
        this.pageIndex = 1;
        this.dataListLoading = true;
        var options = this.addOptionsPorp();
        this.$http({
          url: this.$http.adornUrl("/app/testplan/sonlist"),
          method: "get",
          params: this.$http.adornParams(options),
        }).then(({
          data
        }) => {
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
        var ids = id ?
          [id] :
          this.dataListSelections.map((item) => {
            return item.testplanId;
          });
        this.$confirm(
          `确定对[id=${ids.join(",")}]进行[${id ? "删除" : "批量删除"}]操作?`,
          "提示", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning",
          }
        ).then(() => {
          this.$http({
            url: this.$http.adornUrl("/app/testplan/delete"),
            method: "post",
            data: this.$http.adornData(ids, false),
          }).then(({
            data
          }) => {
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
