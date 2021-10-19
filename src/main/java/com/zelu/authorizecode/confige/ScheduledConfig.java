package com.zelu.authorizecode.confige;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * @author wangqiang
 * @Date: 2021/10/16 21:17
 */
@Configuration
public class ScheduledConfig {
    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(3);
        threadPoolTaskScheduler.setRemoveOnCancelPolicy(true);
        return threadPoolTaskScheduler;
    }
}
