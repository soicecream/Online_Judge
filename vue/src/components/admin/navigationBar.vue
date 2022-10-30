<template>


  <el-menu background-color="#304156" active-text-color="#ffd04b" text-color="#ffffff" router
           style="min-height: 100vh; overflow-x: hidden;"
           :default-active="activeIndex"
           :unique-opened="true" :collapse="isCollapse" :collapse-transition="false">
    <!--    logo-->
    <div style="height: 60px; line-height: 60px; text-align: center;" @click="collapse">
      <img src="../../assets/logo.png" style="width: 20px; position: relative; top: 5px;">
      <b style="color: white; margin-left: 5px;" v-show="logoTextShow">后台管理系统</b>
    </div>

    <fragment v-for="item in menus" :key="item.id">
      <el-menu-item v-if="item.path" :index="item.path">
        <i :class="item.icon"></i>
        <span slot="title"> {{ item.name }}</span>
      </el-menu-item>
      <el-submenu v-else :index="item.name">
        <template slot="title">
          <i :class="item.icon"></i>
          <span slot="title">{{ item.name }}</span>
        </template>

        <el-menu-item v-for="subItem in item.children" :key="subItem.id" :index="subItem.path">
          <i :class="subItem.icon"/>{{ subItem.name }}
        </el-menu-item>
      </el-submenu>
    </fragment>

    <!-- 返回前台 -->
    <el-menu-item index="/"><i class="el-icon-switch-button"/><span slot="title"> 返回前台 </span></el-menu-item>

  </el-menu>


</template>

<script>
export default {
  name: "navigationBar",

  props: {
    isCollapse: Boolean,
    logoTextShow: Boolean,

  },

  data() {
    return {
      activeIndex: this.$route.path,
      menus: JSON.parse(localStorage.getItem("user")).menus,

    }
  },

  methods: {

    // 点击后台管理系统 实现收缩侧边栏
    collapse() {
      this.$emit("asideCollapse")
    },

  }

}
</script>

<style scoped>


</style>