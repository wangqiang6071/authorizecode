package com.zelu.authorizecode.entity.params;

import com.zelu.authorizecode.utils.Rquest.entity.redisdata.Out1;
import lombok.Data;

/**
 * 从redis中取出插件最终生成的数据
 * @author wangqiang
 * @Date: 2021/10/9 14:29
 */
@Data
public class SanerPlugsRedisDataParams {

    /**
     * 搜索的关键词
     */
    private String targetName;

    /**
     * 插件组编号
     */
    private String groupNo;

    /**
     * 插件编号
     */
    private String taskNo;

    /**
     * 插件编号
     */
    private String plugsNo;

    /**
     * 插件的属性 discovery/ble 或者 fuzz
     */
    private String typeName;

    /**
     * 接口请求的状态
     */
    private String result;

    /**
     * 插件请求任务的id
     */
    private String task_id;

    /**
     * 返回信息
     */
    private Out1 out;
}
