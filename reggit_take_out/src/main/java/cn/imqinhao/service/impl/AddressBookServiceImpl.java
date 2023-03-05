package cn.imqinhao.service.impl;

import cn.imqinhao.entity.AddressBook;
import cn.imqinhao.mapper.AddressBookMapper;
import cn.imqinhao.service.AddressBookService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author qinhao
 * @version 1.0
 */
@Service
@Slf4j
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
