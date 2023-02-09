<template>
    <div id="bg">
        <div id="content">
            <el-row style="text-align: center">
                <h1 id="h1" ref="h1"
                    style="margin-top: 50px;margin-bottom: 60px">差 旅 报 销 系 统
                </h1>
                <div style="width: 350px;position:relative;right: -160px">
                    <el-form ref="form" :model="loginForm"
                             label-width="80px" :rules="rules">
                        <el-form-item label="员工编号" prop="uid">
                            <el-input v-model="loginForm.uid"
                                      placeholder="请输入员工编号"></el-input>
                        </el-form-item>

                        <el-form-item label="密码" prop="password">
                            <el-input v-model="loginForm.password"
                                      placeholder="请输入密码"
                                      show-password></el-input>
                        </el-form-item>

                        <el-button type="primary" @click="toLogin"
                                   style="margin-bottom: 30px">登录
                        </el-button>
                    </el-form>
                </div>
            </el-row>
        </div>
    </div>
</template>

<script>
export default {
    name: "Login",
    data() {
        return {
            loginForm: {
                uid: "",
                password: ""
            },
            rules: {
                uid: [
                    {required: true, message: '请输入员工编号', trigger: 'blur'},
                    {
                        type: 'number',
                        message: "不能为非数字",
                        trigger: 'blur',
                        transform(value) {
                            return Number(value)
                        }
                    }

                ],
                password: [
                    {required: true, message: '请输入密码', trigger: 'blur'}
                ]
            }
        }
    },
    methods: {
        toLogin() {
            this.$refs["form"].validate((valid) => {
                // valid为boolean类型，通过校验valid为true，否则为false
                if (!valid) {
                    return // 不通过校验，结束方法
                }
                this.$http.post("/login", this.loginForm).then((res) => {
                    if (res.data.msg == "error") {
                        this.$message.error('员工id或密码错误')
                        this.$router.push("/")
                    } else if (res.data.msg == "manager") {
                        // 跳转到管理员页面
                        sessionStorage.setItem("uid", this.loginForm.uid)
                        sessionStorage.setItem("loggedName", res.data.data)
                        this.$router.push("/manager/empManage")
                    } else if (res.data.msg == "employee") {
                        // 跳转到员工页面
                        sessionStorage.setItem("uid", this.loginForm.uid)
                        sessionStorage.setItem("loggedName", res.data.data)
                        this.$router.push("/employee/ticketMessage")
                    } else if (res.data.msg == "deptHead") {
                        // 跳转到部门经理页面
                        sessionStorage.setItem("uid", this.loginForm.uid)
                        sessionStorage.setItem("loggedName", res.data.data)
                        this.$router.push("/deptHead/deptHeadApproval")
                    } else if (res.data.msg == "financial") {
                        sessionStorage.setItem("uid", this.loginForm.uid)
                        sessionStorage.setItem("loggedName", res.data.data)
                        this.$router.push("/financial/financialApproval")
                    }
                })
            })
        }
    }
}
</script>

<style scoped>
#bg{
    background:url("../assets/loginbg.jpg");
    width:100%;
    height:100%;
    position:fixed;
    background-size:100% 100%;
}

#content {
    background:rgba(172, 208, 242,0.85);
    margin-top: 200px;
    padding: 50px;
    width: 700px;
    margin-left: auto;
    margin-right: auto;
    border-radius: 50px;
}
</style>