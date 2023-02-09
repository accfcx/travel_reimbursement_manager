<template>
    <div>
        <el-row>
            <!--流程待办-->
            <el-col :span="12">
                <div class="grid-content bg-purple"></div>
            </el-col>
            <!--超预期扣款-->
            <el-col :span="12">
                <div class="grid-content bg-purple-light"></div>
            </el-col>
        </el-row>
        <el-row>
            <!--催办-->
            <el-col :span="12">
                <div class="grid-content bg-purple"></div>
            </el-col>
            <!--提醒-->
            <el-col :span="12">
                <div class="grid-content bg-purple-light"></div>
            </el-col>
        </el-row>
    </div>
</template>

<script>
export default {
    name: "EmpIndex"
}
</script>

<style scoped>
.bg-purple {
    background: #d3dce6;
}
.bg-purple-light {
    background: #e5e9f2;
}
.grid-content {
    border-radius: 4px;
    min-height: 36px;
    border: 1px solid red;
}
</style>