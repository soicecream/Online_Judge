import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import './assets/gloable.css'

import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'

Vue.use(ElementUI, {zIndex: 2200});

import request from "@/utils/requset"


import Fragment from 'vue-fragment'

Vue.use(Fragment.Plugin)


import VuevideoPlayer from 'vue-video-player';
import 'video.js/dist/video-js.css';

Vue.use(VuevideoPlayer);


//  markdown编辑器
import mavonEditor from 'mavon-editor'
import 'mavon-editor/dist/css/index.css'

Vue.use(mavonEditor)


import 'xe-utils'
import VXETable from 'vxe-table'
import 'vxe-table/lib/style.css'

Vue.use(VXETable)
Vue.prototype.$VXETable = VXETable


Vue.config.productionTip = false

Vue.prototype.request = request

new Vue({
    router, store, render: h => h(App)
}).$mount('#app')
