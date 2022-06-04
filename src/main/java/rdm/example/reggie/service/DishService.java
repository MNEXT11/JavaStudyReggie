package rdm.example.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import rdm.example.reggie.dto.DishDto;
import rdm.example.reggie.entity.Dish;

import java.util.List;

/**
 * @Description
 * @Author rdm
 * @data 2022/5/25 - 19:25
 */
public interface DishService extends IService<Dish> {
    //新增菜品(操作两张表)
    public void saveDishAndFlavors(DishDto dishDto);

    //根据id查询菜品信息
    public DishDto getByIdWithFlavor(Long id);

    //更新菜品
    public void updateDishAndFlavors(DishDto dishDto);

    //删除菜品和所属的口味
    public void deleteByIdsAndFlavors(List<Long> ids);
}
