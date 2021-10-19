package com.zelu.authorizecode.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 任务与插件组-单个插件的关系
 * 任务中可以关联一个或者多个插件组 也可以关联单个或者多个插件 也可以共存
 * @author wangqiang
 * @since 2021-09-16
 */

@Data
@Document(collection = "scaner_task_grop")
public class ScanerTaskGroup implements Serializable {
    private static final long serialVersionUID = -8985545025018238754L;

    /**
     * 任务编号
     */
    @MongoId
    private String taskNo;
    /**
     * 插件组编号
     */
    private String groupNo;

    /**
     * 单个插件编号
     */
    private String plugNo;

    /**
     * 创建时间
     */
    @CreatedDate
    private LocalDateTime createTime;


}
