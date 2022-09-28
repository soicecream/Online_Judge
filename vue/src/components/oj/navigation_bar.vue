<template>
  <div>

    <!--  导航栏-->
    <el-menu class="el-menu-demo" mode="horizontal" active-text-color="#409EFF" router>
      <el-menu-item class="change">ZIME Online Judge</el-menu-item>
      <el-menu-item index="/home"><i class="el-icon-s-home"/>主页</el-menu-item>
      <el-menu-item index="/problem"><i class="el-icon-s-grid"/>问题</el-menu-item>
      <el-menu-item index="2"><i class="el-icon-monitor"/>竞赛/作业</el-menu-item>
      <!--      <el-menu-item index="3"><i class="el-icon-s-order"/>题单</el-menu-item>-->
      <el-menu-item index="4"><i class="el-icon-s-marketing"/>状态</el-menu-item>
      <el-menu-item index="5"><i class="el-icon-s-data"/>排名</el-menu-item>
      <el-menu-item index="6"><i class="el-icon-question"/>常见问题</el-menu-item>
      <!--      <el-menu-item index="7"><i class="el-icon-chat-line-round"/>讨论</el-menu-item>-->
      <!--      <el-menu-item index="8"><i class="el-icon-shopping-bag-1"/>商城</el-menu-item>-->

      <!--      操作用户-->
      <el-menu-item-group style="margin-right: 1%; float: right">
        <!--      未登录 -->
        <div style="float: right" v-if="user_login_check">
          <el-button @click="open_login_user" round> 登录</el-button>
          <el-button @click="open_register_user" round> 注册</el-button>
        </div>

        <!--      已登录 -->
        <div v-else>
          <el-dropdown trigger="click">
            <el-button type="text">
              <el-avatar icon="el-icon-user-solid" :src="user.head_portrait"
                  :size="30" style="display: block; margin-right: 10%; margin-top: -20%; float: left;"/>
              <i class="el-icon-caret-bottom"/>
            </el-button>
            <el-dropdown-menu>
              <el-dropdown-item>
                <el-link type="text" @click="to_user_information_page" :underline="false" >个人主页</el-link>
              </el-dropdown-item>

              <el-dropdown-item>
                <el-link type="text" @click="to_set_user_information_page" :underline="false" >我的设置</el-link>
              </el-dropdown-item>

              <el-dropdown-item v-if="is_admin" divided >
                <el-link type="text" @click="to_admin_page" :underline="false" >后台管理</el-link>
              </el-dropdown-item>

              <el-dropdown-item divided><el-link type="text" @click="logout" :underline="false" >退出登录</el-link></el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>

      </el-menu-item-group>

    </el-menu>

    <!--  登录框 -->
    <el-dialog :visible.sync="user_login" title="欢迎来到 ZIME Online Judge" width="30%">
      <el-form :model="login_form" :rules="login_form_rules" ref="login_user_form" label-position="top">
        <el-form-item prop="username">
          <el-input v-model="login_form.username" class="w-50 m-2" placeholder="用户名"><i slot="prefix"
                                                                                        class="el-icon-user"/>
          </el-input>
        </el-form-item>

        <el-form-item prop="password">
          <el-input v-model="login_form.password" class="w-50 m-2" placeholder="密码" type="password" show-password><i
              slot="prefix" class="el-icon-lock"/></el-input>
        </el-form-item>

        <el-form-item>
          <el-button @click="submit_login_form()" style="width: 100%" type="primary"> 登录</el-button>
        </el-form-item>

        <el-form-item>
          <el-button @click="open_register_user" type="text">没有账号？现在注册！</el-button>
          <el-button @click="forgot_password" type="text" style="float: right;">忘记密码</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>

    <!--  注册框-->
    <el-dialog :visible.sync="user_register" title="欢迎来到 ZIME Online Judge" width="30%">
      <el-form :model="register_form" :rules="register_form_rules" ref="register_user_form" label-position="top">

        <el-form-item prop="username">
          <el-input v-model="register_form.username" class="w-50 m-2" placeholder="用户名"><i slot="prefix"
                                                                                           class="el-icon-user"/>
          </el-input>
        </el-form-item>

        <el-form-item prop="password">
          <el-input v-model="register_form.password" class="w-50 m-2" placeholder="密码" type="password" show-password><i
              slot="prefix" class="el-icon-lock"/></el-input>
        </el-form-item>

        <el-form-item prop="password_again">
          <el-input v-model="register_form.password_again" class="w-50 m-2" placeholder="再次输入密码" type="password"
                    show-password><i slot="prefix" class="el-icon-lock"/></el-input>
        </el-form-item>

        <el-form-item prop="phone">
          <el-input v-model="register_form.phone" class="w-50 m-2" placeholder="电话号码"><i slot="prefix"
                                                                                         class="el-icon-phone"/>
          </el-input>
        </el-form-item>

        <el-form-item prop="verification_code" :inline="true">
          <el-input v-model="register_form.verification_code" class="w-50 m-2" placeholder="验证码" style="width: 65%"><i
              slot="prefix" class="el-icon-help"/></el-input>
          <span @click="get_verification_code" style="cursor: pointer; float: right">
            <VerificationCode :identifyCode="identifyCode"></VerificationCode>
          </span>
        </el-form-item>

        <el-form-item>
          <el-button @click="submit_register_form()" style="width: 100%" type="primary"> 注册</el-button>
        </el-form-item>

        <el-form-item>
          <el-button @click="open_login_user()" style="width: 100%"> 已有账号现在登陆</el-button>
        </el-form-item>

      </el-form>
    </el-dialog>

  </div>
