<template>

  <el-container style="height: 800px; border: 1px solid #eee; width: 1100px; margin: 1% auto auto;">
    <el-aside width="450px">
      <el-header height="350px" style="border: 1px solid red; text-align: center">
        <el-image v-if="user.headPortrait" :src="user.headPortrait" :preview-src-list="previewSrcs" fit="cover" style="height: 150px; width: 150px;"/>
        <el-image v-else style="height: 100%; width: 100%;" :src="require('@/assets/img/image/user.png')"/>
        <h1 style="margin-top: 3%; "> - {{ user.username }} - </h1>
        <h1 style="margin-top: 5%; ">
          {{ user.introduction == null || user.introduction.length == 0 ? "这个人很懒什么都没有写" : user.introduction }} </h1>
      </el-header>

      <el-main>
        Main
      </el-main>
    </el-aside>

    <el-container>
      <el-header style="text-align: right; font-size: 12px">

      </el-header>

      <el-main>

      </el-main>
    </el-container>
  </el-container>

</template>

<script>
export default {
  name: "information",

  data() {
    return {
      user: {
        username: "admin",
        introduction: "这个人很懒什么都没有写",
        headPortrait: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
      },
      // user: {},

      previewSrcs: [],
    }
  },

  created() {
    this.login()

  },

  methods: {
    login() {
      this.request.get('user/' + this.$route.params.id).then(res => {
        if (res.code === '200') {
          this.user = res.data

          this.previewSrcs = new Array()
          if (this.user.headPortrait) {
            this.previewSrcs.push(this.user.headPortrait)
          }
        } else {
          this.$message.error("用户信息提取失败")
        }
      })
    }
  }
}
</script>

<style scoped>

</style>