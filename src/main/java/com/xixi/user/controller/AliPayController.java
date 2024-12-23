package com.xixi.user.controller;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.xixi.user.common.AliPayConfig;
import com.xixi.user.common.Result;
import com.xixi.user.entity.AliPay;
import com.xixi.user.entity.Orders;
import com.xixi.user.service.OrdersService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


/**
 * @author 永
 */
@RestController
@RequestMapping("alipay")
@Transactional(rollbackFor = Exception.class)
public class AliPayController {

    private static final String GATEWAY_URL = "https://openapi-sandbox.dl.alipaydev.com/gateway.do";
    private static final String FORMAT = "JSON";
    private static final String CHARSET = "utf-8";
    private static final String SIGN_TYPE = "RSA2";
    @Resource
    AliPayConfig aliPayConfig;
    @Resource
    private OrdersService ordersService;

    @GetMapping("/pay") // 前端路径参数格式?subject=xxx&traceNo=xxx&totalAmount=xxx
    public void pay(AliPay aliPay, HttpServletResponse httpResponse) throws Exception {
        AlipayClient alipayClient = new DefaultAlipayClient(GATEWAY_URL, aliPayConfig.getAppId(),
                aliPayConfig.getAppPrivateKey(), FORMAT, CHARSET, aliPayConfig.getAlipayPublicKey(), SIGN_TYPE);
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setNotifyUrl(aliPayConfig.getNotifyUrl());
        request.setBizContent("{\"out_trade_no\":\"" + aliPay.getTraceNo() + "\","//订单号
                + "\"total_amount\":\"" + aliPay.getTotalAmount() + "\","//订单总金额
                + "\"subject\":\"" + aliPay.getSubject() + "\","//订单名称
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");//固定配置
        //执行请求，拿到响应的结果，返回给浏览器
        String form = "";
        try {
            // 调用SDK生成表单
            form = alipayClient.pageExecute(request).getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        httpResponse.setContentType("text/html;charset=" + CHARSET);
        // 直接将完整的表单html输出到页面
        httpResponse.getWriter().write(form);
        httpResponse.getWriter().flush();
        httpResponse.getWriter().close();
    }

    @PostMapping("/notify")  // 注意这里必须是POST接口
    public String payNotify(HttpServletRequest request) throws Exception {
        if (request.getParameter("trade_status").equals("TRADE_SUCCESS")) {
            System.out.println("=========支付宝异步回调========");

            Map<String, String> params = new HashMap<>();
            Map<String, String[]> requestParams = request.getParameterMap();
            for (String name : requestParams.keySet()) {
                params.put(name, request.getParameter(name));
            }
            String sign = params.get("sign");
            String content = AlipaySignature.getSignCheckContentV1(params);
            boolean checkSignature = AlipaySignature.rsa256CheckContent(content, sign, aliPayConfig.getAlipayPublicKey(), "UTF-8"); // 验证签名
            // 支付宝验签
            if (checkSignature) {
                // 验签通过
                System.out.println("交易名称: " + params.get("subject"));
                System.out.println("交易状态: " + params.get("trade_status"));
                System.out.println("支付宝交易凭证号: " + params.get("trade_no"));
                System.out.println("商户订单号: " + params.get("out_trade_no"));
                System.out.println("交易金额: " + params.get("total_amount"));
                System.out.println("买家在支付宝唯一id: " + params.get("buyer_id"));
                System.out.println("买家付款时间: " + params.get("gmt_payment"));
                System.out.println("买家付款金额: " + params.get("buyer_pay_amount"));

                String tradeNo = params.get("out_trade_no");//订单编号
                String gmtPayment = params.get("gmt_payment");//支付时间
                String alipayTradeNo = params.get("trade_no");//支付宝交易编号
                // 更新订单已支付的逻辑代码
                Orders orders = ordersService.selectByOrderNo(tradeNo);
                orders.setState("已支付");
                orders.setPayTime(gmtPayment);
                orders.setPayNo(alipayTradeNo);
                ordersService.updateById(orders);
            }
        }
        return "success";
    }

    @GetMapping("/return")
    public Result returnPay(AliPay aliPay) throws AlipayApiException {
        // 7天无理由退款
        String now = DateUtil.now();
        Orders orders = ordersService.selectByOrderNo(aliPay.getTraceNo());
        if (orders != null) {
            // hutool工具类，判断时间间隔
            long between = DateUtil.between(DateUtil.parseDateTime(orders.getPayTime()), DateUtil.parseDateTime(now), DateUnit.DAY);
            if (between > 7) {
                return Result.error("-1", "该订单已超过7天，不支持退款");
            }
        }
        // 1. 创建Client，通用SDK提供的Client，负责调用支付宝的API
        AlipayClient alipayClient = new DefaultAlipayClient(GATEWAY_URL,
                aliPayConfig.getAppId(), aliPayConfig.getAppPrivateKey(), FORMAT, CHARSET,
                aliPayConfig.getAlipayPublicKey(), SIGN_TYPE);
        // 2. 创建 Request，设置参数
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        JSONObject bizContent = new JSONObject();
        bizContent.set("trade_no", aliPay.getAlipayTraceNo());  // 支付宝回调的订单流水号
        bizContent.set("refund_amount", aliPay.getTotalAmount());  // 订单的总金额
        bizContent.set("out_request_no", aliPay.getTraceNo());   //  我的订单编号
        // 返回参数选项，按需传入
        //JSONArray queryOptions = new JSONArray();
        //queryOptions.add("refund_detail_item_list");
        //bizContent.put("query_options", queryOptions);
        request.setBizContent(bizContent.toString());
        // 3. 执行请求
        AlipayTradeRefundResponse response = alipayClient.execute(request);
        if (response.isSuccess()) {  // 退款成功，isSuccess 为true
            System.out.println("调用成功---退款");
            // 4. 更新数据库状态
            orders.setState("已退款");
            orders.setReturnTime(DateUtil.now());
            ordersService.updateById(orders);
            return Result.success();

        } else {   // 退款失败，isSuccess 为false
            System.out.println(response.getBody());
            return Result.error(response.getCode(), response.getBody());
        }
    }
}