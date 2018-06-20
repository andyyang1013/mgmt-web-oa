package com.yxy.oa.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yxy.oa.entity.Oauth;
import com.yxy.oa.mapper.OauthMapper;
import com.yxy.oa.service.IOauthService;
import org.springframework.stereotype.Service;

/**
 * OAuth2.0认证表，包括微信、qq，新浪微博等 服务实现类
 */
@Service
public class OauthServiceImpl extends ServiceImpl<OauthMapper, Oauth> implements IOauthService {

}
