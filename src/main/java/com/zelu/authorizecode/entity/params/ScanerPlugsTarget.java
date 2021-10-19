package com.zelu.authorizecode.entity.params;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 手动录入一个扫描的目标
 * @author wangqiang
 * @Date: 2021/10/16 14:01
 */
@Data
public class ScanerPlugsTarget {
    //插件的唯一值
    private String onlyKey;
    //插件的名字
    private String plugsName;
    private String groupNo;
    private String taskNo;
    private String plugsNo;
    private String targetType;//wife br ble
    private String targetName;
    private String targetMac;
    private String factoryName;//厂商名字
    private LocalDateTime time;
}
