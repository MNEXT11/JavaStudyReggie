package rdm.example.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.yaml.snakeyaml.events.Event;
import rdm.example.reggie.common.BaseContext;
import rdm.example.reggie.common.R;
import rdm.example.reggie.entity.ShoppingCart;
import rdm.example.reggie.service.ShoppingCartService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Description
 * @Author rdm
 * @data 2022/5/30 - 18:40
 */
@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 购物车添加菜品或套餐
     * @param shoppingCart
     * @return
     */
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){
        log.info(shoppingCart.toString()); //没有userid数据
        shoppingCart.setUserId(BaseContext.getCurrentId());

        //查询购物车中有没有相同的菜品或套餐
        Long dishId = shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        if (dishId != null){
            //此时添加的是菜品
            queryWrapper.eq(ShoppingCart::getDishId, dishId);
        } else {
            //此时添加的是套餐
            queryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }

        ShoppingCart cartServiceOne = shoppingCartService.getOne(queryWrapper);
        if (cartServiceOne != null) {
            //若存在数量加一
            Integer number = cartServiceOne.getNumber();
            cartServiceOne.setNumber(number + 1);
            shoppingCartService.updateById(cartServiceOne);
        } else {
            //若不存在添加到购物车中
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            cartServiceOne = shoppingCart;
        }
        return R.success(cartServiceOne);
    }

    /**
     * 查看购物车
     * @return
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        queryWrapper.orderByAsc(ShoppingCart::getCreateTime);
        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);
        return R.success(list);
    }

    /**
     * 清空购物车
     * @return
     */
    @DeleteMapping("/clean")
    public R<String> clean(){
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        shoppingCartService.remove(queryWrapper);
        return R.success("清空购物车成功");
    }

    /**
     * 购物车减去某一个菜品
     * @param shoppingCart
     * @return
     */
    @PostMapping("/sub")
    public R<String> sub(@RequestBody ShoppingCart shoppingCart){
        log.info(shoppingCart.toString());
        Long dishId = shoppingCart.getDishId();
        if (dishId != null){
            //需要删除的是菜品
            LambdaQueryWrapper<ShoppingCart> dishQueryWrapper = new LambdaQueryWrapper<>();
            dishQueryWrapper.eq(ShoppingCart::getDishId, dishId);
            ShoppingCart one = shoppingCartService.getOne(dishQueryWrapper);
            Integer number = one.getNumber();
            if (number > 1){
                one.setNumber(number - 1);
                shoppingCartService.updateById(one);
            }else{
                shoppingCartService.removeById(one);
            }
        }else{
            //需要删除的是套餐
            LambdaQueryWrapper<ShoppingCart> setmealQueryWrapper = new LambdaQueryWrapper<>();
            setmealQueryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
            ShoppingCart one = shoppingCartService.getOne(setmealQueryWrapper);
            Integer number = one.getNumber();
            if (number > 1){
                one.setNumber(number - 1);
                shoppingCartService.updateById(one);
            }else{
                shoppingCartService.removeById(one);
            }
        }
        return R.success("");
    }
}
