<template>
  <!--        用户信息-->
  <div style="display: flex;" class="userSettingStyle">
    <!--          头像-->
    <div class="userImageStyle">
      <div class="userImageSpan">用户头像</div>
      <div style="height: 100px; width: 100px; margin: auto;">
        <el-image style="height: 100%; width: 100%;" :src="user.headPortrait" fit="cover">
          <div slot="error" style="margin-top: 10px;">
            <el-avatar :size="80">
              <img src="https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png"/>
            </el-avatar>
          </div>
        </el-image>
      </div>
      <el-upload :action="'http://'+serverIp+':9090/files/upload'"
                 :http-request="handlerAvatarImport"
                 :before-upload="beforeAvatarUpload"
                 :on-success="handleAvatarSuccess" :on-error="handleAvatarError"
                 style="line-height: 34px; height: 34px; margin-top: 20px;">
        <!--                       :show-file-list="false"-->
        <el-button icon="el-icon-upload2" type="primary" plain>更换头像</el-button>
      </el-upload>
    </div>
  </div>
</template>

<script>
import {serverIp} from "../../../../public/config";

export default {
  name: "information",

  data() {
    return {
      serverIp: serverIp,

      user: {
        id: "1",
        username: "admin",
        realname: "admin",
        nickname: "admin",
        sex: 1,
        phone: "12354678910",
        email: "123@qq.com",
        school: "浙江",
        residence: "浙江温州",
        introduction: "加油",
        headPortrait: "",
        createTime: "2022-09-26T22:48:32",
        lastLoginTime: "2022-09-26T22:48:32",
        language: 1,
      },
      // user: JSON.parse(localStorage.getItem("user")),

    }
  },

  created() {
    // console.log(this.user)
  },

  methods: {
    load_user() {

    },

    handlerAvatarImport(param) {
      let formData  = new FormData()
      formData.append("file", param.file)

      console.log(formData)

      this.request.post("/files/importFile", formData).then(res => {
        if (res.code === '200') {
          this.$message.success("导入成功")

          this.load_user();
        } else
          this.$message.error(res.message)
      })

    },
    handleAvatarSuccess(res) {
      console.log(res)
      this.user.headPortrait = res
      this.$message.success("上传成功")
    },
    handleAvatarError() {
      this.$message.error("上传失败")
    },
    beforeAvatarUpload(file) {
      const vis = file.type === 'image/jpeg' || file.type === 'image/png';
      if (!vis) {
        this.$message.error('上传头像图片只能是 JPG 或 PNG 格式!');
      }
      return vis;
    },

  },


}
</script>

<style scoped>
.userSettingStyle {
  background-color: #fff;
  width: 50%;
  height: calc(100vh - 200px);
  border-radius: 8px;
  padding: 12px;
  margin: auto;

  border: 1px solid red;

}

.userSettingForm {
  text-align: left;
  margin-left: 10px;
  border: 1px solid #ebeef5;
  padding: 16px;
  border-radius: 8px;
  width: 100%;

}

.userImageStyle {
  height: 220px;
  width: 300px;
  text-align: center;
  border: 1px solid #ebeef5;
  padding: 16px;
  border-radius: 8px;
}

/deep/ .proviceStyle .el-form-item__content {
  display: flex;
}

.userImageSpan {
  line-height: 32px;
  margin-bottom: 12px;
  font-weight: 500;
}

</style>