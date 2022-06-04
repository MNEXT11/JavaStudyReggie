package rdm.example.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import rdm.example.reggie.entity.User;

/**
 * @Description
 * @Author rdm
 * @data 2022/5/30 - 10:34
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
