package rdm.example.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import rdm.example.reggie.common.R;
import rdm.example.reggie.dto.OrdersDto;
import rdm.example.reggie.entity.Orders;
import rdm.example.reggie.service.OrderDetailService;
import rdm.example.reggie.service.OrdersService;

/**
 * @Description
 * @Author rdm
 * @data 2022/5/30 - 20:20
 */
@Slf4j
@RestController
@RequestMapping("/order")
public class OrdersController {
    @Autowired
    private OrdersService ordersService;
    @Autowired
    private OrderDetailService orderDetailService;

    /**
     * 提交订单
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        ordersService.submit(orders);
        return R.success("提交成功");
    }

    /**
     * 用户查看订单
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/userPage")
    public R<Page> page(int page , int pageSize){
        Page<OrdersDto> ordersDtoPage = ordersService.userPage(page, pageSize);
        return R.success(ordersDtoPage);
    }
}
