package com.yxy.oa.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yxy.oa.entity.SubsidiaryInfo;
import com.yxy.oa.mapper.SubsidiaryMapper;
import com.yxy.oa.service.SubsidiaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SubsidiaryServiceImpl extends ServiceImpl<SubsidiaryMapper, SubsidiaryInfo> implements SubsidiaryService {

    @Autowired
    private SubsidiaryMapper subsidiaryMapper;

    @Override
    public boolean checkCodeAndName(SubsidiaryInfo subsidiaryInfo) {
        subsidiaryInfo = subsidiaryMapper.checkCodeAndName(subsidiaryInfo);
        return subsidiaryInfo != null ? true : false;
    }

    @Override
    public boolean checkCodeAndNameExcludeThis(SubsidiaryInfo subsidiaryInfo) {
        subsidiaryInfo = subsidiaryMapper.checkCodeAndNameExcludeThis(subsidiaryInfo);
        return subsidiaryInfo != null ? true : false;
    }

    @Override
    public List<Map> selectSubsidiaryNo() {
        return subsidiaryMapper.selectSubsidiaryNo();
    }
}
