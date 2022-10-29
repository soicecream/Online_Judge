<template>
  <div>
    <!-- 搜索栏 -->
    <div class="pd-10">
      <el-input placeholder="请输入角色名称" v-model="search_message.name" suffix-icon="el-icon-user" style="width: 180px;"/>
      <el-input placeholder="请输入角色描述" v-model="search_message.description" suffix-icon="el-icon-user"
                style="width: 180px;" class="mrl-5"/>
      <el-input placeholder="请输入角色唯一标识" v-model="search_message.roleKey" suffix-icon="el-icon-user"
                style="width: 180px;" class="mrl-5"/>
      <el-button type="primary" @click="search"> 搜索</el-button>
      <el-button type="warning" @click="reset"> 重置</el-button>
    </div>

    <!-- 操作 -->
    <div style="margin: 10px 0">
      <el-button @click="handlerAdd" type="primary"> 新增用户 <i class="el-icon-circle-plus"/></el-button>

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
      <el-button type="primary" @click="reverse_order" class="ml-10"> {{ 'id ' + reverse_order_value }}<i
          :class="reverse_order_btncls"/></el-button>

    </div>

    <!-- 信息 -->
    <el-table :data="tableData" border stripe @selection-change="handlerSelectionChange">
      <el-table-column type="selection" align="center"/>
      <el-table-column prop="id" label="id" align="center" width="80"/>
      <el-table-column prop="name" label="角色名称"/>
      <el-table-column prop="description" label="角色描述"/>
      <el-table-column prop="roleKey" label="角色唯一标识"/>

      <!--     操作该角色信息-->
      <el-table-column label="操作" align="center">
        <template #default="scope">

          <el-button type="info" @click="handlerMenu(scope.row)"> 分配菜单</el-button>

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
    <div class="mt-10">
      <el-pagination
          :currentPage="pageNum" :page-size="pageSize"
          :page-sizes="[5, 10, 15, 20]" :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handlerSizeChange" @current-change="handlerCurrentChange"
      />
    </div>

    <!-- 添加弹窗 -->
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
        <el-button @click="handlerAdd_close">取 消</el-button>
        <el-button type="primary" @click="handlerAdd_ok">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 修改弹窗 -->
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

    <!-- 分配角色管理菜单的弹窗 -->
    <el-dialog title="菜单分配" :visible.sync="dialogFormVisible_menu" width="30%">

      <el-tree
          show-checkbox
          node-key="id" default-expand-all
          :data="MenuData" :props="props"
          :default-expanded-keys="menu_expends" :default-checked-keys="menu_checks"
          ref="menu_tree"
      >
        <span class="custom-tree-node" slot-scope="{node, data}">
          <span> <i :class="data.icon"/> {{ data.name }}</span>
        </span>
      </el-tree>

      <div slot="footer" class="dialog-footer">
        <el-button @click="handlerMenu_close">取 消</el-button>
        <el-button type="primary" @click="handlerMenu_ok">确 定</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
export default {
  name: "information",

  data() {
    return {
      tableData: [],

      // 表格设计
      total: 0,
      pageNum: 1,
      pageSize: 10,

      // 搜索的信息
      search_message: {
        name: "",
        description: "",
        roleKey: "",
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
        name: [
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

      dialogFormVisible_menu: false,
      MenuData: [],
      menu_expends: [],
      menu_checks: [],
      props: {
        label: "name",
      },

      roleId: 0,
      roleFlag: '',

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
          roleKey: this.search_message.roleKey,
          desc: this.reverse_order_desc
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
          this.request.post("/role", this.form).then(res => {
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
      this.request.post("/role", form).then(res => {
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


    // 分配菜单
    handlerMenu(role) {

      this.roleId = role.id
      this.roleFlag = role.flag

      this.request.get("/menu/findTreeMenus").then(res => {
        this.MenuData = res.data

        // 把后台返回的菜单数据处理成id数组
        this.menu_expends = this.MenuData.map(v => v.id)
      })

      this.request.get("/role/roleMenu/" + this.roleId).then(res => {

        this.dialogFormVisible_menu = true

        this.menu_checks = res.data
        this.request.get("/menu/ids").then(item => {
          const ids = item.data
          ids.forEach(id => {
            if (!this.menu_checks.includes(id)) {
              // 等到这个元素渲染完成之后 再去执行
              this.$nextTick(() => {
                this.$refs.menu_tree.setChecked(id, false)
              })
            }
          })
        })
      })

    },
    handlerMenu_close() {
      this.dialogFormVisible_menu = false
    },
    handlerMenu_ok() {
      this.request.post("/role/roleMenu/" + this.roleId, this.$refs.menu_tree.getCheckedKeys()).then(res => {
        if (res.code === '200') {
          this.$message.success("分配菜单成功")
          this.handlerMenu_close()


        } else {
          this.$message.error(res.message)
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
      this.request.delete("/role/" + id).then(res => {
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

      this.request.post("/role/delete/batch", ids).then(res => {
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