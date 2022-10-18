<template>
  <router-view/>
</template>

<script>
export default {
  name: 'App',

  // 返回
  // 1.$router.back()
  // 2.$router.go()
  // go(-1) // 原页面表单中的内容会丢失
  // this.$router.go(-1) // 后退+刷新
  // this.$router.go(0) // 刷新
  // this.$router.go(1) // 前进
  // back() //原页表表单中的内容会保留
  // this.$router.back() //后退
  // this.$router.back(0) //刷新
  // this.$router.back(1) //前进

  // 相当于注册  也就是定义
  components: {},

  // 数据
  data() {
    return {}
  },

  // 计算属性
  computed: {},

  // 定义函数  事件处理器
  methods: {
    // beforeunloadHandler(e) {
    //   // console.log(e)
    //   this._beforeUnload_time = new Date().getTime()
    // },
    //
    // unloadHandler(e) {
    //   this._gap_time = new Date().getTime() - this._beforeUnload_time
    //   //判断是窗口关闭还是刷新
    //   if (this._gap_time <= 5) {
    //     // 窗口关闭
    //     this.$store.commit('logout')
    //   }
    // }
  },

  // 侦听器
  watch: {},


  // 下面都是 生命周期钩子

  // 在实例初始化之后,进行数据侦听和事件/侦听器的配置之前同步调用。
  beforeCreate() {

  },

  // 在实例创建完成后被立即同步调用。  创建阶段：数据的准备
  created() {

  },

  // 实例被挂载后调用  挂载阶段：数据加载到页面
  mounted() {
    // 关闭页面操作
    // window.addEventListener('beforeunload', e => this.beforeunloadHandler(e))
    // window.addEventListener('unload', e => this.unloadHandler(e))

  },

  // 在数据发生改变后，DOM 被更新之前被调用。
  beforeUpdate() {

  },

  // 在数据更改导致的虚拟 DOM 重新渲染和更新完毕之后被调用。   数据更新后调用
  // 该钩子在服务器端渲染期间不被调用。
  updated() {

  },

  // 被 keep-alive 缓存的组件激活时调用。
  // 该钩子在服务器端渲染期间不被调用。
  activated() {

  },

  // 被 keep-alive 缓存的组件失活时调用。
  // 该钩子在服务器端渲染期间不被调用。
  deactivated() {

  },

  // 实例销毁之前调用。在这一步，实例仍然完全可用。
  // 该钩子在服务器端渲染期间不被调用。
  beforeDestroy() {

  },

  // 实例销毁后调用。
  //  该钩子被调用后，对应Vue实例的所有指令都被解绑，所有的事件监听器被移除，所有的子实例也都被销毁。
  // 该钩子在服务器端渲染期间不被调用。
  destroyed() {
    // window.removeEventListener('beforeunload', e => this.beforeunloadHandler(e))
    // window.removeEventListener('unload', e => this.unloadHandler(e))

  },


}
</script>

<style>


</style>
