import axios from "axios";
import router from "@/router";

//import config from "vue/src/core/config";
//创建一个新的axios对象
const request = axios.create({
    baseURL: 'http://localhost:8080',//后端接口地址
    timeout: 30000//设置超时时间为30s
})

request.interceptors.request.use(config => {
    config.headers['Content-Type'] = 'application/json;charset=utf-8';//浏览器中的响应标头
    let user = JSON.parse(localStorage.getItem("honey-user") || '{}')
    config.headers['token'] = user.token//设置请求头

    return config;
}, error => {
    console.error('request error:' + error)//打印错误信息
    return Promise.reject(error)
});

request.interceptors.response.use(
    response => {
        let res = response.data;
        if (typeof res === 'string') {
            res = res ? JSON.parse(res) : res
        }
        if (res.code === '401') {
            router.push('/login')//跳转到登录界面
        }
        return res;
    },
    error => {
        console.error('response error:' + error)
        return Promise.reject(error)
    }
)
export default request