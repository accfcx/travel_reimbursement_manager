import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from '../views/Login';
import BlankPage from "../views/BlankPage";
import HomePage from "../views/HomePage.vue";
import HomeIndex from "../views/home/HomeIndex.vue";
import HomeApproval from "../views/home/HomeApproval.vue";
import HomeRecipe from "../views/home/HomeRecipe.vue";
import HomeSetting from "../views/home/HomeSetting.vue";
import HomeAnalyze from "../views/home/HomeAnalyze.vue";
import HomeProcess from "../views/home/HomeProcess.vue";

import RecipeSQD from "../views/recipe/RecipeSQD.vue";
import RecipeBXD from "../views/recipe/RecipeBXD.vue";
import WorkOverTime from "../views/recipe/WorkOvertime.vue";
import ReimbursementV2 from "../views/recipe/Reimbursement.vue";

Vue.use(VueRouter)

const routes = [
    {
        path: '/',
        name: 'Login',
        component: Login
    },
    {
        path: '/panel',
        component: () =>
            import ('../views/process/panel')
    },
    {
        path: '/instance',
        component: () =>
            import ('../views/process/instance')
    },
    {
        path: '/recipeSQD',
        name: 'RecipeSQD',
        component: RecipeSQD
    },
    {
        path: '/recipeBXD',
        name: 'RecipeBXD',
        component: RecipeBXD
    },
    {
        path: '/workOverTime',
        name: 'WorkOverTime',
        component: WorkOverTime
    },
    {
        path: '/reimbursement',
        name: 'Reimbursement',
        component: ReimbursementV2
    },
    {
        path: '/home',
        name: 'HomePage',
        component: HomePage,
        // redirect: '/index',
        children: [
            {
                path: 'index',
                name: 'HomeIndex',
                component: HomeIndex
            },
            {
                path: 'approval',
                name: 'HomeApproval',
                component: HomeApproval
            },
            {
                path: 'recipe',
                name: 'HomeRecipe',
                component: HomeRecipe
            },
            {
                path: 'setting',
                name: 'HomeSetting',
                component: HomeSetting
            },
            {
                path: 'analyze',
                name: 'HomeAnalyze',
                component: HomeAnalyze
            },
            {
                path: 'process',
                name: 'HomeProcess',
                component: HomeProcess
            }
        ]
    },
    {
        path: '/blankPage',
        name: 'BlankPage',
        component: BlankPage
    }

]

const router = new VueRouter({
    routes
})

router.beforeEach((to, from, next) => {
    // 检查session中是否有uid
    let path = to.fullPath
    let uid = sessionStorage.getItem("uid")
    if (path == "/" || path == "/blankPage") {
        // 访问的是登录界面，直接放行
        next();
    } else if (uid == null) {
        // 未登录状态下访问登录后的页面，需要登录后访问
        next("/")
    } else if (path.includes("/employee") && uid.toString().charAt(0) == "3") {
        // 访问员工界面，且当前登录的是员工，直接放行
        next();
    } else if (path.includes("/manager") && uid.toString().charAt(0) == "1") {
        // 访问管理员界面，且当前登录的是管理员，直接放行
        next();
    } else if (path.includes("/deptHead") && uid.toString().charAt(0) == "2") {
        // 访问部门经理界面，且当前登录的是部门经理，直接放行
        next();
    } else if (path.includes("/financial") && uid.toString().charAt(0) == "4") {
        // 访问部门经理界面，且当前登录的是部门经理，直接放行
        next();
    } else {
        next()
    }
})

export default router
