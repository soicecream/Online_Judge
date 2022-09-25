<template>
  <el-container>
    <el-header>
      <Navigation_bar/>
    </el-header>
    <el-main>
      <router-view/>
    </el-main>
  </el-container>
</template>

<script>

import Navigation_bar from "@/components/oj/navigation_bar";

export default {
  name: "oj",
  components: {Navigation_bar},

}
</script>

<style scoped>

</style>
