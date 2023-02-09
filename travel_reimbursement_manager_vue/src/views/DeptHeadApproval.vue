<template>
    <div>
        <!--操作表单-->

        <el-row>
            <el-button type="success" @click="passAll" icon="el-icon-check"
                       style="position:relative;right: -777px;bottom: 10px">
                一键通过
            </el-button>
        </el-row>
        <!--表格-->
        <el-table
                :data="travelForm"
                stripe
                style="width: 100%;font-size: 17px"
                height="550px"
                @select="selectRow"
                @select-all="selectAll"
                :header-cell-style="{background:'#2e59a7',color:'white'}"
                :border="true"
                :row-key="getRowKeys">
            <!--复选框-->
            <el-table-column type="selection" width="55" :reserve-selection="true"/>
            <el-table-column
                    prop="travelId"
                    label="单据序号"
                    width="180"
                    align="center">
            </el-table-column>
            <el-table-column
                    prop="ename"
                    label="报销人"
                    width="180"
                    align="center">
            </el-table-column>
            <el-table-column
                    prop="travelDesc"
                    label="出差事由"
                    align="center">
            </el-table-column>
            <el-table-column
                    prop="reimbursementTotal"
                    label="报销金额"
                    align="center">
            </el-table-column>
            <el-table-column
                    prop="startTime"
                    label="报销日期"
                    align="center">
            </el-table-column>
            <el-table-column
                    prop="arrivalTime"
                    label="操作"
                    align="center"
                    width="400px">
                <template slot-scope="scope">
                    <el-button
                            icon="el-icon-view"
                            size="medium"
                            type="primary"
                            @click="showInfo(scope.$index, scope.row)">查看详细信息
                    </el-button>
                    <el-button
                            icon="el-icon-check"
                            size="medium"
                            type="success"
                            @click="pass(scope.row)">通过
                    </el-button>
                    <el-button
                            icon="el-icon-close"
                            size="medium"
                            type="danger"
                            @click="reject(scope.row)">驳回
                    </el-button>
                </template>
            </el-table-column>
            <div slot="empty">
                <el-empty description="没有待审批~"></el-empty>
            </div>
        </el-table>

        <!--查看报销信息弹出层-->
        <el-dialog title="差旅详细信息" :visible.sync="isShow"
                   width="1700px" top="10px">
            <!--差旅报销-->
            <div id="reimbursement">
                <el-row style="text-align: left">
                    <label style="font-size: 22px;font-weight: bold">差旅详细信息
                    </label>
                    <el-button
                            icon="el-icon-check"
                            size="medium"
                            type="success"
                            @click="passInShow()"
                            style="margin-left: 1325px">通过
                    </el-button>
                    <el-button
                            icon="el-icon-check"
                            size="medium"
                            type="danger"
                            @click="rejectInShow">驳回
                    </el-button>
                </el-row>
                <el-row>
                    <table id="reimbursementTable">
                        <tr style="height: 40px">
                            <td style="width: 200px;background: #bcd4e7">单据序号
                            </td>
                            <td style="width: 500px">
                                {{travelInfo.travelId}}
                            </td>
                            <td style="width: 200px;background: #bcd4e7">报销人
                            </td>
                            <td colspan="3"
                                style="width: 750px">
                                {{travelInfo.ename}}
                            </td>
                        </tr>
                        <tr style="height: 40px">
                            <td style="background: #bcd4e7">出发时间</td>
                            <td>{{travelInfo.startTime}}</td>
                            <td style="background: #bcd4e7">到达时间</td>
                            <td colspan="3">
                                {{travelInfo.arrivalTime}}
                            </td>
                        </tr>
                        <tr style="height: 40px">
                            <td style="background: #bcd4e7">天数</td>
                            <td>{{travelInfo.travelDays}}</td>
                            <td style="background: #bcd4e7">报销金额</td>
                            <td style="width: 150px">
                                {{travelInfo.reimbursementTotal}}
                            </td>
                            <td style="width: 150px;background: #bcd4e7">大写</td>
                            <td>{{
                                    travelInfo.moneyUppercase
                                }}
                            </td>
                        </tr>
                        <tr style="height: 40px">
                            <td style="background: #bcd4e7">出差事由</td>
                            <td>
                                {{travelInfo.travelDesc}}
                            </td>
                            <td style="background: #bcd4e7">报销时间</td>
                            <td colspan="3">
                                {{travelInfo.reimbursementDate}}
                            </td>
                        </tr>
                    </table>
                </el-row>
            </div>
            <!--差旅信息-->
            <div id="travelInfo" style="margin-top: 30px;height: 250px">
                <el-row style="text-align: left">
                    <label style="font-size: 22px;font-weight: bold">车票信息
                    </label>
                </el-row>
                <el-table
                        :data="tickets"
                        stripe
                        style="width: 100%;margin-top: 10px"
                        :header-cell-style="{background:'#a9be7b',color:'white'}"
                        :border="true"
                        max-height="220">
                    <el-table-column
                            type="index"
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
                </el-row>
                <el-table
                        :data="subsidyForm"
                        stripe
                        style="width: 100%"
                        :header-cell-style="{background:'#a9be7b',color:'white'}"
                        :border="true"
                        max-height="220">
                    <el-table-column
                            type="index"
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
        </el-dialog>

        <!--分页功能-->
        <div id="page">
            <el-pagination
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page=query.page
                    :page-sizes="[8, 16, 24, 32]"
                    :page-size=query.limit
                    layout="total, sizes, prev, pager, next, jumper"
                    :total=total
                    :background="true">
            </el-pagination>
        </div>

        <!--驳回原因弹窗-->
        <el-dialog :visible.sync="rejectionInput" :show-close="false"
                   width="420px" top="360px" :modal="false" title="驳回原因">
            <el-row>
                <el-input v-model="rejectForm.rejectionReason"
                          autocomplete="off" placeholder="请输入驳回原因" clearable
                          style="width:300px"
                ></el-input>
                <el-button type="primary" style="margin-left: 10px"
                           @click="cofirmReject">确定
                </el-button>
            </el-row>
        </el-dialog>
    </div>
