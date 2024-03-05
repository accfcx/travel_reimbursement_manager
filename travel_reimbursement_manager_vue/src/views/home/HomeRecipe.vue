<template>
  <el-container>
    <!-- Main Section -->
    <el-main>
      <div class="tabs">
        <el-button @click="currentTab = 'tab1'">报销单({{ bxdCount }})</el-button>
        <el-button @click="currentTab = 'tab2'">申请单({{ sqdCount }})</el-button>
        <el-button @click="currentTab = 'tab2'">问题单据({{ problemRecipe }})</el-button>
      </div>

      <el-form :inline="true" :model="query" class="demo-form-inline">
        <div id="search">
          <el-form-item label="提交人:">
            <el-input v-model="query.ename" clearable
                      placeholder="请输入提交人"></el-input>
          </el-form-item>
          <el-form-item label="事由:">
            <el-input v-model="query.ename" clearable
                      placeholder="请输入事由"></el-input>
          </el-form-item>
          <el-form-item label="单号:">
            <el-input v-model="query.ename" clearable
                      placeholder="请输入单号"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="search"
                       icon="el-icon-search">查询
            </el-button>
          </el-form-item>
        </div>
      </el-form>


      <div v-if="currentTab === 'tab1'">
        <div style="position: relative; left: -540px">报销单列表</div>
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
            <!--复选框-->

            <el-table-column
                prop="travelId"
                label="单号"
                width="120"
                align="center">
            </el-table-column>
            <el-table-column
                prop="travelDesc"
                label="报销金额"
                width="120"
                align="center"
                :formatter="descFormat">
            </el-table-column>
            <el-table-column
                prop="travelDesc"
                label="业务类型"
                width="120"
                align="center"
                :formatter="descFormat">
            </el-table-column>
            <el-table-column
                prop="travelDesc"
                label="审核状态"
                width="120"
                align="center"
                :formatter="descFormat">
            </el-table-column>
            <el-table-column
                prop="reimbursementTotal"
                width="120"
                label="事由"
                align="center"
                :formatter="reimbursementTotalFormat">
            </el-table-column>
            <el-table-column
                prop="reimbursementDate"
                width="120"
                label="报销人"
                align="center"
                :formatter="submitTimeFormat">
            </el-table-column>
            <el-table-column
                prop="reimbursementDate"
                width="120"
                label="提交人"
                align="center"
                :formatter="submitTimeFormat">
            </el-table-column>
            <el-table-column
                prop="reimbursementDate"
                label="最后更新时间"
                width="120"
                align="center"
                :formatter="descFormat">
            </el-table-column>
            <el-table-column
                prop="reimbursementDate"
                label="创建时间"
                width="120"
                align="center"
                :formatter="descFormat">
            </el-table-column>
            <el-table-column
                label="操作"
                width="200"
                align="center">
              <template slot-scope="scope">
                <el-button
                    size="small"
                    @click="editInfo(scope.row)">编辑
                </el-button>
                <el-button
                    size="small"
                    style="margin-left: 0"
                    @click="editInfo(scope.row)">删除
                </el-button>
                <el-button
                    size="small"
                    style="margin-left: 0"
                    @click="editInfo(scope.row)">审批
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
        <div style="position: relative; left: -540px">申请单列表</div>
        <div v-if="loadingTab2"></div>
        <div v-else>

        </div>
      </div>
      <div v-if="currentTab === 'tab3'">
        <div style="position: relative; left: -540px">问题单据列表</div>
        <div v-if="loadingTab3"></div>
        <div v-else>

        </div>
      </div>
    </el-main>
  </el-container>
</template>

