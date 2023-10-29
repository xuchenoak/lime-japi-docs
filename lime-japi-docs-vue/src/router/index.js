import Vue from 'vue'
import Router from 'vue-router'
import DocsPage from '../view/DocsPage'
import ProjectCenter from '@/view/ProjectCenter'


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
]


export default new Router({
    mode: 'history', // 去掉url中的#
    scrollBehavior: () => ({ y: 0 }),
    routes: constantRoutes
})
