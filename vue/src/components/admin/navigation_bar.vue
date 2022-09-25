<template>

  <el-menu background-color="#304156" active-text-color="#ffd04b" text-color="#ffffff"
           style="min-height: 100vh; overflow-x: hidden;"
           :collapse-transition="false"
           :collapse="isCollapse"
           router
           :unique-opened="true"
  >
    <!--    logo-->
    <div style="height: 60px; line-height: 60px; text-align: center;" @click="collapse">
      <img src="../../assets/logo.png" style="width: 20px; position: relative; top: 5px;">
      <b style="color: white; margin-left: 5px;" v-show="logoTextShow">后台管理系统</b>
    </div>

    <!--    首页-->
    <el-menu-item index="/admin/home"><i class="el-icon-s-help"/><span slot="title"> 首页 </span></el-menu-item>

    <!--    用户管理-->
    <el-submenu index="admin">
      <template slot="title"><i class="el-icon-user-solid"/><span slot="title">用户管理</span></template>

      <el-menu-item-group title="用户管理">
        <el-menu-item index="/admin/user_list"><i class="el-icon-user"/><span>用户列表</span></el-menu-item>
        <el-menu-item index="/admin/user_update_password"><i class="el-icon-edit"/><span>用户修改密码</span></el-menu-item>
      </el-menu-item-group>

      <el-menu-item-group title="权限管理">
        <el-menu-item index="/admin"><i class="el-icon-tickets"/>权限列表</el-menu-item>
        <el-menu-item index="/admin"><i class="el-icon-coordinate"/>权限添加</el-menu-item>
      </el-menu-item-group>
    </el-submenu>

    <!--    问题管理-->

    <!--    竞赛管理-->

    <!--    系统管理-->

    <!--    Swagger-->
    <el-submenu index="Swagger">
      <template slot="title"><i class="el-icon-s-promotion"/><span slot="title">Swagger调试</span></template>

      <el-menu-item><a href="http://localhost:9090/swagger-ui/index.html" target="_blank">原始Swagger</a></el-menu-item>
      <el-menu-item><a href="http://localhost:9090/doc.html" target="_blank">Swagger ui</a></el-menu-item>

    </el-submenu>


    <!-- 返回前台 -->
    <el-menu-item index="/">
      <template slot="title"><i class="el-icon-switch-button"/><span slot="title">返回前台</span></template>
    </el-menu-item>

  </el-menu>


</template>

<script>
export default {
  name: "navigation_bar",

  props: {
    isCollapse: Boolean,
    logoTextShow: Boolean,
  },

  data() {
    return {}
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
.el-menu-vertical-demo:not(.el-menu--collapse) {
  width: 250px;
  min-height: 400px;
}

a {
  color: white;
  text-decoration: none;
}
</style>