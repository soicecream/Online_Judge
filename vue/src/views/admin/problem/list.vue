<template>
  <div>

    <!-- 搜索栏 -->
    <div class="pd-10">
      <el-input v-model="search_message.id" placeholder="请输入题目id" clearable suffix-icon="el-icon-user"
                style="width: 150px;"/>
      <el-input v-model="search_message.title" placeholder="请输入题目标题" clearable suffix-icon="el-icon-user" class="ml-5"
                style="width: 150px;"/>

      <el-select v-model="search_message.difficulty" placeholder="请选择题目难度" clearable style="width: 150px;" class="ml-5">
        <el-option label="未知" value="0"/>
        <el-option label="简单" value="1"/>
        <el-option label="中等" value="2"/>
        <el-option label="困难" value="3"/>
      </el-select>

      <el-select v-model="search_message.auth" placeholder="请选择题目状态" clearable style="width: 150px;" class="ml-5">
        <el-option label="启用" value="1"/>
        <el-option label="禁用" value="0"/>
      </el-select>

      <el-button type="primary" @click="search"> 搜索</el-button>
      <el-button type="warning" @click="reset"> 重置</el-button>
    </div>

    <!-- 操作 -->
    <div style="margin: 10px 0">
      <el-button @click="handlerAdd" type="primary"> 新增题目 <i class="el-icon-circle-plus"/></el-button>

      <!--      批量删除-->
      <el-popconfirm confirm-button-text="确定" cancel-button-text="我再想想"
                     icon="el-icon-info" icon-color="#ff0000"
                     title="您确定批量删除这些数据吗?" class="mrl-10"
                     @confirm="handlerDelBatch">
        <template #reference>
          <el-button type="danger" slot="reference"> 批量删除 <i class="el-icon-remove"/></el-button>
        </template>
      </el-popconfirm>

      <!--      导入-->
      <!--      <el-upload action="" :show-file-list="false" accept="xlsx"-->
      <!--                 :http-request="handlerExcelImport"-->
      <!--                 :on-success="handlerExcelImportSuccess" :on-error="handlerExcelImportError"-->
      <!--                 style="display: inline-block;" class="mrl-10">-->
      <!--        <el-button type="primary"> 导入用户 <i class="el-icon-folder-add"></i></el-button>-->
      <!--      </el-upload>-->
      <!--      <el-button type="primary" @click="exportFile" class="ml-10"> 导出题目 <i class="el-icon-folder-checked"></i>-->
      <!--      </el-button>-->

      <!--      显示顺序-->
      <el-button type="primary" @click="reverse_order" class="ml-10"> {{ 'id ' + reverse_order_value }}<i
          :class="reverse_order_btncls"/></el-button>

      <el-button type="primary" @click="enable_start" class="ml-10"> 启用 <i class="el-icon-success"/></el-button>
      <el-button type="primary" @click="enable_end" class="ml-10"> 禁用 <i class="el-icon-error"/></el-button>

    </div>

    <!-- 信息 -->
    <el-table :data="tableData" border stripe @selection-change="handlerSelectionChange">
      <el-table-column type="selection" fixed align="center" width="50"/>
      <el-table-column prop="id" fixed label="id" width="50" align="center"/>
      <el-table-column prop="title" fixed label="标题" width="130"/>
      <el-table-column prop="author" label="作者" width="130"/>
      <el-table-column label="类型" width="100" align="center">
        <template #default="scope">
          <el-tag>
            {{ scope.row.type }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="timeLimit" label="时间限制" width="130" align="center"/>
      <el-table-column prop="memoryLimit" label="空间限制" width="130" align="center"/>
      <el-table-column prop="stackLimit" label="栈限制" width="130" align="center"/>

      <!--    难度-->
      <el-table-column label="难度" width="100" align="center">
        <template #default="scope">
          <!--          ----------------------------------------     ============================================= ------------------------------------------------------------     -->
          <el-tag>
            {{ scope.row.difficulty }}
          </el-tag>
        </template>
      </el-table-column>

      <!--    状态-->
      <el-table-column label="状态" width="70" align="center">
        <template #default="scope">
          <el-switch v-model="scope.row.auth" @change="update_information(scope.row)" active-color="#13ce66"
                     inactive-color="#ff4949"/>
        </template>
      </el-table-column>

      <el-table-column prop="updateTime" label="修改时间" width="200" align="center"/>


      <!--     操作该信息-->
      <el-table-column label="操作" fixed="right" align="center">
        <template #default="scope">

          <el-button type="success" @click="handlerEdit(scope.row)"> 编辑</el-button>

          <!--        删除-->
          <el-popconfirm
              confirm-button-text='确定' cancel-button-text='我再想想'
              icon="el-icon-info" icon-color="#ff0000"
              title="您确定删除吗？" class="ml-10"
              @confirm="handlerDelete(scope.row.id)"
          >
            <template #reference>
              <el-button type="danger" slot="reference"> 删除</el-button>
            </template>
          </el-popconfirm>

        </template>
      </el-table-column>

    </el-table>

    <!--   底部分页选项的选择 -->
    <div class="mt-10">
      <el-pagination
          :currentPage="pageNum" :page-size="pageSize"
          :page-sizes="[5, 10, 15, 20]" :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handlerSizeChange" @current-change="handlerCurrentChange"
      />
    </div>

    <!-- 添加弹窗 -->
    <el-dialog title="添加用户信息" :visible.sync="dialogFormVisible" width="30%">
      <el-form :model="form" :rules="form_rules" ref="user_form" label-width="100px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username"/>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password"/>
        </el-form-item>
        <el-form-item label="姓名">
          <el-input v-model="form.realname"/>
        </el-form-item>
        <el-form-item label="昵称" prop="nickname">
          <el-input v-model="form.nickname"/>
        </el-form-item>
        <el-form-item label="性别">
          <el-select v-model="form.sex" placeholder="请选择性别">
            <el-option label="男" value="1"/>
            <el-option label="女" value="0"/>
          </el-select>
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input v-model="form.phone"/>
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="form.email"/>
        </el-form-item>
        <el-form-item label="学校">
          <el-input v-model="form.school"/>
        </el-form-item>
        <el-form-item label="地址">
          <el-input v-model="form.address"/>
        </el-form-item>
        <el-form-item label="介绍">
          <el-input v-model="form.introduction" type="textarea"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="handlerAdd_close">取 消</el-button>
        <el-button type="primary" @click="handlerAdd_ok">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 修改弹窗 -->
    <el-dialog title="修改用户信息" :visible.sync="dialogFormVisible_update" width="30%">
      <el-form label-width="100px" :model="form_update" :rules="form_update_rules" ref="user_update_form">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form_update.username" class="input_not_input"/>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="form_update.password"/>
        </el-form-item>
        <el-form-item label="姓名">
          <el-input v-model="form_update.realname"/>
        </el-form-item>
        <el-form-item label="昵称" prop="nickname">
          <el-input v-model="form_update.nickname"/>
        </el-form-item>
        <el-form-item label="性别">
          <el-select v-model="form_update.sex" placeholder="请选择性别">
            <el-option label="男" :value="1"/>
            <el-option label="女" :value="0"/>
          </el-select>
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input v-model="form_update.phone"/>
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="form_update.email"/>
        </el-form-item>
        <el-form-item label="学校">
          <el-input v-model="form_update.school"/>
        </el-form-item>
        <el-form-item label="地址">
          <el-input v-model="form_update.address"/>
        </el-form-item>
        <el-form-item label="介绍">
          <el-input v-model="form_update.introduction" type="textarea"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="handlerEdit_close">取 消</el-button>
        <el-button type="primary" @click="handlerEdit_ok">确 定</el-button>
      </div>
    </el-dialog>

  </div>

</template>

<script>

import {serverIp} from "/public/config";

export default {
  name: "list",

  data() {
    return {
      // tableData: [{
      //   id: 1,
      //   username: "admin",
      //   password: "admin",
      //   realname: "admin",
      //   nickname: "admin",
      //   sex: 1,
      //   phone: "11111111111",
      //   email: "admin@163.com",
      //   school: "浙江机电",
      //   address: "浙江温州",
      //   introduction: "加油",
      //   headPortrait: null,
      //   createTime: "2022-09-29T15:39:51",
      //   lastLoginTime: "2022-09-29T15:39:51",
      //   language: 0,
      //   enable: true,
      //   isRank: false,
      //   isDelete: 0,
      // }],
      tableData: [],

      // 表格设计
      total: 0,
      pageNum: 1,
      pageSize: 10,

      // 搜索的信息
      search_message: {
        id: "",
        title: "",
        sex: "",
        address: "",
        enable: "",
        isRank: "",
      },

      // 显示顺序
      reverse_order_value: "正序",
      reverse_order_desc: false,
      reverse_order_btncls: 'el-icon-bottom',

      // 复选框选中
      multipleSelection: {},
      multipleSelection_length: "",

      // 添加用户信息弹窗
      dialogFormVisible: false,
      form: {},
      form_rules: {
        username: [
          {required: true, message: '请输入用户名', trigger: 'blur'},
          {min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur'}
        ],
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'},
          {min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur'}
        ],
        nickname: [
          {required: true, message: '请输入昵称', trigger: 'blur'},
          {min: 1, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur'}
        ],
        phone: [
          {min: 11, max: 11, message: '请填写11位的电话号码', trigger: 'blur'},
          {pattern: /^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\d{8}$/, message: "请输入正确的电话号码",}
        ],
      },

      // 修改用户信息弹窗
      dialogFormVisible_update: false,
      form_update: {},
      form_update_rules: {
        username: [
          {required: true, message: '请输入用户名', trigger: 'blur'},
          {min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur'}
        ],
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'},
          {min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur'}
        ],
        nickname: [
          {required: true, message: '请输入昵称', trigger: 'blur'},
          {min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur'}
        ],
        phone: [
          {min: 11, max: 11, message: '请填写11位的电话号码', trigger: 'blur'},
          {pattern: /^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\d{8}$/, message: "请输入正确的电话号码",}
        ],
      },

    }
  },

  created() {
    this.load_problem()

  },

  methods: {
    // 加载用户
    load_problem() {
      this.request.get("/problem/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
        }
      }).then(res => {
        if (res.code === '200') {
          this.tableData = res.data.records
          this.total = res.data.total
        } else {
          this.$message.error("请求失败")
        }
      })
    },

    // 搜索
    search() {
      this.pageNum = 1
      this.load_problem()
    },

    // 重置搜索
    reset() {
      Object.keys(this.search_message).forEach(key => (this.search_message[key] = ""))
      this.load_problem()
    },

    // 分页查询
    handlerSizeChange(pageSize) {
      this.pageSize = pageSize
      this.load_problem()
    },
    handlerCurrentChange(pageNum) {
      this.pageNum = pageNum
      this.load_problem()
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
      this.load_problem()
    },


    // 添加用户
    handlerAdd() {
      this.dialogFormVisible = true
      if (this.$refs.user_form !== undefined)
        this.$refs.user_form.resetFields()
    },
    handlerAdd_close() {
      // 关闭添加的窗口
      this.dialogFormVisible = false
      // 清空数据
      this.$refs.user_form.resetFields()
    },
    handlerAdd_ok() {
      this.$refs.user_form.validate((valid) => {
        if (valid) {
          this.request.post("/problem", this.form).then(res => {
            if (res.code === "200") {
              this.$message.success("用户添加成功")

              this.reverse_order_desc = true
              this.load_problem()

              this.handlerAdd_close()
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

    // 修改用户信息
    update_information(form) {
      this.request.post("/problem", form).then(res => {
        if (res.code === '200') {
          this.$message.success("修改成功")

          this.load_problem()
        } else
          this.$message.error("修改失败")
      })
    },

    // 操作的编辑修改用户信息
    handlerEdit(row) {
      this.dialogFormVisible_update = true
      this.form_update = Object.assign({}, row)
    },
    handlerEdit_close() {
      this.dialogFormVisible_update = false
      this.$refs.user_update_form.resetFields()
    },
    handlerEdit_ok() {
      this.$refs.user_update_form.validate((valid) => {
        // 表单校验是否合法
        if (valid) {
          this.update_information(this.form_update)

          this.handlerEdit_close()
        } else {
          this.$message.error("请确认输入")
        }
      })
    },

    // 重置用户的密码
    handlerEdit_password(row) {
      row.password = "123456"
      this.update_information(row)
    },


    // 赋予复选框选中的值
    handlerSelectionChange(val) {
      this.multipleSelection = val
    },

    // 批量修改用户信息
    update_information_batch(list) {
      this.request.post("/problem/batch", list).then(res => {
        if (res.code === '200') {
          this.$message.success("修改成功")

          this.load_problem()
        } else
          this.$message.error("修改失败")
      })
    },

    // 判断选中的用户信息
    check_multipleSelection() {
      this.multipleSelection_length = 0
      if (this.multipleSelection.length === undefined || this.multipleSelection.length === 0) {
        this.$message.error("请选择用户")
        return false
      }
      this.multipleSelection_length = this.multipleSelection.length
      return true
    },

    // 批量启用禁用状态 还有加入退出排名
    enable_start() {
      this.check_multipleSelection()
      if (this.multipleSelection_length == 0)
        return false

      this.multipleSelection.forEach(function (item) {
        item.enable = true
      })
      this.update_information_batch(this.multipleSelection)
    },
    enable_end() {
      this.check_multipleSelection()
      if (this.multipleSelection_length == 0)
        return false

      this.multipleSelection.forEach(function (item) {
        item.enable = false
      })
      this.update_information_batch(this.multipleSelection)
    },
    isRank_start() {
      this.check_multipleSelection()
      if (this.multipleSelection_length == 0)
        return false

      this.multipleSelection.forEach(function (item) {
        item.isRank = true
      })
      this.update_information_batch(this.multipleSelection)
    },
    isRank_end() {
      this.check_multipleSelection()
      if (this.multipleSelection_length == 0)
        return false

      this.multipleSelection.forEach(function (item) {
        item.isRank = false
      })
      this.update_information_batch(this.multipleSelection)
    },


    // 删除用户信息
    handlerDelete(id) {
      this.request.delete("/problem/" + id).then(res => {
        if (res.code === '200') {
          this.$message.success("删除成功")

          this.load_problem()
        } else
          this.$message.error("删除失败")
      })
    },
    handlerDelBatch() {
      this.check_multipleSelection()
      if (this.multipleSelection_length == 0)
        return false

      // 将对象数据 变成 id数组
      // [{}, {}, {}] => [1, 2, 3 ]
      let ids = this.multipleSelection.map(v => v.id)
      console.log(ids)

      this.request.post("/problem/delete/batch", ids).then(res => {
        if (res.code === '200') {
          this.$message.success("删除成功")

          this.load_problem()
        } else
          this.$message.error("删除失败")
      })
    },

    // 导入用户信息
    handlerExcelImport(param) {
      let formData = new FormData()
      formData.append("file", param.file)

      this.request.post("/problem/import", formData).then(res => {
        if (res.code === '200') {
          this.$message.success("导入成功")

          this.load_problem()
        } else
          this.$message.error(res.message)
      })
    },
    handlerExcelImportSuccess() {
      this.$message.success("导入成功")
    },
    handlerExcelImportError() {
      this.$message.error("导入失败")
    },

    // 导出用户信息
    exportFile() {
      window.open(`http://${serverIp}:9090/user/export`)
    },


  },

}
</script>

<style scoped>

</style>