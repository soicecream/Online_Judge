<template>
  <div>

    <!--  导航栏-->
    <el-menu :default-active="activeIndex" active-text-color="#ff5252cc" router class="el-menu-demo" mode="horizontal">
      <el-menu-item class="zimeLogo">ZIME Online Judge</el-menu-item>
      <el-menu-item index="/home"><i class="el-icon-s-home"/>主页</el-menu-item>
      <el-menu-item index="/problem"><i class="el-icon-s-grid"/>问题</el-menu-item>
      <el-menu-item index=""><i class="el-icon-monitor"/>竞赛/作业</el-menu-item>
      <el-menu-item index=""><i class="el-icon-s-order"/>题单</el-menu-item>
      <el-menu-item index=""><i class="el-icon-s-marketing"/>状态</el-menu-item>
      <el-menu-item index=""><i class="el-icon-s-data"/>排名</el-menu-item>
      <el-menu-item index=""><i class="el-icon-question"/>常见问题</el-menu-item>
      <el-menu-item index=""><i class="el-icon-chat-line-round"/>讨论</el-menu-item>
      <el-menu-item index=""><i class="el-icon-shopping-bag-1"/>商城</el-menu-item>

      <!--      操作用户-->
      <el-menu-item-group style="margin-right: 1%; float: right">
        <el-dropdown trigger="click">
          <el-button type="text" style="display: block;">
            <el-image v-if="user.headPortrait" :src="user.headPortrait" fit="cover" class="zimeUserImage"/>
            <el-image v-else :src="require('@/assets/img/image/user.png')" class="zimeUserImage"/>
            <i class="el-icon-caret-bottom" style="margin-top: -10%;"/>
          </el-button>
          <el-dropdown-menu>
            <el-dropdown-item>
              <el-link type="text" @click="to_user_information_page" :underline="false">个人主页</el-link>
            </el-dropdown-item>
            <el-dropdown-item>
              <el-link type="text" @click="to_set_user_information_page" :underline="false">我的设置</el-link>
            </el-dropdown-item>

            <el-dropdown-item v-if="isAdmin" divided>
              <el-link type="text" @click="to_admin_page" :underline="false">后台管理</el-link>
            </el-dropdown-item>

            <el-dropdown-item divided>
              <el-link type="text" @click="logout" :underline="false">退出登录</el-link>
            </el-dropdown-item>

          </el-dropdown-menu>
        </el-dropdown>
      </el-menu-item-group>
    </el-menu>

  </div>
</template>

<script>
import VerificationCode from "@/components/share/verificationCode";

export default {
  name: "navigationBar",

  components: {
    VerificationCode,

  },

  props: {
    user: Object,
    isAdmin: Boolean,

  },

  data() {
    return {
      activeIndex: this.$route.path,

    }
  },

  created() {
    // this.user.headPortrait = ""

  },

  methods: {
    // 跳转到用户信息界面
    to_user_information_page() {
      // console.log(this.user.id)
      this.$router.push('/information/' + this.user.id)
    },

    // 跳转到个人设置
    to_set_user_information_page() {
      this.$router.push({name: 'userSetting'})
    },

    // 跳转到后台管理页面
    to_admin_page() {
      this.$router.push('/admin/home')
    },

    // 退出用户
    logout() {
      this.$store.commit('logout')

      this.$message.success("退出成功")
    }

  }

}
</script>

<style scoped>
.zimeLogo {
  font-size: 1.5rem;
  font-weight: bold;
  color: #303133;
}

/* 移动到字体上出现下划线 */
.zimeLogo:after {
  content: '';
  width: 100%;
  height: 2px;
  position: absolute;
  bottom: 0;
  left: 0;
  transform: scaleX(0);
  background-color: #000000;
  transition-duration: 0.2s;
}

.zimeLogo:hover:after {
  transform: scaleX(1);
}

.zimeUserImage {
  height: 30px;
  width: 30px;
  margin-top: -25%;
  border-radius: 50%;
}

</style>
