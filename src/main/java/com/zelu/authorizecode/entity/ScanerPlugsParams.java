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
 * <p>
 * 插件参数
 * </p>
 * @author wangqiang
 * @since 2021-09-16
 */
@Data
@Document(collection = "scaner_plugs_params")
public class ScanerPlugsParams implements Serializable {

    private static final long serialVersionUID = -8985545025018238754L;

    @MongoId
    private String plugsNo;
    private String type;//base fuction
    private String plugsName;//插件的名字
    private List<Object> params;//数组参数[String,RequestObj对象]
    //创建时间
    @CreatedDate
    private LocalDateTime createTime;
}
