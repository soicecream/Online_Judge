<template>
  <!--        用户信息-->
  <div style="display: flex;" class="userSettingStyle">
    <!--          头像-->
    <div class="userImageStyle">
      <div style="width: 100%; margin: auto; text-align: center">
        <el-image v-if="form.headPortrait" :src="form.headPortrait" :preview-src-list="previewSrcs" fit="cover" style="height: 150px; width: 150px;"/>
        <el-image v-else style="height: 150px; width: 150px;" :src="require('@/assets/img/image/user.png')"/>
        <el-divider/>
        <el-upload action="" :http-request="handlerAvatarImport" :show-file-list="false" accept=".jpg, .png">
          <el-button>修改头像</el-button>
        </el-upload>
      </div>


    </div>

    <div class="userSettingInformation">
      <el-form :model="form" :rules="form_rules" ref="user_form" label-width="100px">
        <el-form-item label="用户名" prop="username"><el-input v-model="form.username" class="input_not_input"/></el-form-item>
        <el-form-item label="姓名"><el-input v-model="form.realname"/></el-form-item>
        <el-form-item label="昵称" prop="nickname"><el-input v-model="form.nickname"/></el-form-item>
        <el-form-item label="性别">
          <el-select v-model="form.sex" placeholder="请选择性别">
            <el-option label="男" value="1"/>
            <el-option label="女" value="0"/>
          </el-select>
        </el-form-item>
        <el-form-item label="电话" prop="phone"><el-input v-model="form.phone"/></el-form-item>
        <el-form-item label="邮箱"><el-input v-model="form.email"/></el-form-item>
        <el-form-item label="学校"><el-input v-model="form.school"/></el-form-item>
        <el-form-item label="地址"><el-input v-model="form.address"/></el-form-item>
        <el-form-item label="介绍"><el-input v-model="form.introduction" type="textarea"/></el-form-item>
      </el-form>
    </div>

  </div>
</template>

<script>
import {serverIp} from "/public/config";

export default {
  name: "information",

  data() {
    return {
      serverIp: serverIp,

      form: {},
      form_rules: {
        nickname: [
          {required: true, message: '请输入昵称', trigger: 'blur'},
          {min: 1, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur'}
        ],
        phone: [
          {min: 11, max: 11, message: '请填写11位的电话号码', trigger: 'blur'},
          {pattern: /^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\d{8}$/, message: "请输入正确的电话号码",}
        ],
      },

      previewSrcs: [],

    }
  },

  created() {
    this.load_user()

  },

  methods: {
    load_user() {
      const id = JSON.parse(localStorage.getItem("user")).id
      this.request.get("user/" + id).then(res => {
        if (res.code === '200') {
          this.form = res.data

          this.previewSrcs = new Array()
          this.previewSrcs.push(this.form.headPortrait)
        } else {
          this.$message.error(res.message)
        }
      })
    },

    // 头像导入
    handlerAvatarImport(param) {
      let formData = new FormData()
      formData.append("file", param.file)
      this.request.post("/files/import/file", formData).then(res => {
        if (res.code === '200') {
          this.form.headPortrait = res.data
          this.request.post("/user", this.form).then(res => {
            if (res.code === '200') {
              this.$message.success("修改成功")

              // 触发父级的获取用户信息
              this.$emit("refreshUser")
            } else
              this.$message.error("修改失败")
          })
        } else
          this.$message.error(res.message)
      })
    },

  },


}
</script>

<style scoped>
.userSettingStyle {
  background-color: #fff;
  width: 60%;
  border-radius: 8px;
  padding: 12px;
  margin: auto;
}

.userImageStyle {
  height: 250px;
  width: 300px;
  text-align: center;
  border: 1px solid #ebeef5;
  padding: 16px;
  border-radius: 8px;
}

.userSettingInformation {
  background-color: #fff;
  width: 50%;
  border-radius: 8px;
  padding: 12px;
  margin: auto;
}

</style>