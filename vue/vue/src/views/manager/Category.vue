<template>
  <div>
    <div>
      <el-button plain type="primary" @click="handleAdd">新增</el-button>
      <el-button plain type="danger" @click="delBatch">批量删除</el-button>
    </div>
    <div style="margin: 10px 0"></div>

    <el-table :data="tableData" :header-cell-style="{backgroundColor: 'aliceblue',color: '#666'}" stripe
              @selection-change="handleSelectionChange">>
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column label="分类名称" prop="categoryName"></el-table-column>
      <el-table-column label="创建人" prop="createUser"></el-table-column>
      <el-table-column label="创建时间" prop="time"></el-table-column>
      <el-table-column label="是否启用">
        <template v-slot="scope">
          <el-switch v-model="scope.row.open" @change="changeOpen(scope.row)"></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="150">
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
          :page-sizes="[100, 200, 300, 400]"
          :total="total"
          layout="total, prev, pager, next"
          @current-change="handleCurrentChange">
      </el-pagination>
    </div>
    <el-dialog :visible.sync="fromVisible" title="编辑" width="30%">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px" style="padding-right: 20px">
        <el-form-item label="分类名称" prop="categoryName">
          <el-input v-model="form.categoryName" placeholder="分类名称"></el-input>
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
  name: "Category",
  data() {
    return {
      tableData: [],  //所有的数据
      pageNum: 1,  //当前的页码
      pageSize: 15,  // 每页现实的个数
      total: 0,
      fromVisible: false,
      form: {},
      categoryName: '',
      user: JSON.parse(localStorage.getItem('honey-user') || '{}'),
      rules: {
        categoryName: [
          {required: true, message: '请输入商品名称', trigger: 'blur'}
        ]
      },
      ids: []
    }
  },
  created() {
    this.load()
  },
  methods: {
    changeOpen(form) {
      //调用更新接口，更新数据到数据库
      this.form = JSON.parse(JSON.stringify(form))
      this.$request({
        url: this.form.id ? '/category/update' : '/category/add',
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
    delBatch() {
      if (!this.ids.length) {
        this.$message.warning('请选择数据')
        return
      }
      this.$confirm('您确认批量删除吗？', '确认删除', {type: "warning"}).then(response => {
        this.$request.delete('/category/delete/batch', {data: this.ids}).then(res => {
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
        this.$request.delete('/category/delete/' + id).then(res => {
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
          this.$request({
            url: this.form.id ? '/category/update' : '/category/add',
            method: this.form.id ? 'PUT' : 'POST',//根据id判断是新增还是编辑
            data: this.form
          }).then(res => {
            if (res.code === '200') {
              this.$message.success('修改成功')
              this.load(1)
              this.fromVisible = false//关闭弹窗
            } else {
              this.$message.error("此分类已存在")
            }
          })
        }
      })
    },
    reset() {
      this.categoryName = ''
      this.load()
    },
    load(pageNum) {//分页查询
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/category/selectByPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize
        }
      }).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },
    handleCurrentChange(pageNum) {
      this.load(pageNum)
    },
  }
}


</script>

<style scoped>


</style>