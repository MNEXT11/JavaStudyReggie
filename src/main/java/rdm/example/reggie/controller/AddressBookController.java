package rdm.example.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import rdm.example.reggie.common.BaseContext;
import rdm.example.reggie.common.R;
import rdm.example.reggie.entity.AddressBook;
import rdm.example.reggie.service.AddressBookService;

import java.util.List;

/**
 * @Description
 * @Author rdm
 * @data 2022/5/30 - 11:26
 */
@Slf4j
@RestController
@RequestMapping("/addressBook")
public class AddressBookController {
    @Autowired
    private AddressBookService addressBookService;

    /**
     * 新增地址
     * @param addressBook
     * @return
     */
    @PostMapping
    public R<AddressBook> save(@RequestBody AddressBook addressBook){
        //获取当前登录用户，设置地址对应的用户对象
        addressBook.setUserId(BaseContext.getCurrentId());
        //日志
        log.info("addressBook:{}" , addressBook);
        addressBookService.save(addressBook);
        return R.success(addressBook);
    }

    /**
     * 设置默认收货地址（修改is_default字段）
     * @param addressBook
     * @return
     */
    @PutMapping("default")
    public R<AddressBook> setDefault(@RequestBody AddressBook addressBook){
        // 增加日志
        log.info("addressBook:{}", addressBook);
        //1.修改用户的地址所有is_default字段为0，默认地址只有一个
        LambdaUpdateWrapper<AddressBook> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(AddressBook::getUserId,BaseContext.getCurrentId());
        updateWrapper.set(AddressBook::getIsDefault, 0);
        addressBookService.update(updateWrapper);

        //2.将当前地址的is_defaul设置为1
        addressBook.setIsDefault(1);
        addressBookService.updateById(addressBook);
        return R.success(addressBook);
    }

    /**
     * 根据id查询地址
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<AddressBook> get(@PathVariable Long id) {
        AddressBook addressBook = addressBookService.getById(id);
        if (addressBook != null) {
            return R.success(addressBook);
        } else {
            return R.error("没有找到该地址");
        }
    }

    /**
     * 查询默认地址
     * @return
     */
    @GetMapping("default")
    public R<AddressBook> getDefault() {
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId, BaseContext.getCurrentId());
        queryWrapper.eq(AddressBook::getIsDefault, 1);

        // SQL:select * from address_book where user_id = ? and is_default = 1
        AddressBook addressBook = addressBookService.getOne(queryWrapper);

        if (null == addressBook) {
            return R.error("没有找到该对象");
        } else {
            return R.success(addressBook);
        }
    }

    /**
     * 查询指定用户的全部地址
     * @param addressBook
     * @return
     */
    @GetMapping("/list")
    public R<List<AddressBook>> list(AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        log.info("addressBook:{}", addressBook);

        // 条件构造器
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(null != addressBook.getUserId(), AddressBook::getUserId, addressBook.getUserId());
        queryWrapper.orderByDesc(AddressBook::getUpdateTime);
        // SQL:select * from address_book where user_id = ? order by update_time desc
        return R.success(addressBookService.list(queryWrapper));
    }

    /**
     * 修改地址信息。
     * @param addressBook
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody AddressBook addressBook) {
        log.info("修改地址信息：{}", addressBook);
        // 进行更新
        if (addressBookService.updateById(addressBook)) {
            return R.success("更新用户信息成功");
        }
        return R.success("更新用户信息失败");
    }

    /**
     * 根据id删除地址（逻辑删除）
     *
     * @return
     */
    @DeleteMapping
    public R<String> delete(Long ids) {
        log.info("根据id删除地址：{}", ids);
        // 逻辑删除：把指定id的用户is_deleted改为1即可
        //update address_book set id_deleted =1 where id=ids;
        UpdateWrapper<AddressBook> addressBookUpdateWrapper = new UpdateWrapper<>();
        addressBookUpdateWrapper.set("is_deleted", 1);
        addressBookUpdateWrapper.eq("id", ids);
        if (!addressBookService.update(addressBookUpdateWrapper)) {
            return R.error("删除地址失败");
        }
        return R.error("删除地址成功");
    }

}
