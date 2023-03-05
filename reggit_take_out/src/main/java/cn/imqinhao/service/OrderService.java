package cn.imqinhao.service;

import cn.imqinhao.entity.Orders;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author qinhao
 * @version 1.0
 */
public interface OrderService extends IService<Orders> {
    /**
     * 提交订单
     * @param orders
     */
    public void submit(Orders orders);
}
