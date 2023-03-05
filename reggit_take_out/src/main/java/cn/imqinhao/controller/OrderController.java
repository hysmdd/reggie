package cn.imqinhao.controller;

import cn.imqinhao.common.BaseContext;
import cn.imqinhao.common.R;
import cn.imqinhao.dto.OrdersDto;
import cn.imqinhao.entity.OrderDetail;
import cn.imqinhao.entity.Orders;
import cn.imqinhao.entity.ShoppingCart;
import cn.imqinhao.service.OrderDetailService;
import cn.imqinhao.service.OrderService;
import cn.imqinhao.service.ShoppingCartService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author qinhao
 * @version 1.0
 */
@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 提交订单
     *
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders) {
        System.out.println(orders);
        orderService.submit(orders);
        return R.success("订单提交成功");
    }

    /**
     * 获取订单信息
     *
     * @param page     当前页
     * @param pageSize 每页显示的数量
     * @return
     */
    @GetMapping("/userPage")
    public R<IPage<OrdersDto>> getOrders(Integer page, Integer pageSize) {
        IPage<Orders> iPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Orders> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Orders::getUserId, BaseContext.getCurrentId());
        lambdaQueryWrapper.orderByDesc(Orders::getCheckoutTime);
        IPage<Orders> ordersIPage = orderService.page(iPage, lambdaQueryWrapper);

        IPage<OrdersDto> ordersDtoIPage = new Page<>();
        // 对象拷贝
        BeanUtils.copyProperties(iPage, ordersIPage, "records");

        List<Orders> orders = ordersIPage.getRecords();
        List<OrdersDto> ordersDtoList = orders.stream().map((item) -> {
            OrdersDto ordersDto = new OrdersDto();
            BeanUtils.copyProperties(item, ordersDto);
            LambdaQueryWrapper<OrderDetail> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(OrderDetail::getOrderId, item.getNumber());
            List<OrderDetail> details = orderDetailService.list(queryWrapper);
            ordersDto.setOrderDetails(details);
            return ordersDto;
        }).collect(Collectors.toList());
        ordersDtoIPage.setRecords(ordersDtoList);
        return R.success(ordersDtoIPage);
    }

    /**
     * 分页查看订单信息
     *
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<IPage> page(Integer page, Integer pageSize, Long number) {
        IPage<Orders> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Orders> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(number != null, Orders::getNumber, number);
        lambdaQueryWrapper.orderByDesc(Orders::getCheckoutTime);
        orderService.page(pageInfo, lambdaQueryWrapper);
        return R.success(pageInfo);
    }

    /**
     * 更新订单状态
     *
     * @param orders
     * @return
     */
    @PutMapping
    public R<Orders> updateStatus(@RequestBody Orders orders) {
        System.out.println(orders);
        orderService.updateById(orders);
        return R.success(orders);
    }

    /**
     * 再来一单
     *
     * @return
     */
    @PostMapping("/again")
    public R<String> again(@RequestBody Orders orders) {
        Orders order = orderService.getById(orders.getId());
        String number = order.getNumber();  // 获取订单号
        LambdaQueryWrapper<OrderDetail> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(number != null, OrderDetail::getOrderId, number);
        List<OrderDetail> orderDetailList = orderDetailService.list(lambdaQueryWrapper);
        List<ShoppingCart> shoppingCarts = orderDetailList.stream().map((item) -> {
            ShoppingCart shoppingCart = new ShoppingCart();
            BeanUtils.copyProperties(item, shoppingCart);
            shoppingCart.setUserId(BaseContext.getCurrentId());
            shoppingCart.setCreateTime(LocalDateTime.now());
            return shoppingCart;

        }).collect(Collectors.toList());
        shoppingCartService.saveBatch(shoppingCarts);
        return R.success("已将商品放入购物车");
    }
}
