<template>
  <div style="padding: 5px 200px">
    <el-card>
      <div style="display: flex">
        <div style="width: 300px">
          <img :src="goods.img" alt="" style="width: 350px;height: 350px">
        </div>
        <div style="flex: 1;padding-left: 100px">
          <div class="item1"><h3>{{ goods.name }}</h3></div>
          <div class="item1" style="font-size: 13px">{{ goods.description }}</div>
          <div class="item1" style="color: orangered;font-size: 18px">¥ {{ goods.price }}</div>
          <div class="item1" style="font-size: 13px">库存{{ goods.store }}<span style="margin-left: 10px">{{
              goods.unit
            }}</span></div>
          <div class="item1">
            <el-input-number v-model="form.num" :max="100" :min="1" label="描述文字"></el-input-number>
            <span style="margin-left: 10px">{{ goods.unit }}</span>
          </div>
          <div class="item1" style="margin-top: 10px">
            <el-button icon="el-icon-shopping-bag-1" size="medium" style="background-color: darksalmon;color: white" @click="addCart">
              购买
            </el-button>
            <el-button icon="el-icon-shopping-cart-2" size="medium" style="background-color: darksalmon;color: white"
                       @click="addCart">加入购物车
            </el-button>
          </div>
        </div>
      </div>
    </el-card>
  </div>
</template>
<script>

export default {
  name: "Detail",
  data() {
    return {
      id: this.$route.query.id,
      goods: {},
      form: {num: 1},
      user: JSON.parse(localStorage.getItem('honey-user') || '{}'),
    }
  },
  created() {
    this.load()
  },
  methods: {
    addCart() {
      if (!this.user.username) {
        this.$message.warning("请登录")
        this.$router.push('/login')
        return
      }
      this.form.goodsId = this.goods.id
      this.$request.post('/Cart/add', this.form).then(res => {
        if (res.code === "200") {
          this.$message.success("加入购物车成功")
          this.$router.push('/front/cart')
        } else {
          this.$message.error(res.msg)
        }
      })
    },

    load() {
      this.$request.get('/goods/selectById/' + this.id).then(res => {
        this.goods = res.data
      })
    }
  }
}
</script>

<style scoped>
.item1 {
  padding: 10px;
  color: #666;
}
</style>