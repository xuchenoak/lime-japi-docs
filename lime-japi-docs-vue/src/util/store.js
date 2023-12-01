import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)
const store = new Vuex.Store({
    strict: true,
    state: {
        loading: false,
        phoneView: false
    },
    mutations: {
        changePhoneView (state, val) {
            state.phoneView = val
        },
        setLoading(state, val) {
            state.loading = val
        }
    },
    getters: {
        phoneView: state => state.phoneView,
    }
})
export default store
