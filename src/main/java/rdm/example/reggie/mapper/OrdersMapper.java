package rdm.example.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import rdm.example.reggie.entity.Orders;

/**
 * @Description
 * @Author rdm
 * @data 2022/5/30 - 20:16
 */
@Mapper
public interface OrdersMapper extends BaseMapper<Orders> {
}
