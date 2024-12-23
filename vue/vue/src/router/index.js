import Vue from 'vue'
import VueRouter from 'vue-router'


// 解决导航栏或者底部导航tabBar中的vue-router在3.0版本以上频繁点击菜单报错的问题。
const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push(location) {
    return originalPush.call(this, location).catch(err => err)
}


Vue.use(VueRouter)

const routes = [
    {
        path: '/',
        name: 'Manager',
        component: () => import('../views/Manager.vue'),
        redirect: '/home',//重定向到主页
        children: [
            {path: '403', name: 'Auth', meta: {name: '无权限'}, component: () => import('../views/manager/Auth.vue')},
            {
                path: 'home',
                name: 'Home',
                meta: {name: '系统首页'},
                component: () => import('../views/manager/Home.vue')
            },
            {
                path: 'user',
                name: 'User',
                meta: {name: '用户信息'},
                component: () => import('../views/manager/User.vue')
            },
            {
                path: 'person',
                name: 'Person',
                meta: {name: '个人信息'},
                component: () => import('../views/manager/Person.vue')
            },
            {
                path: 'password',
                name: 'Password',
                meta: {name: '修改密码'},
                component: () => import('../views/manager/Password.vue')
            },
            {
                path: 'shop',
                name: 'Shop',
                meta: {name: '商品管理'},
                component: () => import('../views/manager/Shop.vue')
            },
            {
                path: 'category',
                name: 'Category',
                meta: {name: '分类管理'},
                component: () => import('../views/manager/Category.vue')
            },
            {
                path: 'notice',
                name: 'Notice',
                meta: {name: '公告管理'},
                component: () => import('../views/manager/Notice.vue')
            },
            {
                path: 'logs',
                name: 'Logs',
                meta: {name: '系统日志'},
                component: () => import('../views/manager/Logs.vue')
            },
            {
                path: 'goods',
                name: 'Goods',
                meta: {name: '上传商品'},
                component: () => import('../views/manager/Goods.vue')
            },
            {
                path: 'cart',
                name: 'Cart',
                meta: {name: '购物车管理'},
                component: () => import('../views/manager/Cart.vue')
            },
            {
                path: 'orders',
                name: 'Orders',
                meta: {name: '订单管理'},
                component: () => import('../views/manager/Orders.vue')
            },
        ]
    },
    {
        path: '/front',
        name: 'Front',
        component: () => import('../views/front/Front.vue'),
        redirect: '/front/home',//重定向到主页
        children: [
            {path: 'home', name: 'Home2', meta: {name: '平台首页'}, component: () => import('../views/front/Home.vue')},
            {
                path: 'detail',
                name: 'Detail',
                meta: {name: '商品详情'},
                component: () => import('../views/front/Detail.vue')
            },
            {path: 'cart', name: 'Cart2', meta: {name: '购物车'}, component: () => import('../views/front/Cart.vue')},
            {
                path: 'orders',
                name: 'Orders2',
                meta: {name: '订单管理'},
                component: () => import('../views/front/Orders.vue')
            },
            {
                path: 'notice',
                name: 'Notice2',
                meta: {name: '系统公告'},
                component: () => import('../views/front/Notice.vue')
            },
            {
                path: 'person',
                name: 'Person2',
                meta: {name: '个人信息'},
                component: () => import('../views/front/Person.vue')
            },
            {
                path: 'password',
                name: 'Password2',
                meta: {name: '修改密码'},
                component: () => import('../views/front/Password.vue')
            },
        ]
    },

    {path: '/login', name: 'Login', meta: {name: '登录'}, component: () => import('../views/Login.vue')},
    {path: '/register', name: 'Register', meta: {name: '注册'}, component: () => import('../views/Register.vue')},
    {path: '*', name: '404', meta: {name: '无法访问'}, component: () => import('../views/404.vue')},

]

const router = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
})

router.beforeEach((to, from, next) => {
    //to 是到达的路由信息
    //from是开源的路由信息
    //next是帮助我们跳转路由的函数
    let adminPath = ['/user']
    // let shangjiaPath= ['/uploadShop']
    let user = JSON.parse(localStorage.getItem('honey-user') || '{}')

    if (user.role !== '管理员' && adminPath.includes(to.path)) {
        //如果当前登录的用户不是管理员，到达管理员才能访问的路径时，就让用户到一个无权限的页面，不让他访问实际的页面
        next('/403')
    } else {
        next()
    }
    // if ((user.role !== '商家'||'管理员') && shangjiaPath.includes(to.path)){
    //   //shangjiaPath这个路径只能管理员或商家有权访问
    //   next('/403')
    // }else {
    //   next()
    // }
})
export default router
