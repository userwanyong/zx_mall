# 臻享商城
## 简介
一个基于vue、springboot搭建的商城项目。具有`用户端`与`后台管理端`
## 功能
### 基本功能
1、登录注册

2、忘记密码、修改密码

3、修改个人信息

4、系统公告

5、系统日志

6、用户、商品、分类、公告、购物车、订单管理

7、上传商品

8、批量上传与下载

9、支付与退款

### 系统功能模块结构图
<img alt="img_11.png" src="introduce/img_11.png" width="700"/>

## 实现
1、后端使用了springboot+vue+mysql+mybatis-plus等技术

2、前端使用vue+element+axios等

3、使用jwt实现身份验证与数据的安全传输

4、文件上传下载采用本地的方式而非oss

5、使用支付宝沙箱支付+napapp内网穿透来模拟用户支付与退款

6、使用aop技术记录操作日志

7、详细编写历程请见csdn《臻享商城》专栏
https://blog.csdn.net/nener_give_up/category_12862693.html

## 功能展示+流程图
### 1、登录、注册
<img alt="img_4.png" src="introduce/img_4.png" width="700"/>
<img alt="img_6.png" src="introduce/img_6.png" width="700"/>
<img alt="img_5.png" src="introduce/img_5.png" width="700"/>

### 2、忘记密码、修改密码
<img alt="img_7.png" src="introduce/img_7.png" width="700"/>
<img alt="img.png" src="introduce/img_8.png" width="700"/>
<img alt="img_1.png" src="introduce/img_9.png" width="700"/>

### 3、修改个人信息
<img alt="img.png" src="introduce/img.png" width="700"/>
<img alt="img_1.png" src="introduce/img_1.png" width="700" height="600"/>

### 4、用户、商品、分类、公告、购物车、订单管理
<img alt="img_13.png" src="introduce/img_13.png" width="700"/>
<img alt="img_14.png" src="introduce/img_14.png" width="700"/>
<img alt="img_15.png" src="introduce/img_15.png" width="700"/>
<img alt="img_16.png" src="introduce/img_16.png" width="700"/>
<img alt="img_17.png" src="introduce/img_17.png" width="700"/>
<img alt="img_18.png" src="introduce/img_18.png" width="700"/>
<img alt="img_19.png" src="introduce/img_19.png" width="700"/>
<img alt="img_12.png" src="introduce/img_12.png" width="700"/>

### 5、用户端
<img alt="img_20.png" src="introduce/img_20.png" width="700"/>
<img alt="img_21.png" src="introduce/img_21.png" width="700"/>
<img alt="img_22.png" src="introduce/img_22.png" width="700"/>
<img alt="img_23.png" src="introduce/img_23.png" width="700"/>

### 6、支付与退款
<img alt="img_24.png" src="introduce/img_24.png" width="700"/>
<img alt="img_25.png" src="introduce/img_25.png" width="700"/>
<img alt="img_26.png" src="introduce/img_26.png" width="700"/>

### 7、文件批量导入、导出
导出

<img alt="img_27.png" src="introduce/img_27.png" width="700"/>
导入

<img alt="img_28.png" src="introduce/img_28.png" width="700"/>
<img alt="img_29.png" src="introduce/img_29.png" width="700"/>

### 8、系统日志
<img alt="img_30.png" src="introduce/img_30.png" width="700"/>

## 缺点与不足
1、存在部分bug

2、使用雪花id增强安全性

3、优化：加入redis缓存在查询商品后将数据放到redis中，减少数据库压力

4、优化：使用es搜索引擎实现对商品的搜索

5、优化：加入聊天功能、加入商品评价功能、加入百度地图定位功能、加入销售量统计功能

6、优化：使用xxl-job实现定时任务，定时清理未付款订单

7、优化：使用ant design设计前端页面

7、福利：可以用这个练习单体->微服务哦