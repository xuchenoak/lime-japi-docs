import Vue from 'vue'
import App from './App.vue'

// ant-design UI插件
import Antd from 'ant-design-vue'
import 'ant-design-vue/dist/antd.less'
Vue.use(Antd)

// 日期格式化插件
import Moment from 'moment'
Vue.prototype.$moment = Moment
Vue.prototype.$formatDate = (date, format)=> {
    return Moment(date).format(format)
}

// css动画插件
import animated from 'animate.css'
Vue.use(animated)

// 自定义公共样式
import '@/assets/styles/public-style.css'

// 代码编辑器组件
import CodeEditor from '@/components/CodeEditor'
Vue.component('CodeEditor', CodeEditor)

// 复制组件
import CopyText from '@/components/CopyText'
Vue.component('CopyText', CopyText)

// 空盒组件
import EmptyBox from '@/components/EmptyBox'
Vue.component('EmptyBox', EmptyBox)

// Loading组件
import Loading from '@/components/Loading'
Vue.component('Loading', Loading)

// LimeLogo
import LimeLogo from '@/components/LimeLogo'
Vue.component('LimeLogo', LimeLogo)

// 路由
// import router from '@/router'
Vue.config.productionTip = false
new Vue({
  // router,
  render: h => h(App)
}).$mount('#app')
