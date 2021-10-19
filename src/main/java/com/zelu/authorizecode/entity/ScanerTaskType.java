package com.zelu.authorizecode.entity;

import java.io.Serializable;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

/**
 * <p>
 * 任务类型
 * </p>
 * @author wangqiang
 * @since 2021-09-16
 */
@Data
@Document(collection = "scaner_task_type")
public class ScanerTaskType implements Serializable {

    private static final long serialVersionUID = -8985545025018238754L;

    /**
     * 任务类型编号
     */
    @MongoId
    private String typeNo;

    /**
     * 任务类型名字
     */
    private String typeName;


}