</template>

<script>
export default {
    name: "DeptHeadApproval",
    data() {
        return {
            // 通过报销要传递的数据
            passForm: {
                // 要通过的差旅编号
                travelIds: [],
                // 审批人的id
                uid: sessionStorage.getItem("uid")
            },
            // 驳回表单要传递的数据
            rejectForm: {
                // 要驳回的差旅id
                rejectTravelId: "",
                // 审批人的id
                uid: sessionStorage.getItem("uid"),
                // 驳回原因
                rejectionReason: ""
            },
            // 驳回原因弹窗是否显示
            rejectionInput: false,
            // 差旅详细信息弹出层是否显示
            isShow: false,
            // 数据总条数，默认为0
            total: 0,
            // 已经提交关联的报销数据
            reimbursementInfo: [{
                // 补助信息数据源
                subsidyForm: [],
                // 车票关联差旅信息的数据源
                tickets: [],
                travelForm: {
                    // 申请差旅报销员工姓名
                    ename: "",
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
                    status: "3"
                }
            }],
            subsidyForm: [],
            tickets: [],
            travelInfo: {
                // 申请差旅报销员工姓名
                ename: "",
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
                status: "3"
            },
            // 表格数据源
            travelForm: [],
            // 查询条件
            query: {
                empno: sessionStorage.getItem("uid"),
                status: 3,
                page: 1,
                limit: 8
            }
        }
    },
    methods: {
        getRowKeys(row) {
            // id 是后台传递的每行信息唯一标识
            return row.travelId;
        },
        // 在查看详细信息中点击编辑
        passInShow() {
            this.passForm.travelIds.push(this.travelInfo.travelId)
            this.$http.post("/passReimbursement", this.passForm).then(res => {
                if (res.data == "success") {
                    this.$message.success("审批成功")
                    this.search()
                    this.passForm.travelIds = []
                }
            }).catch(err => {
                this.$message.error("操作失败，请联系管理员~")
            })
            this.isShow = false
        },
        // 在查看详细信息中点击驳回
        rejectInShow() {
            this.rejectForm.rejectionReason = ""
            this.rejectForm.rejectTravelId = this.travelInfo.travelId
            this.rejectionInput = true
        },
        // 全选，获取选中信息的编号
        selectAll(selection) {
            for (let i = 0; i < selection.length; i++) {
                this.passForm.travelIds.push(selection[i].travelId)
            }
        },
        // 选中某行数据，获取选中信息的编号
        selectRow(row) {
            this.passForm.travelIds.push(row[0].travelId)
        },
        // 当每页多少条被改变时，val代表改变后的每页多少条
        handleSizeChange(val) {
            this.query.limit = val
            this.search()
        },
        // 当前页被改变时，val代表当前页
        handleCurrentChange(val) {
            this.query.page = val
            this.getReimbursementList()
        },
        // 分页查询
        search() {
            this.query.page = 1
            this.getReimbursementList()
        },
        // 查看详细信息
        showInfo(index) {
            this.isShow = true
            this.travelInfo = this.reimbursementInfo[index].travelForm
            this.tickets = this.reimbursementInfo[index].tickets
            this.subsidyForm = this.reimbursementInfo[index].subsidyForm
        },
        // 点击通过
        pass(row) {
            this.passForm.travelIds.push(row.travelId)
            this.$http.post("/passReimbursement", this.passForm).then(res => {
                if (res.data == "success") {
                    this.$message.success("审批成功")
                    this.search()
                    this.passForm.travelIds = []
                }
            }).catch(err => {
                this.$message.error("操作失败，请联系管理员~")
            })
        },
        // 一键通过
        passAll() {
            if (this.passForm.travelIds.length == 0) {
                this.$message.warning("请至少选择一条数据~")
                return
            }
            this.$http.post("/passReimbursement", this.passForm).then(res => {
                if (res.data == "success") {
                    this.$message.success("审批成功~")
                    this.search()
                    this.passForm.travelIds = []
                }
            }).catch(err => {
                this.$message.error("操作失败，请联系管理员~")
            })
        },
        // 确定驳回
        cofirmReject() {
            this.$http.post("/rejectReimbursement", this.rejectForm).then(res => {
                if (res.data == "success") {
                    this.$message.success("审批成功")
                    this.search()
                    this.rejectionInput = false
                    this.isShow = false
                }
            }).catch(err => {
                this.$message.error("操作失败，请联系管理员~")
            })
        },
        // 点击驳回
        reject(row) {
            this.rejectForm.rejectionReason = ""
            this.rejectForm.rejectTravelId = row.travelId
            this.rejectionInput = true
        },
        // 获取差旅详细信息
        getReimbursementList() {
            this.$http.post("/getReimbursementList", this.query).then(res => {
                let reimbursementList = res.data.data
                let travelInfos = []
                let reimbursementInfos = []
                // 取出已关联信息中的差旅信息
                for (let i = 0; i < reimbursementList.length; i++) {
                    let reimbursementInfo = {
                        travelForm: reimbursementList[i].travelForm,
                        tickets: reimbursementList[i].tickets,
                        subsidyForm: reimbursementList[i].subsidyForm
                    }
                    reimbursementInfos.push(reimbursementInfo)
                    travelInfos.push(reimbursementList[i].travelForm)
                }
                this.reimbursementInfo = reimbursementInfos
                this.travelForm = travelInfos
                this.total = res.data.count
            }).catch(err => {
                this.$message.error("获取报销单据失败，请联系管理员~")
            })
        }
    },
    created() {
        this.search()
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