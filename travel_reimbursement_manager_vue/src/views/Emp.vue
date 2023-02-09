<template>
    <div>
        <el-container>
            <el-header>
                <div id="header">
                    <img src="../assets/jxdlogo.png" style="height: 50px">
                    <div style="position:absolute;top: 2px;right: 50px">
                        <label style="font-size: 20px;font-weight: bold;margin-right: 30px">
                            当前登录：{{loggedName}}</label>
                        <el-button type="text" size="medium"
                                   @click="changePwd">修改密码
                        </el-button>
                        <el-button icon="el-icon-switch-button"
                                   type="warning"
                                   circle
                                   style="margin-left: 20px"
                                   @click="logout"></el-button>
                    </div>
                </div>
            </el-header>
            <el-container>
                <el-aside width="200px">
                    <el-col :span="120">
                        <el-menu
                                :default-active=checkedMenu
                                class="el-menu-vertical-demo"
                                background-color="#06436f"
                                text-color="#fff"
                                active-text-color="#6e9bc5"
                                :router="true">
                            <el-menu-item index="/employee/ticketMessage">
                                <i class="el-icon-user-solid"></i>
                                <span slot="title">车票信息</span>
                            </el-menu-item>
                            <el-menu-item index="/employee/reimbursement">
                                <i class="el-icon-truck"></i>
                                <span slot="title">差 旅 报 销</span>
                            </el-menu-item>
                        </el-menu>
                    </el-col>
                </el-aside>
                <el-main>
                    <div id="showView">
                        <router-view/>
                    </div>
                </el-main>
            </el-container>
        </el-container>

        <!--修改密码弹出框-->
        <el-dialog title="修改密码" :visible.sync="dialogFormVisible"
                   @close="closeDialog" width="700px">
            <el-form :model="pwdForm" ref="pwdForm" :rules="rules">
                <el-form-item label="旧密码" :label-width="formLabelWidth"
                              prop="oldPwd">
                    <el-input v-model="pwdForm.oldPwd"
                              autocomplete="off" type="password"
                              placeholder="请输入旧密码"></el-input>
                </el-form-item>
                <el-form-item label="新密码" :label-width="formLabelWidth"
                              prop="newPwd">
                    <el-input v-model="pwdForm.newPwd"
                              autocomplete="off" type="password"
                              placeholder="请输入新密码"></el-input>
                </el-form-item>
                <el-form-item label="确认密码" :label-width="formLabelWidth"
                              prop="confirmPwd">
                    <el-input v-model="pwdForm.confirmPwd"
                              autocomplete="off" type="password"
                              placeholder="请确认密码"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogFormVisible = false">取 消</el-button>
                <el-button type="primary"
                           @click="submit('pwdForm')">确 定
                </el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
export default {
    name: "Manager",
    data() {
        var validatePass = (rule, value, callback) => {
            if (value === '') {
                callback(new Error('请输入密码'));
            } else {
                if (this.pwdForm.confirmPwd !== '') {
                    this.$refs.pwdForm.validateField('confirmPwd');
                }
                callback();
            }
        };
        var validateConfirm = (rule, value, callback) => {
            if (value === '') {
                callback(new Error('请再次输入密码'));
            } else if (value !== this.pwdForm.newPwd) {
                callback(new Error('两次输入密码不一致!'));
            } else {
                callback();
            }
        };
        return {
            rules: {
                oldPwd: [
                    {required: true, message: "旧密码不能为空", trigger: 'blur'}
                ],
                newPwd: [
                    {required: true, validator: validatePass, trigger: 'blur'}
                ],
                confirmPwd: [
                    {
                        required: true,
                        validator: validateConfirm,
                        trigger: 'blur'
                    }
                ]
            },
            dialogFormVisible: false,
            formLabelWidth: '120px',
            pwdForm: {
                uid: sessionStorage.getItem("uid"),
                oldPwd: "",
                newPwd: "",
                confirmPwd: ""
            },
            checkedMenu: this.$route.path,
            loggedName: sessionStorage.getItem("loggedName")
        }
    },
    methods: {
        // 确认修改
        submit() {
            this.$refs["pwdForm"].validate((valid) => {
                if (valid) {
                    this.$http.post("/changePwd",this.pwdForm).then(res => {
                        if (res.data == "成功") {
                            this.$message.success("密码修改成功~")
                        } else if (res.data == "失败") {
                            this.$message.error("密码修改失败~")
                        } else {
                            this.$message.error(res.data)
                        }
                    }).catch(err => {
                        this.$message.error("遇到了一些问题，请联系管理员~")
                    })
                } else {
                    console.log('error submit!!')
                    return false;
                }
                this.$refs["pwdForm"].resetFields()
                this.dialogFormVisible = false
            });
        },
        // 退出登录
        logout() {
            sessionStorage.removeItem("uid");
            sessionStorage.removeItem("loggedName");
            this.$router.push("/");
        },
        // 修改密码
        changePwd() {
            this.dialogFormVisible = true
        },
        closeDialog() {
            this.$refs["pwdForm"].resetFields()
            console.log(this.pwdForm)
        }
    }
}
</script>

<style scoped>
#showView {
    margin-top: 80px;
}

.el-menu-item {
    margin-top: 20px;
}

.el-menu {
    height: 870px;
    width: 199px;
}

.el-header {
    background: #3271ae;
    color: #333;
    line-height: 60px;
    padding-top: 6px;
    padding-left: 50px;
    height: 100px;
}

.el-aside {
    background-color: #6e9bc5;
    color: #333;
    text-align: center;
    line-height: 800px;
    height: 870px;
}

.el-main {
    background-color: #E9EEF3;
    color: #333;
    text-align: center;
}
</style>