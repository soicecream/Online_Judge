import Vue from 'vue'
import Vuex from 'vuex'
import router, {resetRouter} from "@/router";

Vue.use(Vuex)

export default new Vuex.Store({
    state: {},

    getters: {},

    mutations: {
        logout() {
            // 清空缓存
            window.localStorage.clear()
            router.push('/login')

            resetRouter()
        }
    },

    actions: {},

    modules: {},

})
