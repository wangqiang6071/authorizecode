package com.zelu.authorizecode.entity;

import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.List;

import com.zelu.authorizecode.entity.params.SanerPlugsParams;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

/**
 * <p>
 * 插件与插件组的关系
 * </p>
 * @author wangqiang
 * @since 2021-09-16
 */
@Data
@Document(collection = "scaner_plugs_group")
public class ScanerPlugsGroup implements Serializable {

    private static final long serialVersionUID = -8985545025018238754L;

    /**
     * 插件组编号
     */
    @MongoId
    private String groupNo;

    /**
     * 插件列表
     */
    private List<SanerPlugsParams> plugsNoList;

    /**
     * 创建时间
     */
    @CreatedDate
    private LocalDateTime createTime;
}
