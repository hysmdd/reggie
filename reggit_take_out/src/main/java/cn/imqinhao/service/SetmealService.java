package cn.imqinhao.service;

import cn.imqinhao.common.R;
import cn.imqinhao.dto.SetmealDto;
import cn.imqinhao.entity.Setmeal;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author qinhao
 * @version 1.0
 */
public interface SetmealService extends IService<Setmeal> {

    /**
     * 新增套餐，同时需要保存菜品与套餐的关联关系
     * @param setmealDto
     */
    public void saveWithDish(SetmealDto setmealDto);

    /**
     * 删除套餐和套餐对应菜品的关联关系
     * @param ids
     */
    public void removeWithDish(List<Long> ids);

    /**
     * 禁售、起售切换
     * @param isOpen 当前状态
     * @param ids 要切换的id
     */
    public void updateStatus(Integer isOpen, Long[] ids);

    /**
     * 查询套餐及其分类信息
     * @param id 套餐id
     */
    public SetmealDto getWithCategoryName(Long id);

    /**
     * 修改套餐信息（并修改对应菜品信息）
     */
    public void updateWithDish(SetmealDto setmealDto);

    /**
     * 根据类型查找对应菜品信息
     * @param setmeal
     * @return
     */
    List<Setmeal> listByCategoryId(Setmeal setmeal);
}
