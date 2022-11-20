import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import './assets/gloable.css'


// element
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'

Vue.use(ElementUI, {zIndex: 2200});


// 请求接口
import request from "@/utils/requset"
Vue.prototype.request = request


// 渲染
import Fragment from 'vue-fragment'

Vue.use(Fragment.Plugin)


// 视频播放
import VuevideoPlayer from 'vue-video-player';
import 'video.js/dist/video-js.css';

Vue.use(VuevideoPlayer);


//  markdown编辑器
import mavonEditor from 'mavon-editor'
import 'mavon-editor/dist/css/index.css'

Vue.use(mavonEditor)


// 表格
import 'xe-utils'
import VXETable from 'vxe-table'
import 'vxe-table/lib/style.css'

Vue.use(VXETable)
Vue.prototype.$VXETable = VXETable


// 复制到剪切板
import Clipboard from 'v-clipboard'

Vue.use(Clipboard)


Vue.config.productionTip = false


new Vue({
    router, store, render: h => h(App)
}).$mount('#app')
