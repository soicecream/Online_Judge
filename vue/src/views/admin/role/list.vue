<template>
  <div>
    <!-- 搜索栏 -->
    <div class="pd-10">
      <el-input placeholder="请输入用户名" v-model="search_message.userUsername" clearable suffix-icon="el-icon-user"
                style="width: 180px;"/>
      <el-input placeholder="请输入用户姓名" v-model="search_message.userRealname" clearable suffix-icon="el-icon-user"
                style="width: 180px;" class="mrl-5"/>
      <el-select v-model="search_message.roleId" placeholder="请选择角色权限" filterable clearable style="width: 180px;"
                 class="mrl-5">
        <el-option v-for="item in roleList" :key="item.id" :label="item.description" :value="item.id">
          {{ item.description }}
        </el-option>
      </el-select>

      <el-button type="primary" @click="search"> 搜索</el-button>
      <el-button type="warning" @click="reset"> 重置</el-button>
    </div>

    <!-- 操作 -->
    <div style="margin: 10px 0">
      <el-button @click="handlerAdd" type="primary"> 新增用户权限 <i class="el-icon-circle-plus"/></el-button>

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

      <!--      显示顺序-->
      <el-button type="primary" @click="reverse_order">
        {{ 'id ' + (!reverse_order_desc ? '正序' : '倒序') }}
        <i v-if="!reverse_order_desc" class="el-icon-bottom"/>
        <i v-else class="el-icon-top"/>
      </el-button>

    </div>

    <!--   分页选项的选择 -->
    <div class="mtb-10">
      <el-pagination :currentPage="pageNum" :page-size="pageSize"
                     :page-sizes="[5, 10, 15, 20]" :total="total"
                     layout="total, sizes, prev, pager, next, jumper"
                     @size-change="handlerSizeChange" @current-change="handlerCurrentChange"/>
    </div>

    <!-- 信息 -->
    <el-table :data="tableData" border stripe @selection-change="handlerSelectionChange">
      <el-table-column type="selection" align="center"/>
      <el-table-column prop="id" label="id" width="50" align="center"/>
      <el-table-column prop="userUsername" label="用户名"/>
      <el-table-column prop="userRealname" label="用户姓名"/>
      <el-table-column label="角色权限">
        <template #default="scope">
          {{ roleList[scope.row.roleId - 1].description }}
        </template>
      </el-table-column>

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

    <!--   分页选项的选择 -->
    <div class="mtb-10">
      <el-pagination
          :currentPage="pageNum" :page-size="pageSize"
          :page-sizes="[5, 10, 15, 20]" :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handlerSizeChange" @current-change="handlerCurrentChange"
      />
    </div>

    <!-- 添加的弹窗 -->
    <el-dialog title="添加用户权限信息" :visible.sync="dialogFormVisible" width="30%">

      <el-form :model="form" :rules="form_rules" ref="user_form" label-width="100px">
        <el-form-item label="用户名" prop="userUsername">
          <el-input v-model="form.userUsername" clearable/>
        </el-form-item>
        <el-form-item label="用户权限" prop="roleId">
          <el-select v-model="form.roleId" placeholder="请选择" filterable clearable style="width: 100%;">
            <el-option v-for="item in roleList" :key="item.id" :label="item.description" :value="item.id">
              {{ item.description }}
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="handlerAdd_close">取 消</el-button>
        <el-button type="primary" @click="handlerAdd_ok">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 修改用户角色的弹窗 -->
    <el-dialog title="修改用户权限信息" :visible.sync="dialogFormVisible_update" width="30%">

      <el-form label-width="100px" :model="form_update" :rules="form_update_rules" ref="user_update_form">
        <el-form-item label="用户名" prop="userid">
          <el-input v-model="form_update.userUsername" class="mouse-hand"/>
        </el-form-item>
        <el-form-item label="权限" prop="roleId">
          <el-select v-model="form_update.roleId" placeholder="请选择角色权限" filterable clearable style="width: 100%;">
            <el-option v-for="item in roleList" :key="item.id" :label="item.description" :value="item.id">
              {{ item.description }}
            </el-option>
          </el-select>
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
        userUsername: "",
        userRealname: "",
        roleId: "",
      },

      // 显示顺序
      reverse_order_desc: false,

      // 复选框选中
      multipleSelection: {},
      multipleSelection_length: "",

      // 添加角色信息弹窗
      dialogFormVisible: false,
      form: {userUsername: "", roleId: ""},
      form_rules: {
        userUsername: [
          {required: true, message: '请输入用户id', trigger: 'blur'},
        ],
        roleId: [
          {required: true, message: '请选权限', trigger: 'change'}
        ],
      },

      // 修改角色信息弹窗
      dialogFormVisible_update: false,
      form_update: {},
      form_update_rules: {
        roleId: [
          {required: true, message: '请选权限', trigger: 'change'}
        ],

      },

      roleList: [],

    }
  },

  created() {
    this.load_role()
    this.load_roleList()

  },

  methods: {
    // 加载用户权限
    load_role() {
      this.request.get("/userRole/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          userUsername: this.search_message.userUsername,
          userRealname: this.search_message.userRealname,
          roleId: this.search_message.roleId,
          desc: this.reverse_order_desc,
        }
      }).then(res => {
        if (res.code === '200') {
          this.tableData = res.data.records
          this.total = res.data.total
        } else {
          this.$message.error(res.message)
        }
      })
    },

    // 加载权限信息
    load_roleList() {
      this.request.get("/role").then(res => {
        if (res.code === '200') {
          this.roleList = res.data
        } else {
          this.$message.error(res.message)
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
    handlerSizeChange(pageSize) {
      this.pageSize = pageSize
      this.load_role()
    },
    handlerCurrentChange(pageNum) {
      this.pageNum = pageNum
      this.load_role()
    },

    // 显示顺序
    reverse_order() {
      this.reverse_order_desc = !this.reverse_order_desc
      this.load_role()
    },


    // 添加角色
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
          console.log(this.form)
          this.request.post("/userRole/addRole", this.form).then(res => {
            if (res.code === "200") {
              this.$message.success("添加成功")

              this.reverse_order_desc = true
              this.load_role()

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

    // 修改角色信息
    update_information(form) {
      console.log(form)
      this.request.post("/userRole/update", form).then(res => {
        if (res.code === '200') {
          this.$message.success("修改成功")

          this.load_role()
        } else
          this.$message.error("修改失败")
      })
    },

    // 操作的编辑修改角色信息
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

    // 赋予复选框选中的值
    handlerSelectionChange(val) {
      this.multipleSelection = val
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
      this.request.delete("/userRole/" + id).then(res => {
        if (res.code === '200') {
          this.$message.success("删除成功")

          this.load_role()
        } else
          this.$message.error("删除失败")
      })
    },
    handlerDelBatch() {
      this.check_multipleSelection()
      if (this.multipleSelection_length == 0)
        return false

      // 将对象数据 变成 id数组   [{}, {}, {}] => [1, 2, 3 ]
      let ids = this.multipleSelection.map(v => v.id)

      this.request.post("/userRole/delete/batch", ids).then(res => {
        if (res.code === '200') {
          this.$message.success("批量删除成功")

          this.load_role()
        } else
          this.$message.error("批量删除失败")
      })
    },

  },
}
</script>

<style scoped>

</style>