<template>
  <div style="display: flex">
    <el-card style="width: 80%">
      <div style="margin-bottom: 15px;font-size: 20px;font-weight: bold">系统公告</div>
      <el-timeline style="padding: 0">
        <el-timeline-item v-for="item in notice" :key="item.id" :timestamp="item.ctime" placement="top">
          <el-card>
            <h4>{{ item.title }}</h4>
            <p>{{ item.content }}</p>
          </el-card>
        </el-timeline-item>
      </el-timeline>
    </el-card>
  </div>
</template>
<script>
export default {
  name: "Home",
  data() {

    return {
      user: JSON.parse(localStorage.getItem('honey-user') || '{}'),
      notice: []
    }
  },
  created() {
    this.loadNotice()
  },
  methods: {
    loadNotice() {
      this.$request.get('/notice/selectUserData').then(res => {
        this.notice = res.data
      })
    }
  }
}


</script>


<style scoped>
.main-content {
  height: 100%;
  background-size: 100%;

}

.left {
  width: 30%;
  background-repeat: no-repeat;
}

.right {
  width: 30%;
  background-repeat: no-repeat;
}

.el-carousel__item h3 {
  color: #475669;
  font-size: 14px;
  opacity: 0.75;
  line-height: 150px;
  margin: 0;
}


</style>