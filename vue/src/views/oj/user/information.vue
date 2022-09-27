<template>
  <div class="userSettingStyle">
    <el-tabs>
      <el-tab-pane label="个人信息">
        <!--        用户信息-->
        <div style="display: flex;">
          <!--          头像-->
          <div class="userImageStyle">
            <div class="userImageSpan">用户头像</div>
            <div style="height: 100px; width: 100px; margin: auto;">
              <el-image style="height: 100%; width: 100%;" :src="user.head_portrait" fit="cover">
                <div slot="error" style="margin-top: 10px;">
                  <el-avatar :size="80" icon="el-icon-user-solid"/>
                </div>
              </el-image>
            </div>
            <el-upload action="" style="line-height: 34px; height: 34px; margin-top: 20px;">
              <el-button icon="el-icon-upload2" type="primary" plain>更换头像</el-button>
            </el-upload>
          </div>

          <!--          其他信息-->
          <el-form class="userSettingForm" :model="user" label-width="100px">
            <el-form-item label="用户名">
              <el-input v-model="user.username" class="input_not_input"/>
            </el-form-item>
            <el-form-item label="昵称">
              <el-input v-model="user.nickname" class="input_not_input"/>
            </el-form-item>
            <el-form-item label="姓名">
              <el-input v-model="user.realname" style="width: 50%" class="input_not_input"/>
              <el-tag :type="user.sex ? '' : 'danger'" effect="dark" >
                <i :class="user.sex == 1 ? 'el-icon-male' : 'el-icon-female'"/>
              </el-tag>
            </el-form-item>
            <el-form-item label="联系方式">
              <el-input v-model="user.phone" class="input_not_input"/>
            </el-form-item>
            <el-form-item label="邮箱">
              <el-input v-model="user.email" class="input_not_input"/>
            </el-form-item>
            <el-form-item label="学校">
              <el-input v-model="user.school" class="input_not_input"/>
            </el-form-item>
            <el-form-item label="地址">
              <el-input v-model="user.residence" class="input_not_input"/>
            </el-form-item>
            <el-form-item label="个人介绍">
              <el-input type="textarea" v-model="user.introduction" class="input_not_input"/>
            </el-form-item>
            <el-form-item label="注册时间">
              <el-input v-model="user.createTime" class="input_not_input"/>
            </el-form-item>
            <el-form-item label="最后登录时间">
              <el-input v-model="user.lastLoginTime" class="input_not_input"/>
            </el-form-item>
          </el-form>
        </div>
      </el-tab-pane>

      <el-tab-pane label="个人主页">

      </el-tab-pane>

    </el-tabs>

  </div>
</template>

<script>
export default {
  name: "information",

  data() {
    return {
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
        head_portrait: "",
        create_time: "2022-09-26T22:48:32",
        last_login_time: "2022-09-26T22:48:32",
        language: 1,
      },
      // user: JSON.parse(localStorage.getItem("user")),

    }
  },

  created() {
    console.log(this.user)
  },

  methods: {}


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

.settingStyleTitle {
  margin-bottom: 12px;
  color: rgba(0, 0, 0, 0.85);
  font-weight: 500;
  font-size: 20px;
  line-height: 28px;
  text-align: left;
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