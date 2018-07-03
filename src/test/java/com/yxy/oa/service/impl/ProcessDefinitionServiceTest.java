package com.yxy.oa.service.impl;

import com.yxy.oa.service.IProcessDefinitionService;
import com.yxy.oa.vo.ProcessDefinitionVO;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProcessDefinitionServiceTest {

    @Autowired
    private IProcessDefinitionService processDefinitionService;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void list() {
        List<ProcessDefinitionVO> list = processDefinitionService.list();
        System.out.printf(list.toString());
    }
}