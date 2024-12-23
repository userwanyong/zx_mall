<template>
  <div>
    <div style="padding: 1px 200px;font-size: 18px">
      <el-button size="medium" type="danger" @click="delBatch">批量移除</el-button>
      <el-button icon="el-icon-shopping-bag-1" size="medium" type="success" @click="settleAccount">去结算</el-button>
      合计：<span style="color: orangered">¥ {{ totalPrice }}</span>
    </div>
    <div style="margin: 10px 0"></div>
    <el-table :data="tableData" :header-cell-style="{backgroundColor: 'skyblue',color: '#000000'}" size="medium" stripe
              style="padding: 1px 200px"
              @selection-change="handleSelectionChange">
      <el-table-column align="center" type="selection" width="55"></el-table-column>
      <el-table-column label="商品图片" prop="goodsImg" width="180">
        <template v-slot="scope">
          <el-image :preview-src-list="[scope.row.goodsImg]" :src="scope.row.goodsImg"
                    style="width: 130px;height: 130px"></el-image>
        </template>
      </el-table-column>
      <el-table-column label="商品名称" prop="goodsName" width="220"></el-table-column>
      <el-table-column label="商品数量" prop="num" width="200">
        <template v-slot="scope">
          <el-input-number v-model="scope.row.num" :max="100" :min="1" label="数量"
                           @change="changeNum(scope.row)"></el-input-number>
        </template>
      </el-table-column>
      <el-table-column label="单价" prop="price" width="100">
        <template v-slot="scope">
          <span style="color: orangered">¥ {{ scope.row.price }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作时间" prop="time" width="160"></el-table-column>
      <el-table-column align="center" label="操作" width="300">
        <template v-slot="scope">
          <el-button size="medium" type="primary" @click="toOrder(scope.row)">结算</el-button>
          <el-button plain size="medium" type="danger" @click="del(scope.row.id)">移除</el-button>
        </template>
      </el-table-column>
    </el-table>
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
      total: 0,
      form: {},
      user: JSON.parse(localStorage.getItem('honey-user') || '{}'),
      multipleSelection: [],
      ids: [],
      totalPrice: 0,
    }
  },
  created() {
    this.load()
  },
  methods: {
    toOrder(row) {
      this.$request.post('/orders/add2', row).then(res => {
        if (res.code === '200') {//操作成功
          this.$message.success("提交成功")
          this.$router.push('/front/orders')
          this.load()
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    settleAccount() {
      if (!this.multipleSelection.length) {
        this.$message.warning('请选择需要结算的商品')
      }
      let data = {name: this.multipleSelection[0].goodsName, totalPrice: this.totalPrice, carts: this.multipleSelection}
      this.$request.post('/orders/add', data).then(res => {
        if (res.code === '200') {
          this.$message.success("提交成功")
          this.$router.push('/front/orders')
          this.load()

        } else {
          this.$message.error(res.msg)
        }
      })
    },
    changeNum(row) {
      this.$request.put('/Cart/update', JSON.parse(JSON.stringify(row))).then(res => {
      })
      this.totalPrice = 0
      this.multipleSelection.forEach(item => {
        this.totalPrice += item.num * item.price
      })
      this.totalPrice = this.totalPrice.toFixed(2)//保留两位小数
    },
    delBatch() {
      if (!this.ids.length) {
        this.$message.warning('请选择数据')
        return
      }
      this.$confirm('您确认将所选中商品从购物车移除吗？', '确认移除', {type: "warning"}).then(response => {
        this.$request.delete('/Cart/delete/batch', {data: this.ids}).then(res => {
          if (res.code === '200') {//操作成功
            this.$message.success('移除成功')
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
      this.totalPrice = 0
      this.multipleSelection = rows
      //计算总价
      if (rows && rows.length) {
        rows.forEach(item => {
          this.totalPrice += item.num * item.price
        })
      }
      this.totalPrice = this.totalPrice.toFixed(2)//保留两位小数
    },
    del(id) {
      this.$confirm('您确认将其从购物车移除吗？', '确认移除', {type: "warning"}).then(response => {
        this.$request.delete('/Cart/delete/' + id).then(res => {
          if (res.code === '200') {//操作成功
            this.$message.success('移除成功')
            this.load(1)//刷新表格数据
          } else {
            this.$message.error(res.msg)
          }
        })
      }).catch(() => {
      })
    },

    load(pageNum) {//分页查询
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/Cart/page', {
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
  }
}
</script>

<style>

</style>