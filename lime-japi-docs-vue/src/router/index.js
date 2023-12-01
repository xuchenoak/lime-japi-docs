import Vue from 'vue'
import Router from 'vue-router'
import HomePage from '@/view/HomePage'


Vue.use(Router)

const constantRoutes = [
    {
        path: '/',
        component: HomePage,
    },
]

const router = new Router({
    mode: 'history', // 去掉url中的#
    scrollBehavior: () => ({ y: 0 }),
    routes: constantRoutes
})
export default router
