<template>

  <div>
    <el-card shadow="hover" style="width: 30%; margin: 2% auto auto;">
      <div slot="header">
        <span> 修改用户密码 </span>
      </div>
      <el-form :model="form" status-icon :rules="rules" ref="ruleForm">
        <el-form-item prop="username">
          <el-input v-model="form.username" prefix-icon="el-icon-user" placeholder="用户名"></el-input>
        </el-form-item>

        <el-form-item prop="password">
          <el-input v-model="form.password" prefix-icon="el-icon-lock" placeholder="密码"></el-input>
        </el-form-item>

        <el-form-item>
          <el-button @click="update" type="primary" style="width: 40%;"> 修改</el-button>
          <el-button @click="resetForm" style="width: 40%; float: right;"> 重置表单</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="hover" style="width: 30%; margin: 2% auto auto;">
      <div slot="header">
        <span> 重置用户密码 </span>
      </div>
      <el-form :model="forms" status-icon :rules="forms_rules" ref="ruleForms">
        <el-form-item prop="username">
          <el-input v-model="forms.username" prefix-icon="el-icon-user" placeholder="用户名"></el-input>
        </el-form-item>

        <el-form-item>
          <el-button @click="resetPassword" type="primary" style="width: 40%;"> 重置用户密码</el-button>
          <el-button @click="resetForms" style="width: 40%; float: right;"> 重置表单</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>

</template>

<script>
export default {
  name: "update_password",

  data() {
    return {
      form: {
        username: "",
        password: "",
      },
      rules: {
        username: [
          {required: true, message: '请填写用户名', trigger: 'blur'},
          {min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur'}
        ],
        password: [
          {required: true, message: '请填写密码', trigger: 'blur'},
          {min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur'}
        ],
      },

      forms: {
        username: "",
      },
      forms_rules: {
        username: [
          {required: true, message: '请填写用户名', trigger: 'blur'},
          {min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur'}
        ],
      },

    }
  },

  methods: {
    // 修改密码
    update() {
      this.$refs.ruleForm.validate((valid) => {
        if (valid) {
          this.request.post("/user/upadtePassword", this.form).then(res => {
            if (res.code === "200") {
              this.$message.success("用户密码修改成功")
              this.$refs.ruleForm.resetFields()
            } else {
              this.$message.error(res.message)
              return false
            }
          })
        } else {
          this.$message.error("请确认输入")
          return false
        }
      })
    },

    // 重置表单
    resetForm() {
      if (this.$refs.ruleForm !== undefined)
        this.$refs.ruleForm.resetFields()
    },

    // 重置密码
    resetPassword() {
      this.$refs.ruleForms.validate((valid) => {
        if (valid) {
          this.request.post("/user/resetPassword", this.forms).then(res => {
            if (res.code === '200') {
              this.$message.success("重置密码成功")
            } else
              this.$message.error("重置密码失败")
          })
        } else {
          this.$message.error("请确认输入")
          return false
        }
      })
    },

    resetForms() {
      if (this.$refs.ruleForms !== undefined)
        this.$refs.ruleForms.resetFields()
    }
  },

}
</script>

<style scoped>

</style>