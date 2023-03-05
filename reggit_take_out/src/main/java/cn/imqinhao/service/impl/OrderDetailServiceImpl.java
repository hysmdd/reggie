package cn.imqinhao.service.impl;

import cn.imqinhao.entity.OrderDetail;
import cn.imqinhao.mapper.OrderDetailMapper;
import cn.imqinhao.service.OrderDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author qinhao
 * @version 1.0
 */
@Service
@Slf4j
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
