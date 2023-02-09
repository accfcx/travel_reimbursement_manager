<template>

</template>

<script>
export default {
    name: "BlankPage",
    data(){
        this.$router.replace({
            path: '/employee/reimbursement', //让路由跳转回当前页面
            name: 'Reimbursement'
        })
        return {}
    }
}
</script>

<style scoped>

</style>