import Vue from 'vue'
import Router from 'vue-router'
import store from '../util/store'
import DocsPage from '../view/DocsPage'
import ProjectCenter from '@/view/ProjectCenter'
import HomePage from '@/view/HomePage'
import {common} from "@/api/docsConfig";


Vue.use(Router)

const constantRoutes = [
    {
        path: '/',
        component: ProjectCenter,
    },
    {
        path: '/docs/:id',
        component: DocsPage,
    },
    {
        path: '/home',
        component: HomePage,
    },
]

const router = new Router({
    mode: 'history', // 去掉url中的#
    scrollBehavior: () => ({ y: 0 }),
    routes: constantRoutes
})

// 路由前置事件
router.beforeEach((to, from, next) => {
    if (!store.getters.refresh) {
        // store.commit("setLoading", true)
        common().then(res => {
            store.commit("setCommonConfig", res["data"])
        }).finally(()=> {
            // store.commit("setLoading", false)
            next()
        })
    } else {
        next()
    }
})

export default router
