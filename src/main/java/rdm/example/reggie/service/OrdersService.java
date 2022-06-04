package rdm.example.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.bind.annotation.RequestBody;
import rdm.example.reggie.dto.OrdersDto;
import rdm.example.reggie.entity.Orders;

/**
 * @Description
 * @Author rdm
 * @data 2022/5/30 - 20:17
 */
public interface OrdersService extends IService<Orders> {
    /**
     * 用户下单
     * @param orders
     */
    public void submit(@RequestBody Orders orders);

    /**
     * 用户查看订单
     * @return
     */
    public Page<OrdersDto> userPage(int page , int pageSize);
}
