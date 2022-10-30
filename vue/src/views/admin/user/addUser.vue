<template>
  <div>
<!--    添加用户-->
    <el-card shadow="hover" style="width: 30%; margin: 2% auto auto;">
      <div slot="header"><span> 添加用户 </span></div>
      <el-form :model="userOne" :rules="userOneRules" ref="userOneRuleForm">
        <el-form-item prop="username">
          <el-input v-model="userOne.username" prefix-icon="el-icon-user" placeholder="用户名" clearable></el-input>
        </el-form-item>

        <el-form-item prop="password">
          <el-input v-model="userOne.password" prefix-icon="el-icon-lock" placeholder="密码" clearable></el-input>
        </el-form-item>

        <el-form-item>
          <el-button @click="addUserOne" type="primary" style="width: 40%;"> 添加用户</el-button>
          <el-button @click="userOneResetForm" style="width: 40%; float: right;"> 重置表单</el-button>
        </el-form-item>
      </el-form>
    </el-card>

<!--    批量添加用户-->
    <el-card shadow="hover" style="width: 30%; margin: 2% auto auto;">
      <div slot="header" class="clearfix">
        <span> 批量添加用户 </span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="handlerExcelImport_open"> 导入用户</el-button>
      </div>
      <el-form :model="userList" :rules="userListRules" ref="userListRuleForm">
        <el-form-item prop="userListText">
          <el-input v-model="userList.userListText" type="textarea" rows="15" placeholder="批量输入用户用户名"/>
        </el-form-item>

        <el-form-item>
          <el-button @click="addUserList" type="primary" style="width: 40%;"> 批量添加用户</el-button>
          <el-button @click="userListRestForm" style="width: 40%; float: right;"> 重置表单</el-button>
        </el-form-item>
        <el-divider content-position="left">请一行输入一个用户名</el-divider>
      </el-form>
    </el-card>

    <!--    导入用户信息-->
    <el-dialog title="批量导入" :visible.sync="dialogFormVisible_import" width="30%">
      <div class="importDialog-content">
        <el-upload
            action="" :http-request="handlerExcelImport"
            ref="importExcel"
            drag :limit="1" :auto-upload="false" accept=".xls,.xlsx">
          <i class="el-icon-upload"></i>
          <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
          <div class="el-upload__tip" slot="tip">只能上传xls/xlsx文件</div>
          <div class="el-upload__tip" slot="tip">上传的表格中必须要有 "username" 或 "用户名" 字段</div>
          <div class="el-upload__tip" slot="tip">密码默认为 "zime"+用户名后六位 </div>
        </el-upload>
      </div>
      <span slot="footer">
        <el-button @click="handlerExcelImport_close">取 消</el-button>
        <el-button type="primary" @click="handlerExcelImport_ok">确定上传</el-button>
      </span>
    </el-dialog>

  </div>

</template>

<script>
export default {
  name: "addUser",

  data() {
    return {
      userOne: {
        username: "",
        password: "",
      },
      userOneRules: {
        username: [
          {required: true, message: '请填写用户名', trigger: 'blur'},
          {min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur'}
        ],
        password: [
          {required: true, message: '请填写密码', trigger: 'blur'},
          {min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur'}
        ],
      },

      userList: {
        userListText: "",
      },
      userListRules: {
        userListText: [
          {required: true, message: '请批量输入用户用户名', trigger: 'blur'},
        ],
      },

      // 导入用户信息
      dialogFormVisible_import: false,

    }
  },

  created() {

  },

  methods: {
    addUserOne() {
      this.$refs.userOneRuleForm.validate((valid) => {
        if (valid) {
          this.request.post("/user/addOneUser", this.userOne).then(res => {
            if (res.code === "200") {
              this.$message.success("添加用户成功")
              this.UserOneresetForm()
            } else {
              this.$message.error(res.message)
            }
          })
        } else {
          this.$message.error("请确认输入")
          return false;
        }
      });
    },
    userOneResetForm() {
      if (this.$refs.userOneRuleForm !== undefined) {
        this.$refs.userOneRuleForm.resetFields()
      }
    },

    addUserList() {
      this.$refs.userListRuleForm.validate((valid) => {
        if (valid) {
          this.request.post("/user/addListUser", this.userList).then(res => {
            if (res.code === "200") {
              this.$message.success("添加用户成功")
              this.userListRestForm()
            } else {
              this.$message.error(res.message)
            }
          })
        } else {
          this.$message.error("请确认输入")
          return false;
        }
      });
    },
    userListRestForm() {
      if(this.$refs.userListRuleForm !== undefined) {
        this.$refs.userListRuleForm.resetFields()
      }
    },

    // 导入信息
    handlerExcelImport_open() {
      this.dialogFormVisible_import = true
      if (this.$refs.importExcel)
        this.$refs.importExcel.clearFiles()
    },
    handlerExcelImport_close() {
      this.dialogFormVisible_import = false
    },
    handlerExcelImport_ok() {
      this.$refs.importExcel.submit()
    },
    handlerExcelImport(param) {
      const formData = new FormData()
      formData.append("file", param.file);

      this.request.post("/user/import/userList", formData).then(res => {
        if (res.code === '200') {
          this.$message.success("导入成功")

          this.handlerExcelImport_close()
        } else
          this.$message.error(res.message)
      })
    },

  },


}
</script>

<style scoped>
.importDialog-content {
  display: flex;
  justify-content: center;
}

</style>