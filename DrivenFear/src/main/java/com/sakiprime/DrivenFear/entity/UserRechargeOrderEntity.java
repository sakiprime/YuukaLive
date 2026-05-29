package com.sakiprime.DrivenFear.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRechargeOrderEntity {
    private Long orderId;//订单ID
    private Long id;//商品ID
    private String userId;//用户ID
    private boolean isPaid;
    //以下两个成员变量不来自前端，并会在执行中被商品ID对应的正确数据覆写。
    private BigDecimal paymentAmount;//付款金额（元）
    private Integer tokensAmount;//获得的token数

    private String createTime;
    private String updateTime;
    private String paymentTime;
    private String tradeStatus;
    private String returnUrl;
}
