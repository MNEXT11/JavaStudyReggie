package rdm.example.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import rdm.example.reggie.entity.Category;

/**
 * @Description
 * @Author rdm
 * @data 2022/5/25 - 16:05
 */
public interface CategoryService extends IService<Category> {
    public void remove(Long id);
}
