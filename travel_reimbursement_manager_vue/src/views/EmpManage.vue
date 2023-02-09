<template>
    <div>
        <!--操作表单-->
        <el-form :inline="true" :model="query" class="demo-form-inline">
            <div id="search">
                <el-form-item>
                    <el-input v-model="query.ename" clearable
                              placeholder="输入员工姓名进行查询"></el-input>
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
                ref="empTable"
                :data="emps"
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
                    type="index"
                    :index="calIndex"
                    width="80"
                    prop="no"
                    label="序号"
                    align="center">
            </el-table-column>
            <el-table-column
                    prop="empno"
                    label="员工编号"
                    align="center">
            </el-table-column>
            <el-table-column
                    prop="ename"
                    label="员工姓名"
                    align="center">
            </el-table-column>
            <el-table-column
                    prop="sex"
                    label="性别"
                    align="center">
            </el-table-column>
            <el-table-column
                    prop="tel"
                    label="电话号码"
                    align="center">
            </el-table-column>
            <div slot="empty">
                <el-empty description="好像没有要找的员工~"></el-empty>
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
        <el-dialog :title="title" :visible.sync="dialogFormVisible"
                   width="550px" @close="closeDialog">
            <el-form :model="empForm" ref="empForm" :rules="rules"
                     id="centerForm" :status-icon="true">
                <el-form-item v-if="title == '修改员工信息'" style="width: 400px"
                              label="员工编号"
                              :label-width="formLabelWidth"
                              prop="empno">
                    <el-input v-model="empForm.empno"
                              autocomplete="off" readonly></el-input>
                </el-form-item>
                <el-form-item style="width: 400px" label="姓名"
                              :label-width="formLabelWidth"
                              prop="ename">
                    <el-input v-model="empForm.ename"
                              autocomplete="off"></el-input>
                </el-form-item>

                <el-form-item label="性别" :label-width="formLabelWidth"
                              prop="sex" id="sex">
                    <el-radio v-model="empForm.sex" label="男">男</el-radio>
                    <el-radio v-model="empForm.sex" label="女">女</el-radio>
                </el-form-item>

                <el-form-item style="width: 400px" label="电话"
                              :label-width="formLabelWidth"
                              prop="tel">
                    <el-input v-model="empForm.tel"
                              autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item v-if="title == '修改员工信息'" style="width: 400px"
                              label="重置密码"
                              :label-width="formLabelWidth">
                    <div style="text-align: left">
                        <el-checkbox v-model="resetPwd" label="重置密码"
                                     border></el-checkbox>
                    </div>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="saveEmp">保 存</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>

export default {
    name: "EmpManage",
    data() {
        return {
            // 是否重置密码
            resetPwd: false,
            // 表单验证
            rules: {
                ename: [
                    {required: true, message: "员工姓名不能为空", trigger: "blur"}
                ],
                tel: [
                    {required: true, message: "电话不能为空", trigger: "blur"},
                    {
                        type: 'number',
                        message: "不能为非数字",
                        trigger: 'blur',
                        transform(value) {
                            return Number(value)
                        }
                    }
                ]
            },
            // 表格数据源
            emps: [],
            // 被选中的项的集合
            selectedItems: [],
            // 查询条件
            query: {
                ename: "",
                // 默认当前页为 1
                page: 1,
                limit: 10
            },
            // 数据总条数，默认为0
            total: 0,
            // 弹出层的标题
            title: "",
            // 弹出层是否显示
            dialogFormVisible: false,
            // 弹出层左侧提示文字的宽度
            formLabelWidth: '120px',
            // 新增的表单数据源
            empForm: {
                empno: "",
                ename: "",
                sex: "男",
                tel: ""
            }
        }
    },
    methods: {
        // 关闭弹出层
        closeDialog(){
            this.$refs["empForm"].clearValidate()
        },
        getRowKeys(row) {
            // id 是后台传递的每行信息唯一标识
            return row.empno;
        },
        // 分页查询，相当于更新一次表格
        search() {
            this.query.page = 1
            this.getEmpList()
        },
        // 条件查询员工信息
        getEmpList() {
            this.$http.post("/empList", this.query).then(res => {
                this.emps = res.data.data
                this.total = res.data.count
            }).catch(err => {
                this.$message.error("员工信息加载失败，请联系管理员~")
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
            this.getEmpList()
        },
        // 自定义索引
        calIndex(index) {
            return index + 1 + (this.query.page - 1) * this.query.limit;
        },
        // 保存员工信息
        saveEmp() {
            this.$refs["empForm"].validate(valid => {
                // 校验成功
                if (valid) {
                    // 默认路径为修改员工信息
                    let url = "/updateEmp"
                    let msg = "修改"
                    // 判断是在新增还是在修改
                    if (this.title == "新增员工信息") {
                        url = "/addEmp"
                        msg = "新增"
                    }
                    this.$http.post(url, this.empForm).then(res => {
                        if (res.data == "success") {
                            this.$message.success(msg + "成功！")
                            // 重新加载表格
                            this.search()
                        } else {
                            this.$message.error(msg + "失败！")
                        }
                    }).catch(err => {
                        this.dialogFormVisible = false
                        this.$message.error(msg + "失败，请联系管理员~")
                    })
                    if (this.title == "修改员工信息" && this.resetPwd) {
                        this.$http.get("/resetPwd/" + this.empForm.empno).then(res => {
                            if (res.data == "error") {
                                this.$message.error("重置密码失败")
                            }
                        }).catch(err => {
                            this.$message.error("重置密码失败，请联系管理员~")
                        })
                    }
                    // 关闭弹出层
                    this.dialogFormVisible = false
                    this.$refs["empTable"].clearSelection()
                    this.selectedItems = []
                }
            })
        },
        // 新增员工信息
        add() {
            this.dialogFormVisible = true
            this.title = "新增员工信息"
            this.$refs["empForm"].clearValidate()
            // 手动重置表单数据
            this.empForm = {
                empno: "",
                ename: "",
                sex: "男",
                tel: ""
            }
        },
        // 修改员工信息
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
            // 打开弹出层
            this.dialogFormVisible = true;
            // 修改标题
            this.title = "修改员工信息"
            // 填充选中项的信息
            this.empForm = this.selectedItems[0]
            this.resetPwd = false
        },
        // 删除员工信息
        del() {
            let selection = this.selectedItems
            if (selection.length == 0) {
                this.$message.warning("请至少选择一条数据~")
                return
            }
            // 确认是否删除
            this.$confirm('确定要删除所选员工的全部信息吗?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                let arr = []
                for (let i = 0; i < selection.length; i++) {
                    arr.push(selection[i].empno)
                }
                // 删除员工信息
                this.$http.post("/delbatch", arr).then((res) => {
                    if (res.data == "success") {
                        this.$message.success("删除成功！")
                        // 刷新表格
                        this.search()
                        this.$refs["empTable"].clearSelection()
                        this.selectedItems = []
                    } else {
                        this.$message.error("删除失败！")
                    }
                }).catch((err) => {
                    this.$message.error("删除员工失败，请联系管理员！")
                })
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