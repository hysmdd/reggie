package cn.imqinhao.service.impl;

import cn.imqinhao.entity.User;
import cn.imqinhao.mapper.UserMapper;
import cn.imqinhao.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author qinhao
 * @version 1.0
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
