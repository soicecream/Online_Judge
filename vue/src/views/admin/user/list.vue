<template>
  <div>

    <!-- 搜索栏 -->
    <div class="pd-10">
      <el-input style="width: 150px;" placeholder="请输入用户名" suffix-icon="el-icon-user"
                v-model="search_message.username"/>
      <el-input style="width: 150px;" placeholder="请输入姓名" suffix-icon="el-icon-user" class="ml-5"
                v-model="search_message.realname"/>
      <el-select v-model="search_message.sex" placeholder="请选择性别" style="width: 150px;" class="ml-5">
        <el-option label="男" value="1"></el-option>
        <el-option label="女" value="0"></el-option>
      </el-select>
      <el-input style="width: 200px;" placeholder="请输入地址" suffix-icon="el-icon-position" class="mrl-5"
                v-model="search_message.residence"/>
      <el-select v-model="search_message.deactivate" placeholder="请选择用户状态" style="width: 150px;" class="mrl-5">
        <el-option label="启用" value="1"></el-option>
        <el-option label="禁用" value="0"></el-option>
      </el-select>
      <el-button type="primary" @click="search"> 搜索</el-button>
      <el-button type="warning" @click="reset"> 重置</el-button>
    </div>

    <!-- 操作 -->
    <div style="margin: 10px 0">
      <el-button @click="handleAdd" type="primary"> 新增用户 <i class="el-icon-circle-plus"/></el-button>

      <el-popconfirm
          confirm-button-text="确定"
          cancel-button-text="我再想想"
          icon="InfoFilled"
          icon-color="#ff0000"
          title="您确定批量删除这些数据吗?"
          @confirm="handlerDelBatch"
          @cancel="cancelEvent"
          class="ml-10"
      >
        <template #reference>
          <el-button type="danger" slot="reference"> 批量删除 <i class="el-icon-remove"/></el-button>
        </template>
      </el-popconfirm>

      <el-upload action="http://localhost:9090/user/import" :show-file-list="false" accept="xlsx"
                 :on-success="handleExcelImportSuccess"
                 style="display: inline-block;" class="mrl-10">
        <el-button type="primary"> 导入 <i class="el-icon-folder-add"></i></el-button>
      </el-upload>
      <el-button type="primary" @click="exportFile" class="ml-10"> 导出 <i class="el-icon-folder-checked"></i></el-button>
      <el-button type="primary" @click="reverse_order" class="ml-10"> {{ 'id ' + reverse_order_value }}
        <i :class="reverse_order_btncls"/>
      </el-button>

      <el-button type="primary" class="ml-10"> 启用 <i class="el-icon-success"/></el-button>
      <el-button type="primary" class="ml-10"> 禁用 <i class="el-icon-error"/></el-button>

    </div>

    <!-- 用户信息 -->
    <el-table :data="tableData" border stripe @selection-change="handleSelectionChange" max-height="50rpx">
      <el-table-column type="selection" fixed align="center" width="50"/>
      <el-table-column prop="id" fixed label="id" width="80"/>
      <el-table-column prop="username" fixed label="用户名" width="130"/>
      <el-table-column prop="realname" label="姓名" width="130"/>
      <el-table-column prop="nickname" label="昵称" width="130"/>
      <el-table-column prop="residence" label="地址" width="200"/>

      <!--    性别-->
      <el-table-column label="性别" width="60" align="center">
        <template #default="scope">
          <el-tag :type="scope.row.sex === 1 ? '' : 'warning'">
            {{ scope.row.sex === 1 ? "男" : "女" }}
          </el-tag>
        </template>
      </el-table-column>

      <el-table-column prop="phone" label="电话" width="130" align="center"></el-table-column>
      <el-table-column prop="email" label="邮箱" width="130" align="center"></el-table-column>

      <el-table-column type="expand" label="补充信息" width="100">
        <template #default="props">
          <el-card class="box-card">
            <template #header>
              <div style="display: flex; justify-content: space-between; align-items: center;">
                <span> {{ props.row.username }} </span>
                <el-button class="button" type="success" text> 编辑</el-button>
              </div>
            </template>
            <el-form label-width="130px" label-position="left">
              <el-form-item label="学      校">{{ props.row.school }}</el-form-item>
              <el-form-item label="创 建 时 间">{{ props.row.createTime }}</el-form-item>
              <el-form-item label="最后登录时间">{{ props.row.lastLoginTime }}</el-form-item>
            </el-form>
          </el-card>
        </template>
      </el-table-column>


      <!--    状态-->
      <el-table-column label="状态" width="70rpx" align="center">
        <template #default="scope">
          <el-tag :type="scope.row.deactivate === 1 ? 'success' : 'danger'">
            {{ scope.row.deactivate == 1 ? "启用" : "禁用" }}
          </el-tag>
        </template>
      </el-table-column>

      <el-table-column fixed="right" label="操作" width="400" align="center">
        <template #default="scope">

          <el-button type="success"> 编辑</el-button>

          <el-button :type="scope.row.deactivate === 0 ? 'success' : 'danger'" class="mr-5"
                     @click="handlerEdit(scope.row)"> {{ scope.row.deactivate === 0 ? '启用' : '禁用' }}
          </el-button>

          <!--        重置密码-->
          <el-popconfirm
              confirm-button-text='确定'
              cancel-button-text='我再想想'
              icon="el-icon-info"
              icon-color="#ff0000"
              title="您确定要重置该用户的密码吗？"
              @confirm="headlerEdit_psd(scope.row)"
              class="mr-5"
          >
            <template #reference>
              <el-button type="warning" slot="reference"> 重置密码</el-button>
            </template>
          </el-popconfirm>

          <!--        删除用户-->
          <el-popconfirm
              confirm-button-text='确定'
              cancel-button-text='我再想想'
              icon="el-icon-info"
              icon-color="#ff0000"
              title="您确定删除吗？"
              @confirm="handlerDel(scope.row.id)"
          >
            <template #reference>
              <el-button type="danger" slot="reference"> 删除</el-button>
            </template>
          </el-popconfirm>

        </template>
      </el-table-column>

    </el-table>

    <!--   底部分页选项的选择 -->
    <div class="demo-pagination-block">
      <el-pagination
          v-model:currentPage="pageNum"
          v-model:page-size="pageSize"
          :page-sizes="[5, 10, 15, 20]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
      />
    </div>

  </div>

