<template>
  <div>

    <!-- 操作 -->
    <div style="margin: 10px 0">
      <el-button @click="handlerAdd()" type="primary"> 新增菜单 <i class="el-icon-circle-plus"/></el-button>

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

    </div>

    <!-- 菜单信息 -->
    <el-table :data="tableData" border stripe @selection-change="handlerSelectionChange"
              row-key="id" default-expand-all>
      <el-table-column type="selection" align="center"/>
      <el-table-column prop="id" label="id" align="center" width="80"/>
      <el-table-column prop="name" label="菜单名称"/>
      <el-table-column prop="path" label="菜单路劲"/>
      <el-table-column label="菜单图标" align="center">
        <template #default="scope">
          <i :class="scope.row.icon" style="font-size: 30px;"/>
        </template>
      </el-table-column>
      <el-table-column prop="description" label="菜单描述"/>
      <el-table-column prop="pagePath" label="页面路径"/>

      <!--     操作该菜单信息-->
      <el-table-column label="操作" width="300" align="center">
        <template #default="scope">

          <el-button type="primary" @click="handlerAdd(scope.row.id)"> 新增子菜单</el-button>

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

    <!-- 添加菜单的弹窗 -->
    <el-dialog title="添加菜单信息" :visible.sync="dialogFormVisible" width="30%">

      <el-form :model="form" :rules="form_rules" ref="user_form" label-width="100px">
        <el-form-item label="菜单名称" prop="name">
          <el-input v-model="form.name"/>
        </el-form-item>
        <el-form-item label="菜单路径" prop="path">
          <el-input v-model="form.path"/>
        </el-form-item>

        <el-form-item label="菜单图标" prop="icon">
          <el-select v-model="form.icon" placeholder="请选择" filterable clearable style="width: 100%;">
            <el-option v-for="item in options" :key="item.name" :label="item.name" :value="item.value">
              <i :class="item.value"/> {{ item.name }}
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="菜单描述" prop="description"><el-input v-model="form.description"/></el-form-item>
        <el-form-item label="页面路径" prop="pagePath"><el-input v-model="form.pagePath"/></el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="handlerAdd_close">取 消</el-button>
        <el-button type="primary" @click="handlerAdd_ok">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 修改菜单的弹窗 -->
    <el-dialog title="修改菜单信息" :visible.sync="dialogFormVisible_update" width="30%">

      <el-form label-width="100px" :model="form_update" :rules="form_update_rules" ref="user_update_form">
        <el-form-item label="菜单名称" prop="name">
          <el-input v-model="form_update.name"/>
        </el-form-item>
        <el-form-item label="菜单路径" prop="path">
          <el-input v-model="form_update.path"/>
        </el-form-item>

        <el-form-item label="菜单图标" prop="icon">
          <el-select v-model="form_update.icon" placeholder="请选择" filterable clearable style="width: 100%;">
            <el-option v-for="item in options" :key="item.name" :label="item.name" :value="item.value">
              <i :class="item.value"/> {{ item.name }}
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="菜单描述" prop="description"><el-input v-model="form_update.description"/></el-form-item>
        <el-form-item label="页面路径" prop="pagePath"><el-input v-model="form_update.pagePath"/></el-form-item>
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

      // 复选框选中
      multipleSelection: {},
      multipleSelection_length: "",

      // 添加菜单信息弹窗
      dialogFormVisible: false,
      form: {},
      form_rules: {
        name: [
          {required: true, message: '请输入菜单名称', trigger: 'blur'},
          {min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur'}
        ],
      },

      // 修改菜单信息弹窗
      dialogFormVisible_update: false,
      form_update: {},
      form_update_rules: {
        name: [
          {required: true, message: '请输入菜单名称', trigger: 'blur'},
          {min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur'}
        ],
      },

      // 图标
      options: [],

    }
  },

  created() {
    this.load_role()

  },

  methods: {
    // 加载菜单
    load_role() {
      this.request.get("/menu/findTreeMenus").then(res => {
        if (res.code === '200') {
          this.tableData = res.data
        } else {
          this.$message.error("请求失败")
        }
      })
    },


    // 请求图标
    load_icons() {
      this.request.get("/menu/icons").then(res => {
        if (res.code === '200') {
          this.options = res.data
        } else {
          this.$message.error("请求失败")
        }
      })
    },

    // 添加菜单
    handlerAdd(rowId) {
      this.dialogFormVisible = true
      if (this.$refs.user_form !== undefined)
        this.$refs.user_form.resetFields()

      if (rowId)
        this.form.pid = rowId

      this.load_icons()
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
          console.log(this.form.id)
          this.request.post("/menu", this.form).then(res => {
            if (res.code === "200") {
              this.$message.success("添加成功")

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

    // 修改菜单信息
    update_information(form) {
      this.request.post("/menu", form).then(res => {
        if (res.code === '200') {
          this.$message.success("修改成功")

          this.load_role()
        } else
          this.$message.error("修改失败")
      })
    },

    // 操作的编辑修改菜单信息
    handlerEdit(row) {
      this.dialogFormVisible_update = true
      this.form_update = Object.assign({}, row)

      this.load_icons()
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
        this.$message.error("请选择菜单")
      }
      this.multipleSelection_length = this.multipleSelection.length
    },

    // 删除菜单信息
    handlerDelete(id) {
      this.request.delete("/menu/" + id).then(res => {
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
      console.log(ids)

      this.request.post("/menu/delete/batch", ids).then(res => {
        if (res.code === '200') {
          this.$message.success("删除成功")

          this.load_role()
        } else
          this.$message.error("删除失败")
      })
    },

  },
}
</script>

<style scoped>

</style>