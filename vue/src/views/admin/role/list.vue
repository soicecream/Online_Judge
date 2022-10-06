<template>
  <div>
    <!-- 搜索栏 -->
    <div class="pd-10">
      <el-input placeholder="请输入角色名称" v-model="search_message.name" suffix-icon="el-icon-user" style="width: 150px;"/>
      <el-input placeholder="请输入角色描述" v-model="search_message.description" suffix-icon="el-icon-user" style="width: 150px;" class="mrl-5"/>
      <el-button type="primary" @click="search"> 搜索</el-button>
      <el-button type="warning" @click="reset"> 重置</el-button>
    </div>

    <!-- 操作 -->
    <div style="margin: 10px 0">
      <el-button @click="handleAdd" type="primary"> 新增用户 <i class="el-icon-circle-plus"/></el-button>

      <!--      批量删除-->
      <el-popconfirm
          confirm-button-text="确定" cancel-button-text="我再想想"
          icon="el-icon-info" icon-color="#ff0000"
          title="您确定批量删除这些数据吗?" class="mrl-10"
          @confirm="handlerDelBatch"
      >
        <template #reference>
          <el-button type="danger" slot="reference"> 批量删除 <i class="el-icon-remove"/></el-button>
        </template>
      </el-popconfirm>

      <el-button type="primary" @click="exportFile" class="ml-10"> 导出角色信息 <i class="el-icon-folder-checked"></i>
      </el-button>

      <!--      显示顺序-->
      <el-button type="primary" @click="reverse_order" class="ml-10"> {{ 'id ' + reverse_order_value }}<i
          :class="reverse_order_btncls"/></el-button>

    </div>

    <!-- 角色信息 -->
    <el-table :data="tableData" border stripe @selection-change="handleSelectionChange">
      <el-table-column type="selection" align="center"/>
      <el-table-column prop="id" label="id" align="center" width="80"/>
      <el-table-column prop="name" label="角色名称"/>
      <el-table-column prop="description" label="角色描述"/>

      <!--     操作该角色信息-->
      <el-table-column label="操作" align="center">
        <template #default="scope">

          <el-button type="success" @click="handlerEdit(scope.row)"> 编辑</el-button>

          <!--        删除该角色-->
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
    <div class="demo-pagination-block">
      <el-pagination
          :currentPage="pageNum" :page-size="pageSize"
          :page-sizes="[5, 10, 15, 20]" :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange" @current-change="handleCurrentChange"
      />
    </div>

    <!-- 添加用户角色的弹窗 -->
    <el-dialog title="添加角色信息" :visible.sync="dialogFormVisible" width="30%">

      <el-form :model="form" :rules="form_rules" ref="user_form" label-width="100px">
        <el-form-item label="角色名称" prop="name">
          <el-input v-model="form.name"/>
        </el-form-item>
        <el-form-item label="角色描述" prop="description">
          <el-input v-model="form.description"/>
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="handleAdd_close">取 消</el-button>
        <el-button type="primary" @click="handleAdd_ok">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 修改用户角色的弹窗 -->
    <el-dialog title="修改角色信息" :visible.sync="dialogFormVisible_update" width="30%">

      <el-form label-width="100px" :model="form_update" :rules="form_update_rules" ref="user_update_form">
        <el-form-item label="角色名称" prop="name">
          <el-input v-model="form_update.name"/>
        </el-form-item>
        <el-form-item label="角色描述" prop="description">
          <el-input v-model="form_update.description"/>
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
export default {
  name: "list",

  data() {
    return {
      tableData: [],

      // 表格设计
      total: 0,
      pageNum: 1,
      pageSize: 10,

      // 搜索的信息
      search_message: {
        name: "", // 角色名称
        description: "", // 角色描述
      },

      // 显示顺序
      reverse_order_value: "正序",
      reverse_order_desc: false,
      reverse_order_btncls: 'el-icon-bottom',

      // 复选框选中
      multipleSelection: {},
      multipleSelection_length: "",

      // 添加角色信息弹窗
      dialogFormVisible: false,
      form: {},
      form_rules: {
        username: [
          {required: true, message: '请输入角色名称', trigger: 'blur'},
          {min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur'}
        ],
      },

      // 修改角色信息弹窗
      dialogFormVisible_update: false,
      form_update: {},
      form_update_rules: {
        name: [
          {required: true, message: '请输入角色名称', trigger: 'blur'},
          {min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur'}
        ],
      },

    }
  },

  created() {
    this.load_role()

  },

  methods: {
    // 加载用户
    load_role() {
      this.request.get("/role/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.search_message.name,
          description: this.search_message.description,
          desc: this.reverse_order_desc,
        }
      }).then(res => {
        if (res.code === '200') {
          this.tableData = res.data.records
          console.log(this.tableData)
          this.total = res.data.total
        } else {
          this.$message.error("请求失败")
        }
      })
    },

    // 搜索
    search() {
      this.pageNum = 1
      this.load_role()
    },

    // 重置搜索
    reset() {
      Object.keys(this.search_message).forEach(key => (this.search_message[key] = ""))
      this.load_role()
    },

    // 分页查询
    handleSizeChange(pageSize) {
      this.pageSize = pageSize
      this.load_role()
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum
      this.load_role()
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
      this.load_role()
    },


    // 添加角色
    handleAdd() {
      this.dialogFormVisible = true
      if (this.$refs.user_form !== undefined)
        this.$refs.user_form.resetFields()
    },
    handleAdd_close() {
      // 关闭添加的窗口
      this.dialogFormVisible = false
      // 清空数据
      this.$refs.user_form.resetFields()
    },
    handleAdd_ok() {
      this.$refs.user_form.validate((valid) => {
        if (valid) {
          this.request.post("/role", this.form).then(res => {
            if (res.code === "200") {
              this.$message.success("角色添加成功")

              this.reverse_order_desc = true
              this.load_role()

              this.handleAdd_close()
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

    // 修改角色信息
    update_information(form) {
      this.request.post("/role", form).then(res => {
        if (res.code === '200') {
          this.$message.success("修改成功")

          this.load_role()
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
    handleSelectionChange(val) {
      this.multipleSelection = val
    },

    // 批量修改用户信息
    update_information_batch(list) {
      this.request.post("/role/batch", list).then(res => {
        if (res.code === '200') {
          this.$message.success("修改成功")

          this.load_role()
        } else
          this.$message.error("修改失败")
      })
    },

    // 判断选中的角色信息
    check_multipleSelection() {
      this.multipleSelection_length = 0
      if (this.multipleSelection.length === undefined || this.multipleSelection.length === 0) {
        this.$message.error("请选择角色")
        return false
      }
      this.multipleSelection_length = this.multipleSelection.length
      return true
    },

    // 删除角色信息
    handlerDelete(id) {
      this.request.delete("/role/" + id).then(res => {
        if (res.code === '200') {
          this.$message.success("删除成功")

          this.load_role()
        } else
          this.$message.error("删除失败")
      })
    },
    handlerDelBatch() {
      // 将对象数据 变成 id数组
      // [{}, {}, {}] => [1, 2, 3 ]
      let ids = this.multipleSelection.map(v => v.id)

      this.request.post("/role/delete/batch", ids).then(res => {
        if (res.code === '200') {
          this.$message.success("删除成功")

          this.load_role()
        } else
          this.$message.error("删除失败")
      })
    },

    // 导出角色信息
    exportFile() {
      window.open("http://localhost:9090/role/export")
    },


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