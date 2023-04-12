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


export default new Router({
    mode: 'hash', // 去掉url中的#
    scrollBehavior: () => ({ y: 0 }),
    routes: constantRoutes
})
