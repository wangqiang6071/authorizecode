package com.zelu.authorizecode.entity;

import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.List;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

/**
 * <p>
 * 所有插件列表
 * </p>
 *
 * @author wangqiang
 * @since 2021-09-16
 */
@Data
@Document(collection = "scaner_plugs")
public class ScanerPlugs implements Serializable {

    private static final long serialVersionUID = -8985545025018238754L;

    /**
     * 插件编号id
     */
    @MongoId
    private String plugsNo;

    /**
     * 插件的唯一标识id
     */
    private String plugsId;

    /**
     * 插件的类型:基础数据(base) 功能性(fuction)
     */
    private String type;

    /**
     * 如果是功能性 wife bluetooth
     */
    private String typeName;

    /**
     * 插件名字
     */
    private String plugsName;

    /**
     * 插件的描述
     */
    private String plugsDesc;

    /**
     * 插件的版本
     */
    private String plugsVersion;

    /**
     * 前端判断需要的字段
     */
    private boolean status;
    /**
     * 前端判断需要的字段
     * 插件对应的参数
     */
    private List<Object> params;
    /**
     * 前端判断需要的字段
     */
    private String childType;
    /**
     * 前端判断需要的字段
     */
    private String firstChild;
    /**
     * 前端判断需要的字段
     */
    private String[] childList;

    /**
     * 创建时间
     */
    @CreatedDate
    private LocalDateTime createTime;
}
