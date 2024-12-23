<template>
  <div>
    <div>
      <el-input v-model="goodsId" placeholder="查询商品id" style="width: 200px"></el-input>
      <el-button icon="el-icon-search" style="margin-left: 10px" type="primary" @click="load(1)">查询</el-button>
      <el-button icon="el-icon-refresh" type="primary" @click="reset">重置</el-button>
      <el-button plain type="danger" @click="delBatch">批量删除</el-button>
    </div>
    <div style="margin: 10px 0"></div>

    <el-table :data="tableData" :header-cell-style="{backgroundColor: 'aliceblue',color: '#666'}" stripe
              @selection-change="handleSelectionChange">>
      <el-table-column align="center" type="selection" width="55"></el-table-column>
      <el-table-column label="商品id" prop="goodsId"></el-table-column>
      <el-table-column label="用户id" prop="userId"></el-table-column>
      <el-table-column label="商品数量" prop="num"></el-table-column>
      <el-table-column label="操作时间" prop="time"></el-table-column>
      <el-table-column align="center" label="操作" width="206">
        <template v-slot="scope">
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

  </div>
</template>

<script>

export default {
  name: "Cart",
  data() {
    return {
      tableData: [],  //所有的数据
      pageNum: 1,  //当前的页码
      pageSize: 9,  // 每页现实的个数
      goodsId: '',
      total: 0,
      fromVisible: false,
      form: {},
      user: JSON.parse(localStorage.getItem('honey-user') || '{}'),
      ids: [],
    }
  },
  created() {
    this.load()
  },
  methods: {
    delBatch() {
      if (!this.ids.length) {
        this.$message.warning('请选择数据')
        return
      }
      this.$confirm('您确认批量删除吗？', '确认删除', {type: "warning"}).then(response => {
        this.$request.delete('/Cart/delete/batch', {data: this.ids}).then(res => {
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
        this.$request.delete('/Cart/delete/' + id).then(res => {
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
    reset() {
      this.goodsId = ''
      this.load()
    },
    load(pageNum) {//分页查询
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/Cart/page', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          goodsId: this.goodsId,
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
  max-width: 300px !important;
}
</style>