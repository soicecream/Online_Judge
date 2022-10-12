<template>
  <div>

    <el-table :data="tableData" style="width: 60%; margin: auto">
      <el-table-column prop="id" label="#" />
      <el-table-column prop="subject" label="题目" />
      <el-table-column prop="difficulty" label="难度" />
      <el-table-column prop="sum_total" label="总数" />
      <el-table-column prop="passing_rate" label="通过率" />
      <el-table-column v-if="table_label" prop="label" label="标签" />
    </el-table>
  </div>
</template>

<script>
export default {
  name: "problem_list",

  data() {
    return {
      table_label: false,
      table_total: 0,
      table_num: 1,
      table_size: 5,

      tableData: [
        {id: "0001", subject: "A + B Problem", difficulty: "低", sum_total: "100", passing_rate: "100%", label: "输出",},
        {id: "0001", subject: "A + B Problem", difficulty: "低", sum_total: "100", passing_rate: "100%", label: "输出",},
        {id: "0001", subject: "A + B Problem", difficulty: "低", sum_total: "100", passing_rate: "100%", label: "输出",},
        {id: "0001", subject: "A + B Problem", difficulty: "低", sum_total: "100", passing_rate: "100%", label: "输出",},
      ],
    }
  },

  methods: {
    get_table() {

    },
  }

}
</script>

<style scoped>

</style>
