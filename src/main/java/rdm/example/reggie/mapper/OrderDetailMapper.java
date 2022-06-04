package rdm.example.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import rdm.example.reggie.entity.OrderDetail;

/**
 * @Description
 * @Author rdm
 * @data 2022/5/30 - 20:18
 */
@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {
}
