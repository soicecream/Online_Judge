<template>

  <el-container style="height: 80vh; border: 1px solid #eee; width: 60%; margin: 1% auto auto;">
    <el-aside width="450px">
      <el-header height="35vh" style="border: 1px solid red; text-align: center">
        <el-avatar :size="200" icon="el-icon-user-solid" :src="user.head_portrait" style="margin-top: 5%;"/>
        <h1 style="margin-top: 3%; "> - {{ user.username }} - </h1>
        <h1 style="margin-top: 5%; "> {{ user.introduction == null || user.introduction.length == 0 ? "这个人很懒什么都没有写" : user.introduction }} </h1>
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
      user: {},
      // img_url: '',
      img_url: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
      username: "123456",
      introduction: "这个人很懒什么都没有写",
    }
  },

  created() {
    this.login()
  },

  methods: {
    login() {
      console.log(this.$route.params.id)
      this.request.get('user/' + this.$route.params.id)
          .then(res => {
            console.log(res)
            this.user = res
          })
    }
  }
}
</script>

<style scoped>

</style>