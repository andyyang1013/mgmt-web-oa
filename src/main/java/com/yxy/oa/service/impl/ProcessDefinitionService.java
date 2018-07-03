package com.yxy.oa.service.impl;

import com.yxy.oa.service.IProcessDefinitionService;
import com.yxy.oa.vo.ProcessDefinitionVO;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

/**
 * 流程定义服务实现类
 */
@Service
public class ProcessDefinitionService implements IProcessDefinitionService {
    private final RestTemplate restTemplate;
    public ProcessDefinitionService(RestTemplateBuilder restTemplateBuilder){
        this.restTemplate=restTemplateBuilder.basicAuthorization("kermit","kermit").build();
    }
    @Override
    public List<ProcessDefinitionVO> list() {
        String url = "http://localhost:8080/activiti-rest/service/repository/process-definitions";
        MultiValueMap<String, String> headers=new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT,MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.CONTENT_TYPE,MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(null,headers);
        ResponseEntity<Map> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Map.class);

        return null;
    }
}
