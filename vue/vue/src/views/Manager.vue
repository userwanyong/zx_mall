<template>
  <div>
    <el-container>
      <!--      侧边栏-->
      <el-aside :width="asideWidth" style="min-height: 100vh;background-color: #001529">
        <div style="height: 60px;color: white;display: flex; align-items: center;justify-content: center">
          <img alt="" src="../assets/主页.jpg" style="width: 40px;height: 40px">
          <transition name="el-fade-in-linear">
            <span v-show="!isCollapse" style="margin-left: 5px;font-size: 20px">臻享商城</span>
          </transition>
        </div>

        <!--        :default-openeds="['info','info2']"此属性用来控制折叠栏默认状态是展开还是收起-->
        <el-menu :collapse="isCollapse" :collapse-transition="false" :default-active="$route.path" active-text-color="#fff"
                 background-color="#001529" router style="border: none"
                 text-color="rgba(255,255,255,0.65)">
          <el-menu-item index="/home">
            <i class="el-icon-s-home"></i>
            <span slot="title">系统首页</span>
          </el-menu-item>
          <el-menu-item index="/logs">
            <i class="el-icon-edit"></i>
            <span slot="title">系统日志</span>
          </el-menu-item>
          <el-submenu v-if="user.role === '管理员'" index="info">
            <template slot="title">
              <i class="el-icon-menu"></i>
              <span>信息管理(管理员)</span>
            </template>
            <el-menu-item index="/user">用户管理</el-menu-item>
            <el-menu-item index="/shop">商品管理</el-menu-item>
            <el-menu-item index="/category">分类管理</el-menu-item>
            <el-menu-item index="/notice">公告管理</el-menu-item>
            <el-menu-item index="/cart">购物车管理</el-menu-item>
            <el-menu-item index="/orders">订单管理</el-menu-item>
          </el-submenu>

          <el-submenu index="info3">
            <template slot="title">
              <i class="el-icon-menu"></i>
              <span>信息管理(商家)</span>
            </template>
            <el-menu-item index="/goods">上传商品</el-menu-item>
          </el-submenu>

          <el-submenu index="info2">
            <template slot="title">
              <i class="el-icon-user-solid"></i>
              <span>个人中心</span>
            </template>
            <el-menu-item index="/person">个人信息</el-menu-item>
            <el-menu-item index="/password">修改密码</el-menu-item>
          </el-submenu>

        </el-menu>
      </el-aside>
      <el-container>
        <!--        头部区域-->
        <el-header>

          <i :class="collapseIcon" style="font-size: 46px" @click="handleCollapse"></i>
          <el-breadcrumb separator-class="el-icon-arrow-right" style="margin-left: 20px">
            <el-breadcrumb-item :to="{ path: '/' }">主页</el-breadcrumb-item>
            <el-breadcrumb-item :to="{ path: $router.path }">{{ $route.meta.name }}</el-breadcrumb-item>
          </el-breadcrumb>


          <div style="flex: 1;width: 0;display: flex;align-items: center;justify-content: flex-end">
            <i class="el-icon-quanping" style="font-size: 35px" @click="handleFull"></i>
            <el-dropdown placement="bottom">
              <div style="display: flex;align-items: center; cursor: default">
                <img :src="user.avatar || 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'" alt=""
                     style="width: 40px;height: 40px;border-radius: 50%;margin: 0 7px">
                <span>{{ user.username }}</span>
              </div>
              <el-dropdown-menu>
                <el-dropdown-item @click.native="$router.push('/person')">个人信息</el-dropdown-item>
                <el-dropdown-item @click.native="$router.push('/password')">修改密码</el-dropdown-item>
                <el-dropdown-item @click.native="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </div>


        </el-header>
        <!--        主体区域-->
        <el-main>
          <router-view @update:user="updateUser"/>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>


export default {
  name: 'HomeView',
  data() {
    return {
      isCollapse: false,
      asideWidth: '200px',
      collapseIcon: 'el-icon-s-fold',
      user: JSON.parse(localStorage.getItem('honey-user') || '{}'),
    }
  },
  mounted() {//页面加载完后触发

  },
  methods: {
    updateUser(user) {  //获取子组件传过来的数据  更新当前页面的数据
      this.user = JSON.parse(JSON.stringify(user))  //让父级的对象根子集的对象毫无关系
    },
    logout() {
      localStorage.removeItem('honey-user')//清除当前的token和用户数据
      this.$router.push('/login')
    },
    handleFull() {
      document.documentElement.requestFullscreen()
    },
    handleCollapse() {
      this.isCollapse = !this.isCollapse
      this.asideWidth = this.isCollapse ? '64px' : '200px'
      this.collapseIcon = this.isCollapse ? 'el-icon-s-unfold' : 'el-icon-s-fold'
    },
  }
}
</script>

<style>
.el-menu--inline {
  background-color: #000c17 !important;
}

.el-menu--inline .el-menu-item {
  background-color: #000c17 !important;
  padding-left: 49px !important;
}

.el-menu-item:hover, .el-submenu__title:hover {
  color: #fff !important;
}

.el-submenu__title:hover i {
  color: #fff !important;
}

.el-menu-item.is-active {
  background-color: #40a9ff !important;
  border-radius: 6px !important;
  width: calc(100% - 2px);

}

.el-menu-item.is-active i, .el-menu-item.is-active .el-tooltip {
  margin-left: -4px;
}

.el-menu-item {
  height: 40px !important;
  line-height: 40px !important;
}

.el-submenu__title {
  height: 40px !important;
  line-height: 40px !important;
}

.el-submenu .el-menu-item {
  min-width: 0 !important;
}

.el-menu-inline .el-menu-item.is-active {
  padding-left: 45px !important;
}

.el-aside {
  transition: width .3s;
  box-shadow: 2px 0 6px rgba(0, 21, 41, .35);
}

.el-header {
  box-shadow: 2px 0 6px rgba(0, 21, 41, .35);
  display: flex;
  align-items: center;
}
</style>
