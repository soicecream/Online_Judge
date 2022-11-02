<template>
  <div>

    <!-- 搜索栏 -->
    <div class="pd-10">
      <el-input placeholder="请输入用户名" v-model="search_message.username" clearable style="width: 150px;" suffix-icon="el-icon-user"/>
      <el-input placeholder="请输入姓名" v-model="search_message.realname" clearable suffix-icon="el-icon-user" style="width: 150px;" class="ml-5"/>
      <el-select placeholder="请选择性别" v-model="search_message.sex" clearable style="width: 150px;" class="ml-5">
        <el-option label="男" value="1"></el-option>
        <el-option label="女" value="0"></el-option>
      </el-select>
      <el-input placeholder="请输入地址" v-model="search_message.address" clearable suffix-icon="el-icon-position" class="mrl-5" style="width: 200px;"/>
      <el-select v-model="search_message.enable" placeholder="请选择用户状态" clearable style="width: 150px;" class="mrl-5">
        <el-option label="启用" value="1"></el-option>
        <el-option label="禁用" value="0"></el-option>
      </el-select>
      <el-select v-model="search_message.isRank" placeholder="请选择用户是否参加排名" clearable style="width: 200px;" class="mrl-5">
        <el-option label="参加" value="1"></el-option>
        <el-option label="退出" value="0"></el-option>
      </el-select>
      <el-button type="primary" @click="search"> 搜索</el-button>
      <el-button type="warning" @click="reset"> 重置</el-button>
    </div>

    <!-- 操作 -->
    <div style="margin: 10px 0">
      <el-button @click="handlerAdd" type="primary"> 新增用户 <i class="el-icon-circle-plus"/></el-button>

      <!--      批量删除-->
      <el-popconfirm
          title="您确定批量删除这些数据吗?" confirm-button-text="确定" cancel-button-text="我再想想"
          style="display: inline-block;" class="mrl-10" icon="el-icon-info" icon-color="#ff0000"
          @confirm="handlerDelBatch">
        <template #reference>
          <el-button type="danger" slot="reference"> 批量删除 <i class="el-icon-remove"/></el-button>
        </template>
      </el-popconfirm>

      <!--      导入-->
      <el-button type="primary" @click="handlerExcelImport_open"> 导入用户 <i class="el-icon-folder-add"/></el-button>
      <el-button type="primary" @click="exportFile"> 导出用户 <i class="el-icon-folder-checked"/></el-button>

      <!--      显示顺序-->
      <el-button type="primary" @click="reverse_order">
        {{ 'id ' + (!reverse_order_desc ? '正序' : '倒序') }}
        <i v-if="!reverse_order_desc" class="el-icon-bottom"/>
        <i v-else class="el-icon-top"/>
      </el-button>

      <el-button type="primary" @click="enable_start"> 启用 <i class="el-icon-success"/></el-button>
      <el-button type="primary" @click="enable_end"> 禁用 <i class="el-icon-error"/></el-button>

      <el-button type="primary" @click="isRank_start"> 加入排名 <i class="el-icon-s-claim"/></el-button>
      <el-button type="primary" @click="isRank_end"> 推出排名 <i class="el-icon-s-release"/></el-button>

    </div>

    <!--   分页选项的选择 -->
    <div class="mt-10">
      <el-pagination
          :currentPage="pageNum" :page-size="pageSize"
          :page-sizes="[5, 10, 15, 20]" :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handlerSizeChange" @current-change="handlerCurrentChange"
      />
    </div>

    <!-- 信息 -->
    <el-table :data="tableData" border stripe @selection-change="handlerSelectionChange">
      <el-table-column type="selection" fixed align="center" width="50"/>
      <el-table-column prop="id" fixed label="id" width="50" align="center"/>
      <el-table-column prop="username" fixed label="用户名" width="130"/>
      <el-table-column prop="realname" label="姓名" width="130"/>
      <el-table-column prop="nickname" label="昵称" width="180"/>

      <!--    性别-->
      <el-table-column label="性别" width="60" align="center">
        <template #default="scope">
          <el-tag :type="scope.row.sex ? '' : 'warning'">
            {{ scope.row.sex ? "男" : "女" }}
          </el-tag>
        </template>
      </el-table-column>

      <el-table-column prop="phone" label="电话" width="150"/>

      <el-table-column type="expand" label="补充信息" width="200">
        <template #default="props">
          <el-card class="box-card">
            <template #header>
              <div style="display: flex; justify-content: space-between; align-items: center;">
                <span> {{ props.row.username }} </span>
                <el-button class="button" type="success" text> 编辑</el-button>
              </div>
            </template>
            <el-form label-width="130px" label-position="left">
              <el-form-item label="地      址">{{ props.row.address }}</el-form-item>
              <el-form-item label="学      校">{{ props.row.email }}</el-form-item>
              <el-form-item label="邮      箱">{{ props.row.school }}</el-form-item>
              <el-form-item label="创 建 时 间">{{ props.row.createTime }}</el-form-item>
              <el-form-item label="最后登录时间">{{ props.row.lastLoginTime }}</el-form-item>
            </el-form>
          </el-card>
        </template>
      </el-table-column>


      <!--    状态-->
      <el-table-column label="状态" width="70" align="center">
        <template #default="scope">
          <el-switch v-model="scope.row.enable" @change="update_information(scope.row)" active-color="#13ce66"
                     inactive-color="#ff4949"/>
        </template>
      </el-table-column>

      <!--    排名-->
      <el-table-column label="排名" width="70" align="center">
        <template #default="scope">
          <el-switch v-model="scope.row.isRank" @change="update_information(scope.row)" active-color="#13ce66"
                     inactive-color="#ff4949"/>
        </template>
      </el-table-column>

      <!--     操作该用户信息-->
      <el-table-column label="操作" fixed="right" width="300" align="center">
        <template #default="scope">

          <el-button type="success" @click="handlerEdit(scope.row)"> 编辑</el-button>

          <!--        重置密码-->
          <el-popconfirm
              confirm-button-text='确定' cancel-button-text='我再想想'
              icon="el-icon-info" icon-color="#ff0000"
              title="您确定要重置该用户的密码吗？ (重置的密码为123456)" class="ml-10"
              @confirm="handlerEdit_password(scope.row)"
          >
            <template #reference>
              <el-button type="warning" slot="reference"> 重置密码</el-button>
            </template>
          </el-popconfirm>

          <!--        删除用户-->
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

    <!--   分页选项的选择 -->
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
        <el-form-item label="用户名" prop="username"><el-input v-model="form.username" clearable/></el-form-item>
        <el-form-item label="密码" prop="password"><el-input v-model="form.password" clearable/></el-form-item>
        <el-form-item label="姓名"><el-input v-model="form.realname" clearable/></el-form-item>
        <el-form-item label="昵称" prop="nickname"><el-input v-model="form.nickname" clearable/></el-form-item>
        <el-form-item label="性别">
          <el-select v-model="form.sex" placeholder="请选择性别" clearable>
            <el-option label="男" value="1"/>
            <el-option label="女" value="0"/>
          </el-select>
        </el-form-item>
        <el-form-item label="电话" prop="phone"><el-input v-model="form.phone" clearable/></el-form-item>
        <el-form-item label="邮箱"><el-input v-model="form.email" clearable/></el-form-item>
        <el-form-item label="学校"><el-input v-model="form.school" clearable/></el-form-item>
        <el-form-item label="地址"><el-input v-model="form.address" clearable/></el-form-item>
        <el-form-item label="介绍"><el-input v-model="form.introduction" type="textarea" clearable/></el-form-item>
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
        <!--        <el-form-item label="密码" prop="password"><el-input v-model="form_update.password" clearable/></el-form-item>-->
        <el-form-item label="姓名"><el-input v-model="form_update.realname" clearable/></el-form-item>
        <el-form-item label="昵称" prop="nickname"><el-input v-model="form_update.nickname" clearable/></el-form-item>
        <el-form-item label="性别">
          <el-select v-model="form_update.sex" placeholder="请选择性别" clearable>
            <el-option label="男" :value="1"/>
            <el-option label="女" :value="0"/>
          </el-select>
        </el-form-item>
        <el-form-item label="电话" prop="phone"><el-input v-model="form_update.phone" clearable/></el-form-item>
        <el-form-item label="邮箱"><el-input v-model="form_update.email" clearable/></el-form-item>
        <el-form-item label="学校"><el-input v-model="form_update.school" clearable/></el-form-item>
        <el-form-item label="地址"><el-input v-model="form_update.address" clearable/></el-form-item>
        <el-form-item label="介绍"><el-input v-model="form_update.introduction" type="textarea" clearable/></el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="handlerEdit_close">取 消</el-button>
        <el-button type="primary" @click="handlerEdit_ok">确 定</el-button>
      </div>
    </el-dialog>

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

