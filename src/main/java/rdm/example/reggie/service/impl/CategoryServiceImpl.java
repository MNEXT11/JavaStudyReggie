package rdm.example.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rdm.example.reggie.common.CustomException;
import rdm.example.reggie.entity.Category;
import rdm.example.reggie.entity.Dish;
import rdm.example.reggie.entity.Setmeal;
import rdm.example.reggie.mapper.CategoryMapper;
import rdm.example.reggie.service.CategoryService;
import rdm.example.reggie.service.DishService;
import rdm.example.reggie.service.SetmealService;

/**
 * @Description
 * @Author rdm
 * @data 2022/5/25 - 16:05
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;
    /**
     * 根据id删除分类，删除前需要判断是否关联了菜品
     * @param id
     */
    @Override
    public void remove(Long id) {
        //查询当前分类是否关联了菜品，如果已经关联，抛出业务异常
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);
        int dishCount = dishService.count(dishLambdaQueryWrapper);
        if (dishCount > 0){
            throw new CustomException("当前分类项关联了菜品无法删除");
        }
        //查询当前分类是否关联了套餐，如果已经关联，抛出业务异常
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        int setmealCount = setmealService.count(setmealLambdaQueryWrapper);
        if (setmealCount > 0){
            throw new CustomException("当前分类项关联了套餐无法删除");
        }

        //正常删除分类
        super.removeById(id);
    }
}
