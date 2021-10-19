package com.zelu.authorizecode.entity.params;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 所有插件统一返回结果对象
 * @author wangqiang
 * @Date: 2021/10/14 18:02
 */
@Data
public class PlugsFinalParams {
    //插件组编号
    private String groupNo;
    //插件编号
    private String plugsNo;
    //插件的唯一值
    private String onlyKey;
    //插件的目标值
    private String targetName;
    //插件的mac地址
    private String plugsMac;
    //插件的类型 wifi ble br
    private String plugsType;
    //插件的名字
    private String plugsName;
    //厂商的名字
    private String factoryName;
    //扫描时间
    private LocalDateTime time;
}
