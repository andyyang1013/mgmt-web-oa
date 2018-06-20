package com.yxy.oa;

import com.yxy.oa.service.IUserTempService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class InitOperateListener implements CommandLineRunner, EnvironmentAware {

    private final Logger logger = LoggerFactory.getLogger(InitOperateListener.class);


    @Autowired
    private IUserTempService userTempService;

    private boolean isDevEnvironment = false;

    @Override
    public void run(String... strings) throws Exception {
        logger.info("系统已经启动，可以在此做一些初始化的操作");
        importHistoryData();
    }

    /**
     * 导入历史数据，开发环境多个人操作，暂时不开启初始化导入
     *
     * @param
     * @return
     */
    public void importHistoryData() {
        if (!isDevEnvironment) {
            userTempService.importHistoryData();
        }
    }

    @Override
    public void setEnvironment(Environment environment) {
        String env = environment.getProperty("spring.profiles.active");
        if ("dev".equals(env)) {
            isDevEnvironment = true;
        }
    }
}