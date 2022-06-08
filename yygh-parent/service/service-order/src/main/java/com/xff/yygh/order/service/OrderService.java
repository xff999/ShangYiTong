package com.xff.yygh.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xff.yygh.model.order.OrderInfo;

public interface OrderService extends IService<OrderInfo> {
    //保存订单
    Long saveOrder(String scheduleId, Long patientId);
}
