<template>
  <div>
    <div>
      <el-input v-model="name" placeholder="请输入商品名称" style="width: 200px;margin: 0 10px"></el-input>
      <el-button icon="el-icon-search" type="primary" @click="load(1)">查询</el-button>
      <el-button icon="el-icon-refresh" type="primary" @click="reset">重置</el-button>

      <el-button plain style="margin-left: 900px;" type="primary" @click="handleAdd">新增</el-button>
      <el-button plain type="danger" @click="delBatch">批量删除</el-button>
    </div>

    <div style="margin: 10px 0"></div>

    <el-table :data="tableData" :header-cell-style="{backgroundColor: 'aliceblue',color: '#666'}" stripe
              @selection-change="handleSelectionChange">>
      <el-table-column align="center" type="selection" width="55"></el-table-column>
      N
      <el-table-column label="商品名称" prop="name"></el-table-column>
      <el-table-column label="价格" prop="price"></el-table-column>
      <el-table-column label="描述" prop="description"></el-table-column>
      <el-table-column label="单位" prop="unit"></el-table-column>
      <el-table-column label="库存" prop="store"></el-table-column>
      <el-table-column label="商品图片" width="110">
        <template v-slot="scope">
          <div style="display: flex;align-items: center;width: 100px;height: 100px">
            <el-image v-if="scope.row.img" :preview-src-list="[scope.row.img]" :src="scope.row.img"
                      style="width: 100px;height: 100px;border-radius: 2%"></el-image>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="上架时间" prop="time"></el-table-column>
      <el-table-column label="是否售卖">
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

    <el-dialog :close-on-click-modal="false" :visible.sync="fromVisible" title="上传商品" width="40%">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px" style="padding-right: 20px">
        <el-form-item label="商品名" prop="name">
          <el-input v-model="form.name" placeholder="商品名称"></el-input>
        </el-form-item>
        <el-form-item label="价格" prop="price">
          <el-input v-model="form.price" placeholder="价格"></el-input>
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="form.description" placeholder="描述" type="textarea"></el-input>
        </el-form-item>
        <el-form-item label="单位" prop="unit">
          <el-input v-model="form.unit" placeholder="单位"></el-input>
        </el-form-item>
        <el-form-item label="库存" prop="store">
          <el-input v-model="form.store" placeholder="库存"></el-input>
        </el-form-item>
        <el-form-item label="图片">
          <el-upload
              :headers="{ token: user.token }"
              :on-success="handleAvatarSuccess"
              :show-file-list="true"
              action="http://localhost:8080/file/upload"
              class="avatar-uploader"
              list-type="picture"
          >
            <el-button type="primary">上传商品图片</el-button>
          </el-upload>
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
      pageSize: 5,  // 每页现实的个数
      name: '',
      price: '',
      description: '',
      unit: '',
      store: '',
      total: 0,
      fromVisible: false,
      form: {},
      user: JSON.parse(localStorage.getItem('honey-user') || '{}'),
      rules: {
        name: [
          {required: true, message: '请输入商品名', trigger: 'blur'}
        ],
        price: [
          {required: true, message: '请输入价格', trigger: 'blur'}
        ],
        unit: [
          {required: true, message: '请输入单位', trigger: 'blur'}
        ],
        store: [
          {required: true, message: '请输入库存', trigger: 'blur'}
        ],
        description: [
          {required: true, message: '请输入商品描述', trigger: 'blur'}
        ]
      },
      ids: [],
    }
  },
  created() {
    this.load()
  },
  methods: {
    handleAvatarSuccess(response, file, fileList) {
      //把user的头像换成上传的图片的链接
      this.form.img = response.data
    },
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
        this.$request.delete('/goods/delete/batch', {data: this.ids}).then(res => {
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
        this.$request.delete('/goods/delete/' + id).then(res => {
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
        url: this.form.id ? '/goods/update' : '/goods/add',
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
      this.name = ''
      this.load()
    },
    load(pageNum) {//分页查询
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/goods/selectByPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name
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

<style>
.el-tooltip__popper {
  max-width: 200px !important;
}

</style>
