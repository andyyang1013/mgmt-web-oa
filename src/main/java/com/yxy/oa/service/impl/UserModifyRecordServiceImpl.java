package com.yxy.oa.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yxy.oa.entity.UserModifyRecord;
import com.yxy.oa.mapper.UserModifyRecordMapper;
import com.yxy.oa.service.IUserModifyRecordService;
import org.springframework.stereotype.Service;

/**
 * 用户信息修改记录表 服务实现类
 */
@Service
public class UserModifyRecordServiceImpl extends ServiceImpl<UserModifyRecordMapper, UserModifyRecord> implements IUserModifyRecordService {

}