<script>
export default {
  data() {
    return {
      currentTab: 'tab1',
      loadingTab1: false,
      loadingTab2: false,
      loadingTab3: false,
      tab1Data: [],
      tab2Data: [],
      // 点击查看时当前行差旅信息的编号
      selectedTravelId: "",
      // 被选中项的集合
      selectedItems: [],
      // 保存差旅信息时需要向后台传递的内容
      associateTickets: {
        // 新增补助信息数据源
        subsidyForm: [],
        // 车票关联差旅信息的数据源
        tickets: [],
        travelForm: {
          // 驳回原因
          rejectionReason: "",
          // 申请差旅报销员工编号
          empno: sessionStorage.getItem("uid"),
          // 出差事由
          travelDesc: "",
          // 报销金额
          reimbursementTotal: "",
          // 报销金额大写
          moneyUppercase: "",
          // 出差的出发时间
          startTime: "",
          // 出差的到达时间
          arrivalTime: "",
          // 出差天数
          travelDays: "",
          // 报销日期
          reimbursementDate: "",
          // 差旅编号
          travelId: "",
          // 报销状态
          status: ""
        }
      },
      // 补助金额
      subsidy: "",
      // 暂存选中的待新增车票
      ticketsCache: [],
      // 当前登录员工未关联差旅的车票
      tickets: [],
      // 已登录的用户名
      loggedName: sessionStorage.getItem("loggedName"),
      dialogTableVisible: false,
      // 差旅信息表格数据源
      travelInfos: [],
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
      bxdCount: 10,
      sqdCount: 0,
      problemRecipe: 0
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
      this.$http.post('/travelInfoListByPage', data)
          .then(response => {
            this.tab1Data = response.data.data;
            console.log(this.tab1Data)
            // this.travelInfos = response.data.data;
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
      this.$http.post('/travelInfoListByPage', data)
          .then(response => {
            this.tab2Data = response.data.data;
          })
          .catch(error => {
            console.error('Error fetching data for tab2:', error);
          })
          .finally(() => {
            this.loadingTab2 = false;
          });
    },
    closeEdit() {
      this.showTravelInfoVisible = false
      this.showRejectedTravelInfoVisible = false
    },
    // 是否显示查看页面的编辑按钮
    isShowEdit() {
      let status = this.associateTickets.travelForm.status
      if ([2, 3, 5].includes(status)) {
        return false
      } else {
        return true
      }
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
    // 某条差旅信息是否可以编辑
    isEditable(row) {
      return [1, 2.1, 3.1].includes(row.status);
    },
    // 表格字段为空时默认显示
    descFormat(cellValue) {
      if (cellValue.travelDesc == null || cellValue.travelDesc == "") {
        return "无"
      } else {
        return cellValue.travelDesc
      }
    },
    submitTimeFormat(cellValue) {
      if (cellValue.reimbursementDate == null || cellValue.reimbursementDate == "") {
        return "尚未提交"
      } else {
        return cellValue.reimbursementDate
      }
    },
    reimbursementTotalFormat(cellValue) {
      if (cellValue.reimbursementDate == null || cellValue.reimbursementDate == "") {
        return "尚未填写"
      } else {
        return cellValue.reimbursementDate
      }
    },
    // 获取差旅报销信息
    getTravelInfoList() {
      this.$http.post("/travelInfoListByPage", this.query).then(res => {
        this.travelInfos = res.data.data
        this.total = res.data.count
      }).catch(err => {
        this.$message.error("差旅报销信息获取失败，请联系管理员~")
      })
    },
    // 获取车票信息
    getTicketList() {
      this.$http.get("/ticketList/" + sessionStorage.getItem("uid")).then(res => {
        this.tickets = res.data.data
      }).catch(err => {
        this.$message.error("车票信息加载失败，请联系管理员~")
      })
    },
    // 金额大写转换
    moneyToUpperCase(money) {
      var num = parseFloat(money);
      var strOutput = "",
          strUnit = '仟佰拾亿仟佰拾万仟佰拾元角分';
      num += "00";
      var intPos = num.indexOf('.');
      if (intPos >= 0) {
        num = num.substring(0, intPos) + num.substr(intPos + 1, 2);
      }
      strUnit = strUnit.substr(strUnit.length - num.length);
      for (var i = 0; i < num.length; i++) {
        strOutput += '零壹贰叁肆伍陆柒捌玖'.substr(num.substr(i, 1), 1) + strUnit.substr(i, 1);
      }
      return strOutput.replace(/零角零分$/, '整').replace(/零[仟佰拾]/g, '零').replace(/零{2,}/g, '零').replace(/零([亿|万])/g, '$1').replace(/零+元/, '元').replace(/亿零{0,3}万/, '亿').replace(/^元/, "零元")
    },
    // 自定义数组比较规则，按照日期进行比较
    compare(date) {
      return function (a, b) {
        var value1 = new Date(a[date])
        var value2 = new Date(b[date])
        return value1 - value2
      }
    },
    // 选择差旅信息
    chooseTicket() {
      if (this.associateTickets.tickets.length == 0) {
        this.dialogTableVisible = true
        this.getTicketList()
      } else {
        this.$message.warning("只能上传单次出差的车票信息~")
      }
    },
    // 编辑当前差旅信息
    editInfo(row) {
      this.$http.get("/getAssociatedTickets/" + row.travelId).then(res => {
        this.associateTickets = res.data.data
      }).catch(err => {
        this.$message.error("获取差旅信息失败，请联系管理员~")
      });
      this.dialogFormVisible = true
    },
    // 全选   selection：被选中项的集合
    selectAll(selection) {
      this.ticketsCache = selection
    },
    // 当对某行数据选中或取消选中时   selection：被选中项的集合  row：最近一次选中行
    selectRow(selection, row) {
      // 把选中项的集合赋给data中的集合
      this.ticketsCache = selection
    },
    // 当每页多少条被改变时，val代表改变后的每页多少条
    handleSizeChange(val) {
      this.query.limit = val
      this.search()
    },
    // 当前页被改变时，val代表当前页
    handleCurrentChange(val) {
      this.query.page = val
      this.getTravelInfoList()
    },
    // 自定义索引
    calIndex(index) {
      return index + 1 + (this.query.page - 1) * this.query.limit;
    },
    search() {
      this.query.page = 1
      this.getTravelInfoList()
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
  }
};
</script>

<style scoped>
.container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  //padding: 5px;
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
</style>