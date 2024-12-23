<template>
  <div style="margin: 10px 0;padding: 5px 200px">
    <el-row :gutter="10">
      <el-col v-for="item in tableData" :key="item.id" :span="6" style="margin-bottom: 10px">
        <div style="border: 1px solid #ccc;padding-bottom: 10px;border-radius: 10px;overflow: hidden"
             @click="$router.push('/front/detail?id='+item.id)">
          <img :src="item.img" alt="" style="width: 100%;cursor: pointer">
          <div :title="item.name" class="bz" style="color:#666;padding: 5px;font-size: 18px;height: 55px">
            <b>{{ item.name }}</b></div>
          <div :title="item.description" class="bz" style="color:#666;padding: 5px;font-size: 14px;height: 43px">
            {{ item.description }}
          </div>
          <div class="bz" style="color:orangered;padding: 5px;font-size: 14px">¥ {{ item.price }}</div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>
<script>

export default {
  name: 'HomeView',
  data() {
    return {
      user: JSON.parse(localStorage.getItem('honey-user') || '{}'),
      tableData: [],  //所有的数据
      pageNum: 1,  //当前的页码
      pageSize: 15,  // 每页现实的个数
      name: '',
      total: 0
    }
  },
  created() {
    this.load()//获取后台商品数据
  },
  mounted() {//页面加载完后触发
  },
  methods: {
    load() {//分页查询
      this.$request.get('/goods/selectByPageUser', {
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
    logout() {
      localStorage.removeItem('honey-user')//清除当前的token和用户数据
      this.$router.push('/login')
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum
      this.load()
    },
  },
}
</script>

<style>
.bz {
  cursor: pointer;
  overflow: hidden;
  text-overflow: ellipsis;
//white-space:nowrap; display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}
</style>