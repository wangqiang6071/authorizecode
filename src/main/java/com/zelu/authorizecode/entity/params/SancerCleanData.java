package com.zelu.authorizecode.entity.params;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;

/**
 * 插件清洗之后的数据
 * @author wangqiang
 * @Date: 2021/10/14 20:44
 */
@Data
public class SancerCleanData {
    //任务编号
    private String taskNo;
    List<PlugsFinalParams> plugsList;
}
