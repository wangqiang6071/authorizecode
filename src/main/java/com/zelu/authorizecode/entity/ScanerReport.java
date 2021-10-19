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
 * 报告列表
 * </p>
 *
 * @author wangqiang
 * @since 2021-09-16
 */
@Data
@Document(collection = "scaner_report")
public class ScanerReport implements Serializable {

    private static final long serialVersionUID = -8985545025018238754L;

    /**
     * 报告编号
     */
    @MongoId
    private String reportNo;

    /**
     * 报告名字
     */
    private String reportName;

    /**
     * 任务编号
     */
    private String taskNo;

    /**
     * 创建时间
     */
    @CreatedDate
    private LocalDateTime createTime;


}
