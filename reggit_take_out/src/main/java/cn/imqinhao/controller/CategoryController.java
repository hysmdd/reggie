package cn.imqinhao.controller;

import cn.imqinhao.common.R;
import cn.imqinhao.entity.Category;
import cn.imqinhao.service.CategoryService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分类管理
 * @author qinhao
 * @version 1.0
 */
@RequestMapping("/category")
@RestController
@Slf4j
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 新增分类
     */
    @PostMapping
    public R<String> save(@RequestBody Category category) {
        log.info("category: {}", category);
        categoryService.save(category);
        return R.success("新增分类成功");
    }

    /**
     * 分页展示
     * @param page 当前页
     * @param pageSize 每页显示的数量
     * @return IPage对象
     */
    @GetMapping("/page")
    public R<IPage> getPage(Integer page, Integer pageSize) {
        // 构造分页器
        IPage<Category> categoryIPage = new Page(page, pageSize);
        // 条件构造器
        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 添加排序条件，根据sort进行排序
        lambdaQueryWrapper.orderByAsc(Category::getSort);
        // 进行分页查询
        IPage<Category> iPage = categoryService.page(categoryIPage, lambdaQueryWrapper);
        return R.success(iPage);
    }

    /**
     * 根据id删除分类
     * @return 删除是否成功
     */
    @DeleteMapping
    public R<String> delete(Long id) {
        System.out.println(id);
        categoryService.remove(id);
        return R.success("分类信息删除成功");
    }

    /**
     * 根据id修改分类信息
     * @param category
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody Category category) {
        log.info("修改分类信息： {}", category);
        categoryService.updateById(category);
        return R.success("分类信息修改成功");
    }

    /**
     * 条件查询
     * @param category
     * @return
     */
    @GetMapping("/list")
    public R<List<Category>> list(Category category) {
        // 条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper();
        // 添加条件
        queryWrapper.eq(category.getType() != null, Category::getType, category.getType());
        // 排序，根据序号升序排列，序号相同则根据更新时间排序
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> categoryList = categoryService.list(queryWrapper);
        return R.success(categoryList);
    }
}
