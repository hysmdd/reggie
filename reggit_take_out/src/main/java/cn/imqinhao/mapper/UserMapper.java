package cn.imqinhao.mapper;

import cn.imqinhao.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author qinhao
 * @version 1.0
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
