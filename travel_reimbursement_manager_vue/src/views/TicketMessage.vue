<template>
    <div>
        <!--操作表单-->
        <el-form :inline="true" :model="query" class="demo-form-inline">
            <div class="block">
                <el-date-picker
                        v-model="queryDate"
                        type="daterange"
                        range-separator="至"
                        start-placeholder="开始日期"
                        end-placeholder="结束日期"
                        value-format="”yyyy-MM-dd”"
                        @change="setQueryTime">
                </el-date-picker>
            </div>
            <el-form-item id="search">
                <el-button type="primary" @click="search" icon="el-icon-search">
                    查询
                </el-button>
            </el-form-item>

            <el-form-item id="handle">
                <el-button type="primary" @click="add" icon="el-icon-plus">新增
                </el-button>
                <el-button type="danger" @click="del" icon="el-icon-delete">删除
                </el-button>
            </el-form-item>
        </el-form>

        <!--表格-->
        <el-table
                ref="ticketsTable"
                :data="tickets"
                stripe
                style="width: 100%;font-size: 17px"
                height="550px"
                @select="selectRow"
                @select-all="selectAll"
                :header-cell-style="{background:'#2e59a7',color:'white'}"
                :border="true"
                :row-key="getRowKeys">
            <!--复选框-->
            <el-table-column type="selection" width="55"
                             :reserve-selection="true"/>
            <el-table-column
                    prop="tid"
                    label="车票编号"
                    width="180"
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
            <el-table-column
                    prop="travelDesc"
                    label="出差说明"
                    align="center"
                    :formatter="descFormat">
            </el-table-column>
            <div slot="empty">
                <el-empty description="好像没有要找的车票~"></el-empty>
            </div>
        </el-table>

        <!--分页功能-->
        <div id="page">
            <el-pagination
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page=query.page
                    :page-sizes="[10, 20, 30, 40]"
                    :page-size=query.limit
                    layout="total, sizes, prev, pager, next, jumper"
                    :total=total
                    :background="true">
            </el-pagination>
        </div>

        <!--弹出层-->
        <el-dialog title="新增车票" :visible.sync="dialogFormVisible"
                   width="1500px" @close="closeAdd">
            <el-row>
                <!--左侧表单区域-->
                <el-col :span="12">
                    <div class="grid-content bg-purple" id="left_content">
                        <div id="addForm">
                            <!--标题-->
                            <div style="margin-bottom: 20px">
                                <i class="el-icon-edit"/>&nbsp&nbsp&nbsp&nbsp
                                <h2 style="display: inline-block">
                                    车票信息</h2>
                            </div>

                            <el-form :model="ticketForm" ref="ticketForm"
                                     :rules="rules" id="addForm"
                                     :status-icon="true">
                                <el-form-item label="出差类型"
                                              :label-width="formLabelWidth"
                                              id="select"
                                              prop="travelTypeId"
                                              ref="travelType">
                                    <el-select v-model="ticketForm.travelTypeId"
                                               placeholder="请选择出差类型"
                                               style="width: 350px"
                                               @change="isReturn">
                                        <el-option
                                                v-for="item in
                                                travelTypeForm"
                                                :label="item.travelTypeName"
                                                :value="item.travelTypeId">
                                        </el-option>
                                    </el-select>
                                </el-form-item>

                                <el-form-item style="width: 470px"
                                              label="出发地点"
                                              :label-width="formLabelWidth"
                                              prop="startPoint">
                                    <el-input
                                            v-model="ticketForm.startPoint"
                                            autocomplete="off"
                                            placeholder="请输入出发地点"
                                            @blur="idsLeave"></el-input>
                                </el-form-item>

                                <el-form-item style="width: 470px" label="到达地点"
                                              :label-width="formLabelWidth"
                                              prop="arrivalPoint"
                                              ref="arrivalPoint">
                                    <el-input
                                            v-model="ticketForm.arrivalPoint"
                                            autocomplete="off"
                                            placeholder="请输入到达地点"></el-input>
                                </el-form-item>

                                <el-form-item label="交通工具"
                                              :label-width="formLabelWidth"
                                              id="select"
                                              prop="transId">
                                    <el-select v-model="ticketForm.transId"
                                               placeholder="请选择交通工具"
                                               style="width: 350px">
                                        <el-option
                                                v-for="item in
                                                transportationForm"
                                                :label="item.transportation"
                                                :value="item.transId">
                                        </el-option>
                                    </el-select>
                                </el-form-item>
                                <el-form-item style="width: 470px" label="车票价格"
                                              :label-width="formLabelWidth"
                                              prop="price">
                                    <el-input v-model="ticketForm.price"
                                              autocomplete="off"
                                              placeholder="请输入车票价格"></el-input>
                                </el-form-item>
                                <el-form-item style="width: 470px"
                                              label="出发时间"
                                              :label-width="formLabelWidth"
                                              prop="startTime">
                                    <el-date-picker
                                            v-model="ticketForm.startTime"
                                            type="datetime"
                                            placeholder="出发时间"
                                            value-format="yyyy-MM-dd HH:mm:ss"
                                            style="width:350px;position:relative;left: 0">
                                    </el-date-picker>
                                </el-form-item>
                                <el-form-item style="width: 470px"
                                              label="到达时间"
                                              :label-width="formLabelWidth"
                                              prop="arrivalTime">
                                    <el-date-picker
                                            v-model="ticketForm.arrivalTime"
                                            type="datetime"
                                            placeholder="到达时间"
                                            value-format="yyyy-MM-dd HH:mm:ss"
                                            style="width:350px;position:relative;left: 0">
                                    </el-date-picker>
                                </el-form-item>
                            </el-form>
                        </div>
                    </div>
                </el-col>
                <!--右侧上传图片区域-->
                <el-col :span="12">
                    <div class="grid-content bg-purple-light">
                        <div style="margin-bottom: 20px">
                            <i class="el-icon-picture-outline"/>&nbsp&nbsp&nbsp&nbsp
                            <h2 style="display: inline-block">车票图片</h2>
                        </div>
                        <el-upload
                                class="avatar-uploader"
                                action="#"
                                :show-file-list="false"
                                :on-success="handleAvatarSuccess"
                                :before-upload="beforeAvatarUpload"
                                :http-request="uploadImg"
                                id="upload_box">
                            <img v-if="imageUrl" :src="imageUrl"
                                 class="avatar"
                                 style="width: 400px;height:270px">
                            <i v-else
                               class="el-icon-plus avatar-uploader-icon"
                               id="upload_icon"></i>
                        </el-upload>
                        <div id="upload_desc">
                            <label style="color: gray;margin-right: 50px">
                                点击图片更新图片</label>
                            <el-button @click="closeAdd"
                                       type="text" size="medium">点此删除图片
                            </el-button>
                        </div>
                    </div>
                </el-col>
            </el-row>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogFormVisible = false;closeAdd">取 消
                </el-button>
                <el-button type="primary" @click="saveTicket">保 存</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