</template>

<script>

export default {
  name: "list",

  data() {
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,

      // 搜索的信息
      search_message: {
        username: "", // 用户名
        realname: "", // 姓名
        sex: "", // 性别
        residence: "", // 地址
        deactivate: "", // 状态
      },

      reverse_order_value: "正序",
      reverse_order_desc: false,
      reverse_order_btncls: 'el-icon-bottom',

      multipleSelection: {},

    }
  },

  created() {
    this.load_user()

  },

  methods: {
    // 加载用户
    load_user() {
      this.request.get("/user/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          username: this.search_message.username,
          realname: this.search_message.realname,
          sex: this.search_message.sex,
          residence: this.search_message.residence,
          deactivate: this.search_message.deactivate,
          desc: this.reverse_order_desc
        }
      }).then(res => {
        // console.log(res)

        if(res.code === '200') {
          this.tableData = res.data.records
          this.total = res.data.total
        } else {
          this.$message.error("请求失败")
        }
      })
    },

    // 搜索
    search() {
      this.load_user()
      this.pageNum = 1
    },

    // 重置搜索
    reset() {
      Object.keys(this.search_message)
          .forEach(key => (this.search_message[key] = ""))
      this.load_user()
    },

    // 分页查询
    handleSizeChange(pageSize) {
      this.pageSize = pageSize
      this.load_user()
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum
      this.load_user()
    },

    // 显示顺序
    reverse_order() {
      if (this.reverse_order_desc) {
        this.reverse_order_value = '倒序'
        this.reverse_order_btncls = 'el-icon-top'
      } else {
        this.reverse_order_value = '正序'
        this.reverse_order_btncls = 'el-icon-bottom'
      }
      this.reverse_order_desc = !this.reverse_order_desc
      this.load_user()
    },

    // 添加用户
    handleAdd() {

    },

    cancelEvent() {

    },

    // 输入好了用户信息 将要填写进去
    handleAdd_ok() {

    },

    // 修改用户信息
    handlerEdit(row) {

    },

    // 修改好了用户信息 将要进行修改
    handlerEdit_ok() {

    },

    // 重置用户的密码
    headlerEdit_psd(row) {

    },

    // 赋予复选框选中的值
    handleSelectionChange(val) {
      this.multipleSelection = val
    },

    // 删除用户信息
    handlerDel(id) {

    },

    // 批量删除用户信息
    handlerDelBatch() {

    },

    // 导出用户信息
    exportFile() {
      window.open("http://localhost:9090/user/export")
    },

    // 导入用户信息
    handleExcelImportSuccess() {
      this.$message.success("导入成功")
      this.load_user()
    }


  },

}
</script>

<style scoped>

.demo-pagination-block {
  margin-top: 10px;
}

.box-card {
  width: 480px;
  margin: auto;
}

</style>