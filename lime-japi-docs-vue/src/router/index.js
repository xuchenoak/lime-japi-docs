import Vue from 'vue'
import Router from 'vue-router'
import store from '@/util/store'
import DocsPage from '@/view/DocsPage'
import ProjectCenter from '@/view/ProjectCenter'
import SysInit from '@/view/SysInit'
import NotFoundPage from '@/components/NotFoundPage'


Vue.use(Router)

const constantRoutes = [
    {
        path: '/404',
        component: NotFoundPage,
    },
    {
        path: '/init',
        component: SysInit,
    },
    {
        path: '/docs/:id',
        component: DocsPage,
    },
    {
        path: '/',
        component: ProjectCenter,
    },
    {
        path: '/:viewKey',
        component: ProjectCenter,
    },
]

const router = new Router({
    mode: 'history', // 去掉url中的#
    scrollBehavior: () => ({ y: 0 }),
    routes: constantRoutes
})

// 路由前置事件
router.beforeEach((to, from, next) => {
    if (!store.getters.sysConfig.refresh) {
        store.dispatch("common").then(res => {
            // 若没有初始化则跳转初始化页面
            if (to.path !== '/init' && !res['data'].hasInit) {
                next({ path: '/init' });
            }
            // 若已经初始化再访问初始化页面则跳转主页面
            if (to.path === '/init' && res['data'].hasInit) {
                next({ path: '/' });
            }
        }).finally(()=> {
            next()
        })
    } else {
        next()
    }
})

export default router
