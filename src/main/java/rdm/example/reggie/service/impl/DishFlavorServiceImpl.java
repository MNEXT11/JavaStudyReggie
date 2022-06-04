package rdm.example.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import rdm.example.reggie.entity.DishFlavor;
import rdm.example.reggie.mapper.DishFlavorMapper;
import rdm.example.reggie.service.DishFlavorService;

/**
 * @Description
 * @Author rdm
 * @data 2022/5/26 - 20:18
 */
@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
