package rdm.example.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import rdm.example.reggie.common.R;
import rdm.example.reggie.dto.DishDto;
import rdm.example.reggie.entity.Category;
import rdm.example.reggie.entity.Dish;
import rdm.example.reggie.entity.DishFlavor;
import rdm.example.reggie.service.CategoryService;
import rdm.example.reggie.service.DishFlavorService;
import rdm.example.reggie.service.DishService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description
 * @Author rdm
 * @data 2022/5/26 - 20:19
 */
@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private CategoryService categoryService;

    /**
     * 新增菜品
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());
        dishService.saveDishAndFlavors(dishDto);
        return R.success("新增菜品成功");
    }

    /**
     * 按名字分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page , int pageSize , String name){
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        //用于显示菜品分类名称
        //总体思路，创建dishdto的page，将pageinfo中的信息拷贝到dtopageinfo中
        Page<DishDto> dtoPage = new Page<>();

        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotEmpty(name), Dish::getName, name);
        queryWrapper.orderByDesc(Dish::getUpdateTime);
        //此时只获得了菜品分类的id，没有他的名称，而页面展示需要展示菜品分类名称,利用dishDto,里头有categoryName属性，进行拷贝
        dishService.page(pageInfo, queryWrapper);

        //不拷贝records
        BeanUtils.copyProperties(pageInfo, dtoPage, "records");
        List<Dish> records = pageInfo.getRecords();
        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            String categoryName = category.getName();
            dishDto.setCategoryName(categoryName);
            return dishDto;
        }).collect(Collectors.toList());
        //处理后的records赋值给dtopage
        dtoPage.setRecords(list);

        return R.success(dtoPage);
    }

    /**
     * 根据id查找菜品，用于回显
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> getById(@PathVariable Long id){
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }

    /**
     * 修改菜品
     * @param dishDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        dishService.updateDishAndFlavors(dishDto);
        return R.success("修改成功");
    }

    /**
     * 根据id删除菜品
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> deleteByIds(@RequestParam List<Long> ids){
        log.info(ids.toString());
        dishService.deleteByIdsAndFlavors(ids);
        return R.success("删除成功");
    }

    /**
     * 菜品起售和停售功能
     * @param status
     * @param ids
     * @return
     */
    @PostMapping("/status/{status}")
    public R<String> updateStatus(@PathVariable Integer status , @RequestParam List<Long> ids){
        LambdaUpdateWrapper<Dish> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(Dish::getStatus, status).in(Dish::getId,ids);
        dishService.update(updateWrapper);
        return R.success("修改成功");
    }

    /**
     * 根据条件查询菜品数据（经过改造）
     * @param dish
     * @return
     */
//    @GetMapping("/list")
//    public R<List<Dish>> list(Dish dish){
//        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
//        queryWrapper.eq(Dish::getStatus, 1);
//        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
//        List<Dish> list = dishService.list(queryWrapper);
//        return R.success(list);
//    }
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish){
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
        queryWrapper.eq(Dish::getStatus, 1);
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> list = dishService.list(queryWrapper);
        List<DishDto> dtoList = list.stream().map((item)->{
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            LambdaQueryWrapper<DishFlavor> flavorQueryWrapper = new LambdaQueryWrapper<>();
            flavorQueryWrapper.eq(DishFlavor::getDishId, item.getId());
            List<DishFlavor> flavorList = dishFlavorService.list(flavorQueryWrapper);
            dishDto.setFlavors(flavorList);
            return dishDto;
        }).collect(Collectors.toList());
        return R.success(dtoList);
    }
}
