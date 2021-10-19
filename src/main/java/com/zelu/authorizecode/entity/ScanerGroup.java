package com.zelu.authorizecode.entity;

import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

/**
 * <p>
 * 插件组
 * </p>
 *
 * @author wangqiang
 * @since 2021-09-16
 */
@Data
@Document(collection = "scaner_group")//此注解对应mongodb集合
public class ScanerGroup implements Serializable {

    private static final long serialVersionUID = -8985545025018238754L;

    /**
     * 插件组编号id
     */
    @MongoId
    private String groupNo;

    /**
     * 插件组名字
     */
    private String groupName;

    /**
     * 创建时间
     */
    @CreatedDate
    private LocalDateTime createTime;


}
