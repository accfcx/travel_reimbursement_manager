<template>
  <div>
    <el-container>
      <el-header>
        <div id="header">
          <div>
          </div>
          <div style="position:absolute;top: 2px;right: 50px;font-size: 20px;font-weight: bold">
            姓名：{{ loggedName }}
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
                background-color="#fff"
                text-color="#444"
                active-text-color="#000"
                :router="true">
              <el-menu-item index="/home/index">
                <i class="el-icon-s-home"></i>
                <span slot="title">首 页</span>
              </el-menu-item>
              <el-menu-item index="/home/approval">
                <i class="el-icon-user-solid"></i>
                <span slot="title">审 批 中 心</span>
              </el-menu-item>
              <el-menu-item index="/home/recipe">
                <i class="el-icon-s-order"></i>
                <span slot="title">单 据 列 表</span>
              </el-menu-item>
              <el-menu-item index="/home/setting">
                <i class="el-icon-s-tools"></i>
                <span slot="title">个 人 设 置</span>
              </el-menu-item>
              <el-menu-item index="/home/analyze">
                <i class="el-icon-s-data"></i>
                <span slot="title">数 据 分 析</span>
              </el-menu-item>
              <el-menu-item index="/home/process">
                <i class="el-icon-s-ticket"></i>
                <span slot="title">流 程 管 理</span>
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
  name: "Manager",
  data() {
    return {
      checkedMenu: this.$route.path,
      loggedName: sessionStorage.getItem("loggedName"),
      uid: sessionStorage.getItem("uid")
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
}

.el-menu-item {
  margin-top: 5px;
}

.el-menu {
  height: 870px;
  width: 199px;
}

.el-header {
  background-color: #ffffff;
  color: #333;
  line-height: 60px;
  padding-top: 6px;
  padding-left: 50px;
  height: 100px;
}

.el-aside {
  background-color: #ff7d41;
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