export default {
    name: "TicketMessage",
    data() {
        // 自定义时间校验规则
        let validateArrivalTime = (rule, value, callback) => {
            let tickets = this.tickets;
            let arrival = new Date(value);
            // 保证到达时间不能在已有车票的行程之间
            for (let i = 0; i < tickets.length; i++) {
                let startedTime = new Date(tickets[i].startTime);
                let arrivedTime = new Date(tickets[i].arrivalTime);
                if (arrival > startedTime && arrival < arrivedTime) {
                    callback(new Error('到达时间与已有车票行程冲突~'));
                }
            }
            if (this.ticketForm.startTime != null || this.ticketForm.startTime != "") {
                let start = new Date(this.ticketForm.startTime);
                if (arrival <= start) {
                    callback(new Error('到达时间不能早于出发时间~'));
                } else {
                    callback();
                }
            } else {
                callback();
            }
        }
        var validateStartTime = (rule, value, callback) => {
            let tickets = this.tickets;
            let start = new Date(value)
            // 保证到达时间不能在已有车票的行程之间
            for (let i = 0; i < tickets.length; i++) {
                let startedTime = new Date(tickets[i].startTime);
                let arrivedTime = new Date(tickets[i].arrivalTime);
                if (start > startedTime && start < arrivedTime) {
                    callback(new Error('出发时间与已有车票行程冲突~'));
                }
            }
            if (this.ticketForm.startTime != null || this.ticketForm.startTime != "") {
                let arrival = new Date(this.ticketForm.arrivalTime)
                if (arrival <= start) {
                    callback(new Error('出发时间不能晚于到达时间~'));
                } else {
                    callback();
                }
            } else {
                callback();
            }
        }
        var validateStartPoint = (rule, value, callback) => {
            if (value == this.ticketForm.arrivalPoint) {
                callback(new Error('出发地与到达地不能相同~'));
            } else if (this.ticketForm.travelTypeId == 2 && value == "济南") {
                callback(new Error('出差类型为返回的车票出发地不能为济南~'));
            } else {
                callback();
            }
        }
        var validateArrivalPoint = (rule, value, callback) => {
            if (value == this.ticketForm.startPoint) {
                callback(new Error('出发地与到达地不能相同~'));
            } else if (this.ticketForm.travelTypeId == 1 && value == "济南") {
                callback(new Error('出差类型为出差的车票到达地不能为济南~'));
            } else {
                callback();
            }
        }
        return {
            // 车票表格
            ticketsTable: [],
            // 文件在后台的地址
            backstageURL: '',
            // 上传图片的地址
            imageUrl: '',
            // 出差方式列表
            travelTypeForm: [],
            // 交通工具列表
            transportationForm: [],
            // 表单验证
            rules: {
                startPoint: [
                    {required: true, message: "出发地点不能为空", trigger: 'blur'},
                    {validator: validateStartPoint, trigger: 'blur'}
                ],
                arrivalPoint: [
                    {required: true, message: "到达地点不能为空", trigger: 'blur'},
                    {validator: validateArrivalPoint, trigger: 'blur'}
                ],
                transId: [
                    {required: true, message: "交通工具不能为空", trigger: 'change'}
                ],
                price: [
                    {required: true, message: "车票价格不能为空", trigger: 'blur'},
                    {
                        type: 'number',
                        message: "不能为非数字",
                        trigger: 'blur',
                        transform(value) {
                            return Number(value)
                        }
                    }
                ],
                travelTypeId: [
                    {required: true, message: "出差类型不能为空", trigger: 'change'}
                ],
                startTime: [
                    {validator: validateStartTime, trigger: 'blur'},
                    {
                        type: 'string',
                        required: true,
                        message: "出发时间不能为空",
                        trigger: 'change'
                    }
                ],
                arrivalTime: [
                    {
                        type: 'string',
                        required: true,
                        message: "到达时间不能为空",
                        trigger: 'change'
                    },
                    {validator: validateArrivalTime, trigger: 'blur'}
                ]
            },
            // 表格数据源，从后台接收
            tickets: [],
            // 被选中的项的集合
            selectedItems: [],
            // 查询条件
            query: {
                empno: sessionStorage.getItem("uid"),
                startTime: "",
                arrivalTime: "",
                // 默认当前页为 1
                page: 1,
                limit: 10
            },
            // 数据总条数，默认为0
            total: 0,
            // 弹出层是否显示
            dialogFormVisible: false,
            // 弹出层左侧提示文字的宽度
            formLabelWidth: '120px',
            // 新增的表单数据源，传到后台
            ticketForm: {
                empno: "",
                travelTypeId: "",
                startPoint: "",
                arrivalPoint: "",
                startTime: "",
                arrivalTime: "",
                transId: "",
                price: "",
                ticketPictureUrl: ""
            },
            // 获取查询时标准时间
            queryDate: [],
        }
    },
    methods: {
        // 是否从济南出发
        idsLeave() {
            if (this.ticketForm.startPoint == "济南" &&
                    this.ticketForm.arrivalPoint != "济南") {
                this.ticketForm.travelTypeId = 1
            }
        },
        // 判断是否为返回
        isReturn() {
            // 如果类型为返回，那么到达地为济南
            if (this.ticketForm.travelTypeId == 2) {
                this.ticketForm.arrivalPoint = "济南"
                this.ticketForm.startPoint = ""
                this.$nextTick(() => {
                    this.$refs["arrivalPoint"].clearValidate()
                })
            } else {
                this.ticketForm.arrivalPoint = ""
            }
        },
        // 关闭新增窗口，若未保存则删掉后台上传上去的文件
        closeAdd() {
            if (this.imageUrl != "") {
                this.imageUrl = ""
                this.$http.post("/delFile", this.backstageURL.toString()).then(res => {
                    if (res.data == "error") {
                        this.$message.error("遇到了一点小问题~")
                    }
                }).catch(err => {
                    this.$message.error("请求发送错误！")
                })
            }
        },
        getRowKeys(row) {
            // id 是后台传递的每行信息唯一标识
            return row.tid;
        },
        // 图片上传
        uploadImg(item) {
            let param = new FormData();
            param.append("image", item.file)
            this.$http.post("/uploadImg", param).then(res => {
                this.imageUrl = this.$http.defaults.baseURL + res.data
                this.backstageURL = res.data
                this.ticketForm.ticketPictureUrl = res.data
            }).catch(() => {
                this.$message.error("请求发送错误！")
            })

        },
        // 文件上传成功时的钩子
        handleAvatarSuccess(res, file) {
            this.imageUrl = URL.createObjectURL(file.raw);
        },
        // 上传文件之前的钩子，参数为上传的文件
        beforeAvatarUpload(file) {
            const isJPG = file.type === 'image/jpeg';
            const isLt2M = file.size / 1024 / 1024 < 2;
            if (!isJPG) {
                this.$message.error('上传图片只能是 JPG 格式!');

            }
            if (!isLt2M) {
                this.$message.error('上传图片大小不能超过 2MB!');
            }
            if (this.imageUrl != "" && isJPG && isLt2M) {
                this.closeAdd()
            }
            return isJPG && isLt2M;
        },
        // 获取出差类型
        getTravelType() {
            this.$http.get("/travelTypeList").then(res => {
                this.travelTypeForm = res.data.data
            }).catch(err => {
                this.$message.error("出差方式加载失败，请联系管理员~")
            })
        },
        // 获取交通工具
        getTransportation() {
            this.$http.get("/transList").then(res => {
                this.transportationForm = res.data.data
            }).catch(err => {
                this.$message.error("交通工具加载失败，请联系管理员~")
            })
        },
        // 出差说明字段为空时默认显示
        descFormat(cellValue) {
            if (cellValue.travelDesc == null || cellValue.travelDesc == "") {
                return "无"
            } else {
                return cellValue.travelDesc
            }
        },
        // 获取查询时选中的时间
        setQueryTime() {
            if (this.queryDate != null) {
                this.query.startTime = this.queryDate[0].substring(1,
                        this.queryDate[0].length - 1);
                this.query.arrivalTime = this.queryDate[1].substring(1,
                        this.queryDate[1].length - 1)
            } else {
                this.query.startTime = "";
                this.query.arrivalTime = "";
            }
        },
        // 分页查询，相当于更新一次表格
        search() {
            this.query.page = 1
            this.getTicketList()
        },
        // 根据时间查询车票信息
        getTicketList() {
            this.$http.post("/ticketListByPage", this.query).then(res => {
                this.tickets = res.data.data
                this.total = res.data.count
            }).catch(err => {
                this.$message.error("车票信息加载失败，请联系管理员~")
            })
        },
        // 全选   selection：被选中项的集合
        selectAll(selection) {
            this.selectedItems = selection
        },
        // 当对某行数据选中或取消选中时   selection：被选中项的集合  row：最近一次选中行
        selectRow(selection, row) {
            // 把选中项的集合赋给data中的集合
            this.selectedItems = selection
        },
        // 当每页多少条被改变时，val代表改变后的每页多少条
        handleSizeChange(val) {
            this.query.limit = val
            this.search()
        },
        // 当前页被改变时，val代表当前页
        handleCurrentChange(val) {
            this.query.page = val
            this.getTicketList()
        },
        // 自定义索引
        calIndex(index) {
            return index + 1 + (this.query.page - 1) * this.query.limit;
        },
        // 保存车票信息
        saveTicket() {
            this.$refs["ticketForm"].validate(valid => {
                // 校验成功
                if (valid) {
                    this.$http.post("/addTicket", this.ticketForm).then(res => {
                        if (res.data == "success") {
                            this.$message.success("新增成功！")
                        } else {
                            this.$message.error("新增失败！")
                        }
                        // 关闭弹出层
                        this.dialogFormVisible = false
                        // 重新加载表格
                        this.search()
                    }).catch(err => {
                        this.dialogFormVisible = false
                        this.$message.error("新增失败，请联系管理员~")
                    })
                }
            })
        },
        // 新增车票信息
        add() {
            this.resetAddForm()
        },
        // 重置新增表单
        resetAddForm() {
            this.ticketForm = {
                empno: sessionStorage.getItem("uid"),
                travelTypeId: "",
                startPoint: "",
                arrivalPoint: "",
                startTime: "",
                arrivalTime: "",
                transId: "",
                price: "",
                travelDesc: ""
            }
            this.imageUrl = ""
            this.dialogFormVisible = true
            this.$nextTick(e => {
                this.$refs["ticketForm"].resetFields()
            })
        },
        // 删除车票信息
        del() {
            let selection = this.selectedItems
            if (selection.length == 0) {
                this.$message.warning("请至少选择一条数据~")
                return
            }
            let tids = []
            // 已关联差旅的车票
            let associatedTids = []
            // 判断要删除的车票是否已关联差旅信息
            for (let i = 0; i < selection.length; i++) {
                tids.push(selection[i].tid)
                if (selection[i].travelId != null) {
                    associatedTids.push(selection[i].tid)
                }
            }
            if (associatedTids.length != 0) {
                this.$message.warning("编号为" + associatedTids.toString() +
                        "的车票已关联差旅，不可删除! 请重新选择~");
                return
            }
            // 确认是否删除
            this.$confirm('确定要删除所选车票吗?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                this.$http.post("/delTickets", tids).then((res) => {
                    if (res.data == "success") {
                        this.$message.success("删除成功！")
                        // 刷新表格
                        this.search()
                        this.$refs["ticketsTable"].clearSelection()
                    } else {
                        this.$message.error("删除失败！")
                    }
                }).catch((err) => {
                    this.$message.error("删除失败，请联系管理员！")
                });
            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '已取消删除'
                });
            });
        }
    },
    created() {
        this.search()
        this.getTransportation()
        this.getTravelType()
    }
}
</script>

<style scoped>
#upload_desc {
    margin-top: 100px;
    font-size: 18px;
}

#left_content {
    border-right: 2px solid gray;
}

#upload_icon {
    margin-top: 20px;
    width: 200px;
    height: 200px;
    margin-left: auto;
    background-color: #fbfdff;
    border-radius: 10px;
}

#upload_box {
    border: 1px dotted gray;
    width: 400px;
    height: 270px;
    margin-left: 200px;
    background-color: #d9d9d9;
}

#addForm {
    margin-left: 70px;
}

#select {
    text-align: left;
}

#handle {
    position: relative;
    right: -570px;
}

#search {
    position: absolute;
    left: 600px;
}

.block {
    float: left;
}

#page {
    text-align: center;
}

.avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
}

.avatar-uploader .el-upload:hover {
    border-color: #409EFF;
}

.avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;
}

.avatar {
    width: 178px;
    height: 178px;
    display: block;
}
</style>