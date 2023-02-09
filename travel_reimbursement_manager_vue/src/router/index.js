import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from '../views/Login';
import Emp from '../views/Emp';
import Manager from '../views/Manager';
import DeptHead from '../views/DeptHead';
import EmpManage from '../views/EmpManage';
import TransportManage from '../views/TransportManage';
import TicketMessage from '../views/TicketMessage';
import Reimbursement from '../views/Reimbursement';
import Financial from '../views/Financial';
import DeptHeadApproval from '../views/DeptHeadApproval';
import FinancialApproval from "../views/FinancialApproval";
import BlankPage from "../views/BlankPage";

Vue.use(VueRouter)

const routes = [
    {
        path: '/',
        name: 'Login',
        component: Login
    },
    {
        path: '/employee',
        name: 'Emp',
        redirect: '/ticketMessage',
        component: Emp,
        children: [
            {
                path: 'ticketMessage',
                name: 'TicketMessage',
                component: TicketMessage
            },
            {
                path: 'reimbursement',
                name: 'Reimbursement',
                component: Reimbursement
            }
        ],
    },
    {
        path: '/manager',
        name: 'Manager',
        component: Manager,
        redirect: '/empManage',
        children: [
            {
                path: 'empManage',
                name: 'EmpManage',
                component: EmpManage
            },
            {
                path: 'transportManage',
                name: 'TransportManage',
                component: TransportManage
            }
        ]

    },
    {
        path: '/deptHead',
        name: 'DeptHead',
        redirect: '/deptApproval',
        component: DeptHead,
        children: [
            {
                path: 'deptHeadApproval',
                name: 'DeptHeadApproval',
                component: DeptHeadApproval
            }
        ]
    },
    {
        path: '/financial',
        name: 'Financial',
        redirect: '/financialApproval',
        component: Financial,
        children: [
            {
                path: 'financialApproval',
                name: 'FinancialApproval',
                component: FinancialApproval
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
    } else if (path.includes("/employee")  && uid.toString().charAt(0) == "3") {
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
        next("/")
    }
})

export default router
