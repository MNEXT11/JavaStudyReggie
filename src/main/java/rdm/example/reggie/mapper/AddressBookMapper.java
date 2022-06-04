package rdm.example.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import rdm.example.reggie.entity.AddressBook;

/**
 * @Description
 * @Author rdm
 * @data 2022/5/29 - 23:03
 */
@Mapper
public interface AddressBookMapper extends BaseMapper<AddressBook> {
}
