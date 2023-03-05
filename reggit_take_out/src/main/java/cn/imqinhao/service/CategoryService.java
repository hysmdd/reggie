package cn.imqinhao.service;

import cn.imqinhao.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author qinhao
 * @version 1.0
 */
public interface CategoryService extends IService<Category> {

    public void remove(Long id);
}
