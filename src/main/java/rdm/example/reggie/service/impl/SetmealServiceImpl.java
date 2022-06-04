package rdm.example.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rdm.example.reggie.common.CustomException;
import rdm.example.reggie.entity.Setmeal;
import rdm.example.reggie.entity.SetmealDish;
import rdm.example.reggie.mapper.SetmealMapper;
import rdm.example.reggie.service.SetmealDishService;
import rdm.example.reggie.service.SetmealService;

import java.util.List;

/**
 * @Description
 * @Author rdm
 * @data 2022/5/25 - 19:26
 */
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    SetmealDishService setmealDishService;
    /**
     * 删除套餐和他的菜品
     * @param ids
     */
    @Override
    @Transactional
    public void deleteByIdsAnddishes(List<Long> ids) {
        //查询套餐状态是否可以删除
        //select count(*) from setmeal where id in ids and status = 1
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId, ids);
        queryWrapper.eq(Setmeal::getStatus, 1);
        int count = this.count(queryWrapper);
        if(count > 0 ){
            //不能删除抛出异常
            throw new CustomException("有套餐正在售卖中，无法删除");
        }
        //可以删除先删除套餐表中的数据
        this.removeByIds(ids);
        //删除关系表中的数据
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SetmealDish::getSetmealId, ids);
        setmealDishService.remove(lambdaQueryWrapper);
    }
}
