package rdm.example.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import rdm.example.reggie.entity.AddressBook;
import rdm.example.reggie.mapper.AddressBookMapper;
import rdm.example.reggie.service.AddressBookService;

/**
 * @Description
 * @Author rdm
 * @data 2022/5/29 - 23:04
 */
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
