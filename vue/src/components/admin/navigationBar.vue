<template>

  <el-menu background-color="#304156" active-text-color="#ffd04b" text-color="#ffffff" router
           style="min-height: 100vh; overflow-x: hidden;"
           :default-active="activeIndex"
           :unique-opened="true"

           :collapse="isCollapse" :collapse-transition="false"
  >
    <!--    logo-->
    <div style="height: 60px; line-height: 60px; text-align: center;" @click="collapse">
      <img src="../../assets/logo.png" style="width: 20px; position: relative; top: 5px;">
      <b style="color: white; margin-left: 5px;" v-show="logoTextShow">后台管理系统</b>
    </div>

    <div v-for="item in menus" :key="item.id">
      <div v-if="item.path">
        <el-menu-item :index="item.path">
          <i :class="item.icon"/>
          <span slot="title"> {{ item.name }}</span>
        </el-menu-item>
      </div>
      <div v-else>
        <el-submenu :index="item.name">
          <template slot="title">
            <i :class="item.icon"/>
            <span slot="title">{{item.name}}</span>
          </template>
          <div v-for="subItem in item.children" :key="subItem.id">
            <el-menu-item :index="subItem.path">
              <i :class="subItem.icon"/>
              <span slot="title">{{subItem.name}}</span>
            </el-menu-item>
          </div>
        </el-submenu>
      </div>
    </div>

<!--    &lt;!&ndash;    首页&ndash;&gt;-->
<!--    <el-menu-item index="/admin/home"><i class="el-icon-s-help"/><span slot="title"> 首页 </span></el-menu-item>-->

<!--    &lt;!&ndash;    用户管理&ndash;&gt;-->
<!--    <el-submenu index="user">-->
<!--      <template slot="title"><i class="el-icon-user-solid"/><span slot="title">用户管理</span></template>-->

<!--      <el-menu-item index="/admin/user/list"><i class="el-icon-user"/>用户列表</el-menu-item>-->
<!--      <el-menu-item index="/admin/user/update_password"><i class="el-icon-edit"/>用户修改密码</el-menu-item>-->

<!--    </el-submenu>-->

<!--    <el-submenu index="role">-->
<!--      <template slot="title"><i class="el-icon-user-solid"/><span slot="title">权限管理</span></template>-->
<!--      <el-menu-item index="/admin/role/information"><i class="el-icon-tickets"/>权限信息</el-menu-item>-->
<!--      <el-menu-item index="/admin/role/list"><i class="el-icon-tickets"/>用户权限列表</el-menu-item>-->
<!--      <el-menu-item index=""><i class="el-icon-coordinate"/>用户权限添加</el-menu-item>-->
<!--    </el-submenu>-->

<!--    &lt;!&ndash;    问题管理&ndash;&gt;-->

<!--    &lt;!&ndash;    竞赛管理&ndash;&gt;-->

<!--    &lt;!&ndash;    系统管理&ndash;&gt;-->
<!--    <el-submenu index="system management">-->
<!--      <template slot="title"><i class="el-icon-cpu"/><span slot="title">系统管理</span></template>-->

<!--      <el-menu-item index="/admin/menu/list"><i class="el-icon-receiving"/>菜单管理</el-menu-item>-->

<!--      <el-menu-item index="/admin/file/list"><i class="el-icon-receiving"/>文件管理</el-menu-item>-->


<!--    </el-submenu>-->


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
.el-menu-vertical-demo:not(.el-menu--collapse) {
  width: 250px;
  min-height: 400px;
}

a {
  color: white;
  text-decoration: none;
}
</style>