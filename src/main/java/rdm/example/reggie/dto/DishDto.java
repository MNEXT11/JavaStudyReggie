package rdm.example.reggie.dto;


import lombok.Data;
import rdm.example.reggie.entity.Dish;
import rdm.example.reggie.entity.DishFlavor;

import java.util.ArrayList;
import java.util.List;

/**
 * 封装页面提交的数据
 * 1.添加菜品提交的数据，无法直接封装到dish对象中就需要dto
 */
@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
