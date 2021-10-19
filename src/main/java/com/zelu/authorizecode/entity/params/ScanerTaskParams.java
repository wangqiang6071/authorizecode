package com.zelu.authorizecode.entity.params;

import lombok.Data;

/**
 * @author wangqiang
 * @Date: 2021/9/16 15:39
 */
@Data
public class ScanerTaskParams {
    /**
     * 任务编号
     */
    private String taskNo;

    /**
     * 任务名字
     */
    private String taskName;

    /**
     * 任务类型
     */
    private String taskType;

    /**
     * 任务描述
     */
    private String taskDesc;

    /**
     * 任务状态
     */
    private Integer taskStatus;
    /**
     * 插件组编号
     */
    private String groupNo;

    //分页初始化参数1
    private Integer pageSize;
    //分页初始化参数2
    private Integer pageIndex;
}
