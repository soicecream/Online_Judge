<template>
  <el-container style="max-height: 100vh; ">
    <el-aside :width="sideWidth + 'px'" style="box-shadow: 2px 0 6px rgb(0 21 41 / 35%);">
      <NavigationBar :isCollapse="isCollapse" :logoTextShow="logoTextShow" @asideCollapse="collapse()"/>
    </el-aside>

    <el-container>
      <el-main>
        <router-view/>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import NavigationBar from "@/components/admin/navigation_bar";

export default {
  name: "admin",

  components: {
    NavigationBar,
  },

  data() {
    return {
      collapseBtnClass: true,
      isCollapse: false,
      sideWidth: 250,
      logoTextShow: true,
    }
  },

  methods: {
    // 左侧收缩按钮触发
    collapse() {
      this.isCollapse = !this.isCollapse;
      this.collapseBtnClass = !this.collapseBtnClass
      this.logoTextShow = !this.logoTextShow
      this.sideWidth = 250
      if (this.isCollapse) {
        this.sideWidth = 65
      }
    },


  }

}
</script>

<style scoped>

</style>
