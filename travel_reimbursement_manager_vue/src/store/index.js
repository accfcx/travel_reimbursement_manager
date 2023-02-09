import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
    state: {
        uid: ""
    },
    getters: {},
    mutations: {
        // 第一个参数是上面的state，后面的参数是数据，可以有多个
        setUid(state, uid) {
            if (uid != null) {
                state.uid = uid
            } else {
                state.uid = ""
            }
        }
    },
    actions: {
        // context相当于 vuex.store的实例
        setUid(context, uid) {
            context.commit("setUid", uid)
        }
    },
    modules: {}
})
