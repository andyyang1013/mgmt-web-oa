package com.yxy.oa.service;

import com.yxy.oa.vo.ProcessDefinitionVO;

import java.util.List;

/**
 * 流程定义服务接口
 */
public interface IProcessDefinitionService {
    /**
     * 查询流程定义列表
     * @return 流程定义列表
     */
    List<ProcessDefinitionVO> list();
}