import {serverIp} from "/public/config";

export default {
  name: "list",

  data() {
    return {
      serverIp: serverIp,

      tableData: [],

      // 表格设计
      total: 0,
      pageNum: 1,
      pageSize: 10,

      // 搜索的信息
      search_message: {
        username: "",
        realname: "",
        sex: "",
        address: "",
        enable: "",
        isRank: "",
      },

      // 显示顺序
      reverse_order_desc: false,

      // 复选框选中
      multipleSelection: {},
      multipleSelection_length: "",

      // 添加信息弹窗
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
        phone: [
          {min: 11, max: 11, message: '请填写11位的电话号码', trigger: 'blur'},
          {pattern: /^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\d{8}$/, message: "请输入正确的电话号码",}
        ],
      },

      // 修改信息弹窗
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
        phone: [
          {min: 11, max: 11, message: '请填写11位的电话号码', trigger: 'blur'},
          {pattern: /^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\d{8}$/, message: "请输入正确的电话号码",}
        ],
      },

      // 导入用户信息
      dialogFormVisible_import: false,

    }
  },

  created() {
    this.load_user()

  },

  methods: {
    // 加载信息
    load_user() {
      this.request.get("/user/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          username: this.search_message.username,
          realname: this.search_message.realname,
          sex: this.search_message.sex,
          address: this.search_message.address,
          enable: this.search_message.enable,
          isRank: this.search_message.isRank,
          desc: this.reverse_order_desc,
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
      this.load_user()
    },

    // 重置搜索
    reset() {
      Object.keys(this.search_message).forEach(key => (this.search_message[key] = ""))
      this.load_user()
    },

    // 分页查询
    handlerSizeChange(pageSize) {
      this.pageSize = pageSize
      this.load_user()
    },
    handlerCurrentChange(pageNum) {
      this.pageNum = pageNum
      this.load_user()
    },

    // 显示顺序
    reverse_order() {
      this.reverse_order_desc = !this.reverse_order_desc
      this.load_user()
    },


    // 添加信息
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
          this.request.post("/user/addOneUser", this.form).then(res => {
            if (res.code === "200") {
              this.$message.success("用户添加成功")

              this.pageNum = 1
              this.reverse_order_desc = true
              this.load_user()

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

    // 修改信息
    update_information(form) {
      this.request.post("/user/update", form).then(res => {
        if (res.code === '200') {
          this.$message.success("修改成功")

          this.load_user()
        } else
          this.$message.error("修改失败")
      })
    },

    // 操作的编辑修改信息
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
      this.request.post("/user/resetPassword", row).then(res => {
        if (res.code === "200") {
          this.$message.success("用户密码修改成功")
        } else {
          this.$message.error(res.message)
          return false
        }
      })
    },


    // 赋予复选框选中的值
    handlerSelectionChange(val) {
      this.multipleSelection = val
    },

    // 批量修改信息
    update_information_batch(list) {
      this.request.post("/user/batch", list).then(res => {
        if (res.code === '200') {
          this.$message.success("修改成功")

          this.load_user()
        } else
          this.$message.error("修改失败")
      })
    },

    // 判断选中的信息
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


    // 删除信息
    handlerDelete(id) {
      this.request.delete("/user/" + id).then(res => {
        if (res.code === '200') {
          this.$message.success("删除成功")

          this.load_user()
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

      this.request.post("/user/delete/batch", ids).then(res => {
        if (res.code === '200') {
          this.$message.success("删除成功")

          this.pageNum = 1
          this.load_user()
        } else
          this.$message.error("删除失败")
      })
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
          this.load_user()
        } else
          this.$message.error(res.message)
      })
    },

    // 导出信息
    exportFile() {
      window.open(`http://${serverIp}:9090/user/exportUserList`)
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