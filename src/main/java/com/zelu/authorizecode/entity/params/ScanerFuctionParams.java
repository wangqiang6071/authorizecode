package com.zelu.authorizecode.entity.params;

import com.zelu.authorizecode.utils.Rquest.entity.RedisParams;
import com.zelu.authorizecode.utils.Rquest.entity.RequestModel;
import lombok.Data;

/**
 * 单个fuction插件扫描
 * @author wangqiang
 * @Date: 2021/10/15 16:53
 */
@Data
public class ScanerFuctionParams {
    private RequestModel model;
    private RedisParams params;
}
