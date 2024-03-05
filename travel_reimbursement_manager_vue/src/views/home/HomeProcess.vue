<template>
  <el-container>
    <!-- Main Section -->
    <el-main>
      <div class="tabs">
        <el-button @click="currentTab = 'tab1'">流程列表({{ modelCount }})</el-button>
        <el-button @click="currentTab = 'tab2'">实例列表({{ instanceCount }})</el-button>
      </div>

      <div class="content">
        <router-view></router-view>
      </div>

      <el-form :inline="true" :model="query" class="demo-form-inline">
        <div id="search">
          <el-form-item label="模型:">
            <el-input v-model="query.modelName" clearable
                      placeholder="请输入模型"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="search"
                       icon="el-icon-search">查询
            </el-button>
            <el-button type="primary" @click="showDialog = true">新增</el-button>
          </el-form-item>
        </div>
      </el-form>
      <el-dialog title="新增流程模型定义" :visible.sync="showDialog">
        <el-form :model="xmlSaveRequest">
          <el-form-item label="流程模型名称">
            <el-input v-model="xmlSaveRequest.modelName"></el-input>
          </el-form-item>
          <el-form-item label="流程模型Key">
            <el-input v-model="xmlSaveRequest.modelKey"></el-input>
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
        <el-button @click="cancelAddModel">取 消</el-button>
        <el-button type="primary" @click="saveProcessModel">保 存</el-button>
      </span>
      </el-dialog>

      <div v-if="currentTab === 'tab1'">
        <div style="position: relative; left: -540px">流程列表</div>
        <div v-if="loadingTab1"></div>
        <div v-else>
          <!--表格-->
          <el-table
              ref="reimbursementTable"
              :data="tab1Data"
              stripe
              style="width: 100%;font-size: 15px"
              height="550px"
              @select="selectOneTravelInfo"
              @select-all="selectAllTravelInfo"
              :header-cell-style="{background:'#bababe',color:'black'}"
              :border="true"
              :row-key="getRowKeys">
            <el-table-column
                prop="id"
                label="流程ID"
                width="100"
                align="center">
            </el-table-column>
            <el-table-column
                prop="modelKey"
                label="模型主键"
                width="250"
                align="center">
            </el-table-column>
            <el-table-column
                prop="modelName"
                label="模型名称"
                width="200"
                align="center">
            </el-table-column>
            <el-table-column
                prop="status"
                label="状态"
                width="90"
                align="center"
                :formatter="statusFormatter">
            </el-table-column>
            <el-table-column
                prop="userName"
                width="90"
                label="提交人"
                align="center">
            </el-table-column>
            <el-table-column
                prop="createTime"
                label="创建时间"
                width="180"
                align="center"
                :formatter="formatDate">
            </el-table-column>
            <el-table-column
                prop="updateTime"
                label="最后更新时间"
                width="180"
                align="center"
                :formatter="formatDate">
            </el-table-column>
            <el-table-column
                label="操作"
                width="200"
                align="center">
              <template slot-scope="scope">
                <el-button
                    size="small"
                    @click="viewModelXml(scope.row)">
                  查看
                </el-button>
                <el-button
                    size="small"
                    @click="editInfo(scope.row)"
                    :disabled="scope.row.status === 1">
                  编辑
                </el-button>
                <el-button
                    size="small"
                    @click="publish(scope.row)"
                    :disabled="scope.row.status === 1">
                  发布
                </el-button>
                <el-button
                    size="small"
                    style="margin-left: 0"
                    @click="delete(scope.row)"
                    :disabled="scope.row.status === 1">删除
                </el-button>
              </template>
            </el-table-column>
            <div slot="empty">
            </div>
          </el-table>

          <!--分页功能-->
          <div id="page">
            <el-pagination
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
                :current-page=query.page
                :page-sizes="[10, 20, 50, 100]"
                :page-size=query.limit
                layout="total, sizes, prev, pager, next, jumper"
                :total=total
                :background="true">
            </el-pagination>
          </div>
        </div>
      </div>
      <div v-if="currentTab === 'tab2'">
        <div style="position: relative; left: -540px">实例列表</div>
        <div v-if="loadingTab2"></div>
        <div v-else>
          <!--表格-->
          <el-table
              :data="tab2Data"
              stripe
              style="width: 100%;font-size: 15px"
              height="550px"
              @select="selectOneTravelInfo"
              @select-all="selectAllTravelInfo"
              :header-cell-style="{background:'#bababe',color:'black'}"
              :border="true"
              :row-key="getRowKeys">
            <el-table-column
                prop="processInstanceId"
                label="实例id"
                width="240"
                align="center">
            </el-table-column>
            <el-table-column
                prop="processDefinitionId"
                label="部署版本"
                width="300"
                align="center">
            </el-table-column>
            <el-table-column
                prop="startTime"
                width="120"
                label="开始时间"
                align="center">
            </el-table-column>
            <el-table-column
                prop="isEnded"
                width="120"
                label="结束"
                align="center"
                :formatter="instanceFormatter">
            </el-table-column>
            <el-table-column
                label="操作"
                width="200"
                align="center">
              <template slot-scope="scope">
                <el-button
                    size="small"
                    @click="queryInstance(scope.row)">
                  查看
                </el-button>
              </template>
            </el-table-column>
            <div slot="empty">
            </div>
          </el-table>

          <!--分页功能-->
          <div id="page">
            <el-pagination
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
                :current-page=query.page
                :page-sizes="[10, 20, 50, 100]"
                :page-size=query.limit
                layout="total, sizes, prev, pager, next, jumper"
                :total=total
                :background="true">
            </el-pagination>
          </div>
        </div>
      </div>
    </el-main>
  </el-container>
