package cn.imqinhao.service.impl;

import cn.imqinhao.entity.Employee;
import cn.imqinhao.mapper.EmployeeMapper;
import cn.imqinhao.service.EmployeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author qinhao
 * @version 1.0
 */

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

}
