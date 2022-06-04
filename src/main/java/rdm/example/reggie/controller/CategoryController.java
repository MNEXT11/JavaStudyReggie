package rdm.example.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import rdm.example.reggie.common.R;
import rdm.example.reggie.entity.Category;
import rdm.example.reggie.service.CategoryService;

import java.util.List;

/**
 * @Description
 * @Author rdm
 * @data 2022/5/25 - 16:07
 */
@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 新增分类
     * @param category
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody Category category){
        categoryService.save(category);
        return R.success("添加成功");
    }

    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page , int pageSize){
        //分页构造器
        Page<Category> pageInfo = new Page<>(page, pageSize);
        //条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        //添加条件
        queryWrapper.orderByAsc(Category::getSort);
        //进行查询
        categoryService.page(pageInfo, queryWrapper);

        return R.success(pageInfo);
    }

    /**
     * 删除当前分类(根据id删除)
     * @param id
     * @return
     */
    @DeleteMapping
    public R<String> delete(Long id){
        //categoryService.removeById(id);
        categoryService.remove(id);
        return R.success("分类信息删除成功");
    }

    /**
     * 分类信息修改
     * @param category
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody Category category){
        log.info(category.toString());
        categoryService.updateById(category);
        return R.success("修改成功");
    }

    /**
     * 根据type条件查询
     * 添加菜品需要使用
     * 前端传过来的并不是整个对象，而是某个字段，就不需要加@requestbody
     */
    @GetMapping("/list")
    public R<List<Category>> getByType(Category category){
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(category.getType() != null , Category::getType, category.getType());
        //增加排序条件
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);

        List<Category> categories = categoryService.list(queryWrapper);

        return R.success(categories);
    }
}