</template>

<script>
export default {
  data() {
    return {
      showDialog: false,
      currentTab: 'tab1',
      loadingTab1: false,
      loadingTab2: false,
      tab1Data: [],
      tab2Data: [],
      // 已登录的用户名
      loggedName: sessionStorage.getItem("loggedName"),
      dialogTableVisible: false,
      // 查询条件
      query: {
        empno: sessionStorage.getItem("uid"),
        reimbursementDate: "",
        // 默认当前页为 1
        page: 1,
        limit: 6
      },
      // 数据总条数，默认为0
      total: 0,
      // 新增差旅弹出层是否显示
      dialogFormVisible: false,
      // 查看差旅弹出层
      showTravelInfoVisible: false,
      // 查看被驳回的差旅弹出层
      showRejectedTravelInfoVisible: false,
      // 弹出层左侧提示文字的宽度
      formLabelWidth: '120px',
      // 获取查询时间
      searchTime: "",
      modelCount: 10,
      definitionCount: 10,
      instanceCount: 0,
      processDefinitionKey: 'Process_1',
      xmlSaveRequest: {
        xmlStr: null,
        modelKey: null,
        modelName: null,
        user: null
      }
    };
  },
  methods: {
    recipeSQD() {
      this.$router.push("/recipeSQD");
    },
    recipeBDX() {
      this.$router.push("/recipeBXD");

    },
    workOverTime() {
      this.$router.push("/workOverTime");
    },
    reimbursement() {
      this.$router.push("/reimbursement");
    },
    fetchDataForTab1() {
      this.loadingTab1 = true;
      let data = {
        "empno": 3,
        "page": 1,
        "limit": 10
      }
      this.$http.get('/process/modelMetaList')
          .then(response => {
            this.tab1Data = response.data;
            this.modelCount = this.tab1Data.length
          })
          .catch(error => {
            console.error('Error fetching data for tab1:', error);
          })
          .finally(() => {
            this.loadingTab1 = false;
          });
    },
    fetchDataForTab2() {
      this.loadingTab2 = true;
      let data = {
        "empno": 3,
        "page": 1,
        "limit": 10
      }
      this.$http.get('/instanceList')
          .then(response => {
            this.tab2Data = response.data;
            this.instanceCount = this.tab2Data.length
          })
          .catch(error => {
            console.error('Error fetching data for tab2:', error);
          })
          .finally(() => {
            this.loadingTab2 = false;
          });
    },
    saveProcessModel() {
      this.xmlSaveRequest.user = sessionStorage.getItem("loggedName")
      this.$http.post('/process/saveModelMeta', this.xmlSaveRequest).then(res => {
        console.log(res)
        this.$message.info("新增模型定义成功，请点击编辑按钮操作流程编辑")
      });
      this.showDialog = false;
    },
    cancelAddModel() {
      this.xmlSaveRequest.modelKey = null
      this.xmlSaveRequest.modelName = null
      this.showDialog = false;
    },
    getRowKeys(row) {
      // id 是后台传递的每行信息唯一标识
      return row.travelId;
    },
    // 选中一条差旅信息
    selectOneTravelInfo(selection) {
      this.selectedItems = selection
    },
    // 选中全部差旅信息
    selectAllTravelInfo(selection) {
      this.selectedItems = selection
    },
    statusFormatter(cellValue) {
      if (cellValue.status == null || cellValue.status === 0) {
        return "编辑中"
      } else {
        return "已发布"
      }
    },
    instanceFormatter(cellValue) {
      if (cellValue.isEnded == true) {
        return "已结束"
      } else {
        return "进行中"
      }
    },
    formatDate(cellValue) {
      let v = cellValue.createTime;
      if (v == null) {
        v = cellValue.updateTime;
      }
      let d = new Date(v);
      let year = d.getFullYear();
      let month = ('0' + (d.getMonth() + 1)).slice(-2);
      let day = ('0' + d.getDate()).slice(-2);
      let hour = ('0' + d.getHours()).slice(-2);
      let minutes = ('0' + d.getMinutes()).slice(-2);
      return `${year}年${month}月${day}日 ${hour}:${minutes}`;
    },
    // 获取流程模版列表
    getModelList() {
      this.$http.get("/process/modelMetaList").then(res => {
        this.tab1Data = res.data
        this.total = res.data.length
      }).catch(err => {
        this.$message.error("查询审批流程模版列表失败，请联系管理员")
      })
    },
    viewModelXml(row) {
      this.$router.push({path: "/panel", query: {rowData: row, mode: 'view'}});
    },
    editInfo(row) {
      this.$router.push({path: "/panel", query: {rowData: row, mode: 'edit'}});
    },
    publish(row) {
      console.log(row)
      const xml = row.xmlStr;
      //xml转json,用json处理后在转xml
      const jsonObj = this.$x2js.xml2js(xml);
      this.getFormProperty(jsonObj);
      let newXml = this.$x2js.js2xml(jsonObj);

      this.xmlSaveRequest.xmlStr = newXml
      this.xmlSaveRequest.id = row.id
      this.xmlSaveRequest.modelName = row.modelName
      this.xmlSaveRequest.modelKey = row.modelKey

      this.$http.post("/deployXml", this.xmlSaveRequest).then(res => {
        this.$message.info("发布流程模版定义")
      }).catch(err => {
        this.$message.error("保存xml数据失败，请联系管理员~")
      })
    },
    delete(row) {

    },
    getFormProperty(json) {
      for (let e in json) {
        if (e == 'extensionElements' && json.extensionElements.formData && json.extensionElements.formData.formField) {
          let formProperty = JSON.parse(JSON.stringify(json.extensionElements.formData.formField));
          if (this.isArrayFn(formProperty)) {
            formProperty.forEach(x => {
              x.__prefix = 'activiti';
              x._name = x._label;
              if (x._defaultValue) {
                x._default = x._defaultValue;
              }
            });
          } else {
            formProperty.__prefix = 'activiti';
            formProperty._name = formProperty._label;
            if (formProperty._defaultValue) {
              formProperty._default = formProperty._defaultValue;
            }
          }
          json.extensionElements.formProperty = formProperty;
          delete json.extensionElements.formData;
        } else if (e.includes('camunda:')) {
          let str = e.replace('camunda:', 'activiti:');
          json[str] = json[e];
          delete json[e];
        } else if (typeof json[e] == 'object') {
          this.getFormProperty(json[e]);
        }
      }
    },
    queryInstance(row) {
      this.$router.push({path: "/instance", query: {rowData: row, mode: 'view'}});

    },
    // 当每页多少条被改变时，val代表改变后的每页多少条
    handleSizeChange(val) {
      this.query.limit = val
      this.search()
    },
    // 当前页被改变时，val代表当前页
    handleCurrentChange(val) {
      this.query.page = val
      this.getModelList()
    },
    // 自定义索引
    calIndex(index) {
      return index + 1 + (this.query.page - 1) * this.query.limit;
    },
    search() {
      this.query.page = 1
      this.getModelList()
    }
  },
  watch: {
    currentTab(newValue) {
      if (newValue === 'tab1') {
        this.fetchDataForTab1();
      } else if (newValue === 'tab2') {
        this.fetchDataForTab2();
      }
    }
  },
  created() {
    this.fetchDataForTab1();
    this.fetchDataForTab2();
  }
};
</script>

<style scoped>
.container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.blue {
  background-color: #a5c8d3;
}

.yellow {
  background-color: #e3e3b8;
}

.pink {
  background-color: #e7c5c9;
}

.separator-line {
  width: 100%;
  height: 1px;
  background-color: #ccc;
}

.tabs {
  display: flex;
  justify-content: start;
  border-bottom: 1px solid #ccc;
  padding-bottom: 10px;
}

.tabs el-button {
  margin-right: 10px;
}

.button-row {
  display: flex;
}

.bjs-powered-by {
  display: none !important;
}

</style>