package rdm.example.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import rdm.example.reggie.entity.Dish;

/**
 * @Description
 * @Author rdm
 * @data 2022/5/25 - 19:23
 */
@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
