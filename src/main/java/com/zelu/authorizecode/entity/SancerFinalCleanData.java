package com.zelu.authorizecode.entity;

import com.zelu.authorizecode.entity.params.PlugsFinalParams;
import lombok.Data;
import java.util.List;

/**
 * 返回给前端的数据
 * @author wangqiang
 * @Date: 2021/10/14 20:44
 */
@Data
public class SancerFinalCleanData {
    private String taskNo;
    private List<PlugsFinalParams> plugsList;
}
