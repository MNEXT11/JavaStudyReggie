package rdm.example.reggie.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rdm.example.reggie.common.R;
import rdm.example.reggie.entity.Orders;
import rdm.example.reggie.service.OrderDetailService;
import rdm.example.reggie.service.OrdersService;

/**
 * @Description
 * @Author rdm
 * @data 2022/5/30 - 20:22
 */
@Slf4j
@RestController
@RequestMapping("/orderDetail")
public class OrderDetailController {
    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private OrdersService ordersService;


}
