package cn.imqinhao.service.impl;

import cn.imqinhao.entity.ShoppingCart;
import cn.imqinhao.mapper.ShoppingCartMapper;
import cn.imqinhao.service.ShoppingCartService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author qinhao
 * @version 1.0
 */
@Service
@Slf4j
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
