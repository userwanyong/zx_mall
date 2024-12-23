<template>
  <div>
    <div>
      <el-select v-model="operation" style="margin: 0 5px">
        <el-option v-for="item in ['用户','文件','公告','分类','商品']" :key="item" :label="item"
                   :value="item"></el-option>
      </el-select>
      <el-select v-model="type" style="margin: 0 5px">
        <el-option v-for="item in ['新增','修改','删除','批量删除']" :key="item" :label="item"
                   :value="item"></el-option>
      </el-select>
      <el-input v-model="optUser" placeholder="操作人" style="width: 200px;margin: 0 10px"></el-input>
      <el-button icon="el-icon-search" type="primary" @click="load(1)">查询</el-button>
      <el-button icon="el-icon-refresh" type="primary" @click="reset">重置</el-button>

      <el-button plain type="danger" @click="delBatch">批量删除</el-button>
    </div>

    <div style="margin: 10px 0"></div>

    <el-table :data="tableData" :header-cell-style="{backgroundColor: 'aliceblue',color: '#666'}" stripe
              @selection-change="handleSelectionChange">>
      <el-table-column align="center" type="selection" width="55"></el-table-column>
      <el-table-column label="操作模块" prop="operation"></el-table-column>
      <el-table-column label="操作类型" prop="type">
        <template v-slot="scope">
          <el-tag v-if="scope.row.type==='新增'" type="primary">{{ scope.row.type }}</el-tag>
          <el-tag v-if="scope.row.type==='修改'" type="info">{{ scope.row.type }}</el-tag>
          <el-tag v-if="scope.row.type==='删除'" type="danger">{{ scope.row.type }}</el-tag>
          <el-tag v-if="scope.row.type==='批量删除'" type="danger">{{ scope.row.type }}</el-tag>
          <el-tag v-if="scope.row.type==='登录'" type="success">{{ scope.row.type }}</el-tag>
          <el-tag v-if="scope.row.type==='注册'" type="warning">{{ scope.row.type }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作人IP" prop="ip"></el-table-column>
      <el-table-column label="操作人" prop="user"></el-table-column>
      <el-table-column label="操作时间" prop="time"></el-table-column>
      <el-table-column label="操作详情" prop="methodName" width="100">
        <template v-slot="scope">
          <el-button @click="preview(scope.row.methodName)">显示详情</el-button>
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作" width="180">
        <template v-slot="scope">
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

    <el-dialog :close-on-modal="false" :visible.sync="fromVisible2" destroy-on-close title="详情" width="40%">
      <div v-html="methodName"></div>
    </el-dialog>

  </div>
</template>


<script>
export default {
  name: "Logs",
  data() {
    return {
      tableData: [],  //所有的数据
      pageNum: 1,  //当前的页码
      pageSize: 15,  // 每页现实的个数
      operation: '',
      total: 0,
      form: {},
      user: JSON.parse(localStorage.getItem('honey-user') || '{}'),
      ids: [],
      type: '',
      optUser: '',
      fromVisible2: false,
      methodName: ''
    }
  },
  created() {
    this.load()
  },
  methods: {
    preview(methodName) {
      this.methodName = methodName
      this.fromVisible2 = true
    },
    delBatch() {
      if (!this.ids.length) {
        this.$message.warning('请选择数据')
        return
      }
      this.$confirm('您确认批量删除吗？', '确认删除', {type: "warning"}).then(response => {
        this.$request.delete('/logs/delete/batch', {data: this.ids}).then(res => {
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
        this.$request.delete('/logs/delete/' + id).then(res => {
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

    reset() {//重置
      this.operation = ''
      this.type = ''
      this.optUser = ''
      this.load()
    },
    load(pageNum) {//分页查询
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/logs/selectByPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          operation: this.operation,
          type: this.type,
          user: this.optUser
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
