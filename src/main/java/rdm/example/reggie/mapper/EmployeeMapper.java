package rdm.example.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import rdm.example.reggie.entity.Employee;

/**
 * @Description
 * @Author rdm
 * @data 2022/5/23 - 23:12
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {

}
