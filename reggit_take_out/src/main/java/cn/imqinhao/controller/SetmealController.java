package cn.imqinhao.controller;

import cn.imqinhao.common.R;
import cn.imqinhao.dto.DishDto;
import cn.imqinhao.dto.SetmealDto;
import cn.imqinhao.entity.Category;
import cn.imqinhao.entity.Dish;
import cn.imqinhao.entity.Setmeal;
import cn.imqinhao.entity.SetmealDish;
import cn.imqinhao.service.CategoryService;
import cn.imqinhao.service.DishService;
import cn.imqinhao.service.SetmealDishService;
import cn.imqinhao.service.SetmealService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author qinhao
 * @version 1.0
 */
@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;
    @Autowired
    private SetmealDishService setmealDishService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private DishService dishService;

    /**
     * 套餐分页查询
     * @param page  当前页
     * @param pageSize  每页显示的数量
     * @param name  条件查询（名称）
     * @return 分页对象
     */
    @GetMapping("/page")
    public R<IPage> page(Integer page, Integer pageSize, String name) {
        // 分页构造器对象
        IPage<Setmeal> pageInfo = new Page<>(page, pageSize);
        IPage<SetmealDto> setmealDtoIPage = new Page<>();
        // 条件构造器对象
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper();
        // 增加条件
        queryWrapper.like(name != null, Setmeal::getName, name);
        // 增加排序条件
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        // 执行分页查询
        setmealService.page(pageInfo, queryWrapper);
        // 拷贝对象
        BeanUtils.copyProperties(pageInfo, setmealDtoIPage, "records");
        List<Setmeal> records = pageInfo.getRecords();
        List<SetmealDto> list = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            // 对象拷贝
            BeanUtils.copyProperties(item, setmealDto);
            // 根据分类id查询分类对象
            Category category = categoryService.getById(item.getCategoryId());
            if (null != category) {
                setmealDto.setCategoryName(category.getName());
            }
            return setmealDto;
        }).collect(Collectors.toList());
        setmealDtoIPage.setRecords(list);
        return R.success(setmealDtoIPage);
    }

    /**
     * 新增套餐
     * @param setmealDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto) {
        log.info("套餐信息：{}", setmealDto);
        setmealService.saveWithDish(setmealDto);
        return R.success("套餐添加成功");
    }

    /**
     * 起售、禁售切换
     * @param isOpen
     * @param ids
     * @return
     */
    @PostMapping("/status/{isOpen}")
    public R<String> updateStatus(@PathVariable Integer isOpen, @RequestParam Long[] ids) {
        log.info("状态码：{}", isOpen);
        log.info("数组：{}", ids);
        setmealService.updateStatus(isOpen, ids);
        return R.success("修改成功");
    }

    /**
     * 删除套餐及其菜品关联信息
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids) {
        log.info("ids: {}", ids);
        setmealService.removeWithDish(ids);
        return R.success("套餐删除成功");
    }

    /**
     * 根据id获取套餐信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<SetmealDto> get(@PathVariable Long id) {
        System.out.println(id);
        SetmealDto setmealDto = setmealService.getWithCategoryName(id);
        return R.success(setmealDto);
    }

    /**
     * 修改套餐信息
     * @param setmealDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody SetmealDto setmealDto) {
        System.out.println(setmealDto);
        setmealService.updateWithDish(setmealDto);
        return R.success("修改套餐成功");
    }

    /**
     * 根据分类信息找
     * @param setmeal
     * @return
     */
    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal) {
        List list = setmealService.listByCategoryId(setmeal);
        return R.success(list);
    }

    /**
     * 获取单个套餐详细菜品
     * @param setmealId
     * @return
     */
    @GetMapping("/dish/{setmealId}")
    public R<List<DishDto>> getByDishId(@PathVariable Long setmealId) {
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(setmealId != null, SetmealDish::getSetmealId, setmealId);
        List<SetmealDish> list = setmealDishService.list(lambdaQueryWrapper);
        List<DishDto> dishList = list.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            Long dishId = item.getDishId();
            Dish dish = dishService.getById(dishId);
            BeanUtils.copyProperties(dish, dishDto);
            dishDto.setCopies(item.getCopies());
            return dishDto;
        }).collect(Collectors.toList());
        return R.success(dishList);
    }

}
