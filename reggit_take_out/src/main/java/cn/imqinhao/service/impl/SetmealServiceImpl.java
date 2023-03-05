package cn.imqinhao.service.impl;

import cn.imqinhao.common.CustomException;
import cn.imqinhao.common.R;
import cn.imqinhao.dto.SetmealDto;
import cn.imqinhao.entity.Category;
import cn.imqinhao.entity.Dish;
import cn.imqinhao.entity.Setmeal;
import cn.imqinhao.entity.SetmealDish;
import cn.imqinhao.mapper.SetmealMapper;
import cn.imqinhao.service.CategoryService;
import cn.imqinhao.service.DishService;
import cn.imqinhao.service.SetmealDishService;
import cn.imqinhao.service.SetmealService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author qinhao
 * @version 1.0
 */
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;

    /**
     * 新增套餐，同时需要保存菜品与套餐的关联关系
     *
     * @param setmealDto
     */
    @Override
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        // 保存套餐基本信息
        this.save(setmealDto);
        // 保存套餐和菜品的关联信息
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes = setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishes);
    }

    /**
     * 删除套餐和套餐对应菜品的关联关系
     *
     * @param ids
     */
    @Override
    @Transactional
    public void removeWithDish(List<Long> ids) {
        // 检查所选套餐是否禁售
        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(Setmeal::getId, ids);
        lambdaQueryWrapper.eq(Setmeal::getStatus, 1);
        int count = this.count(lambdaQueryWrapper);
        // 如果查询出来，说明所选套餐有未禁售的套餐
        if (count > 0) {
            // 抛出异常，不能删除
            throw new CustomException("套餐正在售卖中，不能删除");
        }
        // 删除套餐中的菜品关系
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SetmealDish::getDishId, ids);
        setmealDishService.removeByIds(ids);
        // 删除套餐
        this.removeByIds(ids);
    }

    /**
     * 禁售、起售切换
     *
     * @param isOpen 当前状态
     * @param ids    要切换的id
     */
    @Override
    public void updateStatus(Integer isOpen, Long[] ids) {
        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(Setmeal::getId, ids);
        List<Setmeal> setmealList = this.list(lambdaQueryWrapper);
        setmealList = setmealList.stream().map((item) -> {
            item.setStatus(isOpen);
            return item;
        }).collect(Collectors.toList());
        this.updateBatchById(setmealList);
    }

    /**
     * 查询套餐及其分类信息
     *
     * @param id 套餐id
     */
    @Override
    public SetmealDto getWithCategoryName(Long id) {
        Setmeal setmeal = this.getById(id);
        SetmealDto setmealDto = new SetmealDto();
        // 拷贝对象
        BeanUtils.copyProperties(setmeal, setmealDto);
        if (setmeal != null) {
            Long categoryId = setmeal.getCategoryId();
            Category category = categoryService.getById(categoryId);
            setmealDto.setCategoryName(category.getName());
            LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SetmealDish::getSetmealId, setmeal.getId());
            List<SetmealDish> setmealDishList = setmealDishService.list(queryWrapper);
            setmealDto.setSetmealDishes(setmealDishList);
        } else {
            throw new CustomException("套餐信息不存在");
        }
        return setmealDto;
    }

    /**
     * 修改套餐信息（并修改对应菜品信息）
     */
    @Override
    @Transactional
    public void updateWithDish(SetmealDto setmealDto) {
        // 存储套餐基本信息
        this.updateById(setmealDto);
        // 将此套餐对应的菜品全部删除
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(SetmealDish::getSetmealId, setmealDto.getId());
        setmealDishService.remove(lambdaQueryWrapper);
        // 获取菜品信息
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes = setmealDishes.stream().map((item) -> {
            // 将套餐菜品信息加上套餐id
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        // 保存新的套餐菜品信息
        setmealDishService.saveBatch(setmealDishes);
    }

    /**
     * 根据类型查找对应菜品信息
     *
     * @param setmeal
     * @return
     */
    @Override
    public List<Setmeal> listByCategoryId(Setmeal setmeal) {
        Category category = categoryService.getById(setmeal.getCategoryId());
        // 当前需要查询菜品列表
        // 需要查询套餐信息
        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(setmeal.getCategoryId() != null, Setmeal::getCategoryId, setmeal.getCategoryId());
        lambdaQueryWrapper.eq(setmeal.getStatus() != null, Setmeal::getStatus, setmeal.getStatus());
        lambdaQueryWrapper.orderByDesc(Setmeal::getUpdateTime);
        List<Setmeal> setmealList = setmealService.list(lambdaQueryWrapper);
        return setmealList;
    }
}
