package com.yxy.oa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class InitOperateListener implements CommandLineRunner {

    private final Logger logger = LoggerFactory.getLogger(InitOperateListener.class);


    @Override
    public void run(String... strings) throws Exception {
        logger.info("系统已经启动，可以在此做一些初始化的操作");
    }

}