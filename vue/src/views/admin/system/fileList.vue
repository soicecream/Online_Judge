<template>

  <div>

    <!-- 搜索栏 -->
    <div class="pd-10">
      <el-input style="width: 150px;" placeholder="请输入文件名" suffix-icon="el-icon-user"
                v-model="search_message.name"/>
      <el-input style="width: 150px;" placeholder="请输入文件类型" suffix-icon="el-icon-user" class="ml-5"
                v-model="search_message.type"/>
      <el-select v-model="search_message.enable" placeholder="请选择用户状态" style="width: 150px;" class="mrl-5">
        <el-option label="启用" value="1"></el-option>
        <el-option label="禁用" value="0"></el-option>
      </el-select>
      <el-button type="primary" @click="search"> 搜索</el-button>
      <el-button type="warning" @click="reset"> 重置</el-button>
    </div>

    <div style="margin: 10px 0">
      <el-upload action="http://localhost:9090/file/upload" :show-file-list="false"
                 :on-success="handleFileUploadSuccess"
                 style="display: inline-block;" class="mrl-10">
        <el-button type="primary"> 导入 <i class="el-icon-folder-add"></i></el-button>
      </el-upload>

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

      <el-button type="primary" class="ml-10"> 启用 <i class="el-icon-success"/></el-button>
      <el-button type="primary" class="ml-10"> 禁用 <i class="el-icon-error"/></el-button>

    </div>

    <!-- 用户信息 -->
    <el-table :data="tableData" border stripe
              @selection-change="handleSelectionChange"
              max-height="50rpx">
      <el-table-column fixed type="selection" align="center" width="50"/>
      <el-table-column fixed prop="id" label="id" width="80"/>
      <el-table-column fixed prop="name" label="文件名" width="130"/>
      <el-table-column prop="type" label="文件类型" width="130"/>
      <el-table-column prop="size" label="文件大小(KB)" width="130"/>
      <el-table-column label="下载" width="200">
        <template slot-scope="scope">
          <el-button type="primary" @click="download(scope.row.url)">下载</el-button>
        </template>
      </el-table-column>
      <el-table-column label="状态">
        <template slot-scope="scope">
          <el-switch v-model="scope.row.enable" @change="changeEnable(scope.row)" active-color="#13ce66" inactive-color="#ff4949"/>
        </template>
      </el-table-column>

      <!--    状态-->
      <el-table-column label="状态" width="70rpx" align="center">
        <template #default="scope">
          <el-tag :type="scope.row.enable === 1 ? 'success' : 'danger'">
            {{ scope.row.enable == 1 ? "启用" : "禁用" }}
          </el-tag>
        </template>
      </el-table-column>

      <el-table-column fixed="right" label="操作" width="400" align="center">
        <template #default="scope">

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

  </div>
</template>

<script>
export default {
  name: "fileList",

  data() {
    return {
      tableData: [],
      name: "",
      multipleSelection: [],
      search_message: {},

      pageNum: 10,
      pageSize: 5,

    }
  },

  created() {

  },

  methods: {
    // 加载用户
    load_user() {
      // this.request.get("/file/page", {
      //   params: {
      //     pageNum: this.pageNum,
      //     pageSize: this.pageSize,
      //     name: this.name,
      //
      //   }
      // }).then(res => {
      //   // console.log(res)
      //
      //   this.tableData = res.records
      //   this.total = res.total
      // })
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

    // 添加用户
    handleAdd() {

    },

    cancelEvent() {

    },

    // 输入好了用户信息 将要填写进去
    handleAdd_ok() {

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


    handleFileUploadSuccess(res) {
      // console.log(res)
    },

    handlerEdit() {

    },

    // 修改状态
    changeEnable(row) {

    },

    // 下载文件
    download(url) {
      window.open(url)
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