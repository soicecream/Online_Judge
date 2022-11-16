<template>
  <div>
    <div style="flex: auto;">
      <el-row class="problem-box">
        <el-col :sm="24" :md="12" :lg="12" class="problem-left">
          problem-content
        </el-col>

        <div class="problem-resize">

        </div>

        <el-col :sm="24" :md="12" :lg="12" class="problem-right">
          <div>code-mirror</div>
        </el-col>
      </el-row>
    </div>



  </div>

</template>

<script>
export default {
  name: "problem"
}
</script>

<style scoped>

</style>