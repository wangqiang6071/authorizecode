package com.zelu.authorizecode.utils.Rquest.entity.redisdata;

import lombok.Data;
import java.util.List;
import java.util.Map;

/**
 * @author wangqiang
 * @Date: 2021/9/18 14:20
 */
@Data
public class Out2 {
    //weife数据
    //private List<wifeList> networks;
    private List<Map<String,String>> networks;
    //蓝牙数据
    private List<Object> devices;
    private String status;
    private String ssid;
    private String current;
    private Integer parsed;
    private String success;
}
