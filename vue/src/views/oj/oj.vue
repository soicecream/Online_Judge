<template>
  <el-container>
    <el-header>
      <Navigation_bar :user="user" :isAdmin="isAdmin"/>
    </el-header>
    <el-main>
      <router-view @refreshUser="getUser"/>
    </el-main>
  </el-container>
</template>

<script>

import Navigation_bar from "@/components/oj/navigationBar";

export default {
  name: "oj",
  components: {Navigation_bar},

  data() {
    return {
      user: {},

      isAdmin: false,
    }
  },

  created() {
    this.check_admin()
    this.getUser()

  },

  methods: {
    check_admin() {
      if(JSON.parse(localStorage.getItem("user")).menus)
        this.isAdmin = true
    },

    getUser() {
      const id = JSON.parse(localStorage.getItem("user")).id
      this.request.get("user/" + id).then(res => {
        if (res.code === '200') {
          this.user = res.data
        } else {
          this.$message.error(res.message)
        }
      })
    },


  },

}
</script>

<style scoped>

</style>
