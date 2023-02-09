<template xmlns:overflow="http://www.w3.org/1999/xhtml">
    <div>
        <!--操作表单-->
        <el-form :inline="true" :model="query" class="demo-form-inline">
            <el-form-item id="search">
                <el-button type="primary" @click="search" icon="el-icon-search">
                    查询
                </el-button>
            </el-form-item>
            <div class="block">
                <span class="demonstration">报销日期：</span>
                <el-date-picker
                        value-format="”yyyy-MM-dd”"
                        @change="formatSearchTime"
                        v-model="searchTime"
                        type="date"
                        placeholder="选择报销日期">
                </el-date-picker>
            </div>
            <el-form-item id="handle">
                <el-button type="primary" @click="add" icon="el-icon-plus">新增
                </el-button>
                <el-button type="danger" @click="del" icon="el-icon-delete">删除
                </el-button>
            </el-form-item>
        </el-form>

        <!--表格-->
        <el-table
                ref="reimbursementTable"
                :data="travelInfos"
                stripe
                style="width: 100%;font-size: 17px"
                height="550px"
                @select="selectOneTravelInfo"
                @select-all="selectAllTravelInfo"
                :header-cell-style="{background:'#2e59a7',color:'white'}"
                :border="true"
                :row-key="getRowKeys">
            <!--复选框-->
            <el-table-column type="selection" width="55"
                             :reserve-selection="true"/>
            <!--索引列-->
            <el-table-column
                    type="index"
                    :index="calIndex"
                    width="80"
                    prop="no"
                    label="序号"
                    align="center">
            </el-table-column>
            <el-table-column
                    prop="travelId"
                    label="差旅编号"
                    width="180"
                    align="center">
            </el-table-column>
            <el-table-column
                    prop="travelDesc"
                    label="出差事由"
                    width="180"
                    align="center"
                    :formatter="descFormat">
            </el-table-column>
            <el-table-column
                    prop="reimbursementTotal"
                    width="200"
                    label="报销金额"
                    align="center"
                    :formatter="reimbursementTotalFormat">
            </el-table-column>
            <el-table-column
                    prop="reimbursementDate"
                    width="300"
                    label="报销时间"
                    align="center"
                    :formatter="submitTimeFormat">
            </el-table-column>
            <el-table-column
                    label="报销进度"
                    align="center">
                <template slot-scope="scope">
                    <el-steps
                            :active="getReimbursementProcess(scope.row.travelId)"
                            finish-status="success"
                            :process-status="getReimbursementStatus(scope.row.travelId)"
                            space="33%"
                            :align-center="true">
                        <el-step title="员工填写"></el-step>
                        <el-step title="财务审批"></el-step>
                        <el-step title="领导审批"></el-step>
                        <el-step title="出纳付款"></el-step>
                    </el-steps>
                </template>
            </el-table-column>
            <el-table-column
                    label="操作"
                    width="200"
                    align="center">
                <template slot-scope="scope">
                    <el-button
                            size="middle"
                            type="primary"
                            @click="showTravelInfo(scope.row)">查看
                    </el-button>
                    <el-button
                            v-if="!isEditable(scope.row)"
                            size="middle"
                            type="primary"
                            disabled>编辑
                    </el-button>
                    <el-button
                            v-else
                            size="middle"
                            type="primary"
                            @click="editInfo(scope.row)">编辑
                    </el-button>
                </template>
            </el-table-column>
            <div slot="empty">
                <el-empty description="好像没有要找的信息~"></el-empty>
            </div>
        </el-table>

        <!--分页功能-->
        <div id="page">
            <el-pagination
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page=query.page
                    :page-sizes="[6, 12, 18, 24]"
                    :page-size=query.limit
                    layout="total, sizes, prev, pager, next, jumper"
                    :total=total
                    :background="true">
            </el-pagination>
        </div>

        <!--差旅信息填写弹出层-->
        <el-dialog title="差旅信息填写" :visible.sync="dialogFormVisible"
                   width="1700px" top="10px" @close="closeEdit">
            <!--差旅报销-->
            <div id="reimbursement">
                <el-row style="text-align: left">
                    <label style="font-size: 22px;font-weight: bold">差旅报销</label>
                    <el-button type="primary"
                               style="margin-left: 1412px"
                               @click="saveTravelInfo">保存
                    </el-button>
                    <el-button type="success" @click="submit">提交</el-button>
                </el-row>
                <el-row>
                    <table id="reimbursementTable">
                        <tr style="height: 40px">
                            <td style="width: 200px;background: #bcd4e7">单据序号
                            </td>
                            <td style="width: 500px">{{
                                    associateTickets
                                            .travelForm.travelId
                                                     }}
                            </td>
                            <td style="width: 200px;background: #bcd4e7">报销人
                            </td>
                            <td colspan="3"
                                style="width: 750px">{{loggedName}}
                            </td>
                        </tr>
                        <tr style="height: 40px">
                            <td style="background: #bcd4e7">出发时间</td>
                            <td>{{associateTickets.travelForm.startTime}}</td>
                            <td style="background: #bcd4e7">到达时间</td>
                            <td colspan="3">
                                {{associateTickets.travelForm.arrivalTime}}
                            </td>
                        </tr>
                        <tr style="height: 40px">
                            <td style="background: #bcd4e7">天数</td>
                            <td>{{associateTickets.travelForm.travelDays}}</td>
                            <td style="background: #bcd4e7">报销金额</td>
                            <td style="width: 150px">
                                {{associateTickets.travelForm.reimbursementTotal}}
                            </td>
                            <td style="width: 150px;background: #bcd4e7">大写</td>
                            <td>{{
                                    associateTickets.travelForm.moneyUppercase
                                }}
                            </td>
                        </tr>
                        <tr style="height: 40px">
                            <td style="background: #bcd4e7">出差事由</td>
                            <td>
                                <el-input
                                        v-model="associateTickets.travelForm.travelDesc"
                                        placeholder="请输入出差事由"/>
                            </td>
                            <td style="background: #bcd4e7">报销时间</td>
                            <td colspan="3">
                                {{associateTickets.travelForm.reimbursementDate}}
                            </td>
                        </tr>
                    </table>
                </el-row>
            </div>
            <!--差旅信息-->
            <div id="travelInfo" style="margin-top: 30px;height: 250px">
                <el-row style="text-align: left">
                    <label style="font-size: 22px;font-weight: bold">差旅信息
                    </label>
                    <label style="color: #e18a3b">填写格式：济南 -> 目的地 ... 目的地 ->
                                                  济南</label>
                    <el-button type="primary"
                               style="margin-left: 1082px;"
                               @click="chooseTicket">选择车票信息
                    </el-button>
                    <el-button type="danger" @click="clearTicket">清空</el-button>
                </el-row>
                <el-table
                        :data="associateTickets.tickets"
                        stripe
                        style="width: 100%;margin-top: 10px"
                        :header-cell-style="{background:'#a9be7b',color:'white'}"
                        :border="true"
                        max-height="220">
                    <el-table-column
                            type="index"
                            :index="calIndex"
                            prop="no"
                            label="序号"
                            width="50"
                            align="center">
                    </el-table-column>
                    <el-table-column
                            prop="travelType"
                            label="类型"
                            align="center">
                    </el-table-column>
                    <el-table-column
                            prop="startPoint"
                            label="出发地点"
                            align="center">
                    </el-table-column>
                    <el-table-column
                            prop="arrivalPoint"
                            label="到达地点"
                            align="center">
                    </el-table-column>
                    <el-table-column
                            prop="startTime"
                            label="出发时间"
                            align="center">
                    </el-table-column>
                    <el-table-column
                            prop="arrivalTime"
                            label="到达时间"
                            align="center">
                    </el-table-column>
                    <el-table-column
                            prop="transportation"
                            label="交通工具"
                            align="center">
                    </el-table-column>
                    <el-table-column
                            prop="price"
                            label="车票金额"
                            align="center">
                    </el-table-column>
                </el-table>
            </div>
            <!--补助信息-->
            <div id="subsidy" style="margin-top: 30px;height: 220px">
                <el-row style="text-align: left;margin-bottom: 10px">
                    <label style="font-size: 22px;font-weight: bold">补助信息
                    </label>
                    <el-button type="primary"
                               style="margin-left: 1464px;"
                               @click="calSubsidy">计算补助
                    </el-button>
                </el-row>
                <el-table
                        :data="associateTickets.subsidyForm"
                        stripe
                        style="width: 100%"
                        :header-cell-style="{background:'#a9be7b',color:'white'}"
                        :border="true"
                        max-height="220">
                    <el-table-column
                            type="index"
                            :index="calIndex"
                            prop="no"
                            label="序号"
                            width="50px"
                            align="center">
                    </el-table-column>
                    <el-table-column
                            prop="workPoint"
                            label="工作地"
                            align="center">
                    </el-table-column>
                    <el-table-column
                            prop="startTime"
                            label="起始时间"
                            align="center">
                    </el-table-column>
                    <el-table-column
                            prop="endTime"
                            label="截止时间"
                            align="center">
                    </el-table-column>
                    <el-table-column
                            prop="travelDays"
                            label="补助天数"
                            align="center">
                    </el-table-column>
                    <el-table-column
                            prop="subsidy"
                            label="补助金额"
                            align="center">
                    </el-table-column>
                </el-table>
            </div>
            <!--旅程信息选择弹出层-->
            <el-dialog title="旅程信息选择" :visible.sync="dialogTableVisible"
                       :modal="false">
                <el-row style="text-align: right;margin-bottom: 10px">
                    <el-button type="primary" @click="confirm">确定
                    </el-button>
                </el-row>
                <el-table :data="tickets"
                          height="500"
                          max-height="500"
                          :border="true"
                          :header-cell-style="{background:'#5aa4ae',color:'white'}"
                          @select="selectRow"
                          @select-all="selectAll">
                    <el-table-column type="selection" width="55"/>
                    <el-table-column
                            type="index"
                            width="50"
                            prop="no"
                            label="序号"
                            align="center">
                    </el-table-column>
                    <el-table-column
                            prop="travelType"
                            label="类型"
                            width="180"
                            align="center">
                    </el-table-column>
                    <el-table-column
                            prop="startPoint"
                            label="出发地点"
                            align="center">
                    </el-table-column>
                    <el-table-column
                            prop="arrivalPoint"
                            label="到达地点"
                            align="center">
                    </el-table-column>
                    <el-table-column
                            prop="startTime"
                            label="出发时间"
                            align="center">
                    </el-table-column>
                    <el-table-column
                            prop="arrivalTime"
                            label="到达时间"
                            align="center">
                    </el-table-column>
                    <el-table-column
                            prop="transportation"
                            label="交通工具"
                            align="center">
                    </el-table-column>
                    <el-table-column
                            prop="price"
                            label="车票金额"
                            align="center">
                    </el-table-column>
                    <div slot="empty">
                        <el-empty description="这里没有发现车票~"></el-empty>
                    </div>
                </el-table>
                <div>
                    <h2 style="text-align: left;margin-top: 10px">注意事项</h2>
                    <div style="text-align: left;margin-top: 20px">
                        <label style="font-size:16px">
                            所选车票需要是从济南开始，回到济南的回路，且车票日期需构成一条时间线</label>
                    </div>
                </div>
            </el-dialog>
        </el-dialog>

        <!--被驳回时的查看差旅信息弹出层-->
        <el-dialog title="差旅信息" :visible.sync="showRejectedTravelInfoVisible"
                   width="1200px">
            <el-row style="text-align: left">
                <el-button
                        v-if="isShowEdit()"
                        type="primary"
                        style="margin-left: 1090px"
                        @click="editFromShow">编辑
                </el-button>
            </el-row>
            <el-row>
                <table id="reimbursementTable">
                    <tr style="height: 40px">
                        <td style="width: 150px;background: #bcd4e7">单据序号
                        </td>
                        <td style="width: 300px">{{
                                associateTickets
                                        .travelForm.travelId
                                                 }}
                        </td>
                        <td style="width: 150px;background: #bcd4e7">报销人
                        </td>
                        <td colspan="3"
                            style="width: 750px">{{loggedName}}
                        </td>
                    </tr>
                    <tr style="height: 40px">
                        <td style="background: #bcd4e7">出发时间</td>
                        <td>{{associateTickets.travelForm.startTime}}</td>
                        <td style="background: #bcd4e7">到达时间</td>
                        <td colspan="3">
                            {{associateTickets.travelForm.arrivalTime}}
                        </td>
                    </tr>
                    <tr style="height: 40px">
                        <td style="background: #bcd4e7">天数</td>
                        <td>{{associateTickets.travelForm.travelDays}}</td>
                        <td style="background: #bcd4e7">报销金额</td>
                        <td style="width: 200px">
                            {{associateTickets.travelForm.reimbursementTotal}}
                        </td>
                        <td style="width: 150px;background: #bcd4e7">大写</td>
                        <td style="width: 202px">{{
                                associateTickets.travelForm.moneyUppercase
                                                 }}
                        </td>
                    </tr>
                    <tr style="height: 40px">
                        <td style="background: #bcd4e7">出差事由</td>
                        <td>
                            <el-tooltip class="item" effect="dark"
                                        :content="associateTickets.travelForm.travelDesc"
                                        placement="top-start">
                                <label>{{associateTickets.travelForm.travelDesc}}</label>
                            </el-tooltip>
                        </td>
                        <td style="background: #bcd4e7">报销时间</td>
                        <td>
                            {{associateTickets.travelForm.reimbursementDate}}
                        </td>
                        <td style="background: #bcd4e7">
                            驳回原因
                        </td>
                        <td>
                            <el-tooltip class="item" effect="dark"
                                        :content="associateTickets.travelForm.rejectionReason"
                                        placement="top-start">
                                <label>{{
                                        associateTickets.travelForm
                                                .rejectionReason
                                       }}</label>
                            </el-tooltip>
                        </td>
                    </tr>
                </table>
            </el-row>
        </el-dialog>
        <!--未提交或审批中时的查看差旅信息弹出层-->
        <el-dialog title="差旅信息" :visible.sync="showTravelInfoVisible"
                   width="1200px">
            <el-row style="text-align: left">
                <el-button
                        v-if="isShowEdit()"
                        type="primary"
                        style="margin-left: 1090px"
                        @click="editFromShow">编辑
                </el-button>
            </el-row>
            <el-row>
                <table id="reimbursementTable">
                    <tr style="height: 40px">
                        <td style="width: 150px;background: #bcd4e7">单据序号
                        </td>
                        <td style="width: 300px">{{
                                associateTickets
                                        .travelForm.travelId
                                                 }}
                        </td>
                        <td style="width: 150px;background: #bcd4e7">报销人
                        </td>
                        <td colspan="3"
                            style="width: 750px">{{loggedName}}
                        </td>
                    </tr>
                    <tr style="height: 40px">
                        <td style="background: #bcd4e7">出发时间</td>
                        <td>{{associateTickets.travelForm.startTime}}</td>
                        <td style="background: #bcd4e7">到达时间</td>
                        <td colspan="3">
                            {{associateTickets.travelForm.arrivalTime}}
                        </td>
                    </tr>
                    <tr style="height: 40px">
                        <td style="background: #bcd4e7">天数</td>
                        <td>{{associateTickets.travelForm.travelDays}}</td>
                        <td style="background: #bcd4e7">报销金额</td>
                        <td style="width: 200px">
                            {{associateTickets.travelForm.reimbursementTotal}}
                        </td>
                        <td style="width: 150px;background: #bcd4e7">大写</td>
                        <td style="width: 202px">{{
                                associateTickets.travelForm.moneyUppercase
                                                 }}
                        </td>
                    </tr>
                    <tr style="height: 40px">
                        <td style="background: #bcd4e7">出差事由</td>
                        <td>
                            <el-tooltip class="item" effect="dark"
                                        :content="associateTickets.travelForm.travelDesc"
                                        placement="top-start">
                                <label>{{associateTickets.travelForm.travelDesc}}</label>
                            </el-tooltip>
                        </td>
                        <td style="background: #bcd4e7">报销时间</td>
                        <td colspan="3">
                            {{associateTickets.travelForm.reimbursementDate}}
                        </td>
                    </tr>
                </table>
            </el-row>
        </el-dialog>
    </div>
