package com.zelu.authorizecode.utils.Rquest;

import com.zelu.authorizecode.confige.ApplicationTextUtils;
import com.zelu.authorizecode.entity.params.SanerPlugsRedisDataParams;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @author wangqiang
 * @Date: 2021/10/16 21:20
 */
@Data
@Slf4j
public class GetRedisValus implements Runnable{

    private SanerPlugsRedisDataParams params;
    private StartEndUtils utils;
    private String task_id;
    private String status;
    public GetRedisValus(SanerPlugsRedisDataParams params,String task_id){
        this.utils= (StartEndUtils)ApplicationTextUtils.getBeanObj("startEndUtils");
        this.params=params;
        this.task_id=task_id;
    }
    @Override
    public void run() {
        log.info("status==>"+params.getOut().getStatus());
        if(StringUtils.isNotBlank(params.getOut().getStatus())){
            utils.stopTask("com.zelu.authorizecode.utils.Rquest.GetRedisValus");
        }else{
            status=params.getOut().getStatus();
            log.info("正在获取:"+task_id+"的数据,请耐心等待");
        }
    }
}
