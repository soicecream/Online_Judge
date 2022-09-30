import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import './assets/gloable.css'

import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'

import 'default-passive-events'

import request from "@/utils/requset"

Vue.use(ElementUI, { zIndex: 2200 });

Vue.config.productionTip = false

Vue.prototype.request = request

// 全局前置导航守卫 -- 主要用来通过跳转或取消的方式守卫导航
// to: router即将进入的路由对象
// from: 当前导航即将离开的路由
// next: Function函数，一定要调用该方法 resolve这个钩子。执行效果依赖next方法的调用参数。
//       进行管道中的一个钩子，如果执行完了，则导航状态就是confirmed(确认的); 否则就是false, 终止导航。
router.beforeEach(async(to, from, next) => {

  next()
})


new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
