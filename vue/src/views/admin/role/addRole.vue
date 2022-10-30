<template>
  <div>
    <el-card shadow="hover" style="width: 30%; margin: 2% auto auto;">
      <div slot="header"><span> 权限添加 </span></div>
      <el-form :model="role" status-icon :rules="role_rules" ref="roleRuleForm">
        <el-form-item prop="userUsername">
          <el-input v-model="role.userUsername" placeholder="用户名" clearable prefix-icon="el-icon-user"/>
        </el-form-item>

        <el-form-item prop="roleId">
          <el-select v-model="role.roleId" placeholder="请选择角色权限" filterable clearable style="width: 100%;">
            <el-option v-for="item in roleList" :key="item.id" :label="item.description" :value="item.id">
              {{ item.description }}
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button @click="addRole" type="primary" style="width: 40%;"> 添加</el-button>
          <el-button @click="resetRole" style="width: 40%; float: right;"> 重置表单</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="hover" style="width: 30%; margin: 2% auto auto;">
      <div slot="header"><span> 添加超级权限 </span></div>
      <el-form :model="supper_admin" status-icon :rules="supper_admin_rules" ref="superAdminRuleForms">
        <el-form-item prop="userUsername">
          <el-input v-model="supper_admin.userUsername" placeholder="用户名" clearable prefix-icon="el-icon-user"/>
        </el-form-item>

        <el-form-item>
          <el-button @click="addSuperAdmin" type="primary" style="width: 40%;"> 添加超级权限</el-button>
          <el-button @click="resetSuperAdmin" style="width: 40%; float: right;"> 重置表单</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="hover" style="width: 30%; margin: 2% auto auto;">
      <div slot="header"><span> 添加普通权限 </span></div>
      <el-form :model="admin" status-icon :rules="admin_rules" ref="adminRuleForms">
        <el-form-item prop="userUsername">
          <el-input v-model="admin.userUsername" placeholder="用户名" clearable prefix-icon="el-icon-user"/>
        </el-form-item>

        <el-form-item>
          <el-button @click="addAdmin" type="primary" style="width: 40%;"> 添加权限</el-button>
          <el-button @click="resetAdmin" style="width: 40%; float: right;"> 重置表单</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
export default {
  name: "addRole",

  data() {
    return {
      role: {
        userUsername: "",
        roleId: "",
      },
      role_rules: {
        userUsername: [
          {required: true, message: '请填写用户名', trigger: 'blur'},
        ],
        roleId: [
          {required: true, message: '请选择用户权限', trigger: 'change'},
        ]
      },

      supper_admin: {
        userUsername: "",
        roleId: "1",
      },
      supper_admin_rules: {
        userUsername: [
          {required: true, message: '请填写用户名', trigger: 'blur'},
        ]
      },

      admin: {
        userUsername: "",
        roleId: "2",
      },
      admin_rules: {
        userUsername: [
          {required: true, message: '请填写用户名', trigger: 'blur'},
        ]
      },

      roleList: [],

    }
  },

  created() {
    this.load_roleList()

  },

  methods: {
    // 加载权限信息
    load_roleList() {
      this.request.get("/role").then(res => {
        if (res.code === '200') {
          this.roleList = res.data
          this.roleList.splice(2, 2)
        } else {
          this.$message.error(res.message)
        }
      })
    },

    // 添加权限
    addRole() {
      this.$refs.roleRuleForm.validate((valid) => {
        if (valid) {
          this.request.post("/userRole/increase", this.role).then(res => {
            if (res.code === "200") {
              this.$message.success("用户权限成功")
              this.resetRole()
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
    addSuperAdmin() {
      this.$refs.superAdminRuleForms.validate((valid) => {
        if (valid) {
          this.request.post("/userRole/increase", this.supper_admin).then(res => {
            if (res.code === "200") {
              this.$message.success("用户权限成功")
              this.resetSuperAdmin()
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
    addAdmin() {
      this.$refs.adminRuleForms.validate((valid) => {
        if (valid) {
          this.request.post("/userRole/increase", this.admin).then(res => {
            if (res.code === "200") {
              this.$message.success("用户权限成功")
              this.resetAdmin()
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
    resetRole() {
      if (this.$refs.roleRuleForm !== undefined)
        this.$refs.roleRuleForm.resetFields()
    },
    resetSuperAdmin() {
      if (this.$refs.superAdminRuleForms !== undefined)
        this.$refs.superAdminRuleForms.resetFields()
    },
    resetAdmin() {
      if (this.$refs.adminRuleForms !== undefined)
        this.$refs.adminRuleForms.resetFields()
    },

  },
}
</script>

<style scoped>

</style>