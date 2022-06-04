package rdm.example.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import rdm.example.reggie.entity.OrderDetail;
import rdm.example.reggie.mapper.OrderDetailMapper;
import rdm.example.reggie.service.OrderDetailService;

/**
 * @Description
 * @Author rdm
 * @data 2022/5/30 - 20:19
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
