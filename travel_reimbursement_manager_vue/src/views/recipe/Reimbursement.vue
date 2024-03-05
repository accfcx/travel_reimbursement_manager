<template>
  <div class="container">
    <div class="top-section">
      <div class="left">
        <i class="el-icon-back" @click="goBack">返回上一页</i>

      </div>
      <div class="right">
        <el-button type="primary" size="small" @click="save">保存</el-button>
        <el-button type="success" size="small" @click="submit">提交审批</el-button>
      </div>
    </div>

    <div class="middle-section">
      <div>出行人</div>
      <div class="traveler-info">
        <i class="el-icon-user-solid"/>
        <span v-if="username">{{ username }}</span>
        <span v-else>加载中...</span>
        <el-button size="mini" @click="addOrEdit">添加/编辑</el-button>
      </div>
    </div>

    <div class="bottom-section">
      <div class="traveler-label">基础信息</div>
      <el-form label-width="100px">
        <!-- 将申请人和申请类型放在一个 div 中，实现一行展示 -->
        <div class="inline-group">
          <el-form-item label="申请人">
            <el-input v-model="applicant"></el-input>
          </el-form-item>
          <el-form-item label="申请类型">
            <el-select v-model="applyType" placeholder="请选择">
              <el-option label="选项1" value="option1"></el-option>
              <el-option label="选项2" value="option2"></el-option>
            </el-select>
          </el-form-item>
        </div>

        <!-- 将出行时间范围和费用承担部门放在一个 div 中，实现一行展示 -->
        <div class="inline-group">
          <el-form-item label="出行时间范围">
            <el-date-picker
                v-model="dateRange"
                type="daterange"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期">
            </el-date-picker>
          </el-form-item>
          <el-form-item label="费用承担部门">
            <el-select v-model="feeDepart" placeholder="请选择" @focus="fetchApiOptions">
              <el-option v-for="option in departmentList" :key="option.value" :label="option.label"
                         :value="option.value"></el-option>
            </el-select>
          </el-form-item>
        </div>

        <!-- 事由保持不变 -->
        <el-form-item label="事由">
          <el-input type="textarea" v-model="remark" :rows="6"></el-input>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      applicant: '',
      applyType: '',
      dateRange: [],
      feeDepart: '',
      remark: '',
      departmentList: [], // 初始化为空数组，等待从后端获取数据填充
      username: '' // 初始化为空字符串，用于存储当前登录用户名
    };
  },
  created() {
    this.fetchCurrentUser();
    this.fetchApiOptions(); // 页面加载时默认请求一次接口选项
  },
  methods: {
    goBack() {
      this.$router.go(-1);
    },
    save() {
      // Handle save action
    },
    submit() {
      // Handle submit action
    },
    addOrEdit() {
      // Handle add or edit action
    },
    fetchCurrentUser() {
      sessionStorage.getItem("loggedName")
    },
    fetchApiOptions() {
      // 假设后端返回格式为 [{ label: '接口1', value: 'api1' }, { label: '接口2', value: 'api2' }]
      this.$http.post('/api/apiOptions')
          .then(response => {
            this.departmentList = response.data;
          })
          .catch(error => {
            console.error('Failed to fetch API options:', error);
          });
    }
  }
};
</script>

<style scoped>
.container {
  padding: 20px;
}


.top-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.middle-section {
  padding: 20px;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column; /* 确保子元素垂直堆叠 */
}


.bottom-section {
  padding: 20px;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column; /* 确保子元素垂直堆叠 */
}

.title {
  display: flex;
  align-items: center;
}

.title span {
  margin-right: 10px;
}

.title img {
  width: 20px;
  height: 20px;
  margin-right: 10px;
}

.traveler-label {
  margin-bottom: 5px;
}

.traveler-info {
  display: flex;
  align-items: center;
  gap: 5px;
}

.inline-group {
  display: flex;
  flex-wrap: nowrap;
  gap: 10px; /* 在表单项之间添加间距 */
}

</style>
