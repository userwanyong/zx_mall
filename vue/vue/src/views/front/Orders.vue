<template>
  <div>
    <div style="padding: 1px 200px">
      <el-button size="medium" type="danger" @click="delBatch">批量删除</el-button>
    </div>
    <div style="margin: 10px 0"></div>

    <el-table :data="tableData" :header-cell-style="{backgroundColor: 'aliceblue',color: '#666'}" stripe
              style="padding: 1px 200px"
              @selection-change="handleSelectionChange">>
      <el-table-column align="center" type="selection" width="55"></el-table-column>
      <el-table-column label="名称" prop="name" width="200"></el-table-column>
      <el-table-column label="订单编号" prop="no" width="170"></el-table-column>
      <el-table-column label="总价" prop="totalPrice" width="70"></el-table-column>
      <el-table-column label="状态" prop="state" width="70"></el-table-column>
      <el-table-column label="下单时间" prop="time" width="150"></el-table-column>
      <el-table-column label="付款时间" prop="payTime" width="150"></el-table-column>
      <el-table-column align="center" label="查看商品" width="120">
        <template v-slot="scope">
          <el-button plain size="medium" type="primary" @click="viewGoods(scope.row.id)">查看商品</el-button>
        </template>
      </el-table-column>

      <el-table-column align="center" label="操作" width="206">
        <template v-slot="scope">
          <el-button v-if="scope.row.state==='待支付'" size="medium" type="primary" @click="pay(scope.row)">支付
          </el-button>
          <el-button v-if="scope.row.state==='已支付'" size="medium" type="warning" @click="repay(scope.row)">退款
          </el-button>
          <el-button plain size="medium" type="danger" @click="del(scope.row.id)">删除</el-button>
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
    repay(row) {
      const url = `http://localhost:8080/alipay/return?totalAmount=${row.totalPrice}&alipayTraceNo=${row.payNo}&traceNo=${row.no}`//支付宝回调的订单流水号,订单的总金额,我的订单编号
      this.$request.get(url).then(res => {
        if (res.code === '200') {
          this.$message.success("退款成功")
        } else {
          this.$message.error(res.msg)
        }
        this.load()
      })
    },
    pay(row) {
      window.open('http://localhost:8080/alipay/pay?traceNo=' + row.no + "&totalAmount=" + row.totalPrice + "&subject=" + row.name)//订单号，订单总金额，订单名称
    },
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
    // reset() {
    //   this.goodsId = ''
    //   this.load()
    // },
    load(pageNum) {//分页查询
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/orders/selectByPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          // name: this.name,
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