</template>

<script>
export default {
    name: "EmpManage",
    data() {
        return {
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
            searchTime: ""
        }
    },
    methods: {
        closeEdit() {
            this.showTravelInfoVisible = false
            this.showRejectedTravelInfoVisible = false
        },
        // 格式化选中的时间
        formatSearchTime() {
            let date = this.searchTime
            if (date == null) {
                this.query.reimbursementDate = ""
            } else {
                this.query.reimbursementDate =
                        this.searchTime.substring(1, date.length - 1)
            }
        },
        // 在查看差旅页面点击编辑按钮
        editFromShow() {
            this.$http.get("/getAssociatedTickets/" + this.selectedTravelId).then(res => {
                this.associateTickets = res.data.data
            }).catch(err => {
                this.$message.error("获取差旅信息失败，请联系管理员~")
            });
            this.dialogFormVisible = true
            this.showTravelInfoVisible = false
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
        // 报销状态
        getReimbursementStatus(travelId) {
            let travelInfos = this.travelInfos
            let status
            for (let i = 0; i < travelInfos.length; i++) {
                if (travelInfos[i].travelId == travelId) {
                    status = travelInfos[i].status
                    break
                }
            }
            if ([2.1, 3.1].includes(status)) {
                return "error"
            } else {
                return "process"
            }
        },
        // 报销进度
        getReimbursementProcess(travelId) {
            let travelInfos = this.travelInfos
            for (let i = 0; i < travelInfos.length; i++) {
                if (travelInfos[i].travelId == travelId) {
                    return parseInt(travelInfos[i].status) - 1
                }
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
        // 获取当前时间
        getFormatDate() {
            let date = new Date();
            let year = date.getFullYear();
            let mon = (date.getMonth() + 1) < 10 ? "0" + (date.getMonth() + 1)
                    : date.getMonth() + 1;
            let data = date.getDate() < 10 ? "0" + (date.getDate()) :
                    date.getDate();
            let hour = date.getHours() < 10 ? "0" + (date.getHours()) :
                    date.getHours();
            let min = date.getMinutes() < 10 ? "0" + (date.getMinutes()) :
                    date.getMinutes();
            let second = date.getSeconds() < 10 ? "0" + (date.getSeconds()) :
                    date.getSeconds();
            return year + "-" + mon + "-" + data + " " + hour + ":" + min + ":" + second;
        },
        // 计算补助
        calSubsidy() {
            if (this.associateTickets.subsidyForm.length != 0) {
                this.$message.warning("已经计算过啦~")
                return
            } else if (this.associateTickets.tickets.length == 0) {
                this.$message.warning("请选择需要补助的车票~")
            }
            // 总补助金额
            let subsidyTotal = 0;
            // 出差天数
            let travelDays = 0
            // 获取已选中的车票信息
            let tickets = this.associateTickets.tickets
            // 对所有车票按照出发日期排序
            tickets.sort(this.compare("startTime"))
            // 车船费
            let ticketFee = tickets[0].price
            for (let i = 1; i < tickets.length; i++) {
                // 补助信息
                let subsidyInfo =
                        {
                            workPoint: '',
                            startTime: '',
                            endTime: '',
                            travelDays: '',
                            subsidy: ''
                        }
                subsidyInfo.workPoint = tickets[i].startPoint
                subsidyInfo.startTime = tickets[i - 1].arrivalTime
                subsidyInfo.endTime = tickets[i].startTime
                subsidyInfo.travelDays = this.calDays(tickets[i - 1].arrivalTime, tickets[i].startTime)
                subsidyInfo.subsidy = subsidyInfo.travelDays * 200
                subsidyTotal += subsidyInfo.subsidy
                ticketFee += tickets[i].price
                travelDays += subsidyInfo.travelDays
                // 保存每条补助信息
                this.associateTickets.subsidyForm.push(subsidyInfo)
            }
            // 总报销金额
            this.associateTickets.travelForm.reimbursementTotal = subsidyTotal + ticketFee
            // 报销金额转大写
            this.associateTickets.travelForm.moneyUppercase =
                    this.moneyToUpperCase(subsidyTotal + ticketFee)
            this.associateTickets.travelForm.travelDays = travelDays
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
        // 清空已选择的车票及补助信息
        clearTicket() {
            this.ticketsCache = []
            this.associateTickets.tickets = []
            this.associateTickets.subsidyForm = []
            this.associateTickets.travelForm = {
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
        // 确认选择车票
        confirm() {
            // 获取选中的车票数组
            let selection = this.ticketsCache
            if (selection.length == 0) {
                this.$message.warning("请选择车票~")
                return
            }
            // 所选车票是否符合要求
            let isTicketValid = true;
            // 对所有车票按照出发日期排序
            selection.sort(this.compare("startTime"))
            // 出差开始时间
            let startTime = selection[0].startTime
            // 出差结束时间
            let arrivalTime = selection[selection.length - 1].arrivalTime
            // 如果最早一张车票的出发地和最晚一张车票的到达地不是济南，则不符合要求
            if (selection[0].startPoint != "济南" || selection[selection.length - 1].arrivalPoint != "济南") {
                isTicketValid = false
            }
            // 判断所有车票是否能构成回路
            for (let i = 0; i < selection.length - 1; i++) {
                if (selection[i].arrivalPoint != selection[i + 1].startPoint) {
                    isTicketValid = false
                    break
                }
            }
            if (!isTicketValid) {
                this.$message.warning("所选车票有误，请确认车票日期、始发地和终点站是否为济南以及是否构成回路")
                return
            }
            this.associateTickets.travelForm.startTime = startTime
            this.associateTickets.travelForm.arrivalTime = arrivalTime
            this.associateTickets.tickets = this.ticketsCache
            this.dialogTableVisible = false
        },
        // 自定义数组比较规则，按照日期进行比较
        compare(date) {
            return function (a, b) {
                var value1 = new Date(a[date])
                var value2 = new Date(b[date])
                return value1 - value2
            }
        },
        // 计算出差天数
        calDays(startTime, endTime) {
            let start = new Date(startTime)
            let end = new Date(endTime)
            let days = Math.round((end.getTime() - start.getTime()) / (1000 * 3600 * 24))
            // 出差时间在12点之后或到达时间在12点之前，按半天计算
            if (start.getHours() > 12 || end.getHours() < 12) {
                days = days - 0.5
            }
            return days;
        },
        // 保存差旅报销信息
        saveTravelInfo() {
            if (this.associateTickets.tickets.length == 0) {
                this.$message.warning("请选择相关信息后保存~")
                return
            }
            this.$http.post("/associateTickets", this.associateTickets).then(res => {
                if (res.data == "success") {
                    this.$message.success("差旅信息保存成功~")
                    this.search()
                    this.dialogFormVisible = false
                }
            }).catch(err => {
                this.$message.error("保存差旅失败，请联系管理员~")
            })
        },
        // 提交差旅报销信息
        submit() {
            if (this.associateTickets.tickets.length == 0) {
                this.$message.warning("还未选择车票，无法提交~")
                return
            }
            if (this.associateTickets.subsidyForm.length == 0) {
                this.$message.warning("还未计算补助，无法提交~")
                return
            }
            // 获取当前时间
            this.associateTickets.travelForm.reimbursementDate = this.getFormatDate()
            this.$http.post("/submitReimbursement", this.associateTickets).then(res => {
                if (res.data == "success") {
                    this.$message.success("差旅报销信息提交成功~")
                    this.search()
                    this.$router.replace({
                        path: '/blankPage', //空白页的路由地址
                        name: 'BlankPage'
                    })
                }
            }).catch(err => {
                this.$message.error("提交差旅报销信息失败，请联系管理员~")
            })
            this.dialogFormVisible = false
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
        // 查看差旅信息
        showTravelInfo(row) {
            this.selectedTravelId = row.travelId
            if ([2.1, 3.1].includes(row.status)) {
                this.showRejectedTravelInfoVisible = true
            } else {
                this.showTravelInfoVisible = true
            }
            this.$http.get("/getAssociatedTickets/" + row.travelId).then(res => {
                this.associateTickets.travelForm = res.data.data.travelForm
            }).catch(err => {
                this.$message.error("获取差旅信息失败，请联系管理员~")
            })
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
        },
        // 新增差旅信息
        add() {
            this.dialogFormVisible = true
            this.clearTicket()
            this.associateTickets.travelForm = {
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
                travelId: ""
            }
        },
        // 删除差旅信息
        del() {
            // 选中的差旅信息
            let selection = this.selectedItems
            // 已关联的差旅编号
            let associatedTravelIds = []
            // 已通过的差旅报销
            let passedTravelIds = []
            if (selection.length == 0) {
                this.$message.warning("请至少选择一条数据~")
                return
            }
            // 已在审批中的差旅不可删除
            for (let i = 0; i < selection.length; i++) {
                if([2, 3].includes(selection[i].status)){
                    associatedTravelIds.push(selection[i].travelId)
                }
            }
            // 已通过审批的差旅不可删除
            for (let i = 0; i < selection.length; i++) {
                if(selection[i].status == 5){
                    passedTravelIds.push(selection[i].travelId)
                }
            }
            if (associatedTravelIds.length != 0) {
                this.$message.warning("编号为" +
                        associatedTravelIds.toString() + "的差旅已在审批中，不可删除~")
                return
            }
            if (passedTravelIds.length != 0) {
                this.$message.warning("编号为" +
                        passedTravelIds.toString() + "的差旅已通过审批，不可删除~")
                return
            }
            this.$confirm('确定要删除所选差旅信息吗?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                let travelIds = [];
                for (let i = 0; i < selection.length; i++) {
                    travelIds.push(selection[i].travelId)
                }
                this.$http.post("/delTravelInfo", travelIds).then((res) => {
                    if (res.data == "success") {
                        this.$message.success("删除成功！")
                        // 刷新表格
                        this.search()
                        this.$refs["reimbursementTable"].clearSelection()
                        this.selectedItems = []
                    } else {
                        this.$message.error("删除失败！")
                    }
                }).catch((err) => {
                    this.$message.error("删除失败，请联系管理员！")
                })
            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '已取消删除'
                })
            });
        }
    },
    created() {
        this.search()
    },
    mounted() {
        this.getFormatDate()
    }
}
</script>

<style scoped>
#reimbursementTable {
    font-size: 18px;
    margin-top: 10px;
}

#reimbursementTable {
    border: 1px solid #6b5458;
    border-collapse: collapse;
}

#reimbursementTable tr td {
    border: 1px solid #6b5458;
}

#handle {
    position: relative;
    right: -595px;
}

#search {
    position: absolute;
    left: 550px;
}

.block {
    float: left;
}

#page {
    text-align: center;
}
</style>