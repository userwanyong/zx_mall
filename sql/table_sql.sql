create table cart
(
    id       int auto_increment comment '自增id'
        primary key,
    goods_id int          null comment '商品id',
    user_id  int          null comment '用户id',
    num      int          null comment '商品数量',
    time     varchar(255) null comment '添加时间',
    constraint goods_user_uni
        unique (user_id, goods_id)
)
    comment '购物车表';

create table category
(
    id            int auto_increment comment '自增id'
        primary key,
    category_name varchar(255)         null comment '分类名称',
    create_user   varchar(255)         null comment '创建者',
    time          datetime             null comment '创建时间',
    open          tinyint(1) default 1 null comment '是否启用该分类',
    constraint category_pk
        unique (category_name)
)
    comment '分类表';

create table goods
(
    id          int auto_increment comment '自增id'
        primary key,
    name        varchar(255)         null comment '商品名',
    price       decimal(10, 2)       null comment '价格',
    description varchar(255)         null comment '商品描述',
    unit        varchar(255)         null comment '单位',
    store       int                  null comment '库存',
    img         varchar(255)         null comment '图片',
    time        varchar(255)         null comment '上架时间',
    open        tinyint(1) default 1 null comment '是否对外出售'
)
    comment '商品表';

create table logs
(
    id          int auto_increment comment '自增id'
        primary key,
    operation   varchar(255) null comment '模块名',
    type        varchar(255) null comment '操作类型',
    ip          varchar(255) null comment '操作ip',
    user        varchar(255) null comment '操作用户',
    time        varchar(255) null comment '操作时间',
    method_name varchar(500) null comment '操作详情'
)
    comment '日志表';

create table notice
(
    id      int auto_increment comment '自增id'
        primary key,
    title   varchar(255)         null comment '公告标题',
    content varchar(500)         null comment '内容',
    userid  int                  null comment '创建者id',
    ctime   varchar(255)         null comment '创建时间',
    utime   varchar(255)         null comment '更新时间',
    open    tinyint(1) default 1 null comment '是否公示'
)
    comment '公告表';

create table orders
(
    id          int auto_increment comment '自增id'
        primary key,
    name        varchar(255)                  null comment '名称',
    no          varchar(255)                  null comment '订单编号',
    total_price decimal(10, 2)                null comment '总金额',
    state       varchar(255) default '待支付' null comment '状态',
    time        varchar(255)                  null comment '下单时间',
    pay_time    varchar(255)                  null comment '付款时间',
    user_id     int                           null comment '用户id',
    pay_no      varchar(100)                  null comment '支付宝单号',
    return_time varchar(255)                  null comment '退款时间'
)
    comment '订单表';

create table orders_goods
(
    id       int auto_increment comment '自增id'
        primary key,
    order_id int null comment '订单id',
    goods_id int null comment '商品id',
    num      int null comment '商品数量'
)
    comment '订单-商品关联表';

create table shop
(
    id          int auto_increment comment '自增id'
        primary key,
    name        varchar(255)   null comment '菜品名称',
    description varchar(255)   null comment '菜品描述',
    category    varchar(255)   null comment '菜品分类',
    img         varchar(255)   null comment '菜品图片',
    price       decimal(10, 2) null comment '价格',
    status      varchar(255)   null comment '审核状态',
    user_id     int            null comment '审核者id',
    sale_status varchar(255)   null comment '售卖状态',
    read_count  int            null comment '阅读量',
    time        varchar(255)   null comment '创建时间',
    constraint shop_pk
        unique (name)
)
    comment '审核表' row_format = DYNAMIC;

create table user
(
    id       int auto_increment comment '自增id'
        primary key,
    username varchar(255) null comment '用户名',
    password varchar(255) null comment '密码',
    name     varchar(255) null comment '姓名',
    gender   varchar(255) null comment '性别',
    phone    varchar(255) null comment '电话',
    email    varchar(255) null comment '邮箱',
    address  varchar(255) null comment '地址',
    avatar   varchar(255) null comment '头像',
    role     varchar(255) null comment '角色',
    constraint username
        unique (username)
)
    comment '用户表' collate = utf8mb4_unicode_ci;

