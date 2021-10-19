package com.zelu.authorizecode.entity.params;

import com.zelu.authorizecode.utils.Rquest.entity.RequestModel;
import lombok.Data;

/**
 * 单个插件扫描出结果
 * @author wangqiang
 * @Date: 2021/10/13 18:33
 */
@Data
public class ScanerPlugsScanerParams {
    //任务编号 多个插件扫描的时候使用
    private String taskNo;
    //插件组编号 多个插件扫描的时候使用
    private String plugsNo;
}
