package rdm.example.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rdm.example.reggie.dto.DishDto;
import rdm.example.reggie.entity.Dish;
import rdm.example.reggie.entity.DishFlavor;
import rdm.example.reggie.mapper.DishMapper;
import rdm.example.reggie.service.DishFlavorService;
import rdm.example.reggie.service.DishService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description
 * @Author rdm
 * @data 2022/5/25 - 19:27
 */
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;
    /**
     * 新增菜品
     * @param dishDto
     */
    @Transactional
    public void saveDishAndFlavors(DishDto dishDto) {
        //保存菜品信息到菜品表
        this.save(dishDto);

        //保存菜品口味(注意dishId的保存)
        //获取菜品id
        Long dishId = dishDto.getId();
        List<DishFlavor> flavors = dishDto.getFlavors();

        //处理stream流的方式遍历赋值，封装成list
        flavors.stream().map((item)->{
           item.setDishId(dishId);
           return item;
        }).collect(Collectors.toList());

        //保存菜品口味
        dishFlavorService.saveBatch(flavors);

    }

    /**
     * 根据id查询菜品信息
     * @param id
     * @return
     */
    @Override
    public DishDto getByIdWithFlavor(Long id) {
        //查询菜品的基本信息从dish表查询
        Dish dish = this.getById(id);
        //拷贝信息到dishdto
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);

        //查询口味信息
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, id);
        List<DishFlavor> flavors = dishFlavorService.list(queryWrapper);

        //flavors赋值给dishdto
        dishDto.setFlavors(flavors);

        return dishDto;
    }

    /**
     * 修改菜品信息
     * @param dishDto
     */
    @Override
    @Transactional
    public void updateDishAndFlavors(DishDto dishDto) {
        //更新菜品信息
        this.updateById(dishDto);
        //更新口味信息(总体思路，删除原先的，在提交修改后的)
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dishDto.getId());
        dishFlavorService.remove(queryWrapper);

        List<DishFlavor> flavors = dishDto.getFlavors();
        List<DishFlavor> list = flavors.stream().map((item) -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(list);
    }

    /**
     * 根据id删除菜品,和他的口味信息
     * @param ids
     */
    @Override
    @Transactional
    public void deleteByIdsAndFlavors(List<Long> ids) {
        this.removeByIds(ids);
        LambdaQueryWrapper<DishFlavor> flavorQueryWrapper = new LambdaQueryWrapper<>();
        flavorQueryWrapper.in(DishFlavor::getDishId, ids);
        dishFlavorService.remove(flavorQueryWrapper);
    }

}
