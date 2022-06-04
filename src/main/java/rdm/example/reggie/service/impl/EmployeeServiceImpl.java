package rdm.example.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import rdm.example.reggie.entity.Employee;
import rdm.example.reggie.mapper.EmployeeMapper;
import rdm.example.reggie.service.EmployeeService;

/**
 * @Description
 * @Author rdm
 * @data 2022/5/23 - 23:15
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
