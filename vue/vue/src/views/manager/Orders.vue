<template>
  <div>
    <div>
      <el-input v-model="name" placeholder="查询商品名称" style="width: 200px"></el-input>
      <el-button icon="el-icon-search" style="margin-left: 10px" type="primary" @click="load(1)">查询</el-button>
      <el-button icon="el-icon-refresh" type="primary" @click="reset">重置</el-button>
      <el-button plain type="danger" @click="delBatch">批量删除</el-button>
    </div>
    <div style="margin: 10px 0"></div>

    <el-table :data="tableData" :header-cell-style="{backgroundColor: 'aliceblue',color: '#666'}" stripe
              @selection-change="handleSelectionChange">>
      <el-table-column align="center" type="selection" width="55"></el-table-column>
      <el-table-column label="名称" prop="name"></el-table-column>
      <el-table-column label="订单编号" prop="no"></el-table-column>
      <el-table-column label="总价" prop="totalPrice" width="80"></el-table-column>
      <el-table-column label="状态" prop="state" width="100"></el-table-column>
      <el-table-column label="下单时间" prop="time"></el-table-column>
      <el-table-column label="付款时间" prop="payTime"></el-table-column>
      <el-table-column label="用户账号" prop="username" width="100"></el-table-column>
      <el-table-column label="用户昵称" prop="nickname" width="100"></el-table-column>

      <el-table-column align="center" label="查看商品" width="100">
        <template v-slot="scope">
          <el-button plain size="mini" type="primary" @click="viewGoods(scope.row.id)">查看商品</el-button>
        </template>
      </el-table-column>

      <el-table-column align="center" label="操作" width="100">
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

    <el-dialog :close-on-click-modal="false" :visible.sync="dialogFormVisible1" title="商品信息" width="60%">
      <el-table :data="goodsList" border stripe>
        <el-table-column label="商品名称" prop="name"></el-table-column>
        <el-table-column label="价格" prop="price" width="60px"></el-table-column>
        <el-table-column label="购买数量" prop="num" width="70px"></el-table-column>
        <el-table-column label="单位" prop="unit" width="60px"></el-table-column>
        <el-table-column label="描述" prop="description"></el-table-column>
        <el-table-column label="库存" prop="store" width="60px"></el-table-column>
        <el-table-column label="商品图片" width="110">
          <template v-slot="scope">
            <div style="display: flex;align-items: center;width: 100px;height: 100px">
              <el-image v-if="scope.row.img" :preview-src-list="[scope.row.img]" :src="scope.row.img"
                        style="width: 100px;height: 100px;border-radius: 2%"></el-image>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="上架时间" prop="time"></el-table-column>
      </el-table>
    </el-dialog>

  </div>
</template>

<script>

export default {
  name: "Cart",
  data() {
    return {
      tableData: [],  //所有的数据
      goodsList: [],
      pageNum: 1,  //当前的页码
      pageSize: 9,  // 每页现实的个数
      name: '',
      total: 0,
      fromVisible: false,
      dialogFormVisible1: false,
      form: {},
      user: JSON.parse(localStorage.getItem('honey-user') || '{}'),
      ids: [],
    }
  },
  created() {
    this.load()
  },
  methods: {
    viewGoods(order_id) {
      this.$request.get('/orders/getGoodsById/' + order_id).then(res => {
        this.goodsList = res.data
        this.dialogFormVisible1 = true
      })
    },
    delBatch() {
      if (!this.ids.length) {
        this.$message.warning('请选择数据')
        return
      }
      this.$confirm('您确认批量删除吗？', '确认删除', {type: "warning"}).then(response => {
        this.$request.delete('/orders/delete/batch', {data: this.ids}).then(res => {
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
        this.$request.delete('/orders/delete/' + id).then(res => {
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
      this.name = ''
      this.load()
    },
    load(pageNum) {//分页查询
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/orders/selectByPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
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