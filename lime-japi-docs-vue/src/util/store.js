import Vue from 'vue'
import Vuex from 'vuex'
import windowUtil from '@/util/windowUtil'

Vue.use(Vuex)
const store = new Vuex.Store({
    strict: true,
    state: {
        phoneView: windowUtil.getViewportOffset().x < 560
    },
    mutations: {
        changePhoneView (state, val) {
            state.phoneView = val
        }
    }
})
export default store
