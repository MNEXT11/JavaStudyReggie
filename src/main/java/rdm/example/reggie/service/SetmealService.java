package rdm.example.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import rdm.example.reggie.entity.Setmeal;

import java.util.List;

/**
 * @Description
 * @Author rdm
 * @data 2022/5/25 - 19:25
 */
public interface SetmealService extends IService<Setmeal> {
    //删除套餐和他的菜品
    public void deleteByIdsAnddishes(List<Long> ids);
}
