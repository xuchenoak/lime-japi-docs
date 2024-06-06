import Vue from 'vue'
import App from './App.vue'
// ant-design UI插件
import Antd, {Spin} from 'ant-design-vue'
import 'ant-design-vue/dist/antd.less'
Vue.use(Antd)
Spin.setDefaultIndicator({
  indicator: h => {
    return <a-icon type="loading" style="font-size: 24px" spin />
  }
});

// 自定义公共样式
import '@/assets/styles/public-style.css'

// 代码编辑器组件
import CodeEditor from '@/components/CodeEditor'
Vue.component('CodeEditor', CodeEditor)

// 代码查看器组件
import CodeViewer from '@/components/CodeViewer'
Vue.component('CodeViewer', CodeViewer)

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

// GiteeBox
import GiteeBox from '@/components/GiteeBox'
Vue.component('GiteeBox', GiteeBox)

// PoweredBox
import PoweredBox from '@/components/PoweredBox'
Vue.component('PoweredBox', PoweredBox)

// DrawerBox
import DrawerBox from '@/components/DrawerBox'
Vue.component('DrawerBox', DrawerBox)

// WhyBox
import WhyBox from '@/components/WhyBox'
Vue.component('WhyBox', WhyBox)

// WhyBoxText
import WhyBoxText from '@/components/WhyBoxText'
Vue.component('WhyBoxText', WhyBoxText)

// 路由
import router from '@/router'
import store from '@/util/store'
Vue.config.productionTip = false
new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
