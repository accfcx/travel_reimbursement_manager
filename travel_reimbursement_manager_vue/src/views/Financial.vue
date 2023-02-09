<template>
    <div>
        <el-container>
            <el-header>
                <div id="header">
                    <div>
                        <img src="../assets/jxdlogo.png" style="height: 50px">
                    </div>
                    <div style="position:absolute;top: 2px;right: 50px;font-size: 20px;font-weight: bold">
                        当前登录：{{loggedName}}
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
                            <el-menu-item index="/financial/financialApproval">
                                <i class="el-icon-s-check"></i>
                                <span slot="title">审 批</span>
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
    </div>
</template>

<script>
export default {
    name: "Financial",
    data() {
        return {
            checkedMenu: this.$route.path,
            loggedName: sessionStorage.getItem("loggedName")
        }
    },
    methods: {
        logout() {
            sessionStorage.removeItem("uid");
            sessionStorage.removeItem("loggedName");
            this.$router.push("/");
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
    background-color: #3271ae;
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