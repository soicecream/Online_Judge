<template>
  <div>
    <h1>home</h1>
    <el-button @click="toVideo">to video</el-button>

    <el-button @click="toProblem">to problem1</el-button>

  </div>
</template>

<script>
export default {
  name: "home",

  methods: {
    toVideo() {
      this.$router.push({
        name: 'video',
        params: {
          id: 54
        }
      })
    },

    toProblem() {
      this.$router.push({
        name: 'problem',
        params: {
          id: 1
        }
      })
    },

  },

}
</script>

<style scoped>

</style>
