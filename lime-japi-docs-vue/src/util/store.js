import Vue from 'vue'
import Vuex from 'vuex'
import {common} from "@/api/sysConfig";

Vue.use(Vuex)
const store = new Vuex.Store({
    strict: true,
    state: {
        loading: false,
        sysConfig: {
            refresh: false,
            hasInit: false,
            hasLogin: false,
            sysName: '接口文档中心',
            sysSlogan: '这是一个简单的Java接口文档',
            logoUrl: ''

        }
    },
    mutations: {
        setSysConfig(state, val) {
            state.sysConfig = {
                refresh: val['hasInit'],
                hasInit: val['hasInit'],
                hasLogin: val['hasLogin'],
                sysName: val['sysName'] || '接口文档中心',
                sysSlogan: val['sysSlogan'] || '这是一个简单的Java接口文档',
                logoUrl: val['logoUrl']
            }
        },
        setLoading(state, val) {
            state.loading = val
        }
    },
    actions: {
        common({ commit }) {
            return new Promise(resolve => {
                common().then(res => {
                    commit('setSysConfig', res["data"])
                    resolve(res)
                })
            })
        }
    },
    getters: {
        sysConfig: state => state.sysConfig,
    }
})
export default store
