package com.zelu.authorizecode.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

/**
 * <p>
 * 新建任务
 * </p>
 *
 * @author wangqiang
 * @since 2021-09-16
 */
@Data
@Document(collection = "scaner_task")
public class ScanerTask implements Serializable {

    private static final long serialVersionUID = -8985545025018238754L;

    /**
     * 任务编号
     */
    @MongoId
    private String taskNo;

    /**
     * 任务名字
     */
    private String taskName;

    /**
     * 任务描述
     */
    private String taskDesc;

    /**
     * 任务状态
     */
    private Integer taskStatus;

    /**
     * 创建时间
     */
    @CreatedDate
    private LocalDateTime createTime;


}
