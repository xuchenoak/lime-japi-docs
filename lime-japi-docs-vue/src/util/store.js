import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)
const store = new Vuex.Store({
    strict: true,
    state: {
        loading: false,
        commonConfig: {
            refresh: false,
            slogan: "",
            logoExist: false
        }
    },
    mutations: {
        changePhoneView (state, val) {
            state.phoneView = val
        },
        setCommonConfig(state, val) {
            state.commonConfig = {
                refresh: true,
                slogan: val['slogan'] || "",
                logoExist: val['logoExist'] || false
            }
        },
        setLoading(state, val) {
            state.loading = val
        }
    },
    getters: {
        slogan: state => state.commonConfig.slogan,
        logoExist: state => state.commonConfig.logoExist,
        refresh: state => state.commonConfig.refresh,
    }
})
export default store
