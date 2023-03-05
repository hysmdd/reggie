package cn.imqinhao.service;

import cn.imqinhao.dto.DishDto;
import cn.imqinhao.entity.Dish;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author qinhao
 * @version 1.0
 */
public interface DishService extends IService<Dish> {

    /**
     * 保存菜品信息（包含口味信息）
     * @param dishDto
     */
    public void saveWithFlavor(DishDto dishDto);

    /**
     * 获取菜品信息（包含口味信息）
     * @param id
     * @return
     */
    public DishDto getByIdWithFlavor(Long id);

    /**
     * 修改菜品信息（包含口味信息）
     * @param dishDto
     */
    public void updateWithFlavor(DishDto dishDto);
}
