<template>

  <div>

    <!-- 搜索栏 -->
    <div class="pd-10">
      <el-input v-model="search_message.name" placeholder="请输入文件名" clearable suffix-icon="el-icon-user"
                style="width: 150px;"/>
      <el-input v-model="search_message.type" placeholder="请输入文件类型" clearable suffix-icon="el-icon-user"
                style="width: 150px;" class="ml-5"/>
      <el-select v-model="search_message.enable" placeholder="请选择文件状态" clearable style="width: 150px;" class="mrl-5">
        <el-option label="启用" value="1"></el-option>
        <el-option label="禁用" value="0"></el-option>
      </el-select>
      <el-button type="primary" @click="search"> 搜索</el-button>
      <el-button type="warning" @click="reset"> 重置</el-button>
    </div>

    <!--    操作-->
    <div style="margin: 10px 0">
      <el-upload action=""
                 :show-file-list="false"
                 :http-request="handlerFileUploadImport"
                 :on-success="handlerFileUploadImportSuccess" :on-error="handlerFileUploadImportError"
                 style="display: inline-block;" class="mr-10">
        <el-button type="primary"> 上传文件 <i class="el-icon-folder-add"></i></el-button>
      </el-upload>

      <!--      显示顺序-->
      <el-button type="primary" @click="reverse_order">
        {{ 'id ' + (!reverse_order_desc ? '正序' : '倒序') }}
        <i v-if="!reverse_order_desc" class="el-icon-bottom"/>
        <i v-else class="el-icon-top"/>
      </el-button>

      <el-popconfirm confirm-button-text="确定"
                     cancel-button-text="我再想想"
                     icon="InfoFilled"
                     icon-color="#ff0000"
                     title="您确定批量删除这些数据吗?"
                     @confirm="handlerDeleteBatch"
                     class="mrl-10">
        <template #reference>
          <el-button type="danger" slot="reference"> 批量删除 <i class="el-icon-remove"/></el-button>
        </template>
      </el-popconfirm>

      <el-button type="primary" class="ml-10"> 启用 <i class="el-icon-success"/></el-button>
      <el-button type="primary"> 禁用 <i class="el-icon-error"/></el-button>

    </div>

    <!--   分页选项的选择 -->
    <div class="mtb-10">
      <el-pagination
          :currentPage="pageNum" :page-size="pageSize"
          :page-sizes="[5, 10, 15, 20]" :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handlerSizeChange" @current-change="handlerCurrentChange"
      />
    </div>

    <!-- 信息 -->
    <el-table :data="tableData" border stripe
              @selection-change="handleSelectionChange"
              max-height="50rpx">
      <el-table-column type="selection" fixed align="center" width="50"/>
      <el-table-column prop="id" label="id" fixed width="80"/>
      <el-table-column prop="name" label="文件名" fixedwidth="130"/>
      <el-table-column prop="type" label="文件类型" width="130"/>
      <el-table-column prop="size" label="文件大小(KB)" width="130"/>
      <el-table-column label="预览" width="200" align="center">
        <template slot-scope="scope">
          <el-button v-if="scope.row.type == 'png' || scope.row.type == 'jpeg' || scope.row.type === 'jpg'"
                     @click="previewPicture(scope.row.url)" type="primary">预览
          </el-button>
        </template>
      </el-table-column>
      <el-table-column label="下载" width="200" align="center">
        <template slot-scope="scope">
          <el-button @click="download(scope.row.url)" type="primary">下载</el-button>
        </template>
      </el-table-column>

      <el-table-column label="状态" align="center">
        <template slot-scope="scope">
          <el-switch v-model="scope.row.enable" @change="changeEnable(scope.row)" active-color="#13ce66"
                     inactive-color="#ff4949"/>
        </template>
      </el-table-column>

      <el-table-column fixed="right" label="操作" width="400" align="center">
        <template #default="scope">

          <el-button type="success" @click="handlerEdit(scope.row)"> 重命名</el-button>

          <!--        删除用户-->
          <el-popconfirm
              confirm-button-text='确定'
              cancel-button-text='我再想想'
              icon="el-icon-info"
              icon-color="#ff0000"
              title="您确定删除吗？"
              @confirm="handlerDelete(scope.row.id)"
              class="ml-10"
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

    <!-- 修改弹窗 -->
    <el-dialog title="修改文件名" :visible.sync="dialogFormVisible_update" width="30%">
      <el-form label-width="100px" :model="form_update" :rules="form_update_rules" ref="update_form">
        <el-form-item label="文件名" prop="name">
          <el-input v-model="form_update.name" clearable/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="handlerEdit_close">取 消</el-button>
        <el-button type="primary" @click="handlerEdit_ok">确 定</el-button>
      </div>
    </el-dialog>

    <!--    图片预览-->
    <el-dialog :visible.sync="dialogVisible">
      <img width="100%" :src="dialogImageUrl" alt="">
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
      search_message: {name: "", type: "", enable: "",},

      // 显示顺序
      reverse_order_desc: false,

      multipleSelection: [],
      multipleSelection_length: 0,

      pageNum: 1,
      pageSize: 5,
      total: 0,

      // 修改用户信息弹窗
      dialogFormVisible_update: false,
      form_update: {},
      form_update_rules: {
        name: [
          {required: true, message: '请输入文件名', trigger: 'blur'}
        ],
      },

      dialogImageUrl: '',
      dialogVisible: false,

    }
  },

  created() {
    this.load_file()

  },

  methods: {
    // 加载
    load_file() {
      this.request.get("/files/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.search_message.name,
          type: this.search_message.type,
          enable: this.search_message.enable,
          desc: this.reverse_order_desc,
        }
      }).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },

    // 搜索
    search() {
      this.load_file()
      this.pageNum = 1
    },

    // 重置搜索
    reset() {
      Object.keys(this.search_message)
          .forEach(key => (this.search_message[key] = ""))
      this.load_file()
    },

    // 分页查询
    handlerSizeChange(pageSize) {
      this.pageSize = pageSize
      this.load_file()
    },
    handlerCurrentChange(pageNum) {
      this.pageNum = pageNum
      this.load_file()
    },

    // 显示顺序
    reverse_order() {
      this.reverse_order_desc = !this.reverse_order_desc
      this.load_file()
    },

    // 赋予复选框选中的值
    handleSelectionChange(val) {
      this.multipleSelection = val
    },

    // 判断选中的用户信息
    check_multipleSelection() {
      this.multipleSelection_length = 0
      if (this.multipleSelection.length === undefined || this.multipleSelection.length === 0) {
        this.$message.error("请选择文件")
        return false
      }
      this.multipleSelection_length = this.multipleSelection.length
      return true
    },

    // 删除信息
    handlerDelete(id) {
      this.request.delete("/files/" + id).then(res => {
        if (res.code === '200') {
          this.$message.success("删除成功")

          this.load_file()
        } else
          this.$message.error("删除失败")
      })
    },
    handlerDeleteBatch() {
      this.check_multipleSelection()
      if (this.multipleSelection_length == 0)
        return false

      // 将对象数据 变成 id数组
      // [{}, {}, {}] => [1, 2, 3 ]
      let ids = this.multipleSelection.map(v => v.id)
      console.log(ids)

      this.request.post("/files/delete/batch", ids).then(res => {
        if (res.code === '200') {
          this.$message.success("删除成功")

          this.load_file()
        } else
          this.$message.error("删除失败")
      })
    },

    // 修改信息
    update_information(form) {
      this.request.post("/files", form).then(res => {
        if (res.code === '200') {
          this.$message.success("修改成功")

          this.load_file()
        } else
          this.$message.error("修改失败")
      })
    },

    // 修改状态
    changeEnable(row) {
      this.update_information(row)
    },

    // 操作的编辑修改信息
    handlerEdit(row) {
      this.dialogFormVisible_update = true
      this.form_update = Object.assign({}, row)
    },
    handlerEdit_close() {
      this.dialogFormVisible_update = false
      this.$refs.update_form.resetFields()
    },
    handlerEdit_ok() {
      this.$refs.update_form.validate((valid) => {
        // 表单校验是否合法
        if (valid) {
          this.update_information(this.form_update)

          this.handlerEdit_close()
        } else {
          this.$message.error("请确认输入")
        }
      })
    },


    // 导入信息
    handlerFileUploadImport(param) {
      let formData = new FormData()
      formData.append("file", param.file)

      this.request.post("/files/import/file", formData).then(res => {
        if (res.code === '200') {
          this.$message.success("导入成功")

          this.load_file()
        } else
          this.$message.error(res.message)
      })
    },
    handlerFileUploadImportSuccess() {
      this.$message.success("导入成功")
    },
    handlerFileUploadImportError() {
      this.$message.error("导入失败")
    },
    handlePreview(file) {
      console.log("res" + file)
    },

    // 下载文件
    download(url) {
      window.open(url)
    },


    // 预览图片
    previewPicture(url) {
      this.dialogVisible = true
      this.dialogImageUrl = url
    },


  },


}
</script>

<style scoped>

</style>