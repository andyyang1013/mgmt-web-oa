package com.yxy.oa.config.filter.shiro;


import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSubjectFactory;

/**
 * 前后端分离，不需要session。因此修改shiro的默认session配置
 **/
public class PlatformSubjectFactory extends DefaultWebSubjectFactory {
    @Override
    public Subject createSubject(SubjectContext context) {
        //不创建session.
        context.setSessionCreationEnabled(false);
        return super.createSubject(context);
    }
}
