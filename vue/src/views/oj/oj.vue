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
      id: JSON.parse(localStorage.getItem("user")).id,

      user: {},

      isAdmin: true,
    }
  },

  created() {
    this.getUser()

  },

  methods: {
    getUser() {
      this.request.get("user/" + this.id).then(res => {
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
