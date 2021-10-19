package com.zelu.authorizecode.utils.Rquest.entity;

import lombok.Data;

/**
 * @author wangqiang
 * @Date: 2021/9/16 14:17
 * 获取redis中数据 所需的参数
 */
@Data
public class RedisParams {

    RequestModel model;
    /**
     * 数据的唯一属性
     */
    private String onlyKey;
    /**
     * function插件搜索的关键目标
     */
    private String targetName;

    /**
     * 获取redis数据的关键数据
     */
    private String taskId;

    /**
     * 任务编号
     */
    private String taskNo;

    /**
     * 插件组编号
     */
    private String groupNo;

    /**
     * 插件组中的插件编号
     */
    private String plugsNo;

    /**
     * 插件的属性
     */
    private String typeName;
}
