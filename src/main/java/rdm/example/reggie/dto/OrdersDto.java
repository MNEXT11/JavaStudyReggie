package rdm.example.reggie.dto;

import lombok.Data;
import rdm.example.reggie.entity.OrderDetail;
import rdm.example.reggie.entity.Orders;

import java.util.List;

/**
 * @Description
 * @Author rdm
 * @data 2022/5/30 - 22:19
 */
@Data
public class OrdersDto extends Orders {
    private List<OrderDetail> orderDetails;
}