</template>

<script>
import VerificationCode from "@/components/share/verification_code";

export default {
  name: "navigation_bar",

  components: {
    VerificationCode,

  },

  data() {
    return {
      // 用户是否需要登录
      user_login_check: true,

      // 是否可以进入管理员后台
      is_admin: true,

      // 用户登录弹窗
      user_login: false,
      // 用户注册弹窗
      user_register: false,

      // 空用户 用于之后的赋值
      user_null: {},

      // 用户登录及注册的数据和判定规则
      login_form: {username: "", password: ""},
      login_form_rules: {
        username: [{required: true, message: '请填写用户名', trigger: 'blur'},],
        password: [{required: true, message: '请填写密码', trigger: 'blur'},],
      },
      register_form: {},
      register_form_rules: {
        username: [
          {required: true, message: '请填写用户名', trigger: 'blur'},
          {min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur'}
        ],
        password: [
          {required: true, message: '请填写密码', trigger: 'blur'},
          {min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur'}
        ],
        password_again: [
          {required: true, message: '请再次填写密码', trigger: 'blur'},
          {min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur'}
        ],
        phone: [
          {required: true, message: '请填写电话号码', trigger: 'blur'},
          {min: 11, max: 11, message: '请填写11位的电话号码', trigger: 'blur'}
        ],
        verification_code: [
          {required: true, message: '请填写验证码', trigger: 'blur'},
          {min: 4, max: 4, message: '请填写4为验证码', trigger: 'blur'}
        ],
      },

      // 登录后存储的用户信息
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},

      // 图片验证码
      identifyCode: '',
      // 验证码规则
      identifyCodes: '3456789abcdefghjkmnpqrstuvwxyABCDEFGHJKMNPQRSTUVWXY',

    }
  },

  created() {
    if(localStorage.getItem("user") != null)
      this.user_login_check = false
    this.get_verification_code()
  },

  methods: {
    // 清空所有数据
    clear_data() {
      if (this.$refs.login_user_form !== undefined)
        this.$refs.login_user_form.resetFields()
      if (this.$refs.register_user_form !== undefined)
        this.$refs.register_user_form.resetFields()
    },

    // 打开登录 或者 注册的
    open_login_user() {
      this.user_login = true
      this.user_register = false
      this.clear_data()
      this.login_form.username = "admin"
      this.login_form.password = "admin"
    },
    open_register_user() {
      this.user_login = false
      this.user_register = true
      this.clear_data()
    },

    // 提交登录 或者 注册的
    submit_login_form() {
      this.$refs["login_user_form"].validate((valid) => {
        if (valid) {
          this.request.post("/user/login", this.login_form).then(res => {
            console.log(res)
            if (res.code === "200") {
              // 存储用户信息到浏览器中
              localStorage.setItem("user", JSON.stringify(res.data))

              // 关闭框
              this.user_login = false
              this.user_login_check = false

              this.$message.success("登陆成功")

            } else {
              this.$message.error(res.message)
            }
          })
        }
      });
    },
    submit_register_form() {
      this.$refs["register_user_form"].validate((valid) => {
        if (valid) {
          let form = this.register_form
          console.log(form.verification_code)
          console.log(this.identifyCode)
          if (form.verification_code.toLowerCase() !== this.identifyCode.toLowerCase()) {
            this.$message.warning("验证码错误")
            form.verification_code = ""
            return false
          }
          if (form.password !== form.password_again) {
            this.$message.warning("两个密码不一样，请确认后重试")
            return false
          }

          this.user_register = false

          console.log('submit!');
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },

    // 获取验证码
    get_verification_code() {
      this.identifyCode = ''
      for (let i = 0; i < 4; i++) {
        this.identifyCode += this.identifyCodes[Math.floor(Math.random() * (this.identifyCodes.length - 0) + 0)]
      }
      // console.log(this.identifyCode)
    },

    // 忘记密码
    forgot_password() {

    },


    // 跳转到用户信息界面
    to_user_information_page() {
      console.log(this.user.id)
      this.$router.push('/user_information/' + this.user.id)
    },

    // 跳转到个人设置
    to_set_user_information_page() {
      this.$router.push('/user_setting/' + this.user.id)
    },

    // 跳转到后台管理页面
    to_admin_page() {
      this.$router.push('/admin')
    },

    // 退出用户
    logout() {
      localStorage.removeItem("user")
      this.user_login_check = true
      this.$router.push('/home')
      this.$message.success("退出成功")
    }

  }

}
</script>

<style scoped>
.change {
  font-size: 1.3rem;
  font-weight: bold;
  color: #303133;
  position: relative;
}

/* 移动到字体上出现下划线 */
.change:after {
  content: '';
  width: 100%;
  height: 1px;
  position: absolute;
  bottom: 0;
  left: 0;
  transform: scaleX(0);
  background-color: #000000;
  transition-duration: 0.2s;
}

.change:hover:after {
  transform: scaleX(1);
}


</style>
