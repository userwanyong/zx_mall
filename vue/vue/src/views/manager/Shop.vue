<template>
  <div>
    <div>
      <el-input v-model="name" placeholder="查询商品名" style="width: 200px"></el-input>
      <el-select v-model="category" style="margin: 0 5px">
        <el-option v-for="item in form" :key="item" :label="item" :value="item"></el-option>
      </el-select>
      <el-button icon="el-icon-search" style="margin-left: 10px" type="primary" @click="load(1)">查询</el-button>
      <el-button icon="el-icon-refresh" type="primary" @click="reset">重置</el-button>
      <el-button plain type="danger" @click="delBatch">批量删除</el-button>
    </div>
    <div style="margin: 10px 0"></div>

    <el-table :data="tableData" :header-cell-style="{backgroundColor: 'aliceblue',color: '#666'}" stripe
              @selection-change="handleSelectionChange">>
      <el-table-column align="center" type="selection" width="55"></el-table-column>
      <el-table-column label="商品名称" prop="name" show-overflow-tooltip></el-table-column>
      <el-table-column label="商品图片" width="110">
        <template v-slot="scope">
          <div style="display: flex;align-items: center;width: 57px;height: 57px">
            <el-image v-if="scope.row.img" :preview-src-list="[scope.row.img]" :src="scope.row.img"
                      style="width: 57px;height: 57px;border-radius: 5%"></el-image>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="商品详情" prop="description" width="170">
        <template v-slot="scope">
          <el-button @click="preview(scope.row.description)">显示详情</el-button>
        </template>
      </el-table-column>
      <el-table-column label="商品分类" prop="category"></el-table-column>
      <el-table-column label="商品价格" prop="price"></el-table-column>
      <el-table-column label="审核状态" prop="status">
        <template v-slot="scope">
          <el-tag v-if="scope.row.status==='待审核'" type="warning">待审核</el-tag>
          <el-tag v-if="scope.row.status==='通过'" type="success">通过</el-tag>
          <el-tag v-if="scope.row.status==='失败'" type="danger">失败</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="所属用户ID" prop="userId"></el-table-column>

      <el-table-column label="上架状态" prop="saleStatus"></el-table-column>
      <el-table-column label="浏览量" prop="readCount"></el-table-column>
      <el-table-column label="审核日期" prop="time" width="90"></el-table-column>

      <el-table-column align="center" label="操作" width="206">
        <template v-slot="scope">
          <el-button plain size="mini" type="success" @click="changeStatus(scope.row,'通过')">通过</el-button>
          <el-button plain size="mini" type="warning" @click="changeStatus(scope.row,'失败')">拒绝</el-button>
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


    <el-dialog :close-on-modal="false" :visible.sync="fromVisible2" destroy-on-close title="内容" width="40%">
      <div v-html="description"></div>
    </el-dialog>


  </div>
</template>


<script>


export default {
  name: "Shop",
  computed: {
    // category() {
    //   return category
    // }
  },
  data() {
    return {
      tableData: [],  //所有的数据
      pageNum: 1,  //当前的页码
      pageSize: 9,  // 每页现实的个数
      name: '',
      category: '',
      total: 0,
      fromVisible: false,
      form: {},
      user: JSON.parse(localStorage.getItem('honey-user') || '{}'),
      rules: {
        name: [
          {required: true, message: '请输入商品名称', trigger: 'blur'}
        ]
      },
      ids: [],
      description: '',
      fromVisible2: false
    }
  },
  created() {
    this.selectCategory()
    this.load()
  },
  methods: {
    selectCategory() {
      this.$request.get('/category/selectByCategoryName').then(res => {
        if (res.code === '200') {//操作成功
          this.form = res.data
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    preview(description) {
      this.description = description
      this.fromVisible2 = true
    },
    changeStatus(row, status) {
      this.$confirm('您确认操作吗？', '确认', {type: "warning"}).then(response => {
        this.form = JSON.parse(JSON.stringify(row))
        this.form.status = status
        this.$request.put('/shop/update', this.form).then(res => {
          if (res.code === '200') {
            this.$message.success('修改成功')
            this.load(1)
          } else {
            this.$message.error(res.msg)
          }
        })
      }).catch(() => {
      })


    },
    delBatch() {
      if (!this.ids.length) {
        this.$message.warning('请选择数据')
        return
      }
      this.$confirm('您确认批量删除吗？', '确认删除', {type: "warning"}).then(response => {
        this.$request.delete('/shop/delete/batch', {data: this.ids}).then(res => {
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
        this.$request.delete('/shop/delete/' + id).then(res => {
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

    reset() {
      this.category = ''
      this.name = ''
      this.load()
    },
    load(pageNum) {//分页查询
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/shop/selectByPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
          category: this.category
        }
      }).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },
    handleCurrentChange(pageNum) {
      this.load(pageNum)
    },
    handleAvatarSuccess(response, file, fileList) {
      //把user的头像换成上传的图片的链接
      this.form.img = response.data
    },
  }


}


</script>

<style>
.el-tooltip__popper {
  max-width: 300px !important;
}

</style>
