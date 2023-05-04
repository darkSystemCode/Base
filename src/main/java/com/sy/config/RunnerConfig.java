package com.sy.config;

import com.sy.core.DataBase;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @Author: shuYan
 * @Date: 2023/5/3 12:42
 * @Descript: 需要再SpringBoot项目启动后执行的方法，全部写在run函数中
 */
@Component
public class RunnerConfig implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        DataBase.init();
    }
}
