package rdm.example.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import rdm.example.reggie.entity.Category;

/**
 * @Description
 * @Author rdm
 * @data 2022/5/25 - 16:04
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}
