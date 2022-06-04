package rdm.example.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import rdm.example.reggie.entity.ShoppingCart;

/**
 * @Description
 * @Author rdm
 * @data 2022/5/30 - 18:38
 */
@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {
}
