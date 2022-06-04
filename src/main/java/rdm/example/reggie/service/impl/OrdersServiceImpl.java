package rdm.example.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rdm.example.reggie.common.BaseContext;
import rdm.example.reggie.dto.OrdersDto;
import rdm.example.reggie.entity.*;
import rdm.example.reggie.mapper.OrdersMapper;
import rdm.example.reggie.service.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @Description
 * @Author rdm
 * @data 2022/5/30 - 20:18
 */
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {
    @Autowired
    private OrdersService ordersService;
    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private UserService userService;
    @Autowired
    private AddressBookService addressBookService;
    /**
     * 用户下单
     * @param orders
     */
    @Override
    @Transactional
    public void submit(Orders orders) {
        //获得当前用户id
        Long id = BaseContext.getCurrentId();
        //查询用户购物车数据
        LambdaQueryWrapper<ShoppingCart> cartQueryWrapper = new LambdaQueryWrapper<>();
        cartQueryWrapper.eq(ShoppingCart::getUserId, id);
        List<ShoppingCart> shoppingCarts = shoppingCartService.list(cartQueryWrapper);
        //向订单表和订单明细表插入数据
        //1-1获取用户信息
        User user = userService.getById(id);
        //1-2查询地址信息
        Long addressBookId = orders.getAddressBookId();
        AddressBook addressBook = addressBookService.getById(addressBookId);

        long orderId = IdWorker.getId();//生成订单号
        //计算金额和订单明细数据（遍历购物车数据）
        AtomicInteger amount = new AtomicInteger(0);//(可以保证在多线程计算无误，保证线程安全)
        List<OrderDetail> orderDetails = shoppingCarts.stream().map((item)->{
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderId);
            orderDetail.setNumber(item.getNumber());
            orderDetail.setDishFlavor(item.getDishFlavor());
            orderDetail.setDishId(item.getDishId());
            orderDetail.setSetmealId(item.getSetmealId());
            orderDetail.setName(item.getName());
            orderDetail.setImage(item.getImage());
            orderDetail.setAmount(item.getAmount());
            //累加
            amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());
            return orderDetail;
        }).collect(Collectors.toList());



        //保存
        orders.setNumber(String.valueOf(orderId));
        orders.setId(orderId);
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setStatus(2);
        orders.setAmount(new BigDecimal(amount.get()));//总金额
        orders.setUserId(id);
        orders.setNumber(String.valueOf(orderId));
        orders.setUserName(user.getName());
        orders.setConsignee(addressBook.getConsignee());
        orders.setPhone(addressBook.getPhone());
        orders.setAddress((addressBook.getProvinceName() == null ? "" : addressBook.getProvinceName())
                + (addressBook.getCityName() == null ? "" : addressBook.getCityName())
                + (addressBook.getDistrictName() == null ? "" : addressBook.getDistrictName())
                + (addressBook.getDetail() == null ? "" : addressBook.getDetail()));


        this.save(orders);
        orderDetailService.saveBatch(orderDetails);

        //清空购物车数据
        shoppingCartService.remove(cartQueryWrapper);
    }

    /**
     * 用户查看自己的订单,需要用到ordersdto类
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public Page<OrdersDto> userPage(int page, int pageSize) {
        Page<Orders> ordersPage = new Page<>(page,pageSize);
        Page<OrdersDto> ordersDtoPage = new Page<>(page,pageSize);

        LambdaQueryWrapper<Orders> ordersQueryWrapper = new LambdaQueryWrapper<>();
        ordersQueryWrapper.eq(Orders::getUserId,BaseContext.getCurrentId());
        this.page(ordersPage, ordersQueryWrapper);

        BeanUtils.copyProperties(ordersPage, ordersDtoPage,"records");

        List<Orders> records = ordersPage.getRecords();
        List<OrdersDto> dtoList = records.stream().map((item)->{
            OrdersDto ordersDto = new OrdersDto();
            BeanUtils.copyProperties(item, ordersDto);
            Long orderId = item.getId();
            LambdaQueryWrapper<OrderDetail> orderDetailQueryWrapper = new LambdaQueryWrapper<>();
            orderDetailQueryWrapper.eq(OrderDetail::getOrderId, orderId);
            List<OrderDetail> list = orderDetailService.list(orderDetailQueryWrapper);
            ordersDto.setOrderDetails(list);
            return ordersDto;
        }).collect(Collectors.toList());

        ordersDtoPage.setRecords(dtoList);
        return ordersDtoPage;
    }
}
