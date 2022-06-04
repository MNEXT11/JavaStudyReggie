package rdm.example.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import rdm.example.reggie.entity.User;
import rdm.example.reggie.mapper.UserMapper;
import rdm.example.reggie.service.UserService;

/**
 * @Description
 * @Author rdm
 * @data 2022/5/30 - 10:35
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
