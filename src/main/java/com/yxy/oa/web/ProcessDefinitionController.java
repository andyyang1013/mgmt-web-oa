package com.yxy.oa.web;

import com.yxy.oa.service.IProcessDefinitionService;
import com.yxy.oa.vo.ProcessDefinitionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 流程定义
 **/
@RestController
@RequestMapping("/processDefinition")
public class ProcessDefinitionController extends BaseController {

    @Autowired
    private IProcessDefinitionService processDefinitionService;

    @RequestMapping("/list")
    public List<ProcessDefinitionVO> list() {
        return processDefinitionService.list();
    }

}
