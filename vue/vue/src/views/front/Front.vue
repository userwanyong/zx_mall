<template>
  <div>
    <el-container>
      <!--        头部区域-->
      <el-header>
        <div>
          <el-menu :default-active="$route.path" class="el-menu-demo" mode="horizontal" router>
            <el-menu-item index="/front/home">商城首页</el-menu-item>
            <el-menu-item index="/front/cart">我的购物车</el-menu-item>
            <el-menu-item index="/front/orders">我的订单</el-menu-item>
            <el-menu-item index="/front/notice">系统公告</el-menu-item>
          </el-menu>
        </div>
        <div style="flex: 1;width: 0;display: flex;align-items: center;justify-content: flex-end">
          <el-dropdown placement="bottom">
            <div style="display: flex;align-items: center; cursor: default">
              <img :src="user.avatar || 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'" alt=""
                   style="width: 40px;height: 40px;border-radius: 50%;margin: 0 7px">
              <span>{{ user.username }}</span>
            </div>
            <el-dropdown-menu>
              <el-dropdown-item @click.native="$router.push('/front/person')">个人信息</el-dropdown-item>
              <el-dropdown-item @click.native="$router.push('/front/password')">修改密码</el-dropdown-item>
              <el-dropdown-item @click.native="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </el-header>
      <el-main>
        <router-view @update:user="updateUser"/>
      </el-main>
    </el-container>
  </div>
</template>

<script>

export default {
  name: 'HomeView',
  data() {
    return {
      user: JSON.parse(localStorage.getItem('honey-user') || '{}'),
    }
  },
  mounted() {//页面加载完后触发
    if (location.href.indexOf("#reloaded") === -1) {
      location.href = location.href + "#reloaded";
      location.reload();
    }
  },
  methods: {
    updateUser(user) {  //获取子组件传过来的数据  更新当前页面的数据
      this.user = JSON.parse(JSON.stringify(user))  //让父级的对象根子集的对象毫无关系
    },
    logout() {
      localStorage.removeItem('honey-user')//清除当前的token和用户数据
      this.$router.push('/login')
    },
  },


}
</script>

<style>
.el-header {
  box-shadow: 2px 0 1px rgba(0, 21, 41, .35);
  display: flex;
  align-items: center;
}

</style>
