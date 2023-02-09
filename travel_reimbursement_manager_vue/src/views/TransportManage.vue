<template>
    <div>
        <!--操作表单-->
        <el-form :inline="true" :model="query" class="demo-form-inline">
            <div id="search">
                <el-form-item>
                    <el-input v-model="query.transportation" clearable
                              placeholder="输入交通工具进行查询"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="search"
                               icon="el-icon-search">
                        查询
                    </el-button>
                </el-form-item>
            </div>

            <el-form-item id="handle">
                <el-button type="primary" @click="add" icon="el-icon-plus">
                    新增
                </el-button>
                <el-button type="primary" @click="update"
                           icon="el-icon-edit">
                    修改
                </el-button>
                <el-button type="danger" @click="del" icon="el-icon-delete">
                    删除
                </el-button>
            </el-form-item>
        </el-form>

        <!--表格-->
        <el-table
                ref="transTable"
                :data="transportations"
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
            <!--索引列-->
            <el-table-column
                    width="80"
                    prop="transId"
                    label="编号"
                    align="center">
            </el-table-column>
            <el-table-column
                    prop="transportation"
                    label="交通工具"
                    align="center">
            </el-table-column>
            <div slot="empty">
                <el-empty description="好像没有要找的交通工具~"></el-empty>
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

        <el-dialog :title="title" :visible.sync="dialogFormVisible"
                   width="550px" @close="closeDialog">
            <el-form :model="transportationForm" ref="transportationForm"
                     :rules="rules"
                     id="centerForm" :status-icon="true">
                <el-form-item v-if="title == '修改交通工具'" style="width: 400px"
                              label="编号"
                              :label-width="formLabelWidth"
                              prop="transId">
                    <el-input v-model="transportationForm.transId"
                              autocomplete="off" readonly></el-input>
                </el-form-item>
                <el-form-item style="width: 400px" label="交通工具名称"
                              :label-width="formLabelWidth"
                              prop="transportation">
                    <el-input v-model="transportationForm.transportation"
                              autocomplete="off"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="saveTransportation">保
                                                                      存
                </el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
export default {
    name: "TransportManage",
    data() {
        return {
            title: "",
            // 表单验证
            rules: {
                transportation: [
                    {required: true, message: "交通工具名称不能为空", trigger: "blur"}
                ],
            },
            formLabelWidth: '120px',
            // 新增数据
            transportation: "",
            dialogFormVisible: false,
            // 被选中的项的集合
            selectedItems: [],
            total: 0,
            // 查询条件
            query: {
                transportation: "",
                // 默认当前页为 1
                page: 1,
                limit: 10
            },
            // 表格数据源
            transportations: [],
            transportationForm: {
                transId: "",
                transportation: ""
            },
        }
    },
    methods: {
        closeDialog() {
            this.$refs["transportationForm"].clearValidate()
        },
        // 获取交通工具列表
        getTransportationList() {
            this.$http.post("/transListByPage", this.query).then(res => {
                this.transportations = res.data.data
                this.total = res.data.count
            }).catch(err => {
                this.$message.error("交通工具信息加载失败，请联系管理员~")
            })
        },
        // 当每页多少条被改变时，val代表改变后的每页多少条
        handleSizeChange(val) {
            this.query.limit = val
            this.search()
        },
        // 当前页被改变时，val代表当前页
        handleCurrentChange(val) {
            this.query.page = val
            this.getTransportationList()
        },
        getRowKeys(row) {
            // id 是后台传递的每行信息唯一标识
            return row.transId;
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
        // 分页查询，相当于更新一次表格
        search() {
            this.query.page = 1
            this.getTransportationList()
        },
        // 新增交通工具信息
        add() {
            this.dialogFormVisible = true
            this.title = "新增交通工具"
            // 手动重置表单数据
            this.$refs["transportationForm"].clearValidate()
            this.transportationForm = {
                transId: "",
                transportation: ""
            }
        },
        // 修改交通工具信息
        update() {
            // 判断是否选中数据
            if (this.selectedItems.length == 0) {
                this.$message.warning("请至少选择一条数据~")
                return
            }
            // 判断是否选中多条数据
            if (this.selectedItems.length > 1) {
                this.$message.warning("只能修改一条数据~");
                return;
            }
            // 修改标题
            this.title = "修改交通工具"
            // 打开弹出层
            this.dialogFormVisible = true;
            // 填充选中项的信息
            this.transportationForm = this.selectedItems[0]
        },
        // 删除交通工具信息
        del() {
            let selection = this.selectedItems
            if (selection.length == 0) {
                this.$message.warning("请至少选择一条数据~")
                return
            }
            // 确认是否删除
            this.$confirm('确定要删除所选交通工具吗?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                let transIds = []
                for (let i = 0; i < selection.length; i++) {
                    transIds.push(selection[i].transId)
                }
                // 删除交通工具
                this.$http.post("/delTransportation", transIds).then((res) => {
                    if (res.data == "success") {
                        this.$message.success("删除成功！")
                        // 刷新表格
                        this.search()
                        this.$refs["transTable"].clearSelection()
                        this.selectedItems = []
                    } else {
                        this.$message.error("删除失败~")
                    }
                }).catch((err) => {
                    this.$message.error("删除失败，请联系管理员！")
                })
            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '已取消删除'
                });
            });
        },
        saveTransportation() {
            this.$refs["transportationForm"].validate(valid => {
                // 校验成功
                if (valid) {
                    // 默认路径为修改交通工具
                    let url = "/updateTransportation"
                    let msg = "修改"
                    // 判断是在新增还是在修改
                    if (this.title == "新增交通工具") {
                        url = "/addTransportation"
                        msg = "新增"
                    }
                    this.$http.post(url, this.transportationForm).then(res => {
                        if (res.data == "success") {
                            this.$message.success(msg + "成功！")
                            // 重新加载表格
                            this.search()
                        } else {
                            this.$message.error(msg + "失败！")
                        }
                    }).catch(err => {
                        this.$message.error(msg + "失败，请联系管理员~")
                    })
                    // 关闭弹出层
                    this.dialogFormVisible = false
                    this.$refs["transTable"].clearSelection()
                    this.selectedItems = []
                }
            })
        }
    },
    created() {
        this.search()
    }
}
</script>

<style scoped>
#handle {
    position: relative;
    right: -690px;
}

#search {
    position: absolute;
    left: 220px;
}

#page {
    text-align: center;
}

#sex {
    text-align: left;
}

#centerForm {
    text-align: center;
}
</style>