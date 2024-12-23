<template>
  <div>
    <div>
      <el-input v-model="title" placeholder="查询标题" style="width: 200px;margin: 0 10px"></el-input>
      <el-button icon="el-icon-search" type="primary" @click="load(1)">查询</el-button>
      <el-button icon="el-icon-refresh" type="primary" @click="reset">重置</el-button>

      <el-button plain style="margin-left: 946px;" type="primary" @click="handleAdd">新增</el-button>
      <el-button plain type="danger" @click="delBatch">批量删除</el-button>
    </div>

    <div style="margin: 10px 0"></div>

    <el-table :data="tableData" :header-cell-style="{backgroundColor: 'aliceblue',color: '#666'}" stripe
              @selection-change="handleSelectionChange">>
      <el-table-column align="center" type="selection" width="55"></el-table-column>
      <el-table-column label="标题" prop="title"></el-table-column>
      <el-table-column label="内容" prop="content" show-overflow-tooltip></el-table-column>
      <el-table-column label="发布人" prop="user"></el-table-column>
      <el-table-column label="发布时间" prop="ctime"></el-table-column>
      <el-table-column label="更新时间" prop="utime"></el-table-column>
      <el-table-column label="是否公开">
        <template v-slot="scope">
          <el-switch v-model="scope.row.open" @change="changeOpen(scope.row)"></el-switch>
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作" width="180">
        <template v-slot="scope">
          <el-button plain size="mini" type="primary" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button plain size="mini" type="danger" @click="del(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div style="margin: 10px 0">
      <el-pagination
          :current-page="pageNum"
          :page-size="pageSize"
          :total="total"
          layout="total, prev, pager, next"
          @current-change="handleCurrentChange">
      </el-pagination>
    </div>

    <el-dialog :close-on-click-modal="false" :visible.sync="fromVisible" title="公告信息" width="40%">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px" style="padding-right: 20px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title" placeholder="标题"></el-input>
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <el-input v-model="form.content" placeholder="内容" type="textarea"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="fromVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>

  </div>
</template>


<script>
export default {
  name: "Notice",
  data() {
    return {
      tableData: [],  //所有的数据
      pageNum: 1,  //当前的页码
      pageSize: 15,  // 每页现实的个数
      username: '',
      title: '',
      total: 0,
      fromVisible: false,
      form: {},
      user: JSON.parse(localStorage.getItem('honey-user') || '{}'),
      rules: {
        title: [
          {required: true, message: '请输入标题', trigger: 'blur'}
        ],
        content: [
          {required: true, message: '请输入内容', trigger: 'blur'}
        ]
      },
      ids: [],
      content: '',
    }
  },
  created() {
    this.load()
  },
  methods: {
    changeOpen(form) {
      //调用更新接口，更新数据到数据库
      this.form = JSON.parse(JSON.stringify(form))
      this.sendSaveRequest()
    },
    delBatch() {
      if (!this.ids.length) {
        this.$message.warning('请选择数据')
        return
      }
      this.$confirm('您确认批量删除吗？', '确认删除', {type: "warning"}).then(response => {
        this.$request.delete('/notice/delete/batch', {data: this.ids}).then(res => {
          if (res.code === '200') {//操作成功
            this.$message.success('操作成功')
            this.load(1)//刷新表格数据
          } else {
            this.$message.error(res.msg)
          }
        })
      }).catch(() => {
      })
    },
    handleSelectionChange(rows) {
      this.ids = rows.map(v => v.id)
    },
    del(id) {
      this.$confirm('您确认删除吗？', '确认删除', {type: "warning"}).then(response => {
        this.$request.delete('/notice/delete/' + id).then(res => {
          if (res.code === '200') {//操作成功
            this.$message.success('删除成功')
            this.load(1)//刷新表格数据
          } else {
            this.$message.error(res.msg)
          }
        })
      }).catch(() => {
      })
    },
    handleEdit(row) {//编辑数据
      this.form = JSON.parse(JSON.stringify(row))//给form对象赋值
      this.fromVisible = true//打开弹窗
    },
    handleAdd() {//新增数据
      this.form = {}  //新增时清空数据
      this.fromVisible = true  //打开弹窗
    },
    save() { //保存按钮触发的逻辑 新增或更新
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          this.sendSaveRequest()
        }
      })
    },
    sendSaveRequest() {
      this.$request({
        url: this.form.id ? '/notice/update' : '/notice/add',
        method: this.form.id ? 'PUT' : 'POST',//根据id判断是新增还是编辑
        data: this.form
      }).then(res => {
        if (res.code === '200') {
          this.$message.success('修改成功')
          //为方便使用，修改成功后最好不要跳转到第一页
          // this.load(1)
          this.fromVisible = false//关闭弹窗
        } else {
          this.$message.error(res.msg)//弹出错误信息
        }
      })
    },
    reset() {
      this.title = ''
      this.load()
    },
    load(pageNum) {//分页查询
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/notice/selectByPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          title: this.title
        }
      }).then(res => {
        this.tableData = res.data.records ? res.data.records : ''
        this.total = res.data.total
      })
    },
    handleCurrentChange(pageNum) {
      this.load(pageNum)
    },

  }
}


</script>

<style>
.el-tooltip__popper {
  max-width: 200px !important;
}

</style>
