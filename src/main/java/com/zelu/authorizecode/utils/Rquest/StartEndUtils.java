package com.zelu.authorizecode.utils.Rquest;

import com.zelu.authorizecode.confige.ScheduledFutureHolder;
import com.zelu.authorizecode.entity.params.SanerPlugsRedisDataParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.concurrent.ScheduledFuture;

/**
 * @author wangqiang
 * @Date: 2021/10/16 21:22
 */
@Component
public class StartEndUtils {
    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    //存储任务执行的包装类
    private HashMap<String, ScheduledFutureHolder> scheduleMap = new HashMap<>();

    /**
     *启动任务
     * 如果不想手动触发任务可以使用 @PostConstruct注解来启动
     */
    public void startTask(SanerPlugsRedisDataParams params, String stask_id)  {
        try {
            //初始化一个任务（这里可以初始话多个）
            GetRedisValus helloTask = new GetRedisValus(params,stask_id);
            String corn = "0/10 * * * *  ?";
            //将任务交给任务调度器执行
            ScheduledFuture<?> schedule = threadPoolTaskScheduler.schedule(helloTask, new CronTrigger(corn));
            //将任务包装成ScheduledFutureHolder
            ScheduledFutureHolder scheduledFutureHolder = new ScheduledFutureHolder();
            scheduledFutureHolder.setScheduledFuture(schedule);
            scheduledFutureHolder.setRunnableClass(helloTask.getClass());
            scheduledFutureHolder.setCorn(corn);
            scheduleMap.put(scheduledFutureHolder.getRunnableClass().getName(),scheduledFutureHolder);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 查询所有的任务
     */
    public void queryTask(){
        scheduleMap.forEach((k,v)->{
            System.out.println(k+"  "+v);
        });
    }

    /**
     *
     * @param className 当前线程的全路由名字
     */
    public void stopTask(String className){
        if(scheduleMap.containsKey(className)){//如果包含这个任务
            ScheduledFuture<?> scheduledFuture = scheduleMap.get(className).getScheduledFuture();
            if(scheduledFuture!=null){
                scheduledFuture.cancel(true);
            }
        }
    }


    /**
     * 重启任务，修改任务的触发时间
     * @param className
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public void restartTask(String className) throws InstantiationException, IllegalAccessException {
        if(scheduleMap.containsKey(className)){//如果包含这个任务
            //这里的corn可以通过前端动态传过来
            String corn = "0/50 * * * *  ?";
            ScheduledFutureHolder scheduledFutureHolder = scheduleMap.get(className);
            ScheduledFuture<?> scheduledFuture = scheduledFutureHolder.getScheduledFuture();
            if(scheduledFuture!=null){
                //先停掉任务
                scheduledFuture.cancel(true);

                //修改触发时间重新启动任务
                Runnable runnable = scheduledFutureHolder.getRunnableClass().newInstance();

                ScheduledFuture<?> schedule = threadPoolTaskScheduler.schedule(runnable, new CronTrigger(corn));

                scheduledFutureHolder.setScheduledFuture(schedule);
                scheduledFutureHolder.setCorn(corn);

                scheduleMap.put(scheduledFutureHolder.getRunnableClass().getName(),scheduledFutureHolder);
            }
        }
    }
}
