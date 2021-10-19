package com.zelu.authorizecode.entity;

import com.zelu.authorizecode.entity.params.SanerPlugsRedisDataParams;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * 插件组中的扫描数据
 * 当前任务-当前插件组-当前插件（多条数据）
 * @author wangqiang
 * @Date: 2021/10/15 18:17
 */
@Data
@Document(collection = "scaner_group_plugs_data")
public class ScanerGropPlugsData {
    private String taskNo;
    private String groupNo;
    private String plugsNo;
    private List<SanerPlugsRedisDataParams> plugsDataList;
}
